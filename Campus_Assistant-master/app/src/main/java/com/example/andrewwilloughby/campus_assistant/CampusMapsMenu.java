package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CampusMapsMenu extends AMenu {

    private Button whiteknightsBtn;
    private Button loroBtn;
    private Button hallsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_maps_menu);

        setTitle("Campus Maps");
        initialiseToolbarBtns();

        whiteknightsBtn = (Button) findViewById(R.id.campusMapsMenuWhiteknightsBtn);
        whiteknightsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("Whiteknights Campus Map"); } });

        loroBtn = (Button) findViewById(R.id.campusMapsLoroBtn);
        loroBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("London Road Campus Map"); } });

        hallsBtn = (Button) findViewById(R.id.campusMapsHallsBtn);
        hallsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("Student Halls Map"); } });

    }
}
