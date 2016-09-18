package com.example.johnteng.foodfinder;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by johnteng on 16-09-17.
 */

public class jsonParser {
    public jsonParser () {
    }

    public void parseSearchJson (JSONObject j) {
        try {

            JSONArray dataJsonArray = j.getJSONArray("businesses");
            for(int i=0; i<dataJsonArray.length(); i++) {
                JSONObject dataObj = (JSONObject)dataJsonArray.get(i);
                foodResponse.id = dataObj.getString("id");
                foodResponse.name = j.getString("name");
                foodResponse.image_url=j.getString("image_url");
                foodResponse.url = j.getString("url");
                foodResponse.price = j.getString("price");
                foodResponse.phone = j.getString("phone");
                foodResponse.rating = j.getDouble("rating");
                foodResponse.reviewCount = j.getInt("review_count");
                foodResponse.location = j.getString("location");
                //Similarly you can extract for other fields.
            }
            /*
            foodResponse.id = j.getString("businesses[0].id");
            foodResponse.name = j.getString("businesses[0].name");
            foodResponse.image_url=j.getString("businesses[0].image_url");
            foodResponse.url = j.getString("businesses[0].url");
            foodResponse.price = j.getString("businesses[0].price");
            foodResponse.phone = j.getString("businesses[0].phone");
            foodResponse.rating = j.getDouble("businesses[0].rating");
            foodResponse.reviewCount = j.getInt("businesses[0].review_count");
            foodResponse.location = j.getString("businesses[0].location");
            */

        }
        catch (JSONException e) {
            Log.d("ERROR", "Unable to parse JSON from the server");
        }
        Log.d("Log", foodResponse.id);
        Log.d("Log", foodResponse.name);
        Log.d("Log",foodResponse.image_url);
        Log.d("Log",foodResponse.url);
        Log.d("Log",foodResponse.price );
        Log.d("Log",foodResponse.phone);
        Log.d("Log",Double.toString(foodResponse.rating));
        Log.d("Log",Integer.toString(foodResponse.reviewCount));
        Log.d("Log",foodResponse.location);


    }

    public void parseAuthJson (JSONObject j) {
        try {
            authResponse.tokenType = j.getString("token_type");
            authResponse.expiresIn = j.getInt("expires_in");
            authResponse.accessToken = j.getString("access_token");
        }
        catch (JSONException e) {
            Log.d("ERROR", "Unable to parse JSON from the server");
        }
        Log.d("Log","The object a contains access token " + authResponse.accessToken);
        Log.d("Log", "The object a contains token type of " + authResponse.tokenType);
        Log.d("Log","The object has an expiry time of " + authResponse.expiresIn);
    }
}
