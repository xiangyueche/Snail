package com.example.westsnow.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.westsnow.myapplication.Constant;
import com.example.westsnow.myapplication.JSONParser;
import com.example.westsnow.myapplication.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.*;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yingtan on 5/19/15.
 */
public class CurLocaTracker extends ActionBarActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient m_GoogleApiClient;
    public GoogleMap m_map;
    public Location m_LastLocation;
    public Marker m_LastMarker;
    protected String username;

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private static final String TAG_USER = "users";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        JSONParser jParser = new JSONParser();
        private final String url = Constant.serverDNS + "/getMoments.php";

        private final View myContentsView;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            // getting JSON string from URL
            params.add(new BasicNameValuePair("email", username));
            //System.out.println(url);
            //JSONObject json = jParser.makeHttpRequest(url, "GET", params);
            //System.out.println("here");
            //try {
//                JSONArray moments = json.getJSONArray(TAG_USER);
//                System.out.println("here");
//                JSONObject c = moments.getJSONObject(0);
//                System.out.println("here");
//                String context = c.getString("context");
//                System.out.println("here");
//                String imageLocation = c.getString("imageLocation");
//                System.out.println("here");
//                String imageURL = Constant.serverDNS + "/" + imageLocation;
//                System.out.println("here");

                TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
                tvTitle.setText(marker.getTitle());
                TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
                tvSnippet.setText(marker.getSnippet()); //Should be changed to address on EC2
                //tvSnippet.setText(context); //Should be changed to address on EC2
                ImageView ivImage = ((ImageView)myContentsView.findViewById(R.id.image));
                //new DownloadImageTask(ivImage).execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");
                ivImage.setImageResource(R.drawable.photoarea); //Should be changed to address on EC2
                ivImage.getLayoutParams().height = 250;
            //} catch (JSONException e) {
            //    e.printStackTrace();
            //}

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
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



    protected synchronized void buildGoogleApiClient() { //called inside onCreate, add connectionListen to client, call onstart in build()
        m_GoogleApiClient = new GoogleApiClient.Builder(this) // after building, called onConnected (callback function) immediately
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() { // connect apiClient, and call onConnected
        super.onStart();
        m_GoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle connectionHint){// request update

        //Task 1: get current location
        getCurLocation();

        //Task 2: update current location
        LocaChangeTracker tracker = new LocaChangeTracker(this);
        tracker.trackChangedLocation(this);


    }

    @Override
    public void onMapReady(GoogleMap map) { // when Map is loaded, call it
        System.out.println("Map is ready");
    }

    @Override
    protected void onStop() {
        System.out.println("on stop!");
        super.onStop();
        if (m_GoogleApiClient.isConnected()) {
            m_GoogleApiClient.disconnect();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        System.out.println( "Connection suspended");
        m_GoogleApiClient.connect();
    }

    public void onConnectionFailed(ConnectionResult result) {
        System.out.println("Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    public void getCurLocation(){
        System.out.println("Connected, GetCurLocation ");
        try {
            m_LastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    m_GoogleApiClient);

            if (m_LastLocation == null) {
                System.out.println("Null!!!!");
                throw new SnailException(SnailException.EX_DESP_LocationNotExist);
            } else {
                System.out.println(m_LastLocation.getLatitude() + "," + m_LastLocation.getLongitude());
                LatLng curLocation = new LatLng(m_LastLocation.getLatitude(), m_LastLocation.getLongitude());

                m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(curLocation, 13));

                m_LastMarker = m_map.addMarker(new MarkerOptions()
                        .title("Current Location")
                        .snippet("The most populous city in")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .position(curLocation));
                System.out.println("here in getCurLocation()");

                m_map.setInfoWindowAdapter(new MyInfoWindowAdapter());
            }
        }catch(SnailException e){
            System.out.println(SnailException.EX_DESP_LocationNotExist);
        }
    }

}
