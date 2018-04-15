package com.example.andrei.newsappstage1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Andrei on 13.04.2018.
 * <p>
 * The model of the HttpHandler is taken from the Pokemon app.
 * The handler has the role to connect to the API and get the data.
 */

public class HttpHandler {

    public HttpHandler() {
    }

    public String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();       //set connection
            urlConnection.setRequestMethod("GET");                          //GET method for retrieving data
            urlConnection.setReadTimeout(10000);                            //delays for no-answer handle
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();                                        //connect
            inputStream = urlConnection.getInputStream();                   //get the date
            jsonResponse = convertStreamToString(inputStream);              //convert the stream to string

        }   //the internet exception will be thrown to the calling class, no need to catch it here
        finally {
            if (urlConnection != null) {        //close connections
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return jsonResponse;
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
