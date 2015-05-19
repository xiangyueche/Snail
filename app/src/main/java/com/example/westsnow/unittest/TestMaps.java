package com.example.westsnow.unittest;

import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.example.westsnow.myapplication.*;
import com.example.westsnow.util.*;
import android.R.*;

// test track map, and update map's location
public class TestMaps extends CurLocaTracker{

    @Override
    protected void onCreate(Bundle savedInstanceState) { // when load main app, trigger onCreate
        super.onCreate(savedInstanceState);
        setContentView(com.example.westsnow.myapplication.R.layout.activity_test_maps);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(com.example.westsnow.myapplication.R.id.map);
        mapFragment.getMapAsync(this);
        m_map = mapFragment.getMap();
        buildGoogleApiClient();

    }


}
