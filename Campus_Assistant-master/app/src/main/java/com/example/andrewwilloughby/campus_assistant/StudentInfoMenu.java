package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentInfoMenu extends AMenu {

    public StudentInfoMenu(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info_menu);

        setTitle("Student Information");
        initialiseToolbarBtns();

        Button studentServicesBtn = (Button) findViewById(R.id.studentInfoServicesBtn);
        studentServicesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("Student Services"); } });

        Button libraryBtn = (Button) findViewById(R.id.studentInfoLibraryBtn);
        libraryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("Library"); } });

        Button paymentsBtn = (Button) findViewById(R.id.studentInfoPaymentsBtn);
        paymentsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("University Payments"); } });

        Button risisBtn = (Button) findViewById(R.id.studentInfoRisisBtn);
        risisBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("RISIS"); } });

        Button staffDirectoryBtn = (Button) findViewById(R.id.studentInfoStaffDirectoryBtn);
        staffDirectoryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("Staff Search"); } });
    }
}
