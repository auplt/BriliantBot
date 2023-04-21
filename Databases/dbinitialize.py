from pathlib import Path

from psycopg2 import OperationalError

from Databases.project_config import ProjectConfig
from Databases.createdb import InitConnection
from Databases.oauthdb.tables.pass_table import PassTable
from Databases.oauthdb.tables.tokens_table import TokensTable
from Databases.serverdb.records_table import RecordsTable
from Databases.sessiondb.sessions_table import SessionsTable


# Создание базы данных по переданному имени
def create_db(db_name):
    conn = InitConnection(config, db_name)
    while True:
        if conn.check_database():
            point = input(f"Database {db_name} already exists. Overwrite? Y/N: ")
            if point.upper() == "Y":
                conn.drop_database()
                conn.create_database()
                print(f"Database {db_name}: successfully created")
                return
        else:
            point = input(f"Create new database {db_name}? Y/N: ")
            if point.upper() == "Y":
                conn.create_database()
                print(f"Database {db_name}: successfully created")
                return
        if point.upper() == "N":
            print("Aborted")
            return
        if point.upper() not in ["Y", "N"]:
            point = input(f"Database {db_name} already exists. Overwrite? Y/N: ")
        if point.upper() in ["Y", "N"]:
            return


# Создание таблицы (на вход передается объект - таблица)
def create_table(table):
    while True:
        if table.check_table():
            point = input(f"Table {table.table_name()} already exists. Overwrite? Y/N: ")
            if point.upper() == "Y":
                table.drop()
                table.create()
                print(f"Table {table.table_name()}: successfully created")
                return
        else:
            point = input(f"Create new table {table.table_name()}? Y/N: ")
            if point.upper() == "Y":
                table.create()
                print(f"Table {table.table_name()}: successfully created")
                return
        if point.upper() == "N":
            print("Aborted")
            return
        if point.upper() not in ["Y", "N"]:
            point = input(f"Table {table.table_name()} already exists. Overwrite? Y/N ")
        if point.upper() in ["Y", "N"]:
            return


# Добавление данных в таблицу из csv файла (на вход - объект - таблица)
def insert_data(table, file_path):
    if table.check_table():
        while True:
            if table.all():
                point = input(f"Table {table.table_name()} already has some values. Overwrite? Y/N: ")
                if point.upper() == "Y":
                    table.delete_all()
                    table.insert_from_file(file_path)
                    print(f"Table {table.table_name()}: values successfully inserted")
                    return
            else:
                point = input(f"Insert new values in {table.table_name()}? Y/N: ")
                if point.upper() == "Y":
                    table.insert_from_file(file_path)
                    print(f"Table {table.table_name()}: values successfully inserted")
                    return
            if point.upper() == "N":
                print("Aborted")
                return
            if point.upper() not in ["Y", "N"]:
                point = input(f"Table {table.table_name()} already has some values. Overwrite? Y/N: ")
            if point.upper() in ["Y", "N"]:
                return
    else:
        print(f"Can't insert values: table {table.table_name()} doesn't exist")


# Проведение действий с БД oauth
def oauth(config_path):
    db_name = 'oauthdb'
    create_db(db_name)
    try:
        pt = PassTable(ProjectConfig(config_path), db_name)
        create_table(pt)
        insert_data(pt, r"./data/pass_table_insert.csv")
        tt = TokensTable(ProjectConfig(config_path), db_name)
        create_table(tt)
        insert_data(tt, r"./data/tokens_table_insert.csv")
    except OperationalError:
        print(f"Database {db_name} doesn't exist")


# Проведение действий с БД recordsdb
def records(config_path):
    db_name = 'recordsdb'
    create_db(db_name)
    try:
        rt = RecordsTable(ProjectConfig(config_path), db_name)
        create_table(rt)
        insert_data(rt, r"./data/records_table_insert.csv")
    except OperationalError:
        print(f"Database {db_name} doesn't exist")


# Проведение действий с БД sessionsdb
def sessions(config_path):
    db_name = 'sessionsdb'
    create_db(db_name)
    try:
        st = SessionsTable(ProjectConfig(config_path), db_name)
        create_table(st)
        insert_data(st, r"./data/sessions_table_insert.csv")
    except OperationalError:
        print(f"Database {db_name} doesn't exist")


# Относительный путь файла - до папки BrilliantBot включительно
config_path = str(Path(__file__).parents[1])
config = ProjectConfig(config_path)
while True:
    point = str(input("1 - oauthdb\n2 - recordsdb\n3 - sessionsdb\n0 - exit\nYour choise: "))
    while True:
        if point == "1":
            oauth(config_path)
        elif point == "2":
            records(config_path)
        elif point == "3":
            sessions(config_path)
        if point in ["1", "2", "3", "0"]:
            break
        point = str(input("1 - oauthdb\n2 - recordsdb\n3 - sessionsdb\n0 - exit\nYour choise: "))
    if point == '0':
        break
