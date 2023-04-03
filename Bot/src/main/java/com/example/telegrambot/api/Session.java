package com.example.telegrambot.api;

import java.util.Date;

public class Session {
    private String login;
    private String tdId;
    private Date createDate;
    private String token;

    public Session(String login, String tdId, Date createDate, String token) {
        this.login = login;
        this.tdId = tdId;
        this.createDate = createDate;
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getTdId() {
        return tdId;
    }

    public void setTdId(String tdId) {
        this.tdId = tdId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Session{" +
                "login='" + login + '\'' +
                ", tdId='" + tdId + '\'' +
                ", createDate=" + createDate +
                ", token='" + token + '\'' +
                '}';
    }
}
