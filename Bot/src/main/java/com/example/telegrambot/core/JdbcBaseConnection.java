package com.example.telegrambot.core;

import com.example.telegrambot.core.configure.Config;
import com.example.telegrambot.core.configure.Reader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcBaseConnection {
    private static final Logger LOGGER = Logger.getLogger(JdbcSessionConnection.class.getName());
    static Reader reader = new Reader();
    private static Optional<Connection> connection = Optional.empty();

    public static Optional<Connection> getConnection(String dbName) {
        Config config = reader.ReadYamlAsBean();
        String url = "jdbc:postgresql://" + config.getHost() + ":" + config.getPort() + "/" + dbName;
        String user = config.getUser();
        String password = config.getPassword();

        try {
            connection = Optional.ofNullable(DriverManager.getConnection(url, user, password));
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return connection;
    }


    public static void main(String[] args) {
        JdbcSessionConnection.getConnection("sessionsdb");
//        System.out.println("\n*** Read YAML as Bean ***");
//        readYamlAsBean();
    }
}