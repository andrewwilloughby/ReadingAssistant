package com.example.andrewwilloughby.campus_assistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Timetable extends AppCompatActivity {

    private String TAG = Timetable.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ListView listView;
    private static String url;
    ArrayList<HashMap<String,String>> classList;
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
/*
        setTitle("Student Timetable");

        listView = (ListView) findViewById(R.id.list);

        File calendarFile;

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
        */
    }
/*
    private class GetTimetable extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            progressDialog = new ProgressDialog(Timetable.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String calStr = sh.makeServiceCall("https://bc016938:Spandy1591@reading.ac.uk/mytimetable/m");

            Log.d(TAG, calStr);

            try{
                outputStream = openFileOutput("timetable", Context.MODE_PRIVATE);
                outputStream.write(calStr.getBytes());

                outputStream.close();
            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             *
            ListAdapter adapter = new SimpleAdapter(
                    Timetable.this, classList,
                    R.layout.bus_list_item, new String[]{"routeNumber", "destination",
                    "expectedDepTime"}, new int[]{R.id.routeNumberTextView,
                    R.id.busDestinationTextView, R.id.busExpectedDepTextView});

            listView.setAdapter(adapter);
        }
    }

            */

}
