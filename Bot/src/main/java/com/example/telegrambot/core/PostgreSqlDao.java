package com.example.telegrambot.core;

import com.example.telegrambot.api.Student;
import com.example.telegrambot.spi.Dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlDao implements Dao<Student, Integer> {

    private static final Logger LOGGER = Logger.getLogger(PostgreSqlDao.class.getName());
    private final Optional<Connection> connection;

    public PostgreSqlDao() {
        this.connection = JdbcConnection.getConnection();
    }

    @Override
    public Optional<Student> get(String login) {
        return connection.flatMap(conn -> {
            Optional<Student> student = Optional.empty();
            String sql = "SELECT * FROM records WHERE login = '" + login + "'";
//            System.out.println(sql);
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

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
            try (Statement statement = conn.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

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

//    @Override
//    public Optional<Integer> save(Student student) {
//        String message = "The student to be added should not be null";
//        Student nonNullCustomer = Objects.requireNonNull(student, message);
//        String sql = "INSERT INTO "
//                + "student(first_name, last_name, email) "
//                + "VALUES(?, ?, ?)";
//
//        return connection.flatMap(conn -> {
//            Optional<Integer> generatedId = Optional.empty();
//
//            try (PreparedStatement statement = conn.prepareStatement(sql,
//                    Statement.RETURN_GENERATED_KEYS)) {
//
//                statement.setString(1, nonNullCustomer.getFirstName());
//                statement.setString(2, nonNullCustomer.getLastName());
//                statement.setString(3, nonNullCustomer.getEmail());
//
//                int numberOfInsertedRows = statement.executeUpdate();
//
//                //Retrieve the auto-generated id
//                if (numberOfInsertedRows > 0) {
//                    try (ResultSet resultSet = statement.getGeneratedKeys()) {
//                        if (resultSet.next()) {
//                            generatedId = Optional.of(resultSet.getInt(1));
//                        }
//                    }
//                }
//
//                LOGGER.log(Level.INFO, "{0} created successfully? {1}",
//                        new Object[]{nonNullCustomer, numberOfInsertedRows > 0});
//            } catch (SQLException ex) {
//                LOGGER.log(Level.SEVERE, null, ex);
//            }
//
//            return generatedId;
//        });
//    }
//
//    @Override
//    public void update(Student student) {
//        String message = "The student to be updated should not be null";
//        Student nonNullCustomer = Objects.requireNonNull(student, message);
//        String sql = "UPDATE student "
//                + "SET "
//                + "first_name = ?, "
//                + "last_name = ?, "
//                + "email = ? "
//                + "WHERE "
//                + "customer_id = ?";
//
//        connection.ifPresent(conn -> {
//            try (PreparedStatement statement = conn.prepareStatement(sql)) {
//
//                statement.setString(1, nonNullCustomer.getFirstName());
//                statement.setString(2, nonNullCustomer.getLastName());
//                statement.setString(3, nonNullCustomer.getEmail());
//                statement.setInt(4, nonNullCustomer.getId());
//
//                int numberOfUpdatedRows = statement.executeUpdate();
//
//                LOGGER.log(Level.INFO, "Was the student updated successfully? {0}",
//                        numberOfUpdatedRows > 0);
//
//            } catch (SQLException ex) {
//                LOGGER.log(Level.SEVERE, null, ex);
//            }
//        });
//    }
//
//    @Override
//    public void delete(Student student) {
//        String message = "The student to be deleted should not be null";
//        Student nonNullCustomer = Objects.requireNonNull(student, message);
//        String sql = "DELETE FROM student WHERE customer_id = ?";
//
//        connection.ifPresent(conn -> {
//            try (PreparedStatement statement = conn.prepareStatement(sql)) {
//
//                statement.setInt(1, nonNullCustomer.getId());
//
//                int numberOfDeletedRows = statement.executeUpdate();
//
//                LOGGER.log(Level.INFO, "Was the student deleted successfully? {0}",
//                        numberOfDeletedRows > 0);
//
//            } catch (SQLException ex) {
//                LOGGER.log(Level.SEVERE, null, ex);
//            }
//        });
//    }

}