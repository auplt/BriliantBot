from flask import Flask, request, jsonify
import authModel

from flask_cors import CORS, cross_origin

# Инициализация приложения
app = Flask(__name__)
cors = CORS(app)
app.config['CORS_HEADERS'] = 'Content-Type'


# API для проверки логина и пароля
@app.route("/brilliantbot/api/auth", methods=["POST"])
@cross_origin()  # Применение cors для этого API
def auth():
    # Получение логина и пароля от формы
    login = request.form.get("login")
    passwd = request.form.get("passwd")

    # Вызов метода authentificate
    authentication = authModel.authenticate(login, passwd)
    if not authentication:
        authentication = {'success': False}
    return jsonify(authentication)


# Проверка времени жизни токена
@app.route("/brilliantbot/api/check_token", methods=["POST"])
def check_token():
    # Получение токена из запроса
    token = request.form.get("token")
    availability = authModel.check_availability(token)
    return jsonify(availability)


# Запуск приложения
if __name__ == "__main__":
    app.run(debug=True, port=5001)
