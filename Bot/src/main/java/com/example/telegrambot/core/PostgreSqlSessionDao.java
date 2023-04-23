package com.example.telegrambot.core;

import com.example.telegrambot.api.Session;
import com.example.telegrambot.spi.Dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlSessionDao implements Dao<Session, Integer> {

    private static final Logger LOGGER = Logger.getLogger(com.example.telegrambot.core.PostgreSqlSessionDao.class.getName());
    private final Optional<Connection> connection;

    public PostgreSqlSessionDao() {
        this.connection = JdbcSessionConnection.getConnection();
    }

    @Override
    public Optional<Session> get(String login) {
        return connection.flatMap(conn -> {
            Optional<Session> session = Optional.empty();
            String sql = "SELECT * FROM sessions WHERE login = '" + login + "'";
            try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    String tgId = resultSet.getString("tg_id");
                    Date createDate = resultSet.getDate("end_date");
                    String token = resultSet.getString("token");

                    session = Optional.of(new Session(login, tgId, createDate, token));

                    LOGGER.log(Level.INFO, "Found {0} in database", session.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
            return session;
        });
    }

    @Override
    public Optional<Session> getById(String tgId) {
        return connection.flatMap(conn -> {
            Optional<Session> session = Optional.empty();
            String sql = "SELECT * FROM sessions WHERE tg_id = '" + tgId + "'";
            try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {
                if (resultSet.next()) {
                    String login = resultSet.getString("login");
                    Date createDate = resultSet.getDate("end_date");
                    String token = resultSet.getString("token");

                    session = Optional.of(new Session(login, tgId, createDate, token));

                    LOGGER.log(Level.INFO, "Found {0} in database", session.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return session;
        });
    }

    @Override
    public Collection<Session> getAll() {
        Collection<Session> sessions = new ArrayList<>();
        String sql = "SELECT * FROM sessions";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String login = resultSet.getString("login");
                    String tdId = resultSet.getString("tg_id");
                    Date createDate = resultSet.getDate("create_date");
                    String token = resultSet.getString("token");

                    Session session = new Session(login, tdId, createDate, token);
                    sessions.add(session);

                    LOGGER.log(Level.INFO, "Found {0} in database");
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });
        return sessions;
    }

    @Override
    public void insert(Session session) {
        String sql = "INSERT INTO sessions (login, tg_id, end_date, token) VALUES (?, ?, ?, ?)";

        connection.ifPresent(conn -> {
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                java.sql.Timestamp timestamp = new java.sql.Timestamp(session.getCreateDate().getTime());
                st.setString(1, session.getLogin());
                st.setString(2, session.getTdId());
                st.setTimestamp(3, timestamp);
                st.setString(4, session.getToken());

                st.executeUpdate();

                LOGGER.log(Level.INFO, "Insert completed");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
    }

    @Override
    public void update(Session session) {
        String sql = "UPDATE sessions SET tg_id=?, end_date=?, token=? WHERE login=?";

        connection.ifPresent(conn -> {
            try {
                PreparedStatement st = conn.prepareStatement(sql);
                java.sql.Timestamp timestamp = new java.sql.Timestamp(session.getCreateDate().getTime());
                st.setString(1, session.getTdId());
                st.setTimestamp(2, timestamp);
                st.setString(3, session.getToken());
                st.setString(4, session.getLogin());

                st.executeUpdate();

                LOGGER.log(Level.INFO, "Update completed", session.toString());
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
            }
        });
    }
}

