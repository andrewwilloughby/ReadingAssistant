package com.example.andrewwilloughby.campus_assistant;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Activity for displaying live bus times.
 *
 * @author Andrew Willoughby
 */
public class BusTimesActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    private ProgressDialog pDialog;
    private ListView busListView;
    private String url;
    private ArrayList<HashMap<String, String>> departureList;
    private SwipeRefreshLayout swipeLayout;

    /**
     * Method to set up the Activity upon creation.
     *
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_times);

        Button chancellorWayBusBtn, whiteknightsHouseBusBtn, readingStationBusBtn;

        setTitle("Live Bus Departures");

        departureList = new ArrayList<>();

        busListView = (ListView) findViewById(R.id.busList);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.bus_swipe_layout);
        swipeLayout.setOnRefreshListener(this);

        chancellorWayBusBtn = (Button) findViewById(R.id.chancellorWayBusBtn);
        chancellorWayBusBtn.setOnClickListener(this);

        whiteknightsHouseBusBtn = (Button) findViewById(R.id.whiteknightsHouseBusBtn);
        whiteknightsHouseBusBtn.setOnClickListener(this);

        readingStationBusBtn = (Button) findViewById(R.id.readingStationBusBtn);
        readingStationBusBtn.setOnClickListener(this);

        chancellorWayBusBtn.performClick();
    }

    /**
     * Method to handle the onRefresh events from the swipe layout.
     */
    public void onRefresh() {
        departureList.clear();
        swipeLayout.setRefreshing(true);
        new GetDepartures().execute(url);
        swipeLayout.setRefreshing(false);
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     *
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput) {
        departureList.clear();

        switch (viewInput.getId()){
            case R.id.whiteknightsHouseBusBtn: url = getApplicationContext().getString(R.string.whiteknightsHouseBusUrl); break;
            case R.id.readingStationBusBtn: url = getApplicationContext().getString(R.string.railStationBusUrl); break;
            case R.id.chancellorWayBusBtn: url = getApplicationContext().getString(R.string.chancellorWayBusUrl); break;
        }

        new GetDepartures().execute(url);
    }

    /**
     * Private AsyncTask to download the live bus times away from the main thread.
     */
    protected class GetDepartures extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BusTimesActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... arg0) {
            DownloadUrl downloadUrl = new DownloadUrl();
            String jsonStr = downloadUrl.readUrl(url);

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

                        HashMap<String, String> busDeparture = new HashMap<>();

                        if ((routeNumber.equals("21")) || (routeNumber.equals("21a"))){
                            busDeparture.put("routeNumber", routeNumber);
                            busDeparture.put("destination", destination);
                            busDeparture.put("expectedDepTime", expectedDep);
                            departureList.add(busDeparture);
                        }
                    }
                } catch (final JSONException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {Toast.makeText(getApplicationContext(),
                                "Error with live information. Please retry.",
                                Toast.LENGTH_SHORT).show();}
                    });
                }
            } else { runOnUiThread(new Runnable() {
                    @Override
                    public void run() {Toast.makeText(getApplicationContext(),
                            "Network error. Please check connection.",
                            Toast.LENGTH_SHORT).show();}
                });
            }return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (pDialog.isShowing()) {pDialog.dismiss();}

            ListAdapter adapter = new SimpleAdapter(
                    BusTimesActivity.this, departureList,
                    R.layout.bus_list_item, new String[]{"routeNumber", "destination",
                    "expectedDepTime"}, new int[]{R.id.routeNumberTextView,
                    R.id.busDestinationTextView, R.id.busExpectedDepTextView});
            busListView.setAdapter(adapter);
        }
    }
}