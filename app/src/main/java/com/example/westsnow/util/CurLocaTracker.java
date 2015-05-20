package com.example.westsnow.util;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.*;

/**
 * Created by yingtan on 5/19/15.
 */
public class CurLocaTracker extends ActionBarActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient m_GoogleApiClient;
    public GoogleMap m_map;
    public Location m_LastLocation;
    public Marker m_LastMarker;

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
    public void onConnected(Bundle connectionHint) {// request update

        getCurLocation();

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
        m_LastLocation = LocationServices.FusedLocationApi.getLastLocation(
                m_GoogleApiClient);

        if(m_LastLocation == null) {
            System.out.println("Null!!!!");
            //throw new NullPointerException();// Todo: change to SnailExcpetion()
        }
        else{
            System.out.println(m_LastLocation.getLatitude() + "," + m_LastLocation.getLongitude());
            LatLng curLocation = new LatLng(m_LastLocation.getLatitude(), m_LastLocation.getLongitude());

            m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(curLocation, 13));

            m_LastMarker = m_map.addMarker(new MarkerOptions()
                    .title("Current Location")
                    .snippet("The most populous city in")
                    .position(curLocation));
        }
    }
}
