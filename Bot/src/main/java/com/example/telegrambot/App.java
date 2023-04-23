package com.example.telegrambot;

import com.example.telegrambot.bot.Bot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(com.example.telegrambot.App.class.getName());

    public void run() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot(new DefaultBotOptions()));

            String re = InetAddress.getLoopbackAddress().getHostAddress();
            String hostname = InetAddress.getLocalHost().getHostAddress();
            String host = InetAddress.getLocalHost().getCanonicalHostName();
            System.out.println("(" + host + ") " + re + " -> " + hostname);
            System.out.println("ONLINE");

        } catch (TelegramApiException | UnknownHostException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, null, e);
        }
    }
}