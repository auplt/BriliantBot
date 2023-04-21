from pathlib import Path

import yaml


class ProjectConfig:
    """Класс считывает базовые настройки из файла dbconfig.yaml"""

    def __init__(self):
        config_path = str(Path(__file__).parents[0])
        file_name = "dbconfig.yaml"
        new_path = '\\'.join([config_path, file_name])
        with open(new_path) as f:
            config = yaml.safe_load(f)
            self.host = config['host']
            self.password = config['password']
            self.user = config['user']
            self.port = config['port']


if __name__ == "__main__":
    x = ProjectConfig()
    print(x.password)
