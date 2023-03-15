from create import InitConnection
from dbconnection import DbConnection
from dbrecords import RecordsTable
from dbtable import DbTable
from project_config import ProjectConfig


class Main:

    def __init__(self):
        DbTable.dbconn = DbConnection(ProjectConfig())
        return

    @staticmethod
    def db_init():
        rt = RecordsTable()
        rt.create()
        return

    @staticmethod
    def default_insert():
        rt = RecordsTable()
        rt.insert_one(["test1", "65913"])
        rt.insert_one(["test2", "65917"])

    def main_cycle(self):
        self.db_init()
        self.default_insert()


config = ProjectConfig()
connection = InitConnection(config)
# connection.drop_database(config)
connection.create_database(config)
del connection


m = Main()
m.main_cycle()
del m
