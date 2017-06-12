package com.example.benjamin.travelmap.utils;

import com.google.android.gms.maps.model.LatLng;

public class GpsUtils {

    public static LatLng getLatLng(float[] latLong) {
        if (latLong.length != 2) {
            return null;
        }
        return new LatLng(latLong[0], latLong[1]);
    }
}
