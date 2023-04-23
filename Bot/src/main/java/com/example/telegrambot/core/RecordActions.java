package com.example.telegrambot.core;

import com.example.telegrambot.api.Student;
import com.example.telegrambot.spi.Dao;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

public class RecordActions {

    private static final Logger LOGGER = Logger.getLogger(RecordActions.class.getName());
    private static final Dao<Student, Integer> STUDENT_DAO = new PostgreSqlRecordDao();

    public static Student getRecord(String login) throws Exception {
        Optional<Student> student = STUDENT_DAO.get(login);
        return student.orElseThrow(Exception::new);
    }

    public static Collection<Student> getAllStudents() {
        return STUDENT_DAO.getAll();
    }
}
