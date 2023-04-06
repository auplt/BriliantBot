package com.example.telegrambot;

import com.example.telegrambot.api.Session;
import com.example.telegrambot.core.JdbcSessionConnection;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;

public class PostgreSQL {

    public static void main(String[] args) {
        String test = "test3";
        String passwd = "ccccc";
        String str = "login=" + test + "&passwd=" + passwd;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        String responseBody = "";

        try {
            HttpPost request = new HttpPost("http://127.0.0.1:5001/brilliantbot/api/auth");
            StringEntity params = new StringEntity(str);
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);

            CloseableHttpResponse response = httpClient.execute(request);
            responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception ex) {
        } finally {
//                @Deprecated httpClient.getConnectionManager().shutdown();
        }

        JSONParser parser = new JSONParser();
        try {
            JSONObject JSobj = (JSONObject) parser.parse(responseBody);
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse((String) JSobj.get("end_date"));
            final Session[] session = {new Session(JSobj.get("login").toString(), "1111", date, JSobj.get("token").toString())};
            System.out.println(session[0]);



//            Optional<Connection> connection = Optional.empty();
            String url = "jdbc:postgresql://localhost:5432/sessionsdb";
            String user = "postgres";
            String password = "WiRe7301";
//            try {
//                connection = Optional.ofNullable(
//                        DriverManager.getConnection(url, user, password));
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }



            try (Connection connection = (DriverManager.getConnection(url, user, password))){

//                long now = System.currentTimeMillis();
//                Timestamp sqlTimestamp = new Timestamp(session[0].getCreateDate().getTime());
//                System.out.println("SqlTimestamp          : " + sqlTimestamp);
//                System.out.println("SqlTimestamp          : " + sqlTimestamp);
                String sql = "INSERT INTO sessions (login, tg_id, end_date, token) VALUES (?, ?, ?, ?)";
//                connection.setAutoCommit(false);

                PreparedStatement st = connection.prepareStatement(sql);
                System.out.println(new java.sql.Timestamp(session[0].getCreateDate().getTime()));
                java.sql.Timestamp timestamp =new java.sql.Timestamp(session[0].getCreateDate().getTime());
                st.setString(1, session[0].getLogin());
                st.setString(2, session[0].getTdId());
                st.setTimestamp(3, new java.sql.Timestamp(session[0].getCreateDate().getTime()));
                st.setString(4, session[0].getToken());
//                st.executeUpdate();
                int rows = st.executeUpdate();
//                connection.commit();
//                String sql = "INSERT INTO Products (ProductName, Price) Values (?, ?)";
//                PreparedStatement preparedStatement = conn.prepareStatement(sql);
//                preparedStatement.setString(1, name);
//                preparedStatement.setInt(2, price);
//
//                int rows = preparedStatement.executeUpdate();

                System.out.println(rows);
                connection.close();

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }


//            public Optional<Session> update(){
//
//
//
//
//                connection = JdbcSessionConnection.getConnection();
//             return connection.flatMap(conn -> {
//                    String sql = "INSERT INTO sessions (login, tg_id, create_date, token) VALUES (?, ?, ?, ?)";
////            System.out.println(sql);
//                    try (PreparedStatement st = conn.prepareStatement(sql)) {
//
//
////                    Date date1 = .  .parse((String) JSobj.get("create_date"));
////                    String date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format();
//
//                        System.out.println(session[0].getCreateDate());
//                        st.setString(1, session[0].getLogin());
//                        st.setString(2, session[0].getTdId());
//                        st.setDate(3, new java.sql.Date(session[0].getCreateDate().getTime()));
//                        st.setString(4, session[0].getToken());
////                    st.executeUpdate();
//
////                    if (resultSet.next()) {
//////                        int record = resultSet.getInt("record");
////                        String login = resultSet.getString("login");
////                        Date createDate = resultSet.getDate("create_date");
////                        String token = resultSet.getString("token");
////
////                        session[0] = Optional.of(new Session(login, tgId, createDate, token));
////
////                        LOGGER.log(Level.INFO, "Found {0} in database", session[0].get());
////                    }
//                    } catch (SQLException ex) {
//                        throw new RuntimeException(ex);
//                    }
//
//                    return session;
//                });
//            }
            } catch(ParseException e){
                throw new RuntimeException(e);
            } catch(Exception e){
                throw new RuntimeException(e);
            }







    }
}

