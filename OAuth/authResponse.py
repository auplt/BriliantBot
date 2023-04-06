class authResponse(dict):

    def __init__(self, login, end_date, token):
        self.success = True
        self.login = login
        self.end_date = end_date
        # self.expiresin = expiresin
        self.token = token  # .decode('utf-8')