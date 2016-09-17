package com.example.johnteng.foodfinder;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.stream.JsonReader;
import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v2.model.Content;
import com.ibm.watson.developer_cloud.personality_insights.v2.model.Profile;
import com.ibm.watson.developer_cloud.personality_insights.v2.model.ProfileOptions;
import com.ibm.watson.developer_cloud.util.GsonSingleton;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class FoodFinder extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private EditText edLoc;
    private Button btnFindMe;
    private double lat, lon;
    private String username, password, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_finder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        url = "https://gateway.watsonplatform.net/personality-insights/api";
        username = "c710423e-c611-434f-89d3-aa4e0ce1f503";
        password = "sDQqS0Kjdp74";

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        edLoc = (EditText) findViewById(R.id.edLoc);
        btnFindMe = (Button) findViewById(R.id.btnFindMe);

        btnFindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Finding Waldo...", Snackbar.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    lat = lastLocation.getLatitude();
                    lon = lastLocation.getLongitude();
                    edLoc.setText("(" + lat + ", " + lon + ")");
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Contacting Watson...", Snackbar.LENGTH_LONG).show();
                new Thread(new Runnable() {
                    public void run() {
                        getPersonalityInsights();
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            lat = lastLocation.getLatitude();
            lon = lastLocation.getLongitude();
        }
    }

    @Override
    public void onConnectionSuspended(int i){

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(FoodFinder.class.getSimpleName(), "Can't connect to Google Play Services!");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_food_finder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getPersonalityInsights(){
        PersonalityInsights service = new PersonalityInsights();
        service.setUsernameAndPassword(username, password);

        String text = "Call me Ishmael. Some years ago-never mind how long precisely-having "
                + "little or no money in my purse, and nothing particular to interest me on shore, "
                + "I thought I would sail about a little and see the watery part of the world. "
                + "It is a way I have of driving off the spleen and regulating the circulation. "
                + "Whenever I find myself growing grim about the mouth; whenever it is a damp, "
                + "drizzly November in my soul; whenever I find myself involuntarily pausing before "
                + "coffin warehouses, and bringing up the rear of every funeral I meet; and especially "
                + "whenever my hypos get such an upper hand of me, that it requires a strong moral "
                + "principle to prevent me from deliberately stepping into the street, and methodically "
                + "knocking people's hats off-then, I account it high time to get to sea as soon as I can. "
                + "This is my substitute for pistol and ball. With a philosophical flourish Cato throws himself "
                + "upon his sword; I quietly take to the ship. There is nothing surprising in this. "
                + "If they but knew it, almost all men in their degree, some time or other, cherish "
                + "very nearly the same feelings towards the ocean with me. There now is your insular "
                + "city of the Manhattoes, belted round by wharves as Indian isles by coral reefs-commerce surrounds "
                + "it with her surf. Right and left, the streets take you waterward.";

        Profile profile = service.getProfile(text).execute();
        System.out.println(profile);
    }
}
