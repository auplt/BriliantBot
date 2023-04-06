class authResponse(dict):

    def __init__(self, login, create_date, token):
        self.success = True
        self.login = login
        self.create_date = create_date
        # self.expiresin = expiresin
        self.token = token  # .decode('utf-8')