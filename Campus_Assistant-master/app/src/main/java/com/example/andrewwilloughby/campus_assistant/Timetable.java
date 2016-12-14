package com.example.andrewwilloughby.campus_assistant;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Timetable extends AppCompatActivity {

    private String TAG = Timetable.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ListView listView;
    private static String url;
    ArrayList<HashMap<String,String>> classList;
    FileOutputStream outputStream;

    private static String file_url = "https://www.reading.ac.uk/mytimetable/m/10051/5eb6628e04674376";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        setTitle("Student Timetable");


        listView = (ListView) findViewById(R.id.list);

        new GetTimetable().execute();

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.timetable_swipe_layout);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                classList.clear();
                swipeLayout.setRefreshing(true);
                new GetTimetable().execute();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    private class GetTimetable extends AsyncTask<String, String, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Downloading file. Please wait...");
            progressDialog.setIndeterminate(false);
            progressDialog.setMax(100);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... f_url) {
            int count;

            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();

                InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                OutputStream outputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "timetable.ics");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = inputStream.read(data)) != -1){
                    total += count;
                    //publishing the progress
                    //after this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / fileLength));

                    //writing data to file
                    outputStream.write(data, 0, count);
                }

                outputStream.flush();

                outputStream.close();
                inputStream.close();

                FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory() + "timetable.ics");

                CalendarBuilder builder = new CalendarBuilder();
                Calendar calendar = builder.build(fileInputStream);

                java.util.Calendar today = java.util.Calendar.getInstance();
                today.set(java.util.Calendar.HOUR_OF_DAY, 0);
                today.clear(java.util.Calendar.MINUTE);
                today.clear(java.util.Calendar.SECOND);

                Period period = new Period(new DateTime(today.getTime()), new Dur(1,0,0,0));

                Filter filter = new Filter(new PeriodRule(period));

                List eventsToday = (List) filter.filter(calendar.getComponents(Component.VEVENT));

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }


            return null;

//            HttpHandler sh = new HttpHandler();
//
//            // Making a request to url and getting response
//            String calStr = sh.makeServiceCall("https://www.reading.ac.uk/mytimetable/m/10051/5eb6628e04674376");
//
//            try{
//                outputStream = openFileOutput("timetable", Context.MODE_PRIVATE);
//                outputStream.write(calStr.getBytes());
//                outputStream.close();
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//
//            return null;
        }

        //Updating progress bar.
        protected void onProgressUpdate(String... progress){
            //setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }

            /**
             * Updating parsed JSON data into ListView
             *
             * */

//            ListAdapter adapter = new SimpleAdapter(
//                    Timetable.this, classList,
//                    R.layout.bus_list_item, new String[]{"routeNumber", "destination",
//                    "expectedDepTime"}, new int[]{R.id.routeNumberTextView,
//                    R.id.busDestinationTextView, R.id.busExpectedDepTextView});
//
//            listView.setAdapter(adapter);
             }
    }



}
