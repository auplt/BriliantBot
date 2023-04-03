package com.example.telegrambot.core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JdbcStudentConnection {
    private static final Logger LOGGER =
            Logger.getLogger(JdbcStudentConnection.class.getName());
    private static Optional<Connection> connection = Optional.empty();
    public static Optional<Connection> getConnection() {
        if (connection.isEmpty()) {
            String url = "jdbc:postgresql://localhost:5432/studentsdb";
            String user = "postgres";
            String password = "WiRe7301";
            try {
                connection = Optional.ofNullable(
                        DriverManager.getConnection(url, user, password));
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        return connection;
    }
}
