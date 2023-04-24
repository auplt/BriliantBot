package com.example.telegrambot.server;

import com.example.telegrambot.api.Session;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.telegrambot.core.SessionActions.*;

public class API implements Runnable {

    public void run() {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8002), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.createContext("/telegrambot/api/session", (exchange -> {
            if (exchange.getRequestMethod().equals("POST")) {
                InputStream inputStream = exchange.getRequestBody();
                String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

                JSONObject json = new JSONObject();
                String[] words = result.split("&");
                for (String word : words) {
                    String[] values = word.split("=");
                    json.put(values[0], values[1]);
                }
                try {
                    String preparedDate = ((String) json.get("end_date")).replaceAll("%20", " ");
                    String prepDate = preparedDate.replaceAll("%3A", ":");
                    String newDate = prepDate.replaceAll("\\+", " ");

                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(newDate);
                    final Session[] session = {new Session(json.get("login").toString(), json.get("id").toString(), date, json.get("token").toString())};
                    System.out.println(session[0].toString());
                    try {
                        getSession(session[0].getLogin());
                        updateSession(session[0]);
                    } catch (Exception e) {
                        insertSession(session[0]);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-type", "application/json");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                JSONObject response = new JSONObject();
                response.put("success", true);

                String resp = response.toString();

                try {
                    exchange.sendResponseHeaders(200, resp.length());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                outputStream.write(resp.getBytes());
                outputStream.flush();
                outputStream.close();
            } else {
                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
            }
            exchange.close();
        }));
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}