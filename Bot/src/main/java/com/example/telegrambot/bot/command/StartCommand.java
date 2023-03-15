package com.example.telegrambot.bot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StartCommand extends CustomCommand {

    public StartCommand() {
        super("start", "start using bot");
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        message.setText("Добро пожаловать! \n"
                + "Вас приветствует бот @HelpToPlanStudyBot, у меня простые функции: помагать вам планировать учёбу. \n"
                + "Начнём?");
        super.processMessage(absSender, message, null);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        message.setText("Welcome! (^_^)");
        execute(absSender, message, user);
    }

}
