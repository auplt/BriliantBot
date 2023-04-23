package com.example.telegrambot.core;

import com.example.telegrambot.api.Session;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.telegrambot.core.SessionActions.updateSession;

public class JdbcSessionConnection extends JdbcBaseConnection {
    private static final Logger LOGGER = Logger.getLogger(JdbcSessionConnection.class.getName());

    private static Optional<Connection> connection = Optional.empty();

    public static Optional<Connection> getConnection() {
        if (connection.isEmpty()) {
            connection = JdbcBaseConnection.getConnection("sessionsdb");
        }
        return connection;
    }

    public static void main(String[] args) {
//        JdbcSessionConnection.getConnection();
//        JdbcRecordConnection.getConnection();
//        JdbcSessionConnection.getConnection();

        String test = "test3";
        String passwd = "ccccc";
        String str = "login=" + test + "&password=" + passwd;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        String responseBody = "";

        try {
            HttpPost request = new HttpPost("http://127.0.0.1:5001/brilliantbot/api/auth");
            StringEntity params = new StringEntity(str);
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);

            CloseableHttpResponse response = httpClient.execute(request);
            responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            System.out.println(responseBody);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        JSONParser parser = new JSONParser();
        try {
            JSONObject JSobj = (JSONObject) parser.parse(responseBody);
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse((String) JSobj.get("end_date"));
            final Session[] session = {new Session(JSobj.get("login").toString(), "1111", date, JSobj.get("token").toString())};
            System.out.println(session[0]);
            updateSession(session[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
