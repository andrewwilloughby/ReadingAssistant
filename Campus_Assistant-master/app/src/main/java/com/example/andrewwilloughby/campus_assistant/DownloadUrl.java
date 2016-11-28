package com.example.andrewwilloughby.campus_assistant;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.HttpUrl;

/**
 * Created by andrewwilloughby on 27/11/2016.
 */

public class DownloadUrl {

    public String readUrl(String string) throws IOException {
        String data = "";
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
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();
            Log.d("downloadUrl", data.toString());
            bufferedReader.close();
        } catch (Exception e){
            Log.d("Exception", e.toString());
        } finally {
            inputStream.close();
            httpURLConnection.disconnect();
        }
        return data;
    }
}
