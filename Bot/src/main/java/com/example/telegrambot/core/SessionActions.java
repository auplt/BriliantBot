package com.example.telegrambot.core;

import com.example.telegrambot.api.Session;
import com.example.telegrambot.exception.SessionException;
import com.example.telegrambot.spi.Dao;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

public class SessionActions {

    private static final Logger LOGGER = Logger.getLogger(SessionActions.class.getName());
    private static final Dao<Session, Integer> SESSION_DAO = new PostgreSqlSessionDao();

    public static Session getSessionByID(String id) throws Exception {
//        System.out.println(login);
        Optional<Session> session = SESSION_DAO.getById(id);
        System.out.println(session.toString());
        return session.orElseThrow(Exception::new);
    }

    public static Session getSession(String login) throws Exception {
//        System.out.println(login);
        Optional<Session> session = SESSION_DAO.get(login);
//        System.out.println(session.toString());
        return session.orElseThrow(Exception::new);
    }


    public static void updateSession(Session session) {
        SESSION_DAO.update(session);
    }

    public static void insertSession(Session session) {
        SESSION_DAO.insert(session);
    }

    public static Collection<Session> getAllSession() {
        return SESSION_DAO.getAll();
    }
}
