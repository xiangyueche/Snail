package com.example.westsnow.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.util.*;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.*;
/**
 * Created by yingtan on 5/19/15.
 */
public class GeoCodeRequester {

    public JSONObject jsonObj;
    public String returnedJson;

    private static GeoCodeRequester m_instance = null;

    private GeoCodeRequester(){

    }

    public static GeoCodeRequester getInstance(){
        if(m_instance == null){
            m_instance = new GeoCodeRequester();
        }
        return m_instance;
    }

    public void getGeoLocation(Context context, String locationName) {
        double latitude;
        double longitude;

        List<Address> geocodeMatches = null;

        try {
            geocodeMatches = new Geocoder(context).getFromLocationName(locationName, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!geocodeMatches.isEmpty()) {
            latitude = geocodeMatches.get(0).getLatitude();
            longitude = geocodeMatches.get(0).getLongitude();

            //Todo: show to user  +  save to DB
            System.out.println("[GeoCoding]:"+latitude +" , "+longitude);
        }
    }


}
