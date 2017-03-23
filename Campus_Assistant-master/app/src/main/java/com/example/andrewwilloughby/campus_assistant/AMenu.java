package com.example.andrewwilloughby.campus_assistant;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Abstract class for menus.
 *
 * This class contains the methods commonly used by the Menu activities.
 *
 * @author Andrew Willoughby
 */
public abstract class AMenu extends AppCompatActivity {

    protected ImageButton safetyBtn, viewStyleBtn;
    protected static int menuMode;

    /**
     * Method executed when a menu activity opens.
     * Sets up the Safety Button and View Style Button in the menu bar.
     */
    protected void initialiseToolbarBtns(){

        // Calls the 'safety' activity when clicked.
        safetyBtn = (ImageButton) findViewById(R.id.safetyBtn);
        safetyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                launchActivity("safety");
            }
        });

        viewStyleBtn = (ImageButton) findViewById(R.id.viewStyleBtn);
        viewStyleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                if(getmenuMode() == 1){
                    viewStyleBtn.setImageResource(R.drawable.grid_icon);
                    setMenuMode(2);
                    launchActivity("main menu");
                } else if(getmenuMode() == 2){
                    viewStyleBtn.setImageResource(R.drawable.list_icon);
                    setMenuMode(1);
                }
            }
        });
    }

    /**
     * Method that launches an activity based on the value of activityName parameter.
     *
     * @param activityName name of the activity to be launched.
     */
    protected void launchActivity(String activityName){
        Intent intent = null;

        // Self explanatory, open the activity the correlates to the String passed, otherwise display an error message.
        switch (activityName){
            case "main menu":
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("listmode", true);
                break;
            case "student info menu":
                intent = new Intent(this, StudentInfoMenuActivity.class);
                break;
            case "latest news":
                intent = new Intent(this, LatestNewsActivity.class);
                break;
            case "campus nav menu":
                intent = new Intent(this, CampusNavigationMenuActivity.class);
                break;
            case "travel info menu":
                intent = new Intent(this, TravelInformationMenuActivity.class);
                break;
            case "bb email menu":
                intent = new Intent(this, BBEmailMenuActivity.class);
                break;
            case "timetable":
                intent = new Intent(this, LoginActivity.class);
                break;
            case "safety":
                intent = new Intent(this, SafetyInfoActivity.class);
                break;
            case "interactive map":
                intent = new Intent(this, InteractiveMapActivity.class);
                break;
            case "campus maps":
                intent = new Intent(this, CampusMapsMenuActivity.class);
                break;
            case "rail departures":
                intent = new Intent(this, RailDeparturesActivity.class);
                break;
            case "bus times":
                intent = new Intent(this, BusTimesActivity.class); break;
            default:
                Toast.makeText(getApplicationContext(), "No activity exists for that command.", Toast.LENGTH_SHORT).show();
                break;
        }
        startActivity(intent);
    }

    /**
     * Method that launches a web page based on the value of webpageName in the WebView activity.
     *
     * @param webpageName name of the web page to be launched.
     */
    protected void launchWebView(String webpageName){
        String url = null;

        switch(webpageName){
            case "Student Services":
                url = "https://student.reading.ac.uk/";
                break;
            case "Library":
                url = "http://www.reading.ac.uk/library/";
                break;
            case "University Payments":
                url = "https://www.webpay.reading.ac.uk/studentpayments/";
                break;
            case "RISIS":
                url = "https://www.risisweb.reading.ac.uk/";
                break;
            case "Staff Search":
                url = "https://www.reading.ac.uk/search/search-staff.aspx";
                break;
            case "Whiteknights Campus Map":
                url = "http://www.reading.ac.uk/web/FILES/whiteknights-campus-map-and-keys-2016.pdf";
                break;
            case "London Road Campus Map":
                url = "http://www.reading.ac.uk/web/FILES/University-of-Reading-London-Road-COLOUR-NUMERIC.pdf";
                break;
            case "Student Halls Map":
                url = "https://www.reading.ac.uk/web/FILES/accommodation/Walking_distances_to_halls.pdf";
                break;
            case "University Bus Timetable":
                url = "http://www.reading-buses.co.uk/files/timetables/current/claret%20times.pdf";
                break;
            case "Blackboard":
                url = "https://bb.reading.ac.uk/";
                break;
            case "University Email":
                url = "http://mail.live.reading.ac.uk/";
                break;
            case "Live Rail Departures":
                url = "http://transportapi.com/v3/uk/train/station/RDG/live.json?app_id=03bf8009&app_key=d9307fd91b0247c607e098d5effedc97&darwin=false&train_status=passenger";
                break;
            case "Personal Safety Guide":
                url = "http://www.reading.ac.uk/web/FILES/security/secStayingSafe.pdf";
                break;
            case "Report Health & Safety Incident":
                url = "http://www.reading.ac.uk/internal/health-and-safety/IncidentReportingandEmergencyProcedures/Report_an_Incident_online.aspx";
                break;
        }

        Intent intent = new Intent(this, WebpageViewActivity.class)
                .putExtra("webpageURL", url)
                .putExtra("webpageName", webpageName);

        startActivity(intent);
    }

    /**
     * Method that checks the availability of an active network connection.
     *
     * @return boolean value to indicate availability of network.
     */
    protected boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Getter method for the MENU_MODE value, to indicate the current menu mode.
     *
     * @return MENU_MODE value, indicating current view mode.
     */
    public int getmenuMode(){
        return menuMode;
    }

    /**
     * Setter method for MENU_MODE, to update the value of the current menu view mode.
     *
     * @param value
     */
    public void setMenuMode(int value){
        menuMode = value;
    }
}
