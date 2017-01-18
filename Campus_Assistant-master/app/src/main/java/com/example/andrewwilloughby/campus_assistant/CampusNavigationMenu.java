package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CampusNavigationMenu extends AMenu {

    private Button interactiveMapBtn;
    private Button campusMapsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_navigation_menu);

        setTitle("Campus Navigation");
        initialiseToolbarBtns();

        interactiveMapBtn = (Button) findViewById(R.id.campusNavInteractiveMapBtn);
        interactiveMapBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){launchActivity("interactive map");
            }
        });

        campusMapsBtn = (Button) findViewById(R.id.campusNavMapsBtn);
        campusMapsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){launchActivity("campus maps");
            }
        });
    }
}
