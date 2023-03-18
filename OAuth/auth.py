import os

from flask import Flask, request
import json
import hashlib

import authModel

import secrets
import string

# instantiate the Flask app.
app = Flask(__name__)

def generate_alphanum_crypt_string(length):
    letters_and_digits = string.ascii_letters + string.digits + string.punctuation
    crypt_rand_string = ''.join(secrets.choice(
        letters_and_digits) for i in range(length))
    return crypt_rand_string

# API Route for checking the client_id and client_secret
@app.route("/auth", methods=["POST"])
def auth():
    # get the client_id and secret from the client application
    client_id = request.form.get("client_id") # test1
    client_secret_input = request.form.get("client_secret") # aaaaa

    print(client_id, client_secret_input)

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
    authentication = authModel.authenticate(client_id, client_secret_input)
    if authentication == False:
        return {'success': False}
    else:
        return json.dumps(authentication)


# API route for verifying the token passed by API calls
@app.route("/verify", methods=["POST"])
def verify():
    # verify the token
    authorizationHeader = request.headers.get('authorization')
    token = authorizationHeader.replace("Bearer ", "")
    verification = authModel.verify(token)
    return verification


@app.route("/logout", methods=["POST"])
def logout():
    token = request.form.get("token")
    status = authModel.blacklist(token)
    return {'success': status}


@app.route("/client", methods=["POST", "DELETE"])
def client():
    if request.method == 'POST':

        # verify the token
        authorizationHeader = request.headers.get('authorization')
        token = authorizationHeader.replace("Bearer ", "")
        verification = authModel.verify(token)

        if verification.get("isAdmin") == True:
            # get the client_id and secret from the client application
            client_id = request.form.get("client_id")
            client_secret_input = request.form.get("client_secret")
            is_admin = request.form.get("is_admin")

            # the client secret in the database is "hashed" with a one-way hash
            hash_object = hashlib.sha1(bytes(client_secret_input, 'utf-8'))
            hashed_client_secret = hash_object.hexdigest()

            # make a call to the model to authenticate
            createResponse = authModel.create(client_id, hashed_client_secret, is_admin)
            return {'success': createResponse}
        else:
            return {'success': False, 'message': 'Access Denied'}

    elif request.method == 'DELETE':
        # not yet implemented
        return {'success': False}
    else:
        return {'success': False}


# run the flask app.
if __name__ == "__main__":
    app.run(debug=True)
