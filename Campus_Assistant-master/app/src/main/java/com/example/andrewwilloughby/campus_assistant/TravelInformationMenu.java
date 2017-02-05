package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TravelInformationMenu extends AMenu {
    public TravelInformationMenu(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_information_menu);

        setTitle("Travel Information");
        initialiseToolbarBtns();

        Button  busTimesBtn = (Button) findViewById(R.id.travelInfoBusTimesBtn);
        busTimesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchActivity("bus times"); } });

        Button railTimesBtn = (Button) findViewById(R.id.travelInfoRailTimesBtn);
        railTimesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){launchActivity("rail departures"); } });

        Button busTimetableBtn = (Button) findViewById(R.id.travelInfoBusTimetableBtn);
        busTimetableBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){launchWebView("University Bus Timetable"); } });
    }
}
