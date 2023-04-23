package com.example.telegrambot.core;

import com.example.telegrambot.api.Student;
import com.example.telegrambot.spi.Dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlRecordDao implements Dao<Student, Integer> {

    private static final Logger LOGGER = Logger.getLogger(PostgreSqlRecordDao.class.getName());
    private final Optional<Connection> connection;

    public PostgreSqlRecordDao() {
        this.connection = JdbcRecordConnection.getConnection();

/*  DO NOT ERASE THIS IS ANOTHER REALIZATION
        try {
            this.connection  = Optional.ofNullable(
                    DriverManager.getConnection("jdbc:postgresql://localhost:5432/studentsdb", "postgres", "WiRe7301"));
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
*/
    }

    @Override
    public Optional<Student> get(String login) {
        return connection.flatMap(conn -> {
            Optional<Student> student = Optional.empty();
            String sql = "SELECT * FROM records WHERE login = '" + login + "'";
//            System.out.println(sql);
            try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

                if (resultSet.next()) {
                    int record = resultSet.getInt("record");

                    student = Optional.of(new Student(login, record));

                    LOGGER.log(Level.INFO, "Found {0} in database", student.get());
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }

            return student;
        });
    }

    @Override
    public Collection<Student> getAll() {
        Collection<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM records";

        connection.ifPresent(conn -> {
            try (Statement statement = conn.createStatement(); ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    String login = resultSet.getString("login");
                    int record = resultSet.getInt("record");

                    Student student = new Student(login, record);

                    students.add(student);

                    LOGGER.log(Level.INFO, "Found {0} in database", student);
                }

            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        });

        return students;
    }

    @Override
    public void update(Student student) {

    }

}
