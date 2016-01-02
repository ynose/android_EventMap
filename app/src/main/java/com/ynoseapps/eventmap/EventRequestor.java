package com.ynoseapps.eventmap;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yoshio on 16/01/03.
 */
public class EventRequestor {

    // Jsonを取得
    public static JSONArray requestJason(URL url) throws IOException, JSONException {

        HttpURLConnection connection = null;
        JSONArray json = null;

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode() != 200) {
                return null;
            }

            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            ByteArrayOutputStream responseArray = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];

            int length;
            while((length = inputStream.read(buff)) != -1) {
                if(length > 0) {
                    responseArray.write(buff, 0, length);
                }
            }

            json = new JSONArray(new String(responseArray.toByteArray()));

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            throw e;
        } finally{
            if (connection != null) {
                connection.disconnect();
            }
        }

        return json;
    }


}
