from pathlib import Path

import yaml


class ProjectConfig:
    """Класс считывает базовые настройки из файла dbconfig.yaml"""

    def __init__(self, config_path):
        rel_path = "Databases\\dbconfig.yaml"
        new_path = '\\'.join([config_path, rel_path])
        with open(new_path) as f:
            config = yaml.safe_load(f)
            self.host = config['host']
            self.password = config['password']
            self.user = config['user']
            self.port = config['port']


if __name__ == "__main__":
    config_path = str(Path(__file__).parents[1])
    x = ProjectConfig(config_path)
    print(x.password)
