package com.example.telegrambot.bot.command;

import com.example.telegrambot.api.Student;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import static com.example.telegrambot.CustomerApplication.getCustomer;

public class MarkCommand extends CustomCommand {

    public MarkCommand() {
        super("mark", "get your average mark");
    }

    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        execute(absSender, message.getFrom(), message.getChat(), arguments);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        String result = "no avg_points";

        try {
            // System.out.println(user.getId().toString());
            Student student = getCustomer("test1");

            result = getMark(student.getRecord().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        message.setText(result);
        execute(absSender, message, user);
    }

    public String getMark(String login) throws IOException, ParseException {
        URL url = new URL("http://127.0.0.1:5000/api/avg?stud=" + login);
        Scanner scanner = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();
        while (scanner.hasNext()) {
            result.append(scanner.nextLine());
        }
        scanner.close();

        JSONParser parser = new JSONParser();
        JSONObject JSobj = (JSONObject) parser.parse(result.toString());

        return (String) JSobj.get("avg_points");
    }

}
