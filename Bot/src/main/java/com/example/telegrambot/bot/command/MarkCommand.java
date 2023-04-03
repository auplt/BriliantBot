package com.example.telegrambot.bot.command;

import com.example.telegrambot.api.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
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
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
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
            String token="";
            String tg_id=user.getId().toString();
            if (tg_id.equals("1103638534")) {
                token="e8ce4a906efe272e0d59bd2dc89a2e9bb46e2434a9238d2c10e402355665191d";

            }
            if (tg_id.equals("965427525")) {
                token="222d55e47295a96df56a0ffacfb7929496a91663ea94d967801bc372a8860144";

            }
            System.out.println(token);

            String str = "token="+ token;
            // @Deprecated HttpClient httpClient = new DefaultHttpClient();
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            try {
                HttpPost request = new HttpPost("http://127.0.0.1:5001/brilliantbot/api/check_token");
                StringEntity params = new StringEntity(str);
                request.addHeader("content-type", "application/x-www-form-urlencoded");
                request.setEntity(params);
                HttpResponse response = (HttpResponse) httpClient.execute(request);
                System.out.println("***"+response);
            } catch (Exception ex) {
            } finally {
                // @Deprecated httpClient.getConnectionManager().shutdown();
            }

/*

            final Content postResult = Request.Post("http://127.0.0.1:5001/brilliantbot/api/check_token")
                    .bodyString(str, ContentType.APPLICATION_JSON)
                    .execute().returnContent();
            System.out.println(postResult.asString());
*/
            /*
             var values = new HashMap<String, String>() {{
                put("token", finalToken);
            }};
                    var objectMapper = new ObjectMapper();
                                String requestBody = objectMapper
                                        .writeValueAsString(values);

                                HttpClient client = HttpClient.newHttpClient();
                                HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create("http://127.0.0.1:5001/brilliantbot/api/check_token"))
                                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                                        .build();

                                HttpResponse<String> response = client.send(request,
                                        HttpResponse.BodyHandlers.ofString());

                                System.out.println(response.body());
            */
            // System.out.println(user.getId().toString());

            //Student student = getCustomer("test1");
            String res = "";

            if ("test1".equals("test1")){
                res = "65913";

            }
            result = getMark(res);
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
