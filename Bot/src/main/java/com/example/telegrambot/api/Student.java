package com.example.telegrambot.api;

public class Student {

    private String login;
    private Integer record;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getRecord() {
        return record;
    }

    public void setRecord(Integer record) {
        this.record = record;
    }

    public Student(String login, Integer record) {
        this.login = login;
        this.record = record;
    }


    @Override
    public String toString() {
        return "Student["
                + "login=" + login
                + ", record=" + record
                + ']';
    }

}