package com.example.andrewwilloughby.campus_assistant;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BusTimes extends AppCompatActivity {

    private String TAG = BusTimes.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url;
    ArrayList<HashMap<String, String>> departureList;

    private Button chancellorWayBusBtn;
    private Button whiteknightsHouseBusBtn;
    private Button readingStationBusBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_times);

        setTitle("Live buses: UoR - Reading Centre");

        departureList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetDepartures().execute();

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.bus_swipe_layout);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                departureList.clear();
                swipeLayout.setRefreshing(true);
                new GetDepartures().execute(url);
                swipeLayout.setRefreshing(false);

            }
        });

        chancellorWayBusBtn = (Button) findViewById(R.id.chancellorWayBusBtn);
        chancellorWayBusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                url = getApplicationContext().getString(R.string.chancellorWayBusBtnText);
                new GetDepartures().execute(url);
            }
        });

        whiteknightsHouseBusBtn = (Button) findViewById(R.id.whiteknightsHouseBusBtn);
        whiteknightsHouseBusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                url = getApplicationContext().getString(R.string.whiteknightsHouseBusUrl);
                new GetDepartures().execute(url);
            }
        });

        readingStationBusBtn = (Button) findViewById(R.id.readingStationBusBtn);
        readingStationBusBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                url = getApplicationContext().getString(R.string.railStationBusUrl);
                new GetDepartures().execute(url);
            }
        });
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetDepartures extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BusTimes.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg0) {
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

                        String routeNumber = ob.getString("line");
                        String destination = ob.getString("direction");
                        String expectedDep = ob.getString("aimed_departure_time");

                        // tmp hash map for single contact
                        HashMap<String, String> busDeparture = new HashMap<>();

                        busDeparture.put("routeNumber", routeNumber);
                        busDeparture.put("destination", destination);
                        busDeparture.put("expectedDepTime", expectedDep);

                        departureList.add(busDeparture);
                    }
                }catch (final JSONException e) {
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
                    BusTimes.this, departureList,
                    R.layout.bus_list_item, new String[]{"routeNumber", "destination",
                    "expectedDepTime"}, new int[]{R.id.routeNumberTextView,
                    R.id.busDestinationTextView, R.id.busExpectedDepTextView});

            lv.setAdapter(adapter);
        }
    }


}