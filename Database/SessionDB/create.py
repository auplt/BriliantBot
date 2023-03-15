import psycopg2
from psycopg2 import Error
from psycopg2.extensions import ISOLATION_LEVEL_AUTOCOMMIT


class InitConnection:
    def __init__(self, config):
        self.conn = psycopg2.connect(user=config.user, password=config.password, host=config.host, port=config.port)
        self.conn.set_isolation_level(ISOLATION_LEVEL_AUTOCOMMIT)

    def __del__(self):
        if self.conn:
            self.conn.close()

    def create_database(self, config):
        try:
            cur = self.conn.cursor()
            cur.execute(f"SELECT 1 FROM pg_catalog.pg_database WHERE datname = '{config.database.lower()}'")
            exists = cur.fetchone()
            if not exists:
                cur.execute(f'CREATE DATABASE {config.database}')
        except (Exception, Error) as error:
            print("Ошибка при работе с PostgreSQL", error)

    def drop_database(self, config):
        try:
            cur = self.conn.cursor()
            cur.execute(f"UPDATE pg_database SET datallowconn = 'false' WHERE datname = '{config.database.lower()}'")
            cur.execute(
                f"SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = '{config.database.lower()}' AND pid <> pg_backend_pid()");
            cur.execute(f"DROP DATABASE IF EXISTS {config.database}")
            self.conn.commit()
        except (Exception, Error) as error:
            print("Ошибка при работе с PostgreSQL", error)
        finally:
            if self.conn:
                cur.close()
