package com.example.andrewwilloughby.campus_assistant;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    private String TAG = RailDepartures.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url = "http://transportapi.com/v3/uk/train/station/RDG/live.json?app_id=03bf8009&app_key=d9307fd91b0247c607e098d5effedc97&darwin=false&train_status=passenger";
    ArrayList<HashMap<String, String>> departureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rail_departures);

        setTitle("Live Rail Departures: Reading (RDG)");

        departureList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

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

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetDepartures extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RailDepartures.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

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
                            int departureDelay;
                            String depDelayString = null;
                            try{
                                Date scheduledDepTime = sdf.parse(scheduledDep);
                                Date expectedDepTime = sdf.parse(expectedDep);
                                long diffInMins = ((expectedDepTime.getTime() - scheduledDepTime.getTime()) / 60) / 1000;

                                if (diffInMins == 1){
                                    depDelayString = (Long.toString(diffInMins)) + " minute late";
                                } else{
                                    depDelayString = (Long.toString(diffInMins)) + " minutes late";
                                }


                            } catch (ParseException e){
                                //Invalid date obtained.
                            }

                            departure.put("departureTime", scheduledDep);
                            departure.put("expectedDepTime", expectedDep);
                            departure.put("minsLate", depDelayString);
                        } else if (status == "CANCELLED"){
                            departure.put("departureTime", "-");
                        } else {
                            departure.put("departureTime", scheduledDep);
                            departure.put("minsLate", "On time");
                        }

                        departure.put("destination", destination);

                        if(platform == "null"){
                            departure.put("platform", "-");
                        }else {
                            departure.put("platform", "Plat. " + platform);
                        }


                        departureList.add(departure);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    RailDepartures.this, departureList,
                    R.layout.list_item, new String[]{"departureTime", "destination",
                    "platform", "expectedDepTime", "minsLate"}, new int[]{R.id.scheduledArrTextView,
                    R.id.destinationTextView, R.id.platformTextView, R.id.expectedDepTextView, R.id.minsLateTextView});

            lv.setAdapter(adapter);
        }
    }
}