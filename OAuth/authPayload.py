from datetime import datetime
from datetime import timedelta


class AuthPayload(dict):

    def __init__(self, login, seconds):
        super().__init__()
        self.login = login  # Логин пользователя
        self.exp = datetime.utcnow() + timedelta(seconds=seconds)  # set the expiry attrbute to 30 minutes
