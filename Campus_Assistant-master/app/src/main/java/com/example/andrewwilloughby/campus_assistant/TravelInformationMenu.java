package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TravelInformationMenu extends AMenu implements View.OnClickListener {

    private Button busTimesBtn;
    private Button railTimesBtn;
    private Button busTimetableBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_information_menu);

        setTitle("Travel Information");
        initialiseToolbarBtns();

        busTimesBtn = (Button) findViewById(R.id.travelInfoBusTimesBtn);
        busTimesBtn.setOnClickListener(this);

        railTimesBtn = (Button) findViewById(R.id.travelInfoRailTimesBtn);
        railTimesBtn.setOnClickListener(this);

        busTimetableBtn = (Button) findViewById(R.id.travelInfoBusTimetableBtn);
        busTimetableBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.travelInfoBusTimesBtn:{
                launchActivity("bus times");
                break;
            }
            case R.id.travelInfoRailTimesBtn:{
                launchActivity("rail departures");
                break;
            }
            case R.id.travelInfoBusTimetableBtn:{
                launchWebView("University Bus Timetable");
            }
        }
    }
}
