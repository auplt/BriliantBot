from Databases.dbtable import *


class TokensTable(DbTable):
    """Класс для работы с таблицей tokens БД authdb"""

    def __init__(self, config, db_name):
        super().__init__(config, db_name)

    def table_name(self):
        return "tokens"

    def columns(self):
        return {"login": ["varchar(64)", "PRIMARY KEY"],
                "token": ["varchar(512)"],
                "date_start": ["TIMESTAMP WITH TIME ZONE"],
                "date_end": ["TIMESTAMP WITH TIME ZONE"],
                "date_last": ["TIMESTAMP WITH TIME ZONE"],
                "address": ["varchar(512)"]}

    def find_by_id(self, login):
        cur = self.conn.cursor()
        cur.execute(f"SELECT * FROM {self.table_name()} WHERE login=%(login)s", {'login': str(login)})
        return cur.fetchone()

    def delete(self, login):
        cur = self.conn.cursor()
        cur.execute(f"DELETE FROM {self.table_name()} WHERE login=%(login)s", {"login": str(login)})
        self.conn.commit()
        return

    def update(self, login, vals):
        vals.append(login)
        vals = tuple(vals)
        cur = self.conn.cursor()
        sql = "UPDATE " + self.table_name() + \
              " SET token=%s, date_start=%s, date_end=%s, date_last=%s, address=%s WHERE login=%s"
        cur.execute(sql, vals)
        self.conn.commit()
        return

    def find_by_token(self, token):
        cur = self.conn.cursor()
        cur.execute(f"SELECT * FROM {self.table_name()} WHERE token=%(token)s", {'token': str(token)})
        return cur.fetchone()
