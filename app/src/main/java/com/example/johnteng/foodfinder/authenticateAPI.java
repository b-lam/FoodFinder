package com.example.johnteng.foodfinder;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by johnteng on 16-09-17.
 */
public class CallAPI extends AsyncTask<String, String, String> {
    private String appID = "IOcbXtzaehe_QsCfBsGE8w";
    private String appSecret = "8aQYbsawHFrnDNJLjGLwKbSseQGsIf5AkaFwHcBg4ZVf8e8drIvLKk0xUtfoYaHw";


    public CallAPI(){
        //set context variables if required
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {
        //https://api.yelp.com/oauth2/token
       // String urlString = params[0]; // URL to call
        String urlString = "";
        String resultToDisplay = "";

        InputStream in = null;
        try {

            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            in = new BufferedInputStream(urlConnection.getInputStream());


        } catch (Exception e) {

            System.out.println(e.getMessage());

            return e.getMessage();

        }

        try {
            resultToDisplay = IOUtils.toString(in, "UTF-8");
            //to [convert][1] byte stream to a string
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return resultToDisplay;
    }


    @Override
    protected void onPostExecute(String result) {
        //Update the UI
    }