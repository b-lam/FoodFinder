package com.example.johnteng.foodfinder;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Brandon on 9/26/2016.
 */
public class Yelp {

    String client_id;
    String client_secret;
    String access_token;
    static OkHttpClient client = new OkHttpClient();
    private com.example.johnteng.foodfinder.Callback<Void> mListener;

    public void setListener(com.example.johnteng.foodfinder.Callback<Void> listener) {
        mListener = listener;
    }

    public Yelp(Context context){
        client_id = context.getResources().getString(R.string.yelp_client_id);
        client_secret = context.getResources().getString(R.string.yelp_client_secret);
    }

    public void authenticate(){

        RequestBody requestBody = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", client_id)
                .add("client_secret", client_secret)
                .build();

        Request request = new Request.Builder()
                .url("https://api.yelp.com/oauth2/token")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }else{
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                        access_token = json.getString("access_token");
                        final String token_type = json.getString("token_type");
                        final int expires_in = json.getInt("expires_in");
                        Log.d("Yelp", access_token);
                        Log.d("Yelp", token_type);
                        Log.d("Yelp", String.valueOf(expires_in));
                    } catch (JSONException e) {

                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void businessSearch(){

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.yelp.com/v3/businesses/search").newBuilder();
        urlBuilder.addQueryParameter("term", BusinessSearch.term);
        urlBuilder.addQueryParameter("latitude", String.valueOf(BusinessSearch.latitude));
        urlBuilder.addQueryParameter("longitude", String.valueOf(BusinessSearch.longitude));
        urlBuilder.addQueryParameter("radius", String.valueOf(BusinessSearch.radius));
        urlBuilder.addQueryParameter("limit", String.valueOf(BusinessSearch.limit));
        urlBuilder.addQueryParameter("sort_by", BusinessSearch.sort_by);
        urlBuilder.addQueryParameter("price", BusinessSearch.price);
        urlBuilder.addQueryParameter("open_now", String.valueOf(BusinessSearch.open_now));
        String url = urlBuilder.build().toString();

        Log.d("Yelp Search URL", url);

        Request request = new Request.Builder()
                .header("Authorization", "Bearer " + access_token)
                .url(url)
                .build();

        Log.d("Yelp", request.toString());

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }else{
                    try {
                        String responseData = response.body().string();
                        JSONObject json = new JSONObject(responseData);
                        JSONArray businesses = json.getJSONArray("businesses");
                        JSONObject business = (JSONObject)businesses.get(0);
                        JSONObject location = business.getJSONObject("location");
                        SearchResponse.name = business.getString("name");
                        SearchResponse.price = business.getString("price");
                        SearchResponse.location = location.getString("address1") + ",  " + location.getString("city") + ", " + location.getString("zip_code");
                        SearchResponse.phone = business.getString("phone");
                        SearchResponse.url = business.getString("url");
                        SearchResponse.image_url = business.getString("image_url");

                        Log.d("Yelp", SearchResponse.name);
                        Log.d("Yelp", SearchResponse.price);
                        Log.d("Yelp", SearchResponse.location);
                        Log.d("Yelp", SearchResponse.phone);
                        Log.d("Yelp", SearchResponse.url);
                        Log.d("Yelp", SearchResponse.image_url);

                        Log.d("Yelp", json.toString());

                        if (mListener != null) {
                            mListener.onResult(null);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

}
