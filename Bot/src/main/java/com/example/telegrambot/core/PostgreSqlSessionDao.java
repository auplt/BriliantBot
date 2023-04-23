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

/*      DO NOT ERASE THIS IS ANOTHER REALIZATION
        try {
            this.connection  = Optional.ofNullable(
                    DriverManager.getConnection("jdbc:postgresql://localhost:5432/sessionsdb", "postgres", "WiRe7301"));
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
 */
    }

    @Override
    public Optional<Session> get(String tgId) {
        return connection.flatMap(conn -> {
            Optional<Session> session = Optional.empty();
            String sql = "SELECT * FROM sessions WHERE tg_id = '" + tgId + "'";
//            System.out.println(sql);
            try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
//                        int record = resultSet.getInt("record");
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

                    LOGGER.log(Level.INFO, "Found {0} in database", session);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return sessions;
    }

    @Override
    public void update(Session session) {
        String sql = "INSERT INTO sessions (login, tg_id, end_date, token) VALUES (?, ?, ?, ?)";

        connection.ifPresent(conn -> {
//                connection.setAutoCommit(false);

            try {
                PreparedStatement st = conn.prepareStatement(sql);
                System.out.println(new java.sql.Timestamp(session.getCreateDate().getTime()));
                java.sql.Timestamp timestamp = new java.sql.Timestamp(session.getCreateDate().getTime());
                st.setString(1, session.getLogin());
                st.setString(2, session.getTdId());
                st.setTimestamp(3, new java.sql.Timestamp(session.getCreateDate().getTime()));
                st.setString(4, session.getToken());
//                st.executeUpdate*/();
                int rows = st.executeUpdate();
                System.out.println(rows);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, null, e);
//                throw new RuntimeException(e);
            }


        });
//        return Optional.empty();
    }


}

