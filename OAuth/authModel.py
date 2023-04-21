import hashlib
import math
import datetime
import traceback
import time
import psycopg2
import jwt

from authPayload import AuthPayload
from authResponse import AuthResponse

from Databases.oauthdb.tables.pass_table import PassTable
from Databases.oauthdb.tables.tokens_table import TokensTable
from Databases.project_config import ProjectConfig

AUTHSECRET = "dlksjgf"
EXPIRESSECONDS = 100


def salt_check(client_secret_input, salt):
    key = hashlib.pbkdf2_hmac(
        'sha256',  # Используемый алгоритм хеширования
        client_secret_input.encode('utf-8'),  # Конвертирование пароля в байты
        salt,  # Предоставление соли
        100000,  # Число итераций SHA-256
        dklen=32)
    key_str = key.hex()
    return key_str


def authenticate(login, passwd):
    try:
        pt = PassTable(ProjectConfig(), 'oauthdb')
        rows = pt.find_by_id(login)

        if rows:
            if salt_check(passwd, bytearray(rows[2], "utf-8")) == rows[1]:
                tt = TokensTable(ProjectConfig(), 'oauthdb')
                logins = tt.find_by_id(login)
                if logins:
                    if logins[1]:
                        if time_check(logins[1]):
                            return {'success': False, 'status': 'Unnecessary'}
                        else:
                            payload = AuthPayload(rows[0], EXPIRESSECONDS)
                            encoded_jwt = jwt.encode(payload.__dict__, AUTHSECRET, algorithm='HS256')
                            current_time = datetime.datetime.now()
                            tt.update(rows[0],
                                      [encoded_jwt, current_time, current_time + datetime.timedelta(0, EXPIRESSECONDS),
                                       current_time, "http://"])
                            return AuthResponse(rows[0], current_time + datetime.timedelta(0, EXPIRESSECONDS),
                                                encoded_jwt).__dict__

                payload = AuthPayload(rows[0], EXPIRESSECONDS)
                encoded_jwt = jwt.encode(payload.__dict__, AUTHSECRET, algorithm='HS256')
                current_time = datetime.datetime.now()

                tt.delete(rows[0])
                tt.insert_one([rows[0], encoded_jwt, current_time, current_time + datetime.timedelta(0, EXPIRESSECONDS),
                               current_time, "http://"])
                return AuthResponse(rows[0], current_time + datetime.timedelta(0, EXPIRESSECONDS), encoded_jwt).__dict__
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
        print(traceback.format_exc())
        return {'success': False}


# Проверка срока годности токена
def check_availability(token):
    try:
        tt = TokensTable(ProjectConfig(), 'oauthdb')
        if tt.find_by_token(token):
            if time_check(token):
                return {'success': True}
        return {'success': False}
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
        print(traceback.format_exc())
        return {'success': False}


def time_check(token):
    try:
        token_time = jwt.decode(token, AUTHSECRET, algorithms=['HS256'])['exp']
        # print(math.trunc(time.time()), token_time)
        if math.trunc(time.time()) < float(token_time):
            return True
    except jwt.ExpiredSignatureError:
        return False
