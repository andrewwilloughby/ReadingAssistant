package com.example.andrewwilloughby.campus_assistant;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

/**
 * Activity for the safety information page.
 * @author Andrew Willoughby
 */
public class SafetyInfoActivity extends AMenu implements View.OnClickListener {

    private static final int CALL_PHONE_PERMISSION = 1;
    private Activity activity = this;
    private Intent callIntent = new Intent(Intent.ACTION_DIAL);

    /**
     * Method to set up the Activity upon creation.
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safety_info);
        setTitle("Safety Information");

        //Check not using this hasn't broken buttons.
        //final Activity thisActivity = this;

        Button emergencyContactBtn = (Button) findViewById(R.id.securityEmergencyBtn);
        emergencyContactBtn.setOnClickListener(this);

        Button nonEmergencyContactBtn = (Button) findViewById(R.id.securityNonEmergencyBtn);
        nonEmergencyContactBtn.setOnClickListener(this);

        Button  personalSafetyBtn = (Button) findViewById(R.id.personalSafetyBtn);
        personalSafetyBtn.setOnClickListener(this);

        Button healthAndSafetyBtn = (Button) findViewById(R.id.healthAndSafetyBtn);
        healthAndSafetyBtn.setOnClickListener(this);
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput){
        switch (viewInput.getId()){
            case R.id.securityEmergencyBtn:{
                if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION);
                } else{
                    callIntent.setData(Uri.parse("tel:123"));
                    activity.startActivity(callIntent);
                }
                break;
            }
            case R.id.securityNonEmergencyBtn:{
                if(ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION);
                } else{
                    callIntent.setData(Uri.parse("tel:123"));
                    activity.startActivity(callIntent);
                }
                break;
            }
            case R.id.personalSafetyBtn: launchWebView("Personal Safety Guide"); break;
            case R.id.healthAndSafetyBtn: launchWebView("Report Health & Safety Incident"); break;
        }
    }

    /**
     * Method to act upon request to user for location permission.
     * @param requestCode the type of request.
     * @param permissions the granted permissions.
     * @param grantResults the user's decision.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CALL_PHONE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    callIntent.setData(Uri.parse("tel:07590917581"));
                    activity.startActivity(callIntent);
                }
            }
        }
    }
}
