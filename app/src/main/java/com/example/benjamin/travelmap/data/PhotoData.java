package com.example.benjamin.travelmap.data;

import com.google.android.gms.maps.model.LatLng;

public class PhotoData {


    private final String absolutePath;
    private final String fileName;
    private final LatLng photoPosition;

    public PhotoData(String absolutePath, String fileName, LatLng photoPosition) {
        this.absolutePath = absolutePath;
        this.fileName = fileName;
        this.photoPosition = photoPosition;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public String getFileName() {
        return fileName;
    }

    public LatLng getPhotoPosition() {
        return photoPosition;
    }
}
