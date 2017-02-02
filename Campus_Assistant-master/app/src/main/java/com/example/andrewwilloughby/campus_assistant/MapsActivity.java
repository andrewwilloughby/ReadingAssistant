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
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, AdapterView.OnItemSelectedListener, GoogleMap.OnPoiClickListener {
    protected GoogleMap map;
    private GoogleApiClient googleApiClient;
    protected Location lastLocation;
    private Marker currentLocationMarker;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Button search_Btn;
    private Marker searchMarker;
    private Marker poiMarker;
    private LatLng currentLatLng;
    private ProgressDialog pDialog;
    private static String url;
    private Polyline routeLine;
    private LinearLayout navDetailsLayout;
    private String distance = null;
    private String duration = null;
    private TextView distanceTextView;
    private TextView durationTextView;
    private EditText searchBox;
    private boolean spinnerTouched = false;
    protected MarkerOptions currentLocationMarkerOptions;
    private LinearLayout searchNearbyItems;
    protected String currentNearbySearchCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageButton cancelRouteBtn;
        Spinner searchNearbySpinner;

        setContentView(R.layout.activity_maps);

        //Check whether Google Play Services are available
        if (!CheckGooglePlayServices()) finish();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkPermissionLocation();

        String searchNearbyItemsStrings[] = getResources().getStringArray(R.array.search_nearby_items);
        List<String> searchItems = new ArrayList<>(java.util.Arrays.asList(searchNearbyItemsStrings));

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

        if (!item.equals("none_selected")){
            String url = getPlacesUrl(currentLatLng.latitude, currentLatLng.longitude, item);
            displayToast(item);
            currentNearbySearchCategory = item;
            Object[] DataTransfer = new Object[2];
            DataTransfer[0] = map;
            DataTransfer[1] = url;
            GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
            getNearbyPlacesData.execute(DataTransfer);
        }
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
        double lowerLeftLat = 49.871159, lowerLeftLng = -6.37988, upperRightLat = 55.811741, upperRightLng = 1.76896;

        if (navDetailsLayout.getVisibility() == View.VISIBLE){
            navDetailsLayout.setVisibility(View.GONE);
        }

        if (isNetworkAvailable()) {
            //Close virtual keyboard.
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            if (!location.isEmpty()) {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1, lowerLeftLat, lowerLeftLng, upperRightLat, upperRightLng);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addressList.size() < 1) {
                    displayToast("No locations found.");
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
                    searchMarker.showInfoWindow();
                    map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    map.animateCamera(CameraUpdateFactory.zoomTo(11));
                }
            } else {
                displayToast("No search value entered.");
            }
        } else {
            displayToast("No network available.");
        }
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {
        LatLng poiLatLng;

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

        Marker disabledEntranceMarker;

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        map.setOnPoiClickListener(this);

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (routeLine != null){
                    routeLine.remove();
                }

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
                } catch (InterruptedException | ExecutionException e) {
                    displayToast("Error obtaining route information. Check network connection.");
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
            EditText searchBar = (EditText) findViewById(R.id.searchBox);
            searchBar.setText(intent.getExtras().getString("search_value"));
            search_Btn.performClick();
            searchBar.setText("");
        }

        double lat = Double.parseDouble("51.437977"), lng = Double.parseDouble("-0.944745");
        LatLng latLng = new LatLng(lat, lng);

        MarkerOptions disabledEntranceMarkerOptions = new MarkerOptions()
                .position(latLng)
                .title("Disabled entrance")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.wheelchair_icon));

        disabledEntranceMarker = map.addMarker(disabledEntranceMarkerOptions);
        disabledEntranceMarker.showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));
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
        LocationRequest locationRequest;

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    protected String getPlacesUrl(double latitude, double longitude, String nearbyPlace){
        StringBuilder placesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        placesUrl.append("location=" + latitude + "," + longitude);
        placesUrl.append("&radius=10000");
        placesUrl.append("&type=" + nearbyPlace);
        placesUrl.append("&sensor=true&key=AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
        return (placesUrl.toString());
    }

    protected String makeDirectionsURL(String originLatLng, String destLatLng){
        if ((!originLatLng.matches("^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$")) || (!destLatLng.matches("^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$"))){
            return null;
        }

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

        currentLocationMarkerOptions = new MarkerOptions()
                .position(currentLatLng)
                .title("Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        currentLocationMarker = map.addMarker(currentLocationMarkerOptions);

        if (googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
    }

    public boolean checkPermissionLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }

        return true;
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

        if (activeNetworkInfo != null){
            return true;
        }
        return false;
    }

    public Boolean drawPath(String result) {
        try {
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            final JSONArray legArray = routeArray.getJSONObject(0).getJSONArray("legs");

            JSONObject distanceObj = legArray.getJSONObject(0).getJSONObject("distance");
            distance = distanceObj.getString("text");

            JSONObject durationObj = legArray.getJSONObject(0).getJSONObject("duration");
            duration = durationObj.getString("text");

            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            routeLine = map.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))
                    .geodesic(true)
            );
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected void displayToast(String toastContent){
        if (!toastContent.isEmpty()){
            Toast.makeText(getApplicationContext(), toastContent, Toast.LENGTH_SHORT).show();
        }
    }
    private List<LatLng> decodePoly(String encodedString) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, length = encodedString.length(), lat = 0, lng = 0;

        while (index < length) {
            int b, shift = 0, result = 0;

            do {
                b = encodedString.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encodedString.charAt(index++) - 63;
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
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Fetching route, Please wait...");
            pDialog.setIndeterminate(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            DownloadUrl downloadUrl = new DownloadUrl();
            return downloadUrl.readUrl(url);
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);

            if(pDialog.isShowing())
                pDialog.dismiss();
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
                map = (GoogleMap) params[0];
                url = (String) params[1];
                DownloadUrl downloadUrl = new DownloadUrl();
                placesData = downloadUrl.readUrl(url);
            } catch (Exception e){
                return null;
            }
            return placesData;
        }

        @Override
        protected void onPostExecute(String result){
            DataParser dataParser = new DataParser();
            List<HashMap<String, String>> nearbyPlacesList = dataParser.parse(result);

            if (nearbyPlacesList != null){
                ShowNearbyPlaces(nearbyPlacesList);
            } else {
                displayToast("Error obtaining data from Google Maps");
            }
        }

        private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList){
            for (int i = 0; i < nearbyPlacesList.size(); i++){

                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);

                double lat = Double.parseDouble(googlePlace.get("lat"));
                double lng = Double.parseDouble(googlePlace.get("lng"));
                LatLng latLng = new LatLng(lat, lng);

                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(latLng)
                        .title(placeName + " : " + vicinity);

                switch (currentNearbySearchCategory){
                    case "cafe":{
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.cafe_icon));
                        break;
                    }
                    case "parking":{
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.parking_icon));
                        break;
                    }
                    case "convenience_store":{
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.store_icon));
                        break;
                    }
                    case "pharmacy":{
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.pharmacy_icon));
                        break;
                    }
                    case "hospital":{
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.hospital_icon));
                        break;
                    }
                    case "police":{
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.police_icon));
                        break;
                    }
                }

                map.addMarker(markerOptions);

                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
        }
    }
}
