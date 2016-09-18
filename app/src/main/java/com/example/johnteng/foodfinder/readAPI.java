package com.example.johnteng.foodfinder;

import android.util.Log;
import android.util.Pair;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.widget.SearchViewCompat.getQuery;

/**
 * Created by johnteng on 16-09-17.
 */
public class readAPI extends Thread {
    private final String CLIENT_ID = "IOcbXtzaehe_QsCfBsGE8w";
    private final String CLIENT_SECRET = "8aQYbsawHFrnDNJLjGLwKbSseQGsIf5AkaFwHcBg4ZVf8e8drIvLKk0xUtfoYaHw";
    private final String auth = "https://api.yelp.com/oauth2/token";
    private final String query = "https://api.yelp.com/v3/businesses/search";
    public String JSONresponse = "";
    private jsonParser jp;
    List<Pair> searchParameters;


    public readAPI(jsonParser jp) {
        this.jp = jp;
        //set context variables if required
    }

    public void run() {

        URL url;
        HttpURLConnection urlConnection = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        byte[] postData;
        String urlParameters;
        JSONObject j = null;
        searchParameters = new ArrayList<Pair>();

        try {
            if (FoodFinder.authenticate) {
                //If trying to authenticate to the server
                url = new URL(auth);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlParameters = "grant_type=client_credentials&client_id="+CLIENT_ID+"&client_secret="+CLIENT_SECRET;
                postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
                    //Send in POST params
                    wr.write( postData );
                    wr.close();
                }
            } else {
                //If trying to make a search query
                this.fillParameters();
                url = new URL(query+addToQuery(searchParameters));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Authorization", authResponse.tokenType + " " + authResponse.accessToken);//Authenticate the search request
                //OutputStream os = urlConnection.getOutputStream();
                //BufferedWriter writer = new BufferedWriter(
                  //      new OutputStreamWriter(os, "UTF-8"));
                //writer.write(addToQuery(searchParameters));
                //writer.flush();
                //writer.close();
                //os.close();
            }
            Log.d("LOG","Try block finished");

            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

            Log.d("Log","urlConnection is connected");

            InputStream stream = urlConnection.getInputStream();
            br = new BufferedReader(new InputStreamReader(stream));
            sb = new StringBuffer();
            JSONresponse = createJSON(br, sb);
            Log.d("Log",JSONresponse);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //Close the urlConnection and the bufferedReader object
            if (urlConnection != null)
                urlConnection.disconnect();

            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.d("Log","Finished closing everything");
        }
        try {
            j = new JSONObject(JSONresponse);//create JSON object from the string
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Send the JSON object to either the Authentication handler or the Search handler
        if (FoodFinder.authenticate) {
            jp.parseAuthJson(j);
            FoodFinder.authenticate = false;
        }
        else {
            Log.d("Log", JSONresponse);
            jp.parseSearchJson(j);
        }


    }
    private String addToQuery(List<Pair> searchParameters) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Pair p : searchParameters)
        {
            if (first)
                first = false;
            else
                result.append("&");
            result.append(URLEncoder.encode((String)p.first, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String)p.second, "UTF-8"));
        }
        //business/search?term=foo&location=barr&filters=baz

        return result.toString();
    }

    private void fillParameters () {

        searchParameters.add(new Pair("term", foodQuery.term));
        searchParameters.add(new Pair("location", foodQuery.location));
        searchParameters.add(new Pair("sortBy", foodQuery.sortBy));
        searchParameters.add(new Pair("price", foodQuery.price));
        searchParameters.add(new Pair("openNow", foodQuery.openNow));
        searchParameters.add(new Pair("latitude", foodQuery.latitude));
        searchParameters.add(new Pair("longitude", foodQuery.longitude));
        searchParameters.add(new Pair("radius", foodQuery.radius));
        searchParameters.add(new Pair("limit", foodQuery.limit));
    }
    private String createJSON (BufferedReader br, StringBuffer sb) throws IOException{
        //Create JSON string by reading the web response line by line
        String line = "";
        while ((line = br.readLine())!= null ) {
            sb.append(line);
        }
        return sb.toString();
    }



}