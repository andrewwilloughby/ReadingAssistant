package com.example.andrewwilloughby.campus_assistant;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AMenu {

    private ExpandableListView expList;
    private int lastExpandedPosition = -1;
    boolean doubleBackToExitPressedOnce = false;

    public MainActivity(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final LinearLayout gridLayout = (LinearLayout) findViewById(R.id.gridLayout);
        expList = (ExpandableListView) findViewById(R.id.expandableList);

        initialiseToolbarBtns();

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

        HashMap<String, List<String>> menuItemsList = new HashMap<>();

        String menu_categories[] = getResources().getStringArray(R.array.menuCategories);
        List<String> menuCategory = new ArrayList<>(Arrays.asList(menu_categories));

        String studentInfoItems[] = getResources().getStringArray(R.array.studentInfoItems);
        List<String> studentInfoItemsList = new ArrayList<>(Arrays.asList(studentInfoItems));
        menuItemsList.put(menuCategory.get(0), studentInfoItemsList);

        String latestNewsItems[] = getResources().getStringArray(R.array.latestNewsItems);
        List<String> latestNewsItemsList = new ArrayList<>(Arrays.asList(latestNewsItems));
        menuItemsList.put(menuCategory.get(1), latestNewsItemsList);

        String campusNavigationItems[] = getResources().getStringArray(R.array.campusNavigationItems);
        List<String> campusNavigationItemsList = new ArrayList<>(Arrays.asList(campusNavigationItems));
        menuItemsList.put(menuCategory.get(2), campusNavigationItemsList);

        String travelInfoItems[] = getResources().getStringArray(R.array.travelInformationItems);
        List<String> travelInfoItemsList = new ArrayList<>(Arrays.asList(travelInfoItems));
        menuItemsList.put(menuCategory.get(3), travelInfoItemsList);

        String bbEmailItems[] = getResources().getStringArray(R.array.bbEmailItems);
        List<String> bbEmailItemsList = new ArrayList<>(Arrays.asList(bbEmailItems));
        menuItemsList.put(menuCategory.get(4), bbEmailItemsList);

        String timetableItems[] = getResources().getStringArray(R.array.timetableItems);
        List<String> timetableItemsList = new ArrayList<>(Arrays.asList(timetableItems));
        menuItemsList.put(menuCategory.get(5), timetableItemsList);

        Button studentInfoBtn = (Button) findViewById(R.id.mainStudentInfoBtn);
        studentInfoBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){launchActivity("student info menu");
            }
        });

        Button latestNewsBtn = (Button) findViewById(R.id.mainLatestNewsBtn);
        latestNewsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){launchActivity("latest news");
            }
        });

        Button campusNavBtn = (Button) findViewById(R.id.mainCampusNavBtn);
        campusNavBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){launchActivity("campus nav menu");
            }
        });

        Button travelInfoBtn = (Button) findViewById(R.id.mainTravelInfoBtn);
        travelInfoBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){launchActivity("travel info menu");
            }
        });

        Button bbEmailBtn = (Button) findViewById(R.id.mainBbEmailBtn);
        bbEmailBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){launchActivity("bb email menu"); }
        });

        Button timetableBtn = (Button) findViewById(R.id.mainTimetableBtn);
        timetableBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                launchActivity("timetable");
            }
        });

        final ExpandableListAdapter adapter = new MainMenuExpListAdapter(this, menuItemsList, menuCategory);
        expList.setAdapter(adapter);

        expList.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final String selected = (String) adapter.getChild(groupPosition, childPosition);
                switch (selected){
                    case "Student Services":
                        if (isNetworkAvailable()) {
                            launchWebView(selected);
                        }else {
                            displayToast("This functionality requires an active network connection.");
                        }
                        break;
                    case "Library":
                        if (isNetworkAvailable()) {
                            launchWebView(selected);
                        }else {
                            displayToast("This functionality requires an active network connection.");
                        }
                        break;
                    case "University Payments":
                        if (isNetworkAvailable()) {
                            launchWebView(selected);
                        }else {
                            displayToast("This functionality requires an active network connection.");
                        }
                        break;
                    case "RISIS": launchWebView(selected); break;
                    case "Staff Search": launchWebView(selected); break;
                    case "Latest Tweets":
                        if (isNetworkAvailable()) {
                        launchActivity("latest news");
                        } else {
                            displayToast("This functionality requires an active network connection.");
                        }
                        break;
                    case "Interactive Google Map":
                        if (isNetworkAvailable()) {
                            launchActivity("interactive map"); break;
                        } else {
                            displayToast("This functionality requires an active network connection.");
                        }
                        break;
                    case "Whiteknights Campus Map": launchWebView(selected); break;
                    case "London Road Campus Map": launchWebView(selected); break;
                    case "Student Halls Map": launchWebView(selected); break;
                    case "Live Bus Times": launchActivity("bus times"); break;
                    case "Live Train Times": launchActivity("rail departures"); break;
                    case "University Bus Timetable": launchWebView(selected); break;
                    case "Blackboard": launchWebView(selected); break;
                    case "University Email": launchWebView(selected); break;
                    case "Student Timetable":
                        if (isNetworkAvailable()){
                            launchActivity("timetable");
                        } else {
                            displayToast("This functionality requires an active network connection.");
                        }
                         break;
                    default:
                        displayToast("Unknown request.");
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

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        displayToast("Click Back button again to exit");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
