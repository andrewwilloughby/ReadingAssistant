package com.example.andrewwilloughby.campus_assistant;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public abstract class AMenu extends AppCompatActivity {

    // Defines the buttons used within the top toolbar.
    protected ImageButton safetyBtn;
    protected ImageButton viewStyleBtn;

    // Used to define which menu layout to use (1 = grid buttons, 2 = Expandable menu list).
    protected int MENU_MODE;

    public AMenu(){
        setMENU_MODE(2);
    }

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
                // When the current menu view is grid buttons, change viewStyleBtn to show grid, change MENU_MODE to 2 and launch the main menu activity.
                if(getMENU_MODE() == 1){
                    viewStyleBtn.setImageResource(R.drawable.grid_icon);
                    setMENU_MODE(2);
                    System.out.println(getMENU_MODE());
                    launchActivity("main menu");

                    // Otherwise do the opposite, but no need to launch the main menu activity again.
                } else if(getMENU_MODE() == 2){
                    viewStyleBtn.setImageResource(R.drawable.list_icon);
                    setMENU_MODE(1);
                    System.out.println(getMENU_MODE());
                    launchActivity("main menu");
                }
            }
        });
    }

    protected void launchActivity(String activityName){
        Intent intent = null;

        // Self explanatory, open the activity the correlates to the String passed, otherwise display an error message.
        switch (activityName){
            case "main menu":
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("listmode", true);
                break;
            case "student info menu":
                intent = new Intent(this, StudentInfoMenu.class);
                break;
            case "latest news":
                intent = new Intent(this, LatestNews.class);
                break;
            case "campus nav menu":
                intent = new Intent(this, CampusNavigationMenu.class);
                break;
            case "travel info menu":
                intent = new Intent(this, TravelInformationMenu.class);
                break;
            case "bb email menu":
                intent = new Intent(this, BBEmailMenu.class);
                break;
            case "timetable":
                intent = new Intent(this, LoginActivity.class);
                break;
            case "safety":
                intent = new Intent(this, SafetyInfo.class);
                break;
            case "interactive map":
                intent = new Intent(this, MapsActivity.class);
                break;
            case "campus maps":
                intent = new Intent(this, CampusMapsMenu.class);
                break;
            case "rail departures":
                intent = new Intent(this, RailDepartures.class);
                break;
            case "bus times":
                intent = new Intent(this, BusTimes.class);
            default:
                displayToast("No activity exists for that command.");
                break;
        }
        startActivity(intent);
    }

    protected void launchWebView(String webpageName){
        Intent intent = new Intent(this, WebpageView.class);
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

        intent.putExtra("webpageURL", url);
        intent.putExtra("webpageName", webpageName);

        startActivity(intent);

    }

    protected boolean isNetworkAvailable() {

        // Simple, but important, check for an active network connection.
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // Returns true or false indicating whether network is available.
        if (null != activeNetworkInfo){
            return true;
        } else {
            return false;
        }
    }

    protected void displayToast(String toastContent){
        if (!toastContent.isEmpty()){
            Toast.makeText(getApplicationContext(), toastContent, Toast.LENGTH_SHORT).show();
        }
    }

    public int getMENU_MODE(){
        return MENU_MODE;
    }

    public void setMENU_MODE(int value){
        MENU_MODE = value;
    }
}
