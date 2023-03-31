import datetime
import hashlib
from Database.OAuthDB.tables.db_pass import PassTable
from Database.OAuthDB.tables.db_tokens import TokensTable


# проверка времени жизни
def check_life_time(date_start):
    if datetime.now() < date_start:
        return True
    return False

# проверка действия токена
def check_token (token):
    if TokensTable().find_by_token(token) is not None:
        _,_,_,date_start = TokensTable().find_by_token(token)
        if check_life_time(date_start):
            return True
    return False

# получение аутентификационных данных от формы
#response=requests.get('http://127.0.0.1:8080/Authentification%20Form/')
#print (response)

#проверка существования пользователя и совпадения пароля
def check_passwd(login, passwd_check):
    if PassTable().find_by_id(login) is not None:
        _,passwd,salt=PassTable().find_by_id(login)
        if hashlib.sha512(passwd + salt).hexdigest() == passwd_check:
            return True
    return False

#сообщение джаве????
'''
def message_to_java (token):
    if check_token (token):
        #выдать оценку
    else:
        login, passwd = login()
        if check_passwd(login, passwd):
            #выдать токен
        else:
            #зарегестрировать пользователя
'''

