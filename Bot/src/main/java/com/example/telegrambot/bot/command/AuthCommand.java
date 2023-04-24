package com.example.telegrambot.bot.command;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class AuthCommand extends CustomCommand {
    public AuthCommand() {
        super("auth", "authtorise");
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] strings) {
        message.setText("Авторизируйтесь");
        super.processMessage(absSender, message, null);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        message.setText("Пожалуйста, авторизируйтесь:http://127.0.0.1:5050/Authentification%20Form/index.html?id="+user.getId());
        execute(absSender, message, user);
    }
}
