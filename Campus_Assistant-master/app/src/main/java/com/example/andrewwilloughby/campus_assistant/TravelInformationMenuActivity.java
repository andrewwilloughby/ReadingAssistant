package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity for displaying the Travel Information menu.
 *
 * @author Andrew Willoughby
 */
public class TravelInformationMenuActivity extends AMenu implements View.OnClickListener {

    /**
     * Method to set up the Activity upon creation.
     *
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.travel_info_menu);

        setTitle("Travel Information");
        initialiseToolbarBtns();

        Button busTimesBtn = (Button) findViewById(R.id.travelInfoBusTimesBtn);
        busTimesBtn.setOnClickListener(this);

        Button railTimesBtn = (Button) findViewById(R.id.travelInfoRailTimesBtn);
        railTimesBtn.setOnClickListener(this);

        Button busTimetableBtn = (Button) findViewById(R.id.travelInfoBusTimetableBtn);
        busTimetableBtn.setOnClickListener(this);
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     *
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput) {
        switch (viewInput.getId()){
            case R.id.travelInfoBusTimesBtn: launchActivity("bus times"); break;
            case R.id.travelInfoRailTimesBtn: launchActivity("rail departures"); break;
            case R.id.travelInfoBusTimetableBtn: launchWebView("University Bus Timetable"); break;
        }
    }
}


