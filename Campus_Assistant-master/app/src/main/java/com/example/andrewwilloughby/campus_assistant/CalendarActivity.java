package com.example.andrewwilloughby.campus_assistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CalendarActivity extends AppCompatActivity {

    private String TAG = CalendarActivity.class.getSimpleName();
    CalendarView calendarView;
    private ListView lv;
    ArrayList<HashMap<String, String>> eventsList;
    private ProgressDialog progressDialog;
    Calendar studentCalendar;
    Context context;
    private Button todayBtn;
    DateFormat eventTimeFormat = new SimpleDateFormat("h:mma");
    DateFormat eventDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private String username = null;
    private String password = null;
    private static String file_url = "https://www.reading.ac.uk/mytimetable/m/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        setTitle("Student Timetable");
        context = this;
        eventsList = new ArrayList<>();

        username = getIntent().getExtras().getString("username");
        password = getIntent().getExtras().getString("password");

        lv = (ListView) findViewById(R.id.dayEventsListView);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> event =  (HashMap<String, String>) lv.getItemAtPosition(position);
                String location = event.get("location");

                String addressToPass = getBuildingAddress(location);
                if (addressToPass != null){
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("search_value", addressToPass);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Unable to perform search on that location.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if ((username != null) && (password != null)){
            System.out.println(username);
            System.out.println(password);

            new DownloadTimetable().execute(file_url);
        } else {
            Toast.makeText(context, "Please enter credentials in settings.", Toast.LENGTH_SHORT).show();
        }

        todayBtn = (Button) findViewById(R.id.todayBtn);
        todayBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                calendarView.setDate(System.currentTimeMillis(),false,true);
            }
        });

        calendarView = (CalendarView) findViewById(R.id.timetableCalendar);
        calendarView.setFirstDayOfWeek(2);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Date selected = null;

                eventsList.clear();
                lv.setAdapter(null);

                String yearStr = Integer.toString(year);
                String monthStr = Integer.toString(month + 1);
                String dayStr = Integer.toString(dayOfMonth);
                String dateStr = dayStr + monthStr + yearStr;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMyyyy");

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

                        //get summary of event.
                        String summary = event.getSummary().toString();
                        summary = summary.replace("SUMMARY;LANGUAGE=en-gb:", "");
                        System.out.println(summary);

                        //get location of event.
                        String location = event.getLocation().toString();
                        location = location.replace("LOCATION:", "");
                        System.out.println(location);

                        //get start time of event.
                        DtStart eventStart = (DtStart) event.getProperty(Property.DTSTART);
                        String eventStartDate = eventDateFormat.format(eventStart.getDate());
                        String eventStartTime = eventTimeFormat.format(eventStart.getDate());
                        System.out.println(eventStartDate);
                        System.out.println(eventStartTime);

                        //get end time of event.
                        DtEnd eventEnd = (DtEnd) event.getProperty(Property.DTEND);
                        String eventEndDate = eventDateFormat.format(eventEnd.getDate());
                        String eventEndTime = eventTimeFormat.format(eventEnd.getDate());
                        System.out.println(eventEndDate);
                        System.out.println(eventEndTime);

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
        });
    }

    protected String getBuildingAddress(String buildingName){
        String building = buildingName.substring(0, 3).toLowerCase();
        switch (building){
            case "agr"  : return "Agriculture building, Reading RG6 6BZ";
            case "arch" : return "Archaeology Building, Reading RG6 6AX";
            case "all"  : return "The Allen Laboratory, Earley, Reading RG6 6AX";
            case "bus"  : return "Centre for Entrepreneurship, Henley Business School, Reading RG6 6UD";
            case "car"  : return "Carrington Building, Reading RG6 6UA";
            case "cha"  : return "51.440210, -0.949907"; //Currently co-ordinates for the new building.
            case "che"  : return "Chemistry and Pharmacy Building, Reading RG6 6LA";
            case "whi"  : return "Whiteknights House, Reading RG6";
            case "eng"  : return "Engineering Building, Pepper Lane, Earley";
            case "foo"  : return "Food Biosciences Building, Reading RG6 6LA";
            case "fox"  : return "Foxhill House, Reading RG6 7BY Reading RG6 6LA";
            case "har"  : return "Harbone Building, Reading, RG6 6LA"; //[sic] Google incorrect spelling.
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

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
            String calendarString = null;

            try{
                URL url = new URL(f_url[0]);
                connection = (HttpURLConnection) url.openConnection();
                String userpass = username + ":" + password;
                String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
                connection.setRequestProperty("Authorization", basicAuth);
                inputStream = new BufferedInputStream(connection.getInputStream());
            } catch (MalformedURLException e) {
                Log.e("URL Error: ", e.getMessage());
            } catch (IOException e) {
                Log.e("IO Error: ", e.getMessage());
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

            } catch (UnsupportedEncodingException e) {
                Log.e("Encoding Error: ", e.getMessage());
            } catch (UnknownHostException e) {
                Toast.makeText(context, "Download failed, no network connection.", Toast.LENGTH_SHORT);

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            if (calendarString != null){
                StringReader stringReader = new StringReader(calendarString);
                CalendarBuilder calendarBuilder = new CalendarBuilder();
                try {
                    studentCalendar = calendarBuilder.build(stringReader);
                } catch (IOException e) {
                    Log.e(TAG + "IO Error: ", e.getMessage());
                } catch (ParserException e) {
                    Log.e(TAG + "Parsing Error: ", e.getMessage());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (progressDialog.isShowing()){ progressDialog.dismiss(); }
        }
    }
}