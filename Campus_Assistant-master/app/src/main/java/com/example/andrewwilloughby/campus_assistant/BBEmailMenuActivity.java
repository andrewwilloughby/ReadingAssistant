package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity for displaying the Blackboard & Email menu.
 **
 * @author Andrew Willoughby
 */
public class BBEmailMenuActivity extends AMenu implements View.OnClickListener{

    /**
     * Method to set up the Activity upon creation.
     *
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button blackboardBtn, emailBtn;

        setContentView(R.layout.bb_email_menu);

        setTitle("Blackboard & Email");
        initialiseToolbarBtns();

        blackboardBtn = (Button) findViewById(R.id.bbemailMenuBlackboardBtn);
        blackboardBtn.setOnClickListener(this);

        emailBtn = (Button) findViewById(R.id.bbemailMenuEmailBtn);
        emailBtn.setOnClickListener(this);
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     *
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput){
        switch (viewInput.getId()){
            case R.id.bbemailMenuBlackboardBtn: launchWebView("Blackboard"); break;
            case R.id.bbemailMenuEmailBtn: launchWebView("University Email"); break;
        }
    }
}
