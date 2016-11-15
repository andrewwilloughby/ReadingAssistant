package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TravelInformationMenu extends AMenu {

    private Button busTimesBtn;
    private Button railTimesBtn;
    private Button busTimetableBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_information_menu);

        setTitle("Travel Information");
        initialiseSafetySettingsBtns();

        busTimesBtn = (Button) findViewById(R.id.travelInfoBusTimesBtn);
        busTimesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ReadXMLFile xml = new ReadXMLFile();
                xml.read("http://opendata.reading-travelinfo.co.uk/api/1/bus/calls/rdgagta.xml?key=");
            }
        });

        railTimesBtn = (Button) findViewById(R.id.travelInfoRailTimesBtn);
        railTimesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchActivity("rail departures");
            }
        });

        busTimetableBtn = (Button) findViewById(R.id.travelInfoBusTimetableBtn);
        busTimetableBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchWebView("University Bus Timetable");
            }
        });

    }
}
