package com.users.map.util;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class GeoUtil {

    public static float getDistanceBetweenLocationsInMeters(LatLng locationOne, LatLng locationTwo) {
        float [] results = new float[1];
        Location.distanceBetween(locationOne.latitude,locationOne.longitude,locationTwo.latitude,locationTwo.longitude,results);
        return results[0];
    }
}
