package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity for displaying the Campus Maps menu.
 *
 * @author Andrew Willoughby
 */
public class CampusMapsMenuActivity extends AMenu implements View.OnClickListener {

    /**
     * Method to set up the Activity upon creation.
     *
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_maps_menu);

        setTitle("Campus Maps");
        initialiseToolbarBtns();

        Button whiteknightsBtn = (Button) findViewById(R.id.campusMapsMenuWhiteknightsBtn);
        whiteknightsBtn.setOnClickListener(this);

        Button loroBtn = (Button) findViewById(R.id.campusMapsLoroBtn);
        loroBtn.setOnClickListener(this);

        Button hallsBtn = (Button) findViewById(R.id.campusMapsHallsBtn);
        hallsBtn.setOnClickListener(this);
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     *
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput) {
        switch (viewInput.getId()){
            case R.id.campusMapsMenuWhiteknightsBtn: launchWebView("Whiteknights Campus Map"); break;
            case R.id.campusMapsLoroBtn: launchWebView("London Road Campus Map"); break;
            case R.id.campusMapsHallsBtn: launchWebView("Student Halls Map"); break;
        }
    }
}
