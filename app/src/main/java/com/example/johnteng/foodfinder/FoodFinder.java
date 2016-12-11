package com.example.johnteng.foodfinder;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.ibm.watson.developer_cloud.personality_insights.v2.PersonalityInsights;
import com.ibm.watson.developer_cloud.personality_insights.v2.model.Profile;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;



public class FoodFinder extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private EditText edHandle;
    private double lat, lon;
    private static final String TWITTER_CONSUMER_KEY = BuildConfig.TWITTER_CONSUMER_KEY;
    private static final String TWITTER_CONSUMER_SECRET = BuildConfig.TWITTER_CONSUMER_SECRET;
    private static final String TWITTER_ACCESS_TOKEN = BuildConfig.TWITTER_ACCESS_TOKEN;
    private static final String TWITTER_ACCESS_TOKEN_SECRET = BuildConfig.TWITTER_ACCESS_TOKEN_SECRET;
    private static final String WATSON_USERNAME = BuildConfig.WATSON_USERNAME;
    private static final String WATSON_PASSWORD = BuildConfig.WATSON_PASSWORD;
    private static final String WATSON_URL = "https://gateway.watsonplatform.net/PersonalityInsights-insights/api";
    private int CASE;
    AlertDialog dialogBuilder;
    public final jsonParser jp = new jsonParser();
    Yelp yelp;
    TextView tvCoord, tvName, tvURL, tvPrice, tvAddress, tvTelephone;
    ImageView imgBusiness;

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

        yelp = new Yelp();
        yelp.authenticate();

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        tvCoord = (TextView) findViewById(R.id.tvCoord);
        edHandle = (EditText) findViewById(R.id.edHandle);
        Button btnFindMe = (Button) findViewById(R.id.btnFindMe);

        tvName = (TextView) findViewById(R.id.tvName);
        tvURL = (TextView) findViewById(R.id.tvURL);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvTelephone = (TextView) findViewById(R.id.tvTelephone);
        imgBusiness = (ImageView) findViewById(R.id.imgBusiness);

        tvURL.setClickable(true);
        tvURL.setMovementMethod(LinkMovementMethod.getInstance());

        btnFindMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Finding Waldo...", Snackbar.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    lat = lastLocation.getLatitude();
                    lon = lastLocation.getLongitude();
                    tvCoord.setText("(" + lat + ", " + lon + ")");
                    BusinessSearch.latitude = lat;
                    BusinessSearch.longitude = lon;
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edHandle.getText().length() != 0){
                    Snackbar.make(view, "Contacting Watson...", Snackbar.LENGTH_LONG).show();
                    new Thread(new Runnable() {
                        public void run() {
                            getTweets(edHandle.getText().toString());
                        }
                    }).start();
                }else{
                    makeToast(1);
                }
                if(CASE == 2){
                    makeToast(2);
                }
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
        if (id == R.id.action_howto) {
            howToDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getPersonalityInsights(String text){

        PersonalityInsights service = new PersonalityInsights();
        service.setUsernameAndPassword(WATSON_USERNAME, WATSON_PASSWORD);

        Profile profile = service.getProfile(text).execute();

        try {
            jp.parseWatson(new JSONObject(profile.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        System.out.println(profile.toString());

        searchYelp();

    }

    public void getTweets(String handle){
        String text = "";
        Paging paging = new Paging(1,100);
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
                .setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(TWITTER_ACCESS_TOKEN_SECRET);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        try {
            List<Status> statuses;
            String user;
            user = handle;
            statuses = twitter.getUserTimeline(user, paging);
            Log.i("Status Count", statuses.size() + " Feeds");
            for (int i = 0; i < statuses.size(); i++) {
                Status status = statuses.get(i);
                text += (status.getText());
                Log.i("Tweet Count " + (i + 1), status.getText() + "\n\n");
            }
            if(text.length() != 0){
                Log.d("Log","Outputting the JSON data");
                getPersonalityInsights(text);
            }else{
                CASE = 2;
            }
        } catch (TwitterException te) {
            te.printStackTrace();
        }
    }

    public void searchYelp(){
        final ComputationalMatrix computationalMatrix = new ComputationalMatrix();

        computationalMatrix.calculateStandardForPrice(computationalMatrix.priceMap);
        computationalMatrix.calculateStandardForRadius(computationalMatrix.radiusMap);
        double rating = computationalMatrix.calculateStandardForRatings(computationalMatrix.ratingMap);
        double review_count = computationalMatrix.calculateStandardForReviews(computationalMatrix.reviewsMap);
        Log.d("Yelp", String.valueOf(rating) + ", " + String.valueOf(review_count));
        BusinessSearch.term = "food";
        BusinessSearch.limit = 1;
        if(rating > review_count && Math.abs(rating-review_count) > 10){
            BusinessSearch.sort_by = "rating";
        }else if(review_count > rating && Math.abs(rating-review_count) > 10){
            BusinessSearch.sort_by = "review_count";
        }else{
            BusinessSearch.sort_by = "best_match";
        }
        Log.d("Yelp Sort By", BusinessSearch.sort_by);
        BusinessSearch.open_now = true;
        yelp.businessSearch();
        yelp.setListener(new Callback<Void>() {
            @Override
            public void onResult(Void result) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        tvName.setText("Name: " + SearchResponse.name);
                        tvPrice.setText("Price: " + SearchResponse.price);
                        tvAddress.setText("Address: " + SearchResponse.location);
                        tvTelephone.setText("Telephone: " + SearchResponse.phone);
                        String url = "<a href='" + SearchResponse.url + "'> Go to Yelp </a>";
                        tvURL.setText(Html.fromHtml(url));
                        new DownloadImageTask(imgBusiness).execute(SearchResponse.image_url);
                    }
                });
            }
        });
    }

    public void makeToast(int CASE){
        if(CASE == 1){
            Toast.makeText(getApplicationContext(), "Please enter a Twitter handle", Toast.LENGTH_LONG).show();
        }else if(CASE == 2){
            Toast.makeText(getApplicationContext(), "I couldn't find any tweets!", Toast.LENGTH_LONG).show();
        }
    }

    private void howToDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View calibrateView = layoutInflater.inflate(R.layout.how_it_works_layout, null);

        dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.setView(calibrateView);

        Button btnClose = (Button) calibrateView.findViewById(R.id.close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.show();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urlDisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void writeToFile(String data,Context context) throws IOException {
        File path = context.getExternalFilesDir(null);
        File file = new File(path, "json.txt");

        FileOutputStream stream = new FileOutputStream(file);
        try {
            Log.d("Print", "Writing to file");
            stream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stream.close();
        }
    }
}
