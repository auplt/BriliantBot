package com.example.telegrambot;

import com.example.telegrambot.api.Student;
import com.example.telegrambot.core.PostgreSqlDao;
import com.example.telegrambot.spi.Dao;
import java.util.Collection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerApplication {

    private static final Logger LOGGER = Logger.getLogger(CustomerApplication.class.getName());
    private static final Dao<Student, Integer> CUSTOMER_DAO = new PostgreSqlDao();

    public static void main(String[] args) {
        //Test whether an exception is thrown when
        //the database is queried for a non-existent student
        //But, if the student does exist, the details will be printed
        //on the console
        try {
            Student student = getCustomer("test1");
//            System.out.println(student);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
        }
        //Test whether a student can be added to the database
        getAllCustomers().forEach(System.out::println);
//        getCustomer("test1")
    }

    public static Student getCustomer(String login) throws Exception {
        Optional<Student> student = CUSTOMER_DAO.get(login);
        return student.orElseThrow(Exception::new);
    }

    public static Collection<Student> getAllCustomers() {
        return CUSTOMER_DAO.getAll();
    }
}
