package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BBEmailMenu extends AMenu {

    public BBEmailMenu(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button blackboardBtn, emailBtn;

        setContentView(R.layout.activity_bbemail_menu);

        setTitle("Blackboard & Email");
        initialiseToolbarBtns();

        blackboardBtn = (Button) findViewById(R.id.bbemailMenuBlackboardBtn);
        blackboardBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("Blackboard"); } });

        emailBtn = (Button) findViewById(R.id.bbemailMenuEmailBtn);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("University Email"); } });
    }
}
