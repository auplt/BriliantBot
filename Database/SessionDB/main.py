from create import InitConnection
from dbconnection import DbConnection
from db_sessions import RecordsTable
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
        rt.insert_one(["test1", "32874234", "2022-04-04 14:33:06.442768", "e8ce4a906efe272e0d59bd2dc89a2e9bb46e2434a9238d2c10e402355665191d"])
        rt.insert_one(["test2", "23943290", "2021-04-04 14:33:06.442768", "222d55e47295a96df56a0ffacfb7929496a91663ea94d967801bc372a8860144"])

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
