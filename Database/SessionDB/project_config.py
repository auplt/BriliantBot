import yaml


class ProjectConfig:
    """Класс считывает базовые настройки из файла config.yaml"""

    def __init__(self):
        with open('config.yaml') as f:
            config = yaml.safe_load(f)
            self.host = config['host']
            self.password = config['password']
            self.user = config['user']
            self.database = config['database']
            self.port = config['port']


if __name__ == "__main__":
    x = ProjectConfig()
    print(x.password)
