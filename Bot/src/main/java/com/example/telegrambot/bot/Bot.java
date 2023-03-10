package com.example.telegrambot.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
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
        return "HelpToPlanStudyBot";
    }

    @Override
    public String getBotToken() {
        return "6015366458:AAHOfVyAH1zFCRIkmIgEBLh2artNvpntfTw";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(update.getMessage().getText());

            String ReceivedText = update.getMessage().getText() + " T_T";

            message.setText(ReceivedText);
            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}