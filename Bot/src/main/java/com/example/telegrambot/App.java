package com.example.telegrambot;

import com.example.telegrambot.bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class App {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi =
                    new TelegramBotsApi(
                            DefaultBotSession.class);
            botsApi.registerBot(new Bot());
            System.out.println("YES");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}