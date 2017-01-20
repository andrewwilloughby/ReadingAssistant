package com.example.andrewwilloughby.campus_assistant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

public class SafetyInfo extends AMenu {

    private Button emergencyContactBtn;
    private Button nonEmergencyContactBtn;
    private Button personalSafetyBtn;
    private Button healthAndSafetyBtn;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private Activity activity = this;
    private Intent callIntent = new Intent(Intent.ACTION_DIAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_info);

        final Activity thisActivity = this;
        final Context context = this;
        setTitle("Safety Information");

        emergencyContactBtn = (Button) findViewById(R.id.securityEmergencyBtn);
        emergencyContactBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
            if(ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            } else{
                callIntent.setData(Uri.parse("tel:07590917581"));
                activity.startActivity(callIntent);
            }
            }
        });

        nonEmergencyContactBtn = (Button) findViewById(R.id.securityNonEmergencyBtn);
        nonEmergencyContactBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                if(ContextCompat.checkSelfPermission(thisActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else{
                    callIntent.setData(Uri.parse("tel:07590917581"));
                    activity.startActivity(callIntent);
                }
            }
        });

        personalSafetyBtn = (Button) findViewById(R.id.personalSafetyBtn);
        personalSafetyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){launchWebView("Personal Safety Guide");
            }
        });

        healthAndSafetyBtn = (Button) findViewById(R.id.healthAndSafetyBtn);
        healthAndSafetyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){launchWebView("Report Health & Safety Incident"); }
        });
    }

    //Code provided by Google Developer site.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    callIntent.setData(Uri.parse("tel:07590917581"));
                    activity.startActivity(callIntent);
                }
                return;
            }
        }
    }
}
