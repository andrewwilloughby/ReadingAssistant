package com.example.andrewwilloughby.campus_assistant;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
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

/**
 * Activity for the interactive Google Map.
 * @author Andrew Willoughby
 */
public class InteractiveMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, AdapterView.OnItemSelectedListener, GoogleMap.OnPoiClickListener, View.OnClickListener, View.OnTouchListener {
    protected GoogleMap map;
    private GoogleApiClient googleApiClient;
    protected Location currentLocation;
    protected int proximityRadius = 1000;
    public static final int LOCATION_PERMISSION = 99;
    private Button searchBtn;
    private Marker searchMarker, poiMarker;
    private LatLng currentLatLng;
    private ProgressDialog pDialog;
    private Polyline routeLine;
    private LinearLayout navDetailsLayout, searchNearbyItems;
    private String url, distance = null, duration = null;
    private TextView distanceTextView, durationTextView;
    private EditText searchBox;
    private boolean spinnerTouched = false;
    protected String currentNearbySearchCategory;
    private ArrayList<Marker> disabledEntrancesMarkerArray = new ArrayList<>();

    /**
     * Method to set up the Activity upon creation.
     *
     * @param savedInstanceState parameter which indicates the previous state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interactive_map);

        if (!checkGooglePlayServices()) finish();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) checkPermissionLocation();

        String searchNearbyItemsStrings[] = getResources().getStringArray(R.array.search_nearby_items);
        List<String> searchItems = new ArrayList<>(java.util.Arrays.asList(searchNearbyItemsStrings));

        searchNearbyItems = (LinearLayout) findViewById(R.id.searchNearbyItems);

        searchBox = (EditText) findViewById(R.id.searchBox);
        searchBox.setOnClickListener(this);

        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        navDetailsLayout = (LinearLayout) findViewById(R.id.navDetails);
        distanceTextView = (TextView) findViewById(R.id.distanceTxtView);
        durationTextView = (TextView) findViewById(R.id.durationTxtView);

        ImageButton cancelRouteBtn = (ImageButton) findViewById(R.id.cancelRouteBtn);
        cancelRouteBtn.setOnClickListener(this);

        Spinner searchNearbySpinner = (Spinner) findViewById(R.id.searchNearbySpinner);
        searchNearbySpinner.setOnItemSelectedListener(this);
        searchNearbySpinner.setOnTouchListener(this);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, searchItems);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        searchNearbySpinner.setAdapter(spinnerAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Method to handle onTouch events from items in the activity.
     *
     * @param v the view element of the item triggering the event.
     * @param event the type of touch.
     * @return boolean value to indicate whether event has been consumed by the listener.
     */
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()){
            case R.id.searchNearbySpinner: {
                spinnerTouched = true;
                break;
            }
        }
        return false;
    }

    /**
     * Method to handle onClick events from buttons displayed in the activity.
     *
     * @param viewInput the view initiating the onClick method.
     */
    public void onClick(View viewInput){
        switch (viewInput.getId()){
            case R.id.searchBox: searchBox.setText(""); break;
            case R.id.searchBtn: onMapSearch(viewInput); break;
            case R.id.cancelRouteBtn:{
                if (routeLine != null){
                    map.clear();
                    searchBox.setText("");
                }

                navDetailsLayout.setVisibility(View.GONE);
                searchNearbyItems.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    /**
     * Method to handle selection of items within elements in the activity.
     *
     * @param parent the adapterView parent of the element.
     * @param view the view of the element selected.
     * @param position the position of the item selected within the element.
     * @param id the ID of the selected item.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (spinnerTouched) {
            map.clear();

            if (navDetailsLayout.getVisibility() == View.VISIBLE) { navDetailsLayout.setVisibility(View.GONE); }

            String item = parent.getItemAtPosition(position).toString().toLowerCase().replace(" ", "_");

            if (!item.equals("none_selected")) {
                String url = buildPlacesUrl(currentLatLng.latitude, currentLatLng.longitude, item);
                currentNearbySearchCategory = item;
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = map;
                DataTransfer[1] = url;
                GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                getNearbyPlacesData.execute(DataTransfer);
            }
        }
    }

    /**
     * Method to handle searches within the Google Map.
     * @param view the initiating view item.
     */
    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.searchBox);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        double lowerLeftLat = 49.871159, lowerLeftLng = -6.37988, upperRightLat = 55.811741, upperRightLng = 1.76896;

        if (navDetailsLayout.getVisibility() == View.VISIBLE){
            navDetailsLayout.setVisibility(View.GONE);
        }

        if (isNetworkAvailable()) {
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
                    Toast.makeText(getApplicationContext(), "No locations found.", Toast.LENGTH_SHORT).show();
                } else {
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
                Toast.makeText(getApplicationContext(), "No search value entered.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No network available.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to handle clicks on Points of Interest within the Google Map.
     * @param poi the Point of Interest selected.
     */
    @Override
    public void onPoiClick(PointOfInterest poi) {
        if (poiMarker != null){
            poiMarker.remove();
        }

        LatLng poiLatLng = new LatLng(poi.latLng.latitude, poi.latLng.longitude);

        MarkerOptions poiMarkerOptions = new MarkerOptions()
                .position(poiLatLng)
                .title(poi.name);
        poiMarker = map.addMarker(poiMarkerOptions);
        poiMarker.showInfoWindow();

        map.moveCamera(CameraUpdateFactory.newLatLng(poiLatLng));
    }

    /**
     * Method to set up the Google Map once the map fragment is loaded.
     * @param googleMap the Google Map fragment.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        addDisabledEntranceMarkers();

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

                LatLng originLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                String originLatLngStr = originLatLng.toString().replace("lat/lng: ", "").replace("(", "").replace(")", "");

                LatLng destinationLatLng = marker.getPosition();
                String destinationlatLngStr = destinationLatLng.toString().replace("lat/lng: ", "").replace("(", "").replace(")", "");

                url = makeDirectionsUrl(originLatLngStr, destinationlatLngStr);

                try {
                    String jsonStr = new GetJSONStr().execute().get();
                    drawPath(jsonStr);
                } catch (InterruptedException | ExecutionException e) {
                    Toast.makeText(getApplicationContext(), "Error obtaining route information. Check network connection.", Toast.LENGTH_SHORT).show();
                }

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                builder.include(marker.getPosition());

                LatLngBounds bounds = builder.build();

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                map.animateCamera(cameraUpdate);

                navDetailsLayout.setVisibility(View.VISIBLE);
                distanceTextView.setText(distance);
                durationTextView.setText(duration);
            }
        });

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (map.getCameraPosition().zoom > 15.0){
                    for (Marker marker : disabledEntrancesMarkerArray) {
                        marker.setVisible(true);
                    }
                }
                else {
                    for (Marker marker : disabledEntrancesMarkerArray) {
                        marker.setVisible(false);
                    }
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }

        if (getIntent().hasExtra("search_value")){
            EditText searchBar = (EditText) findViewById(R.id.searchBox);
            searchBar.setText(getIntent().getExtras().getString("search_value"));
            searchBtn.performClick();
            searchBar.setText("");
        }

        LatLng universityLatLng = new LatLng(Double.parseDouble("51.441405"), Double.parseDouble("-0.941751"));

        map.moveCamera(CameraUpdateFactory.newLatLng(universityLatLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(11));
    }

    /**
     * Method to set up the obtaining user's current location once connected to Google Play Services.
     * @param bundle data provided by Google Play Services.
     */
    public void onConnected(Bundle bundle){
        LocationRequest locationRequest = new LocationRequest()
                .setInterval(1000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    /**
     * (Unused) method to handle connection loss.
     * @param i the cause of connection loss.
     */
    @Override
    public void onConnectionSuspended(int i){}

    /**
     * Method that handles changes to user's location.
     *
     * @param location the new location.
     */
    @Override
    public void onLocationChanged(Location location){
        currentLocation = location;

        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    /**
     * (Unused) Method that handles failed connection.
     *
     * @param connectionResult the connection result used for resolving the error.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){
    }

    /**
     * Method to act upon request to user for location permission.
     *
     * @param requestCode the type of request.
     * @param permissions the granted permissions.
     * @param grantResults the user's decision.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        if (googleApiClient == null){
                            buildGoogleApiClient();
                        }
                        map.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Method to check the availability of Google Play Services.
     *
     * @return boolean value indicating availability.
     */
    private boolean checkGooglePlayServices(){
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

    /**
     * Method to connect to the Google Maps API.
     */
    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    /**
     * Method to add markers for disabled building entrances.
     */
    public void addDisabledEntranceMarkers(){
        LatLng latLng = new LatLng(Double.parseDouble("51.437977"), Double.parseDouble("-0.944745"));

        MarkerOptions disabledEntranceMarkerOptions = new MarkerOptions()
                .position(latLng)
                .title("Disabled entrance")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.wheelchair_icon))
                .visible(false);

        Marker marker = map.addMarker(disabledEntranceMarkerOptions);
        disabledEntrancesMarkerArray.add(marker);
    }

    /**
     * Method that populates the RESTful URL call to obtain nearby places.
     *
     * @param latitude the user's current latitude.
     * @param longitude the user's current longitude.
     * @param nearbyPlace the type of place selected.
     * @return the populated RESTful URL, ready to be executed.
     */
    protected String buildPlacesUrl(double latitude, double longitude, String nearbyPlace){
        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
                + "location=" + latitude + "," + longitude + "&radius=" + proximityRadius + "&type="
                + nearbyPlace + "&sensor=true&key=AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0";
    }

    /**
     * Method that populates the RESTful URL call to obtain directions to a location.
     *
     * @param originLatLng the user's current LatLng.
     * @param destLatLng the destination LatLng.
     * @return the populated RESTful URL, ready to be executed, or Null if either LatLng is not correct format.
     */
    protected String makeDirectionsUrl(String originLatLng, String destLatLng){
        if ((originLatLng.matches("^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$"))
                && (destLatLng.matches("^(\\-?\\d+(\\.\\d+)?),\\s*(\\-?\\d+(\\.\\d+)?)$"))){
            return "https://maps.googleapis.com/maps/api/directions/json?origin="
                    + originLatLng + "&destination=" + destLatLng
                    + "&sensor=false&mode=walking&alternatives=true&key=AIzaSyCWyi6EulBxUxWDPZKwV9Ud6AZnmBqkWfE";
        }
        return null;
    }

    /**
     * Method to check if user has given permission for location tracking.
     *
     * @return boolean value to indicate permission.
     */
    public boolean checkPermissionLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
            }
            return false;
        }
        return true;
    }

    /**
     * Method that checks the availability of an active network connection.
     *
     * @return boolean value to indicate availability of network.
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null){
            return true;
        }
        return false;
    }

    /**
     * Method to draw the route between user and destination.
     * @param result the JSON string containing route details.
     * @return boolean value to indicate successful drawing of route.
     */
    public boolean drawPath(String result) {
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

    /**
     * Method to decode the encoded polyline, using bit-shifting.
     * Available from: http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * @param encodedString the encoded route string.
     * @return the route in polyline form.
     */
    private List<LatLng> decodePoly(String encodedString) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, length = encodedString.length();
        int lat = 0, lng = 0;

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

    /**
     * (Unused) Method to handle cancelled item selection.
     * @param arg0
     */
    public void onNothingSelected(AdapterView<?> arg0) {}

    /**
     * Private AsyncTask to obtain JSON string containing route.
     */
    private class GetJSONStr extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(InteractiveMapActivity.this);
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

            if(pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    /**
     * Private AsyncTask to utilise nearby places data.
     */
    private class GetNearbyPlacesData extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... params) {
            try {
                String url = (String) params[1];
                DownloadUrl downloadUrl = new DownloadUrl();
                return downloadUrl.readUrl(url);
            } catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result){
            DataParser dataParser = new DataParser();
            List<HashMap<String, String>> nearbyPlacesList = dataParser.parse(result);

            if (nearbyPlacesList != null){
                showNearbyPlaces(nearbyPlacesList);
            } else {
                Toast.makeText(getApplicationContext(), "Error obtaining data from Google Maps", Toast.LENGTH_SHORT).show();
            }
        }

        private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList){
            for (int i = 0; i < nearbyPlacesList.size(); i++){

                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                LatLng latLng = new LatLng(Double.parseDouble(googlePlace.get("lat")), Double.parseDouble(googlePlace.get("lng")));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(placeName + " : " + vicinity);

                switch (currentNearbySearchCategory){
                    case "cafe": markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.cafe_icon)); break;
                    case "parking": markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.parking_icon)); break;
                    case "convenience_store": markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.store_icon)); break;
                    case "pharmacy": markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.pharmacy_icon)); break;
                    case "hospital": markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.hospital_icon)); break;
                    case "police": markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.police_icon)); break;
                }

                map.addMarker(markerOptions);
                map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                map.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
        }
    }
}