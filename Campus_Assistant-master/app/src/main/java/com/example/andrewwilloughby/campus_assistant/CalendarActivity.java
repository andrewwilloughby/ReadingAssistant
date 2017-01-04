package com.example.andrewwilloughby.campus_assistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;

import java.io.BufferedInputStream;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {

    private String TAG = CalendarActivity.class.getSimpleName();
    CalendarView calendarView;
    private ListView listView;
    ArrayList<HashMap<String, String>> dayClassList;
    private ProgressDialog progressDialog;
    Calendar studentCalendar;
    Context context;

    private static String file_url = "https://www.reading.ac.uk/mytimetable/m/10051/5eb6628e04674376";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        setTitle("Student Timetable");
        context = this;

        String file_url = "https://www.reading.ac.uk/mytimetable/m/10051/5eb6628e04674376";

        new DownloadTimetable().execute(file_url);


        listView = (ListView) findViewById(R.id.dayEventsListView);

        calendarView = (CalendarView) findViewById(R.id.timetableCalendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
            }
        });
    }

    private class DownloadTimetable extends AsyncTask<String, Void, Void>{

        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(CalendarActivity.this);
            progressDialog.setMessage("Downloading timetable, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... f_url) {
            int count;
            HttpURLConnection connection = null;
            String fileName = "timetable.ics";
            InputStream inputStream = null;
            StringBuilder stringBuilder = null;
            String calendarString = null;


            try{
                URL url = new URL(f_url[0]);

                connection = (HttpURLConnection) url.openConnection();

                inputStream = new BufferedInputStream(connection.getInputStream());

            } catch (MalformedURLException e) {
                Log.e("URL Error: ", e.getMessage());
            } catch (IOException e) {
                Log.e("IO Error: ", e.getMessage());
            }

            try{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                stringBuilder = new StringBuilder();
                char symbol;
                int counter = 0;
                String thisLine = null;

                while ((thisLine = bufferedReader.readLine()) != null){
                    stringBuilder.append(thisLine);
                    stringBuilder.append("\r\n");
                    System.out.println(thisLine);
                }
                calendarString = stringBuilder.toString();

            } catch (UnsupportedEncodingException e) {
                Log.e("Encoding Error: ", e.getMessage());
            } catch (IOException e) {
                Log.e("IO Error: ", e.getMessage());
            }

            if (calendarString != null){
                StringReader stringReader = new StringReader(calendarString);
                CalendarBuilder calendarBuilder = new CalendarBuilder();
                try {
                    studentCalendar = calendarBuilder.build(stringReader);
                } catch (IOException e) {
                    Log.e("IO Error: ", e.getMessage());
                } catch (ParserException e) {
                    Log.e("Parsing Error: ", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }
}