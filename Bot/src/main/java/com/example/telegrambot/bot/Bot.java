package com.example.telegrambot.bot;

import com.example.telegrambot.bot.command.HelpCommand;
import com.example.telegrambot.bot.command.MarkCommand;
import com.example.telegrambot.bot.command.StartCommand;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Bot extends TelegramLongPollingCommandBot {

    private static final String BOT_NAME = "HelpToPlanStudyBot";
    private static final String BOT_TOKEN = "6015366458:AAHOfVyAH1zFCRIkmIgEBLh2artNvpntfTw";

    public Bot(DefaultBotOptions botOptions) {

        super(botOptions, true);
        this.register(new StartCommand());
        this.register(new HelpCommand(this));
        this.register(new MarkCommand());
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    public String getBotName() {
        return BOT_NAME;
    }

    public void processInvalidCommandUpdate(Update update) {
        System.out.println("Invalid Command");
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());

            String ReceivedText = update.getMessage().getText();
            message.setText(ReceivedText + " T_T");
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}