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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Activity for the main menu displayed upon opening of the application.
 * @author Andrew Willoughby
 */
public class MainActivity extends AMenu implements ExpandableListView.OnGroupExpandListener, OnChildClickListener, View.OnClickListener {

    private ExpandableListView expList;
    private int lastExpandedPosition = -1;
    private boolean backPressedOnce = false;
    private ExpandableListAdapter adapter;
    private LinearLayout gridLayout;

    /**
     * MainActivity constructor.
     */
    MainActivity(){
        setMenuMode(2);
    }

    /**
     * Method to set up the Activity upon creation.
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        gridLayout = (LinearLayout) findViewById(R.id.gridLayout);
        expList = (ExpandableListView) findViewById(R.id.expandableList);

        initialiseToolbarBtns();

        viewStyleBtn.setOnClickListener(this);

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
        studentInfoBtn.setOnClickListener(this);

        Button latestNewsBtn = (Button) findViewById(R.id.mainLatestNewsBtn);
        latestNewsBtn.setOnClickListener(this);

        Button campusNavBtn = (Button) findViewById(R.id.mainCampusNavBtn);
        campusNavBtn.setOnClickListener(this);

        Button travelInfoBtn = (Button) findViewById(R.id.mainTravelInfoBtn);
        travelInfoBtn.setOnClickListener(this);

        Button bbEmailBtn = (Button) findViewById(R.id.mainBbEmailBtn);
        bbEmailBtn.setOnClickListener(this);

        Button timetableBtn = (Button) findViewById(R.id.mainTimetableBtn);
        timetableBtn.setOnClickListener(this);

        adapter = new MainMenuExpListAdapter(this, menuItemsList, menuCategory);
        expList.setAdapter(adapter);
        expList.setOnChildClickListener(this);
        expList.setOnGroupExpandListener(this);
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput){
        switch (viewInput.getId()){
            case R.id.viewStyleBtn:{
                if(getmenuMode() == 1){
                    viewStyleBtn.setImageResource(R.drawable.grid_icon);
                    gridLayout.setVisibility(View.GONE);
                    expList.setVisibility(View.VISIBLE);
                    setMenuMode(2);
                } else if(getmenuMode() == 2){
                    viewStyleBtn.setImageResource(R.drawable.list_icon);
                    setMenuMode(1);
                    gridLayout.setVisibility(View.VISIBLE);
                    expList.setVisibility(View.GONE);
                }
                break;
            }
            case R.id.mainStudentInfoBtn: launchActivity("student info menu"); break;
            case R.id.mainLatestNewsBtn: launchActivity("latest news"); break;
            case R.id.mainCampusNavBtn: launchActivity("campus nav menu"); break;
            case R.id.mainTravelInfoBtn: launchActivity("travel info menu"); break;
            case R.id.mainBbEmailBtn: launchActivity("bb email menu"); break;
            case R.id.mainTimetableBtn: launchActivity("timetable"); break;
        }
    }

    /**
     * Method to handle clicks on child items within the listview.
     * @param parent the parent of the child item clicked.
     * @param v the view element of the child item.
     * @param groupPosition the group position of the item.
     * @param childPosition the position of the item in that group.
     * @param id the ID of the child item.
     * @return boolean value to indicate click was handled.
     */
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        final String selected = (String) adapter.getChild(groupPosition, childPosition);
        switch (selected){
            case "Student Services":
                if (isNetworkAvailable()) {
                    launchWebView(selected);
                } else {
                    Toast.makeText(getApplicationContext(), "This functionality requires an active network connection.", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Library":
                if (isNetworkAvailable()) {
                    launchWebView(selected);
                }else {
                    Toast.makeText(getApplicationContext(), "This functionality requires an active network connection.", Toast.LENGTH_SHORT).show();
                }
                break;
            case "University Payments":
                if (isNetworkAvailable()) {
                    launchWebView(selected);
                }else {
                    Toast.makeText(getApplicationContext(), "This functionality requires an active network connection.", Toast.LENGTH_SHORT).show();
                }
                break;
            case "RISIS": launchWebView(selected); break;
            case "Staff Search": launchWebView(selected); break;
            case "Latest Tweets":
                if (isNetworkAvailable()) {
                    launchActivity("latest news");
                } else {
                    Toast.makeText(getApplicationContext(), "This functionality requires an active network connection.", Toast.LENGTH_SHORT).show();
                }
                break;
            case "Interactive Google Map":
                if (isNetworkAvailable()) {
                    launchActivity("interactive map"); break;
                } else {
                    Toast.makeText(getApplicationContext(), "This functionality requires an active network connection.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "This functionality requires an active network connection.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "Unknown request.", Toast.LENGTH_SHORT).show();
                return false;
        }
        return true;
    }

    /**
     * Method to handle click on group title item in list view.
     * @param groupPosition the position of the title item in the list view.
     */
    public void onGroupExpand(int groupPosition) {
        if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
            expList.collapseGroup(lastExpandedPosition);
        }
        lastExpandedPosition = groupPosition;
    }

    /**
     * Method that handles the hardware back button being pressed.
     */
    @Override
    public void onBackPressed() {
        if (backPressedOnce) { super.onBackPressed(); return; }

        this.backPressedOnce = true;

        Toast.makeText(getApplicationContext(), "Click Back button again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce=false;
            }
        }, 2000);
    }
}
