package com.example.andrewwilloughby.campus_assistant;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RailDepartures extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView railList;
    ArrayList<HashMap<String, String>> departureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rail_departures);
        setTitle("Live Rail Departures: Reading (RDG)");

        departureList = new ArrayList<>();

        railList = (ListView) findViewById(R.id.railList);

        new GetDepartures().execute();

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.rail_swipe_layout);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                new GetDepartures().execute();
                swipeLayout.setRefreshing(false);
            }
        });
    }

    protected void displayToast(String toastContent){
        if (!toastContent.isEmpty()){
            Toast.makeText(getApplicationContext(), toastContent, Toast.LENGTH_SHORT).show();
        }
    }

    private class GetDepartures extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RailDepartures.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            String url = "1v3/uk/train/station/RDG/live.json?app_id=03bf8009&app_key=d9307fd91b0247c607e098d5effedc97&darwin=false&train_status=passenger";
            DownloadUrl downloadUrl = new DownloadUrl();
            String jsonStr = downloadUrl.readUrl(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject allDepartures = new JSONObject(jsonObj.getString("departures"));
                    JSONArray departures = allDepartures.getJSONArray("all");

                    for (int i = 0; i < departures.length(); i++) {
                        JSONObject ob = departures.getJSONObject(i);

                        String scheduledDep = ob.getString("aimed_departure_time");
                        String expectedDep = ob.getString("expected_departure_time");

                        SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");

                        String destination = ob.getString("destination_name");
                        String platform = ob.getString("platform");
                        String status = ob.getString("status");

                        // tmp hash map for single contact
                        HashMap<String, String> departure = new HashMap<>();

                        // adding each child node to HashMap key => value
                        if(status.contains("LATE")){
                            String depDelayString = null;

                            try{
                                Date scheduledDepTime = sdf.parse(scheduledDep), expectedDepTime = sdf.parse(expectedDep);
                                long delayInMinutes = ((expectedDepTime.getTime() - scheduledDepTime.getTime()) / 60) / 1000;

                                if (delayInMinutes == 1){
                                    depDelayString = (Long.toString(delayInMinutes)) + " minute late";
                                } else{
                                    depDelayString = (Long.toString(delayInMinutes)) + " minutes late";
                                }
                            } catch (ParseException e){
                                //Invalid date obtained.
                            }

                            departure.put("departureTime", scheduledDep);
                            departure.put("expectedDepTime", expectedDep);
                            departure.put("minsLate", depDelayString);
                        } else if (status.equals("CANCELLED")){
                            departure.put("departureTime", "-");
                        } else {
                            departure.put("departureTime", scheduledDep);
                            departure.put("minsLate", "On time");
                        }

                        departure.put("destination", destination);

                        if(platform.equals("null")){
                            departure.put("platform", "-");
                        } else {
                            departure.put("platform", "Plat. " + platform);
                        }

                        departureList.add(departure);
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayToast("Error with live information. Please retry.");
                        }
                    });

                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayToast("Couldn't get live departure data. Please check network connection.");
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            ListAdapter adapter = new SimpleAdapter(
                    RailDepartures.this, departureList,
                    R.layout.list_item, new String[]{"departureTime", "destination",
                    "platform", "expectedDepTime", "minsLate"}, new int[]{R.id.scheduledArrTextView,
                    R.id.destinationTextView, R.id.platformTextView, R.id.expectedDepTextView, R.id.minsLateTextView});
            railList.setAdapter(adapter);
        }
    }
}