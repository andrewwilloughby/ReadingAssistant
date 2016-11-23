package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AMenu {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "icXPlnJKZL9eTOpDgtmSOklhi";
    private static final String TWITTER_SECRET = "ObkVdHNrCOFUvarHzS0OWvHkwCsCSefDpInYlJdGk2jVvnBima";

    private Button studentInfoBtn;
    private Button latestNewsBtn;
    private Button campusNavBtn;
    private Button travelInfoBtn;
    private Button bbEmailBtn;
    private Button timetableBtn;
    private ExpandableListView expList;
    private int lastExpandedPosition = -1;

    HashMap<String, List<String>> menuItemsList = new HashMap<String, List<String>>();
    List<String> menuCategory = new ArrayList<String>();
    List<String> studentInfoItemsList = new ArrayList<String>();
    List<String> latestNewsItemsList = new ArrayList<String>();
    List<String> campusNavigationItemsList = new ArrayList<String>();
    List<String> travelInfoItemsList = new ArrayList<String>();
    List<String> bbEmailItemsList = new ArrayList<String>();
    List<String> timetableItemsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);
        final LinearLayout gridLayout = (LinearLayout) findViewById(R.id.gridLayout);

        initialiseSafetySettingsBtns();

        viewStyleBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                if(getMENU_MODE() == 1){
                    viewStyleBtn.setImageResource(R.drawable.grid_icon);
                    gridLayout.setVisibility(View.GONE);
                    expList.setVisibility(View.VISIBLE);
                    setMENU_MODE(2);
                } else if(getMENU_MODE() == 2){
                    viewStyleBtn.setImageResource(R.drawable.list_icon);
                    setMENU_MODE(1);
                    gridLayout.setVisibility(View.VISIBLE);
                    expList.setVisibility(View.GONE);
                }
            }
        });

        String menu_categories[] = getResources().getStringArray(R.array.menuCategories);
        String studentInfoItems[] = getResources().getStringArray(R.array.studentInfoItems);
        String latestNewsItems[] = getResources().getStringArray(R.array.latestNewsItems);
        String campusNavigationItems[] = getResources().getStringArray(R.array.campusNavigationItems);
        String travelInfoItems[] = getResources().getStringArray(R.array.travelInformationItems);
        String bbEmailItems[] = getResources().getStringArray(R.array.bbEmailItems);
        String timetableItems[] = getResources().getStringArray(R.array.timetableItems);

        studentInfoBtn = (Button) findViewById(R.id.mainStudentInfoBtn);
        studentInfoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){launchActivity("student info menu");
            }
        });

        latestNewsBtn = (Button) findViewById(R.id.mainLatestNewsBtn);
        latestNewsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){launchActivity("latest news");
            }
        });

        campusNavBtn = (Button) findViewById(R.id.mainCampusNavBtn);
        campusNavBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){launchActivity("campus nav menu");
            }
        });

        travelInfoBtn = (Button) findViewById(R.id.mainTravelInfoBtn);
        travelInfoBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){launchActivity("travel info menu");
            }
        });

        bbEmailBtn = (Button) findViewById(R.id.mainBbEmailBtn);
        bbEmailBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){launchActivity("bb email menu"); }
        });

        timetableBtn = (Button) findViewById(R.id.mainTimetableBtn);
        timetableBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                launchActivity("timetable");
            }
        });

        for(String item : menu_categories){ menuCategory.add(item);}
        for(String item : studentInfoItems){  studentInfoItemsList.add(item); }
        for(String item : latestNewsItems){ latestNewsItemsList.add(item); }
        for(String item : campusNavigationItems){ campusNavigationItemsList.add(item); }
        for(String item : travelInfoItems){ travelInfoItemsList.add(item); }
        for(String item : bbEmailItems){ bbEmailItemsList.add(item); }
        for(String item : timetableItems){ timetableItemsList.add(item); }

        menuItemsList.put(menuCategory.get(0), studentInfoItemsList);
        menuItemsList.put(menuCategory.get(1), latestNewsItemsList);
        menuItemsList.put(menuCategory.get(2), campusNavigationItemsList);
        menuItemsList.put(menuCategory.get(3), travelInfoItemsList);
        menuItemsList.put(menuCategory.get(4), bbEmailItemsList);
        menuItemsList.put(menuCategory.get(5), timetableItemsList);

        expList = (ExpandableListView) findViewById(R.id.expandableList);
        final ExpandableListAdapter adapter = new MainMenuExpListAdapter(this, menuItemsList, menuCategory);
        expList.setAdapter(adapter);

        expList.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String selected = (String) adapter.getChild(groupPosition, childPosition);
                switch (selected){
                    case "Student Services":
                        launchWebView(selected);
                        break;
                    case "Library":
                        launchWebView(selected);
                        break;
                    case "University Payments":
                        launchWebView(selected);
                        break;
                    case "RISIS":
                        launchWebView(selected);
                        break;
                    case "Staff Search":
                        launchWebView(selected);
                        break;
                    case "Latest Tweets":
                        launchActivity("latest news");
                        break;
                    case "Interactive Google Map":
                        Toast.makeText(getApplicationContext(), "This functionality hasn't been implemented.", Toast.LENGTH_SHORT).show();
                        break;
                    case "Whiteknights Campus Map":
                        launchWebView(selected);
                        break;
                    case "London Road Campus Map":
                        launchWebView(selected);
                        break;
                    case "Student Halls Map":
                        launchWebView(selected);
                        break;
                    case "Live Bus Times":
                        launchActivity("bus times");
                        break;
                    case "Live Train Times":
                        launchActivity("rail departures");
                        break;
                    case "University Bus Timetable":
                        launchWebView(selected);
                        break;
                    case "Blackboard":
                        launchWebView(selected);
                        break;
                    case "University Email":
                        launchWebView(selected);
                        break;
                    case "Student Timetable":
                        Toast.makeText(getApplicationContext(), "This functionality hasn't been implemented.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "This functionality hasn't been implemented.", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        });

        expList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    expList.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Click Back button again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
