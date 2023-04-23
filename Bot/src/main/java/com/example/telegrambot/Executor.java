package com.example.telegrambot;

import com.example.telegrambot.server.API;

public class Executor {
    static App app;
    static API api;

    public static void main(String[] args){
        app = new App();

        Thread appThread = new Thread(app);
        appThread.start();

        api = new API();
        Thread apiThread = new Thread(api);
        apiThread.start();

        System.out.println("Thread stopped");

    }
}
