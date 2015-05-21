package com.example.westsnow.util;

import java.util.Iterator;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.*;
/**
 * Created by yingtan on 5/18/15.
 */
public class LocaChangeTracker extends CurLocaTracker{

    private static final String TAG = "GpsActivity";

    public Location m_curLocation;
    public LocationManager lm;

    public LocaChangeTracker(CurLocaTracker locaTracker){
        m_map = locaTracker.m_map;
        m_curLocation = locaTracker.m_LastLocation;
        m_LastMarker = locaTracker.m_LastMarker;

        try{
            if ((m_map == null)) {
                throw new SnailException("Map does not exist");
            }
        }catch(SnailException e){
            System.out.println(SnailException.EX_DESP_MapNotExist);
        }
    }

    private LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            try {
                if (location == null) {
                    throw new SnailException(SnailException.EX_DESP_LocationNotExist);
                }
                m_curLocation = location;
                System.out.println("location changed!");

                LatLng curLocation = new LatLng(location.getLatitude(), location.getLongitude());

                //display a point to move
                if ((m_LastMarker == null)) {
                    throw new NullPointerException();
                }
                m_LastMarker.setVisible(false);


                m_LastMarker = m_map.addMarker(new MarkerOptions()
                        .title("Current Location")
                        .snippet("The most populous city in")
                        .position(curLocation));

                //Todo: save it to DB


                Log.i(TAG, "changed longtitude:" + location.getLongitude());
                Log.i(TAG, "changed latitude:" + location.getLatitude());

            }catch(SnailException e){
                System.out.println(SnailException.EX_DESP_LocationNotExist);
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.println("status changed!");
            switch (status) {
                case LocationProvider.AVAILABLE:
                    System.out.println("AVAILABLE!");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    System.out.println("OUT_OF_SERVICE!");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    System.out.println("TEMPORARILY_UNAVAILABLE!");
                    break;
            }
        }

        public void onProviderEnabled(String provider) {
            //Location location = lm.getLastKnownLocation(provider);
        }

        public void onProviderDisabled(String provider) {
        }


    };

    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    System.out.println("GPS_EVENT_FIRST_FIX!");
                    break;
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    System.out.println("GPS_EVENT_SATELLITE_STATUS!");

                    /* test code
                    CircleOptions circleOptions = new CircleOptions()
                            .center(new LatLng(m_curLocation.getLatitude(), m_curLocation.getLongitude()))
                            .radius(1000);
                    double d3 = Math.random()*100;
                    System.out.println("d3:"+d3);

                    System.out.println("add marker"+(m_curLocation.getLatitude()+d3)+" ,"+(m_curLocation.getLongitude()+d3));
                    double la = m_curLocation.getLatitude()+d3;
                    double lg = m_curLocation.getLongitude()+d3;
                    curLocation = new LatLng(la, lg);
                    m_map.addMarker(new MarkerOptions()
                            .title("Curr Location")
                            .snippet("The most populous city in")
                            .position(curLocation)
                            .draggable(true));


                    System.out.println("add circle");
                    m_map.addCircle(circleOptions);
                    */

                    GpsStatus gpsStatus=lm.getGpsStatus(null);
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    //System.out.println("count" +count);
                    break;
                case GpsStatus.GPS_EVENT_STARTED:
                    System.out.println("GPS_EVENT_STARTED!");
                    break;
                case GpsStatus.GPS_EVENT_STOPPED:
                    System.out.println("GPS_EVENT_STOPPED!");
                    break;
            }
        };
    };

    public void trackChangedLocation(Context context){
        System.out.println("go into track!");
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            //startActivityForResult(intent, 0);
            return;
        }
        //String bestProvider = lm.getBestProvider(getCriteria(), true);
        lm.addGpsStatusListener(listener);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);

    }

    public Criteria getCriteria(){
        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(false);
        criteria.setBearingRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }


}
