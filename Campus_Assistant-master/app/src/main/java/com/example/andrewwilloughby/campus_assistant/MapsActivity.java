package com.example.andrewwilloughby.campus_assistant;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.fitness.data.Goal;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.text.Text;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, AdapterView.OnItemSelectedListener, GoogleMap.OnPoiClickListener {
    private GoogleMap map;
    private int PROXIMITY_RADIUS = 5000;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location lastLocation;
    Marker currentLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Button search_Btn;
    private Context context;
    private String searchValue;
    private Marker searchMarker;
    private Marker poiMarker;
    private LatLng currentLatLng;
    private LatLng poiLatLng;
    private ProgressDialog pDialog;
    private static String url;
    private Polyline routeLine;
    private LinearLayout navDetailsLayout;
    private String distance = null;
    private String duration = null;
    private TextView distanceTextView;
    private TextView durationTextView;
    private ImageButton cancelRouteBtn;
    private EditText searchBox;
    private Spinner searchNearbySpinner;
    private boolean spinnerTouched = false;
    MarkerOptions currentLocationMarkerOptions;
    private LinearLayout searchNearbyItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        context = this;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ checkPermissionLocation(); }

        //Check whether Google Play Services are available
        if (!CheckGooglePlayServices()){ finish();}

        searchNearbyItems = (LinearLayout) findViewById(R.id.searchNearbyItems);

        searchBox = (EditText) findViewById(R.id.searchBox);
        searchBox.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                searchBox.setText("");
            }
        });

        search_Btn = (Button) findViewById(R.id.searchBtn);
        search_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                onMapSearch(view);
            }
        });

        navDetailsLayout = (LinearLayout) findViewById(R.id.navDetails);
        distanceTextView = (TextView) findViewById(R.id.distanceTxtView);
        durationTextView = (TextView) findViewById(R.id.durationTxtView);

        cancelRouteBtn = (ImageButton) findViewById(R.id.cancelRouteBtn);
        cancelRouteBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if (routeLine != null){
                    map.clear();
                    map.addMarker(currentLocationMarkerOptions);
                    searchBox.setText("");
                }

                if (navDetailsLayout.getVisibility() == View.VISIBLE){
                    navDetailsLayout.setVisibility(View.GONE);
                }

                if (searchNearbyItems.getVisibility() == View.GONE){
                    searchNearbyItems.setVisibility(View.VISIBLE);
                }
            }
        });

        searchNearbySpinner = (Spinner) findViewById(R.id.searchNearbySpinner);
        searchNearbySpinner.setOnItemSelectedListener(this);
        searchNearbySpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spinnerTouched = true;
                return false;
            }
        });

        List<String> searchItems = new ArrayList<String>();
        searchItems.add("Cafe");
        searchItems.add("Restaurant");
        searchItems.add("Bank");
        searchItems.add("Bicycle store");
        searchItems.add("Convenience Store");
        searchItems.add("Doctor");
        searchItems.add("Pharmacy");
        searchItems.add("Hospital");
        searchItems.add("Police");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, searchItems);

        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        searchNearbySpinner.setAdapter(spinnerAdapter);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!spinnerTouched){ return; }

        map.clear();
        map.addMarker(currentLocationMarkerOptions);

        if (navDetailsLayout.getVisibility() == View.VISIBLE){
            navDetailsLayout.setVisibility(View.GONE);
        }

        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString().toLowerCase().replace(" ", "_");

        String url = getPlacesUrl(currentLatLng.latitude, currentLatLng.longitude, item);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = map;
        DataTransfer[1] = url;
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private boolean CheckGooglePlayServices(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS){
            if (googleApiAvailability.isUserResolvableError(result)){
                googleApiAvailability.getErrorDialog(this, result, 0).show();
            }
            return false;
        }
        return true;
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.searchBox);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        // Variables to express lat/lng of England bounding box. Enables Google Maps to prioritise results found within the bounding box.
        double lowerLeftLat = 49.871159;
        double lowerLeftLng = -6.37988;
        double upperRightLat = 55.811741;
        double upperRightLng = 1.76896;

        if (navDetailsLayout.getVisibility() == View.VISIBLE){
            navDetailsLayout.setVisibility(View.GONE);
        }

        if (isNetworkAvailable()) {

            //Close virtual keyboard.
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            if (location != null) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1, lowerLeftLat, lowerLeftLng, upperRightLat, upperRightLng);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addressList.size() < 1) {
                    Toast.makeText(this, "No locations found.", Toast.LENGTH_SHORT);
                } else {
                    //Clear existing search marker.
                    if (searchMarker != null){
                        searchMarker.remove();
                    }

                    if (poiMarker != null){
                        poiMarker.remove();
                    }
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    MarkerOptions searchMarkerOptions = new MarkerOptions().position(latLng).title(address.getAddressLine(0));
                    searchMarker = map.addMarker(searchMarkerOptions);
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    map.animateCamera(CameraUpdateFactory.zoomTo(11));
                }
            }
        } else {
            Toast.makeText(context, "No network available.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {

        if (poiMarker != null){
            poiMarker.remove();
        }

        //Place a marker for current location.
        poiLatLng = new LatLng(poi.latLng.latitude, poi.latLng.longitude);

        MarkerOptions poiMarkerOptions = new MarkerOptions();
        poiMarkerOptions.position(poiLatLng);
        poiMarkerOptions.title(poi.name);
        poiMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        poiMarker = map.addMarker(poiMarkerOptions);

        poiMarker.showInfoWindow();

        map.moveCamera(CameraUpdateFactory.newLatLng(poiLatLng));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnPoiClickListener(this);

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                if (routeLine != null){ routeLine.remove(); }

                if (searchNearbyItems.getVisibility() == View.VISIBLE){
                    searchNearbyItems.setVisibility(View.GONE);
                }

                LatLng originLatLng = currentLocationMarker.getPosition();
                String originLatLngStr = originLatLng.toString().replace("lat/lng: ", "").replace("(", "").replace(")", "");

                LatLng destinationLatLng = marker.getPosition();
                String destinationlatLngStr = destinationLatLng.toString().replace("lat/lng: ", "").replace("(", "").replace(")", "");

                url = makeDirectionsURL(originLatLngStr, destinationlatLngStr);

                try {
                    String jsonStr = new getJSONStr().execute().get();
                    drawPath(jsonStr);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(currentLocationMarker.getPosition());
                builder.include(marker.getPosition());

                LatLngBounds bounds = builder.build();

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                map.animateCamera(cameraUpdate);

                navDetailsLayout.setVisibility(View.VISIBLE);
                distanceTextView.setText(distance);
                durationTextView.setText(duration);

            }
        });

        //Disable the Google Maps toolbar, prevent exit from app.
        map.getUiSettings().setMapToolbarEnabled(false);

        //Initialise Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }

        Intent intent = getIntent();
        if (intent.hasExtra("search_value")){
            searchValue = intent.getExtras().getString("search_value");
        }

        if (searchValue!= null) {
            EditText searchBar = (EditText) findViewById(R.id.searchBox);
            searchBar.setText(searchValue);
            search_Btn.performClick();
        }

    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    public void onConnected(Bundle bundle){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY); //What is this?

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    private String getPlacesUrl(double latitude, double longitude, String nearbyPlace){
        StringBuilder placesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        placesUrl.append("location=" + latitude + "," + longitude);
        placesUrl.append("&radius=" + PROXIMITY_RADIUS);
        placesUrl.append("&type=" + nearbyPlace);
        placesUrl.append("&sensor=true");
        placesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        Log.d("getUrl", placesUrl.toString());
        return (placesUrl.toString());
    }

    private String makeDirectionsURL(String originLatLng, String destLatLng){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");
        urlString.append(originLatLng);

        urlString.append("&destination=");
        urlString.append(destLatLng);

        urlString.append("&sensor=false&mode=walking&alternatives=true");
        urlString.append("&key=AIzaSyCWyi6EulBxUxWDPZKwV9Ud6AZnmBqkWfE");

        return urlString.toString();
    }

    @Override
    public void onConnectionSuspended(int i){

    }

    @Override
    public void onLocationChanged(Location location){
        lastLocation = location;
        if (currentLocationMarker != null){
            currentLocationMarker.remove();
        }

        //Place a marker for current location.
        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentLocationMarkerOptions = new MarkerOptions();
        currentLocationMarkerOptions.position(currentLatLng);
        currentLocationMarkerOptions.title("Current Location");
        currentLocationMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        currentLocationMarker = map.addMarker(currentLocationMarkerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));

        if (googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
    }

    public boolean checkPermissionLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            //Asking user if explanation is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                //Show an explanation to the user (asynchronously) -- don't block
                //this thread waiting for user's response! After the user
                //sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                //No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                //If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if (googleApiClient == null){
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }
                } else {
                    //Permission denied, disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (null != activeNetworkInfo){
            return true;
        } else {
            return false;
        }
    }

    public void drawPath(String  result) {

        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            final JSONArray legArray = routeArray.getJSONObject(0).getJSONArray("legs");

            //Distance
            JSONObject distanceObj = legArray.getJSONObject(0).getJSONObject("distance");
            distance = distanceObj.getString("text"); //String that contains the distance value formated

            JSONObject durationObj = legArray.getJSONObject(0).getJSONObject("duration");
            duration = durationObj.getString("text");

            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            routeLine = map.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );
           /*
           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
                .color(Color.BLUE).geodesic(true));
            }
           */
        }
        catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)), (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    private class getJSONStr extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Fetching route, Please wait...");
            pDialog.setIndeterminate(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);

            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
        }
    }

    // Code developed from tutorial: https://www.androidtutorialpoint.com/intermediate/google-maps-search-nearby-displaying-nearby-places-using-google-places-api-google-maps-api-v2/
    private class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

        String placesData;
        GoogleMap map;
        String url;

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d("GetNearbyPlacesData", "doInBackground entered");
                map = (GoogleMap) params[0];
                url = (String) params[1];
                DownloadUrl downloadUrl = new DownloadUrl();
                placesData = downloadUrl.readUrl(url);
            } catch (Exception e){
                Log.d("GooglePlacesReadTask", e.toString());
            }
            return placesData;
        }

        @Override
        protected void onPostExecute(String result){
            Log.d("GooglePlacesReadTask", "onPostExecute entered");
            List<HashMap<String, String>> nearbyPlacesList = null;
            DataParser dataParser = new DataParser();
            nearbyPlacesList = dataParser.parse(result);
            ShowNearbyPlaces(nearbyPlacesList);
            Log.d("GooglePlacesReadTask", "onPostExecute exit");
        }

        private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList){
            for (int i = 0; i < nearbyPlacesList.size(); i++){
                Log.d("onPostExecute", "entered into showing locations");
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : " + vicinity);
                map.addMarker(markerOptions);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                //move map camera
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
        }
    }



}
