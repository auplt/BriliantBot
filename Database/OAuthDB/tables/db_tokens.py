from dbtable import *


class TokensTable(DbTable):

    def table_name(self):
        return "tokens"

    def columns(self):
        return {"login": ["varchar(64)", "PRIMARY KEY"],
                "token": ["varchar(64)"],
                "date_start": ["TIMESTAMP"],
                "date_end": ["TIMESTAMP"],
                "date_last": ["TIMESTAMP"],
                "address": ["varchar(512)"]}

    def find_by_id(self, login):
        cur = self.dbconn.conn.cursor()
        cur.execute(f"SELECT * FROM {self.table_name()} WHERE login=%(login)s", {'login': str(login)})
        return cur.fetchone()

    def delete(self, login):
        cur = self.dbconn.conn.cursor()
        cur.execute(f"DELETE FROM {self.table_name()} WHERE login=%(login)s", {"del": str(login)})
        self.dbconn.conn.commit()
        return

    def update(self, login, vals):
        vals = tuple(vals)
        cur = self.dbconn.conn.cursor()
        sql = "UPDATE " + self.table_name() + " SET record=%(record)s WHERE login=%(login)s"
        cur.execute(sql, {'record': str(vals[0]), 'login': str(login)})
        self.dbconn.conn.commit()
        return
