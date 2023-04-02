#export FLASK_APP=auth_main.py

from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/', methods=['POST'])
def login():
    data = request.get_json()
    login = data['login']
    passwd= data['password']
    print (login,passwd)
    return jsonify({'message': login+passwd})

if __name__ == '__main__':
    app.run(port=5001)
