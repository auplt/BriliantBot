class AuthResponse(dict):

    def __init__(self, login, end_date, token):
        super().__init__()
        self.success = True
        self.login = login
        self.end_date = end_date
        self.token = token
