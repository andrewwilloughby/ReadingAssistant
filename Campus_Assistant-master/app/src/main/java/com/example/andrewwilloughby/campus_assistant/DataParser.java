package com.example.andrewwilloughby.campus_assistant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class which parses JSON data returned from a RESTful URL call.
 * @author Andrew Willoughby
 */
public class DataParser {

    /**
     * Method that parses JSON string and returns List of places.
     * @param jsonData the JSON data to parse.
     * @return List populated with nearby places.
     */
    public List<HashMap<String, String>> parse(String jsonData){
        JSONArray jsonArray;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
            return getPlaces(jsonArray);
        } catch (Exception e){
            return null;
        }
    }

    /**
     * Method called by parse method, returns a list of places to be returned from the class.
     * @param jsonArray the array constructed in the parse method.
     * @return the list of nearby places.
     */
    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray){
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap;

        for (int i = 0; i < placesCount; i++){
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }

    /**
     * Method which obtains each nearby place.
     * @param googlePlaceJson the JSON object to manipulate.
     * @return Hashmap containing one nearby place's data.
     */
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson){
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-", vicinity = "-NA-";

        try {
            if (!googlePlaceJson.isNull("name")){
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")){
                vicinity = googlePlaceJson.getString("vicinity");
            }
            String latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            String longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            String reference = googlePlaceJson.getString("reference");

            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return googlePlaceMap;
    }
}
