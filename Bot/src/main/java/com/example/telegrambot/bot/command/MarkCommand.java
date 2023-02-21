package com.example.telegrambot.bot.command;

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


public class MarkCommand extends CustomCommand {

    public MarkCommand() {
        super("mark", "get your average mark");
    }

    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        execute(absSender, message.getFrom(), message.getChat(), arguments);
        //System.out.println("MarkProcessMessage");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        //System.out.println("MarkExecute");

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId().toString());

        String result = "no avg_points";

        try {
            // System.out.println(user.getId().toString());
            result = getMark("65913");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        message.setText(result);
        execute(absSender, message, user);
    }

    public String getMark(String stud) throws IOException, ParseException {
        //System.out.println("MarkGetMark");
        URL url = new URL("http://127.0.0.1:5000/api/avg?stud=" + stud);
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
