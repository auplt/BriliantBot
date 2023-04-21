import csv
from Databases.dbconnection import DbConnection


class DbTable(DbConnection):
    """Родительский класс для всех таблиц"""

    def __init__(self, config, db_name):
        super().__init__(config, db_name)
        return

    def table_name(self):
        return "table"

    def columns(self):
        return self.columns().keys()

    def primary_key(self):
        return ['login']

    def column_names_without_id(self):
        res = list(self.columns().keys())
        if 'login' in res:
            res.remove('login')
        return res

    def table_constraints(self):
        return []

    def create(self):
        sql = "CREATE TABLE IF NOT EXISTS " + self.table_name() + "("
        arr = [k + " " + " ".join(v) for k, v in self.columns().items()]
        sql += ", ".join(arr + self.table_constraints())
        sql += ")"
        cur = self.conn.cursor()
        cur.execute(sql)
        self.conn.commit()
        cur.close()
        return

    def drop(self):
        cur = self.conn.cursor()
        cur.execute(f"DROP TABLE IF EXISTS {self.table_name()}")
        self.conn.commit()
        return

    def insert_one(self, vals):
        vals = tuple(vals)
        sql = "INSERT INTO " + self.table_name() + "("
        sql += ", ".join(self.columns()) + ") VALUES( "
        sql += "%s, " * len(vals)
        sql = sql.removesuffix(', ')
        sql += ')'
        cur = self.conn.cursor()
        cur.execute(sql, vals)
        self.conn.commit()
        return

    def all(self):
        sql = "SELECT * FROM " + self.table_name()
        sql += " ORDER BY "
        sql += ", ".join(self.primary_key())
        cur = self.conn.cursor()
        cur.execute(sql)
        return cur.fetchall()

    def check_table(self):
        sql = f"SELECT 1  FROM pg_catalog.pg_tables WHERE tablename = '{self.table_name()}'"
        cur = self.conn.cursor()
        cur.execute(sql)
        return cur.fetchall()

    def insert_from_file(self, file_path):
        with open(file_path, 'r') as file:
            heading = next(file)
            reader = csv.reader(file, delimiter=';')
            for row in reader:
                self.insert_one(row)

    def delete_all(self):
        sql = f"DELETE FROM {self.table_name()}"
        cur = self.conn.cursor()
        cur.execute(sql)
        self.conn.commit()
        return
