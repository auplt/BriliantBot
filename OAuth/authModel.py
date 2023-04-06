import hashlib
import math
import os
import json
import datetime

# pip install psycopg2
import time
import traceback

import psycopg2

# pip install -U python-dotenv
from dotenv import load_dotenv

load_dotenv()

# pip install pyjwt
import jwt

from authPayload import authPayload
from authResponse import authResponse

# Get environment variables
DBNAME = os.getenv('DBNAME')
DBUSER = os.getenv('DBUSER')
DBPASSWORD = os.getenv("DBPASSWORD")
AUTHSECRET = "dlksjgf"
EXPIRESSECONDS = 30000



def salt_check(client_secret_input, salt):
    key = hashlib.pbkdf2_hmac(
        'sha256',  # Используемый алгоритм хеширования
        client_secret_input.encode('utf-8'),  # Конвертирование пароля в байты
        salt,  # Предоставление соли
        100000,  # Рекомендуется использоваться по крайней мере 100000 итераций SHA-256
        dklen=32)
    # print(key)
    key_str = key.hex()
    # print(key_str)
    return key_str


def authenticate(login, passwd):
    conn = None
    tg_id = "11111"
    encoded_jwt = ''
    # query = "select * from passwords where login='" + login + "' and pass='" + passwd + "'"
    # sql = "INSERT INTO tokens VALUES ('" + login + "', '" + encoded_jwt +"', " + "now(), " + "now() + '"+ str(EXPIRESSECONDS) +" second', "+"now())"
    # print(query)
    try:
        # query = "select * from passwords where login='" + login + "' and pass='" + passwd + "'"
        conn = psycopg2.connect("dbname=" + "authdb" + " user=" + "postgres" + " password=" + "WiRe7301")
        cur = conn.cursor()

        query = "select salt, pass from passwords where login='" + login +"'"
        cur.execute(query)
        rows=cur.fetchall()
        # print(rows[0])
        if cur.rowcount == 1:
            if salt_check(passwd, bytearray(rows[0][0], "utf-8")) == rows[0][1]:

                isAdmin = False
                # cur.execute(query)
                # rows = cur.fetchall()

                # print(rows)
                # print("*^&&^*^&", cur.rowcount)
                # if cur.rowcount == 1:

                # if check_token(conn, login):
                #     # print(check_token(conn, login))
                #     cur.execute("DELETE FROM tokens WHERE login='" + login + "'")
                #     conn.commit()

                for row in rows:
                    # print(row)
                    isAdmin = False
                    payload = authPayload(row[0], row[1], isAdmin)
                    # print("ryqwqtyw",payload)
                    break

                # print(payload.__dict__)
                encoded_jwt = jwt.encode(payload.__dict__, AUTHSECRET, algorithm='HS256')
                # print(payload.__dict__)
                # print(encoded_jwt)

                # print(response)
                cur.execute("DELETE FROM tokens WHERE login='" + login + "'")
                conn.commit()
                sql = "INSERT INTO tokens VALUES ('" + login + "', '" + encoded_jwt + "', " + "now(), " + "now() + '" + str(
                    EXPIRESSECONDS) + " second', " + "now())"
                cur.execute(sql)
                conn.commit()

                # cur.execute("SELECT * FROM sessions WHERE login='"  + login + "'")

                # create_date = datetime.datetime.strptime(datetime.datetime.now(), '%Y-%m-%d %H:%M:%S.%f')

                response = authResponse(login, datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S.%f"), encoded_jwt)
                # print("####")
                query = "SELECT * FROM sessions WHERE login='"  + login + "'"
                conn = psycopg2.connect("dbname=" + "sessionsdb" + " user=" + "postgres" + " password=" + "WiRe7301")
                cur = conn.cursor()
                cur.execute(query, str(login))
                # print("&&&&")
                # if cur.rowcount == 1:
                #     cur.execute("DELETE FROM sessions WHERE login='" + login + "'")
                #     conn.commit()
                # insert_session_db(login, tg_id, encoded_jwt)
                # print(jwt.decode(encoded_jwt, AUTHSECRET, algorithms=['HS256']))
                # print(time_check(encoded_jwt))
                # cur.close()
                # print(sql)
                # print(jwt.decode(encoded_jwt, AUTHSECRET, algorithms=['HS256']))
                return response.__dict__


        else:
            return False

    except (Exception, psycopg2.DatabaseError) as error:

        print(error)
        print(traceback.format_exc())

        if conn is not None:
            cur.close()
            conn.close()

        return False
    finally:
        if conn is not None:
            cur.close()
            conn.close()

#проверка срока годности токена
def check_token(token):
    conn = psycopg2.connect("dbname=" + "authdb" + " user=" + "postgres" + " password=" + "WiRe7301")
    query = "select date_end  from tokens where token=\'" + token + "\'"
    cur = conn.cursor()
    cur.execute(query)
    date_end = cur.fetchone()
    # print(date_end)
    if datetime.datetime.now() < date_end[0]:
        return True

    if conn is not None:
        cur.close()
        conn.close()

    return False


"""def time_check(token):
    token_time = jwt.decode(token, AUTHSECRET, algorithms=['HS256'])['exp']
    import time
    # print(math.trunc(time.time()), token_time)
    if math.trunc(time.time()) < float(token_time):
        return True
    return False"""


"""def check_token(conn, login):
    sql = "select * from tokens where login='" + login + "'"
    cur = conn.cursor()
    cur.execute(sql)
    # cur.fetchall()
    # print(cur.rowcount)
    if cur.rowcount == 1:
        return True
    return False
"""

def insert_session_db(login, tg_id, token):
    query = "INSERT INTO sessions (login, tg_id, token) VALUES (%(login)s, %(tg_id)s, %(token)s)"
    conn = psycopg2.connect("dbname=" + "sessionsdb" + " user=" + "postgres" + " password=" + "WiRe7301")
    cur = conn.cursor()
    cur.execute(query, {"login":str(login), "tg_id":str(tg_id), "token":str(token)})
    conn.commit()
    conn.close()



# def verify(token):
#     try:
#         isBlacklisted = checkBlacklist(token)
#         if isBlacklisted == True:
#             return {"success": False}
#         else:
#             decoded = jwt.decode(token, AUTHSECRET, algorithms=['HS256'])
#             return decoded
#     except (Exception) as error:
#         print(error)
#         return {"success": False}




def create(login, passwd, isAdmin):
    conn = None
    query = "insert into clients (\"login\", \"passwd\", \"IsAdmin\") values(%s,%s,%s)"

    try:
        conn = psycopg2.connect("dbname=" + DBNAME + " user=" + DBUSER + " password=" + DBPASSWORD)
        cur = conn.cursor()
        cur.execute(query, (login, passwd, isAdmin))
        conn.commit()
        return True
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
        if conn is not None:
            cur.close()
            conn.close()

        return False
    finally:
        if conn is not None:
            cur.close()
            conn.close()


def blacklist(token):
    conn = None
    query = "insert into blacklist (\"token\") values(\'" + token + "\')"
    try:
        conn = psycopg2.connect("dbname=" + DBNAME + " user=" + DBUSER + " password=" + DBPASSWORD)
        cur = conn.cursor()
        cur.execute(query)
        conn.commit()
        return True
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
        if conn is not None:
            cur.close()
            conn.close()

        return False
    finally:
        if conn is not None:
            cur.close()
            conn.close()


def check_avialability(token):
    query = "select login from tokens where token=\'" + token + "\'"
    try:
        conn = psycopg2.connect("dbname=" + "authdb" + " user=" + "postgres" + " password=" + "WiRe7301")
        cur = conn.cursor()
        cur.execute(query)
        result = cur.fetchone()
        if result is not None:
            return result
        else:
            return False
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
        if conn is not None:
            cur.close()
            conn.close()

        return True
    finally:
        if conn is not None:
            cur.close()
            conn.close()
