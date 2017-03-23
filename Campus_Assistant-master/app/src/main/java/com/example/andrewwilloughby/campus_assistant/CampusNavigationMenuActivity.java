package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity for displaying the Campus Navigation menu.
 *
 * @author Andrew Willoughby
 */
public class CampusNavigationMenuActivity extends AMenu implements View.OnClickListener {

    /**
     * Method to set up the Activity upon creation.
     *
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campus_navigation_menu);

        setTitle("Campus Navigation");
        initialiseToolbarBtns();

        Button interactiveMapBtn = (Button) findViewById(R.id.campusNavInteractiveMapBtn);
        interactiveMapBtn.setOnClickListener(this);

        Button campusMapsBtn = (Button) findViewById(R.id.campusNavMapsBtn);
        campusMapsBtn.setOnClickListener(this);
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     *
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput){
        switch (viewInput.getId()){
            case R.id.campusNavInteractiveMapBtn: launchActivity("interactive map"); break;
            case R.id.campusNavMapsBtn: launchActivity("campus maps"); break;
        }
    }
}
