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
import java.util.Iterator;

public class BusTimes extends AppCompatActivity {

    private String TAG = BusTimes.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url = "http://transportapi.com/v3/uk/bus/stop/039027900001/live.json?app_id=03bf8009&app_key=d9307fd91b0247c607e098d5effedc97&group=route&nextbuses=yes";
    ArrayList<HashMap<String, String>> departureList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_times);

        setTitle("Live Bus Departures: Chancellor's Way");

        departureList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetDepartures().execute();

        final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) findViewById(R.id.bus_swipe_layout);

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
            pDialog = new ProgressDialog(BusTimes.this);
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

                    Iterator<String> arrayIterator = allDepartures.keys();

                    while (arrayIterator.hasNext()){
                        String arrayKey = arrayIterator.next();

                        JSONArray array = allDepartures.getJSONArray(arrayKey);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject ob = array.getJSONObject(i);

                            String routeNumber = ob.getString("line");
                            String destination = ob.getString("direction");
                            String expectedDep = ob.getString("expected_departure_time");

                            // tmp hash map for single contact
                            HashMap<String, String> busDeparture = new HashMap<>();

                            busDeparture.put("routeNumber", routeNumber);
                            busDeparture.put("destination", destination);
                            busDeparture.put("expectedDepTime", expectedDep);

                            departureList.add(busDeparture);
                        }
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
                    BusTimes.this, departureList,
                    R.layout.bus_list_item, new String[]{"routeNumber", "destination",
                    "expectedDepTime"}, new int[]{R.id.routeNumberTextView,
                    R.id.busDestinationTextView, R.id.busExpectedDepTextView});

            lv.setAdapter(adapter);
        }
    }


}