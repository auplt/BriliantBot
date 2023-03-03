from create import InitConnection
from dbconnection import DbConnection
from dbrecords import RecordsTable
from dbtable import DbTable
from project_config import ProjectConfig


class Main:
    config = ProjectConfig()
    connection = DbConnection(config)

    def __init__(self):
        DbTable.dbconn = self.connection
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
    # connection.drop_database(config)

    # del connection


config = ProjectConfig()
connection = InitConnection(config)
connection.create_database(config)
m = Main()
m.main_cycle()
