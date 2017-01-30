package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentInfoMenu extends AMenu {

    public StudentInfoMenu(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button studentServicesBtn;
        Button libraryBtn;
        Button paymentsBtn;
        Button risisBtn;
        Button staffDirectoryBtn;

        setContentView(R.layout.activity_student_info_menu);

        setTitle("Student Information");
        initialiseToolbarBtns();

        studentServicesBtn = (Button) findViewById(R.id.studentInfoServicesBtn);
        studentServicesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("Student Services"); } });

        libraryBtn = (Button) findViewById(R.id.studentInfoLibraryBtn);
        libraryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("Library"); } });

        paymentsBtn = (Button) findViewById(R.id.studentInfoPaymentsBtn);
        paymentsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("University Payments"); } });

        risisBtn = (Button) findViewById(R.id.studentInfoRisisBtn);
        risisBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("RISIS"); } });

        staffDirectoryBtn = (Button) findViewById(R.id.studentInfoStaffDirectoryBtn);
        staffDirectoryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ launchWebView("Staff Search"); } });
    }
}
