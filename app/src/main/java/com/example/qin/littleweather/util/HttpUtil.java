package com.example.qin.littleweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by qin on 2016/3/30.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address,final HttpCallbackListener lisenter){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                URL url = null;
                try {
                    url = new URL(address);

                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8 * 1000);
                    connection.setReadTimeout(8 * 1000);
                    InputStream in = connection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
                    StringBuffer response = new StringBuffer();
                    String line;

                    while ((line = buffer.readLine()) !=null){
                        response.append(line);
                    }

                    if (lisenter == null) {
                        lisenter.onFinish(response.toString());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
