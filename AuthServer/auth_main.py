#export FLASK_APP=auth_main.py

from flask import Flask, request, jsonify

from auth_func import *

app = Flask(__name__)

#аутентификация пользователя
@app.route('/auth_form', methods=['POST'])
def login():
    data = request.get_json()
    login = data['login']
    passwd = data['password']
    check_passwd(login, passwd)


@app.route('/java_bot', methods=['POST'])
def from_java():
    data = request.get_json()
    token = data['token']
    return check_token(token)


if __name__ == '__main__':
    app.run(port=5001)
