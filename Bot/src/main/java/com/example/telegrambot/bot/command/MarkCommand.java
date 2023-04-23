package com.example.telegrambot.bot.command;

import com.example.telegrambot.api.Session;
import com.example.telegrambot.api.Student;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
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
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static com.example.telegrambot.core.SessionActions.getSessionByID;
import static com.example.telegrambot.core.RecordActions.getRecord;

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
        String responseBody = "";

        try {
            Session session = getSessionByID(user.getId().toString());
//            System.out.println(session);
            String token = session.getToken();

            System.out.println(token);

            String str = "token=" + token;

            try {
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost request = new HttpPost("http://127.0.0.1:5001/brilliantbot/api/check_token");
                StringEntity params = new StringEntity(str);
                request.addHeader("content-type", "application/x-www-form-urlencoded");
                request.setEntity(params);

                CloseableHttpResponse response = httpClient.execute(request);

                responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONParser parser = new JSONParser();
            JSONObject JSobj = (JSONObject) parser.parse(responseBody);
            System.out.println(JSobj.get("success"));
            if ((Boolean) JSobj.get("success")) {
                Student student = getRecord(session.getLogin());
                System.out.println(student);
                String result = getMark(student.getRecord().toString());

                message.setText(result);
                execute(absSender, message, user);

            } else {
                System.out.println("Auth failed");
                AuthCommand auth = new AuthCommand();
                auth.execute(absSender, user, chat, arguments);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Auth failed");
            AuthCommand auth = new AuthCommand();
            auth.execute(absSender, user, chat, arguments);
        }
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
