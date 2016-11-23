package com.example.andrewwilloughby.campus_assistant;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class Timetable extends AppCompatActivity {

    private String TAG = Timetable.class.getSimpleName();
    private ProgressDialog progressDialog;
    private ListView listView;
    private static String url;
    ArrayList<HashMap<String,String>> classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        setTitle("Student Timetable");



    }
}
