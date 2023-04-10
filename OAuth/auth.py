import requests
from flask import Flask, request, jsonify
import json
import hashlib
import secrets
import string
import authModel

from flask_cors import CORS, cross_origin

# instantiate the Flask app.
app = Flask(__name__)
cors=CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'

def generate_alphanum_crypt_string(length):
    letters_and_digits = string.ascii_letters + string.digits + string.punctuation
    crypt_rand_string = ''.join(secrets.choice(
        letters_and_digits) for _ in range(length))
    return crypt_rand_string


# API Route for checking the client_id and client_secret
@app.route("/brilliantbot/api/auth", methods=["POST"])
@cross_origin()
def auth():
    # get the client_id and secret from the client application
    login = request.form.get("login")  # test1
    passwd = request.form.get("passwd")  # aaaaa

    print(login, passwd)

    # the client secret in the database is "hashed" with a one-way hash

    # hash_object = hashlib.sha1(bytes(client_secret_input, 'utf-8'))

    # str = generate_alphanum_crypt_string(32)
    # salt = bytearray(str, "utf-8")
    # print(str)

    # salt = os.urandom(32)
    # print(salt)
    # print(salt.decode("utf-8"))
    # salt = b'[\xe6B\xb6\xdb\x7f\x87\x0e\xe3s\xf8\x19\xae8m,mwC\xba:\xc9\xb9\x86\x97,\x13]\xa6je\xee'
    # print(salt.decode("utf-8"))

    # key = hashlib.pbkdf2_hmac(
    #     'sha256',  # Используемый алгоритм хеширования
    #     client_secret_input.encode('utf-8'),  # Конвертирование пароля в байты
    #     salt,  # Предоставление соли
    #     100000,  # Рекомендуется использоваться по крайней мере 100000 итераций SHA-256
    #     dklen=32)
    # # print(key)
    # key_str = key.hex()
    #
    # print(key_str)

    # hashed_client_secret = hash_object.hexdigest()

    # make a call to the model to authenticate
    authentication = authModel.authenticate(login, passwd)
    if not authentication:
        return jsonify({'success': False})
    else:
        # authentication['success'] = True
        # return {'success': True}
        return jsonify(authentication)
            #json.dumps(authentication)


# check the token's lifetime
@app.route("/brilliantbot/api/check_token", methods=["POST"])
def check_tk():
    # print (request.form)
    token = request.form.get("token")
    # print ("**",token)
    status = False
    if authModel.check_avialability(token) is None:
        return {'success': status, "login": None}
    else:
        login = authModel.check_avialability(token)
        status = authModel.check_token(token)
    return {'success': status, "login": login[0]}


@app.route("/brilliantbot/api/logout", methods=["POST"])
def logout():
    token = request.form.get("token")
    status = authModel.blacklist(token)
    return {'success': status}


@app.route("/brilliantbot/api/client", methods=["POST", "DELETE"])
def client():
    if request.method == 'POST':

        # verify the token
        authorization_header = request.headers.get('authorization')
        token = authorization_header.replace("Bearer ", "")
        verification = authModel.verify(token)

        if verification.get("isAdmin"):
            # get the client_id and secret from the client application
            client_id = request.form.get("client_id")
            client_secret_input = request.form.get("client_secret")
            is_admin = request.form.get("is_admin")

            # the client secret in the database is "hashed" with a one-way hash
            hash_object = hashlib.sha1(bytes(client_secret_input, 'utf-8'))
            hashed_client_secret = hash_object.hexdigest()

            # make a call to the model to authenticate
            create_response = authModel.create(client_id, hashed_client_secret, is_admin)
            return {'success': create_response}
        else:
            return {'success': False, 'message': 'Access Denied'}

    elif request.method == 'DELETE':
        # not yet implemented
        return {'success': False}
    else:
        return {'success': False}


# run the flask app.
if __name__ == "__main__":
    app.run(debug=True, port=5001)
