from Databases.dbtable import *


class PassTable(DbTable):
    """Класс для работы с таблицей passwords БД authdb"""

    def __init__(self, config, db_name):
        super().__init__(config, db_name)

    def table_name(self):
        return "passwords"

    def columns(self):
        return {"login": ["varchar(64)", "PRIMARY KEY"],
                "pass": ["varchar(64)"],
                "salt": ["varchar(32)"]}

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
        vals = tuple(vals)
        cur = self.conn.cursor()
        sql = "UPDATE " + self.table_name() + " SET record=%(record)s WHERE login=%(login)s"
        cur.execute(sql, {'record': str(vals[0]), 'login': str(login)})
        self.conn.commit()
        return
