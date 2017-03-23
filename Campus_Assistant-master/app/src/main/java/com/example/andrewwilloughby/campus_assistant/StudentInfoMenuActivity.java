package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Activity for displaying the Student Information menu.
 * @author Andrew Willoughby
 */
public class StudentInfoMenuActivity extends AMenu implements View.OnClickListener{

    /**
     * Method to set up the Activity upon creation.
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_info_menu);
        setTitle("Student Information");

        initialiseToolbarBtns();

        Button studentServicesBtn = (Button) findViewById(R.id.studentInfoServicesBtn);
        studentServicesBtn.setOnClickListener(this);

        Button libraryBtn = (Button) findViewById(R.id.studentInfoLibraryBtn);
        libraryBtn.setOnClickListener(this);

        Button paymentsBtn = (Button) findViewById(R.id.studentInfoPaymentsBtn);
        paymentsBtn.setOnClickListener(this);

        Button risisBtn = (Button) findViewById(R.id.studentInfoRisisBtn);
        risisBtn.setOnClickListener(this);

        Button staffDirectoryBtn = (Button) findViewById(R.id.studentInfoStaffDirectoryBtn);
        staffDirectoryBtn.setOnClickListener(this);
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput){
        switch (viewInput.getId()){
            case R.id.studentInfoServicesBtn: launchWebView("Student Services"); break;
            case R.id.studentInfoLibraryBtn: launchWebView("Library"); break;
            case R.id.studentInfoPaymentsBtn: launchWebView("University Payments"); break;
            case R.id.studentInfoRisisBtn: launchWebView("RISIS"); break;
            case R.id.studentInfoStaffDirectoryBtn: launchWebView("Staff Search"); break;
        }
    }
}
