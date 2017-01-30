package com.example.andrewwilloughby.campus_assistant;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

 class DownloadUrl {

    String readUrl(String string){
        String data;
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        String line;

        try {
            URL url = new URL(string);

            //Creating an http connection to communicate with url.
            httpURLConnection = (HttpURLConnection) url.openConnection();

            //Connecting to url.
            httpURLConnection.connect();

            //Reading data from url.
            inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            data = stringBuilder.toString();
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        } catch (Exception e){
            return null;
        }
        return data;
    }
}