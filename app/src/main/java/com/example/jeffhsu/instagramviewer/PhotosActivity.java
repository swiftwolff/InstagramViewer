package com.example.jeffhsu.instagramviewer;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends Activity {
    public static final String CLIENT_ID = "fc3ab6bd008b43ddb41aaaef79483783";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

//        fetchPopularPhotos();

    }

    private void fetchTimelineAsync(int i) {

        //https://api.instagram.com/v1/media/popular?client_id=CLIENT-ID
        //setup popular url endpoint
        //create the network client
        //handle the successful response
        photos = new ArrayList<InstagramPhoto>();

        //Create adapter bind it to the data in arraylist
        aPhotos = new InstagramPhotosAdapter(this, photos);
        //Populate the data into the listView

        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        // Set the adapter to the listview (population of items)
        lvPhotos.setAdapter(aPhotos);

        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(popularUrl, new JsonHttpResponseHandler(){
            //define success and failure callbacks

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //fired once the successful response back
                //standard url, height, username, caption
                JSONArray photosJSON = null;
                try{
                    photos.clear();
                    photosJSON = response.getJSONArray("data");
                    for(int i=0;i<photosJSON.length();i++){
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        if(!photoJSON.isNull("caption")){
                            if (photoJSON.getJSONObject("caption")!=null && photoJSON.getJSONObject("caption").getString("text")!=null){
                                photo.caption = photoJSON.getJSONObject("caption").getString("text");
                            }
                        }
                        photo.imageProfileUrl = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photos.add(photo);
                    }
                    //Notified the adapter that it should
                    aPhotos.notifyDataSetChanged();
                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
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
}
