package com.example.andrewwilloughby.campus_assistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.filter.Filter;
import net.fortuna.ical4j.filter.PeriodRule;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.ExDate;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Activity for displaying student timetable.
 * @author Andrew Willoughby
 */
public class CalendarActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener, AdapterView.OnItemClickListener{

    private CalendarView calendarView;
    private ListView lv;
    private ArrayList<HashMap<String, String>> eventsList;
    private ProgressDialog progressDialog;
    private Calendar studentCalendar;
    private Context context;
    private DateFormat eventTimeFormat = new SimpleDateFormat("h:mma", Locale.UK), eventDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
    private String username = null, password = null, calendarString = null;
    private boolean credentialsFailFlag = false, noCredentialsFlag = false, failedLoadFlag = false;

    /**
     * Method to set up the Activity upon creation.
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        setTitle("Student Timetable");

        String timetableUrl = "https://www.reading.ac.uk/mytimetable/m/";

        context = this;
        eventsList = new ArrayList<>();

        try{
            username = getIntent().getExtras().getString("username");
            password = getIntent().getExtras().getString("password");
        } catch (NullPointerException e){
            noCredentialsFlag = true;
        }

        if (noCredentialsFlag){
            Toast.makeText(getApplicationContext(), "No credentials found.", Toast.LENGTH_SHORT).show();
        } else {
            new DownloadTimetable().execute(timetableUrl);
        }

        lv = (ListView) findViewById(R.id.dayEventsListView);
        lv.setOnItemClickListener(this);

        Button todayBtn = (Button) findViewById(R.id.todayBtn);
        todayBtn.setOnClickListener(this);

        calendarView = (CalendarView) findViewById(R.id.timetableCalendar);
        calendarView.setFirstDayOfWeek(2);
        calendarView.setOnDateChangeListener(this);
    }

    /**
     * Method to handle click events on items in the events listview.
     * @param parent the parent of the item being clicked.
     * @param view the listview.
     * @param position the position in the listview of the item.
     * @param id the ID of the item to click.
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> event =  (HashMap<String, String>) lv.getItemAtPosition(position);

        String addressToPass = getBuildingAddress(event.get("location"));

        if ((addressToPass != null) && (!addressToPass.equals("non existent"))){
            Intent intent = new Intent(context, InteractiveMapActivity.class);
            intent.putExtra("search_value", addressToPass);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Unable to perform search on that location.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to handle dates within the Calendar being selected.
     * @param view the CalendarView within which the change occurred.
     * @param year the year of the date selected.
     * @param month the month of the date selected.
     * @param dayOfMonth the day of the month of the date selected.
     */
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        if (!credentialsFailFlag){
            Date selected = null;

            eventsList.clear();
            lv.setAdapter(null);

            String yearStr = Integer.toString(year);
            String monthStr = Integer.toString(month + 1);
            String dayStr = Integer.toString(dayOfMonth);
            String dateStr = dayStr + monthStr + yearStr;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMyyyy", Locale.UK);

            try {
                selected = simpleDateFormat.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (selected != null) {
                Period period = new Period(new DateTime(selected), new Dur(1, 0, 0, 0));
                Filter filter = new Filter(new PeriodRule(period));

                for (Object object : filter.filter(studentCalendar.getComponents(Component.VEVENT))) {
                    VEvent event = (VEvent) object;

                    String summary = event.getSummary().toString();
                    summary = summary.replace("SUMMARY;LANGUAGE=en-gb:", "");

                    String location = event.getLocation().toString();
                    location = location.replace("LOCATION:", "");

                    DtStart eventStart = (DtStart) event.getProperty(Property.DTSTART);
                    String eventStartDate = eventDateFormat.format(eventStart.getDate());
                    String eventStartTime = eventTimeFormat.format(eventStart.getDate());

                    DtEnd eventEnd = (DtEnd) event.getProperty(Property.DTEND);
                    String eventEndDate = eventDateFormat.format(eventEnd.getDate());
                    String eventEndTime = eventTimeFormat.format(eventEnd.getDate());

                    String eventTiming = eventStartTime + " to " + eventEndTime;

                    HashMap<String, String> calendarEvent = new HashMap<>();
                    calendarEvent.put("summary", summary);
                    calendarEvent.put("location", location);
                    calendarEvent.put("startTime", eventStartTime);
                    calendarEvent.put("startDate", eventStartDate);
                    calendarEvent.put("endTime", eventEndTime);
                    calendarEvent.put("endDate", eventEndDate);
                    calendarEvent.put("eventTiming", eventTiming);

                    eventsList.add(calendarEvent);

                    ListAdapter adapter = new SimpleAdapter(
                            CalendarActivity.this, eventsList,
                            R.layout.timetable_list_item, new String[]{"summary", "eventTiming"}
                            , new int[]{R.id.summaryTextView, R.id.eventTimingTextView});
                    lv.setAdapter(adapter);
                }
            }
        }
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput){
        switch (viewInput.getId()){
            case R.id.todayBtn: calendarView.setDate(System.currentTimeMillis(),false,true); break;
        }
    }

    /**
     * Method that returns the string value to search within the Google Map to find buildings on the UoR campus.
     * @param buildingName the location of the event clicked in the timetable.
     * @return string value which will be placed in the Search box within the Map activity.
     */
    protected String getBuildingAddress(String buildingName){
        switch (buildingName.substring(0, 3).toLowerCase()){
            case "agr"  : return "Agriculture building, Reading RG6 6BZ";
            case "arc" : return "Archaeology Building, Reading RG6 6AX";
            case "all"  : return "The Allen Laboratory, Earley, Reading RG6 6AX";
            case "bus"  : return "Centre for Entrepreneurship, Henley Business School, Reading RG6 6UD";
            case "car"  : return "Carrington Building, Reading RG6 6UA";
            case "cha"  : return "51.440210, -0.949907"; //Current co-ordinates for the Chancellor's building.
            case "che"  : return "Chemistry and Pharmacy Building, Reading RG6 6LA";
            case "whi"  : return "Whiteknights House, Reading RG6";
            case "eng"  : return "Engineering Building, Pepper Lane, Earley";
            case "foo"  : return "Food Biosciences Building, Reading RG6 6LA";
            case "fox"  : return "Foxhill House, Reading RG6 7BY Reading RG6 6LA";
            case "har"  : return "Harbone Building, Reading, RG6 6LA";
            case "hop"  : return "Hopkins Building, Reading RG6 6LA";
            case "hum"  : return "Humss Building, Earley";
            case "jjt"  : return "JJ Thomson Building, Reading RG6 6AX";
            case "kni"  : return "Knight Building, Reading RG6 6UA";
            case "lyl"  : return "Philip Lyle Building, Reading RG6 6LA";
            case "mat"  : return "Mathematics and IT Services, Reading RG6 6AX";
            case "met"  : return "Meteorology building, Reading RG6 6BB";
            case "mil"  : return "Miller Building, Reading RG6";
            case "pal"  : return "Palmer Building, Reading, Wokingham RG6 6UA, England";
            case "sys"  : return "Systems Engineering Building, Reading RG6 6AX";
            case "tpy"  : return "TOB2, Reading RG6 6BZ";
            default: return null;
        }
    }

    /**
     * Method that handles the hardware back button being pressed, opening the main activity.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Private AsyncTask to download the student timetable away from the main thread.
     */
    private class DownloadTimetable extends AsyncTask<String, Void, Void>{

        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(CalendarActivity.this);
            progressDialog.setMessage("Downloading timetable, please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... f_url) {
            HttpURLConnection connection;
            InputStream inputStream = null;
            StringBuilder stringBuilder;

            try{
                URL url = new URL(f_url[0]);
                connection = (HttpURLConnection) url.openConnection();

                String userpass = username + ":" + password;
                String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP);
                connection.setRequestProperty("Authorization", basicAuth);

                inputStream = new BufferedInputStream(connection.getInputStream());
            } catch (Exception e) {
                System.out.println("IO error");
            }

            try{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                stringBuilder = new StringBuilder();
                String thisLine;

                while ((thisLine = bufferedReader.readLine()) != null){
                    stringBuilder.append(thisLine);
                    stringBuilder.append("\r\n");
                }
                calendarString = stringBuilder.toString();

            } catch (UnknownHostException e) {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(getApplicationContext(), "Download failed, no network connection.", Toast.LENGTH_SHORT).show();

                    }
                });
            } catch (UnsupportedEncodingException e) {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(getApplicationContext(), "Timetable encoding error, please retry.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run(){
                        Toast.makeText(getApplicationContext(), "Please verify your University Credentials.", Toast.LENGTH_SHORT).show();
                        failedLoadFlag = true;
                    }
                });
            }

            if (calendarString != null){
                StringReader stringReader = new StringReader(calendarString);
                CalendarBuilder calendarBuilder = new CalendarBuilder();
                try {
                    studentCalendar = calendarBuilder.build(stringReader);
                } catch (IOException | ParserException e) {
                    Toast.makeText(getApplicationContext(), "Error reading Student Timetable. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                credentialsFailFlag = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()){ progressDialog.dismiss(); }
            if (failedLoadFlag){ onBackPressed(); }
        }
    }
}