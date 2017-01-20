package com.example.andrewwilloughby.campus_assistant;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by andrewwilloughby on 27/11/2016.
 */

public class DownloadUrl {

    public String readUrl(String string) throws IOException {
        String data = null;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try {
            URL url = new URL(string);

            //Creating an http connection to communicate with url.
            httpURLConnection = (HttpURLConnection) url.openConnection();

            //Connecting to url.
            httpURLConnection.connect();

            //Reading data from url.
            inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();
            bufferedReader.close();
            inputStream.close();

            httpURLConnection.disconnect();
        } catch (Exception e){
            return null;
        }
        return data;
    }
}