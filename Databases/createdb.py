import psycopg2
from psycopg2 import Error
from psycopg2.extensions import ISOLATION_LEVEL_AUTOCOMMIT


class InitConnection:
    """Класс для подключения к Postgres и создания баз данных"""

    def __init__(self, config, db_name):
        self.conn = psycopg2.connect(user=config.user, password=config.password, host=config.host, port=config.port)
        self.conn.set_isolation_level(ISOLATION_LEVEL_AUTOCOMMIT)
        self.dbname = db_name

    def __del__(self):
        if self.conn:
            self.conn.close()

    # Проверка наличия базы данных
    def check_database(self):
        cur = self.conn.cursor()
        cur.execute(f"SELECT 1 FROM pg_catalog.pg_database WHERE datname = '{self.dbname}'")
        exists = cur.fetchone()
        if exists:
            return True
        else:
            return False

    # Создание базы данных
    def create_database(self):
        try:
            cur = self.conn.cursor()
            cur.execute(f'CREATE DATABASE {self.dbname}')
        except (Exception, Error) as error:
            print("Ошибка при работе с PostgreSQL", error)

    # Удаление базы данных
    def drop_database(self):
        try:
            cur = self.conn.cursor()
            cur.execute(f"UPDATE pg_database SET datallowconn = 'false' WHERE datname = '{self.dbname}'")
            cur.execute(
                f"SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = '{self.dbname.lower()}' AND pid <> pg_backend_pid()");
            cur.execute(f"DROP DATABASE IF EXISTS {self.dbname}")
            self.conn.commit()
        except (Exception, Error) as error:
            print("Ошибка при работе с PostgreSQL", error)
        finally:
            if self.conn:
                cur.close()
