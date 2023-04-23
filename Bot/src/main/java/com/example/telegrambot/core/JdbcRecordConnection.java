package com.example.telegrambot.core;

import java.sql.Connection;
import java.util.Optional;
import java.util.logging.Logger;

public class JdbcRecordConnection {
    private static final Logger LOGGER = Logger.getLogger(JdbcRecordConnection.class.getName());
    private static Optional<Connection> connection = Optional.empty();

    public static Optional<Connection> getConnection() {
        if (connection.isEmpty()) {
            connection = JdbcBaseConnection.getConnection("recordsdb");
        }
        return connection;
    }
}
