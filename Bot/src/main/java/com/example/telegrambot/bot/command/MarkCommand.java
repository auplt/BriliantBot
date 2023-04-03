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

import static com.example.telegrambot.core.SessionActions.getSession;
import static com.example.telegrambot.core.StudentActions.getStudent;

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
        String responseBody ="";
        try {
            String token="";
            Session session = getSession(user.getId().toString());
            token = session.getToken();

//            System.out.println(token);

            String str = "token="+ token;
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            try {
                HttpPost request = new HttpPost("http://127.0.0.1:5001/brilliantbot/api/check_token");
                StringEntity params = new StringEntity(str);
                request.addHeader("content-type", "application/x-www-form-urlencoded");
                request.setEntity(params);

                CloseableHttpResponse response = httpClient.execute(request);

                responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//                System.out.println("Response body: " + responseBody);
            } catch (Exception ex) {
            } finally {
//                @Deprecated httpClient.getConnectionManager().shutdown();
            }

            JSONParser parser = new JSONParser();
            JSONObject JSobj = (JSONObject) parser.parse(responseBody);
            Student student = getStudent((String) JSobj.get("login"));
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
