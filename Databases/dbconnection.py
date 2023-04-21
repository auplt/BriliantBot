import psycopg2


class DbConnection:
    """Класс для создания подключения к базе данных"""

    def __init__(self, config, db_name):
        self.conn = psycopg2.connect(dbname=db_name, user=config.user, password=config.password,
                                     host=config.host, port=config.port)

    def __del__(self):
        try:
            if self.conn:
                self.conn.close()
        except AttributeError:
            print("Connection hasn't been established")
