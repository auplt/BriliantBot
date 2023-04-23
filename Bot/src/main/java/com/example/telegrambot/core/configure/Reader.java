package com.example.telegrambot.core.configure;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

public class Reader {

    public Config ReadYamlAsBean() {
        Yaml yaml = new Yaml(new Constructor(Config.class));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("dbconfig.yaml");
        Config data = yaml.load(inputStream);
        System.out.println(data);
        return data;
    }
}

