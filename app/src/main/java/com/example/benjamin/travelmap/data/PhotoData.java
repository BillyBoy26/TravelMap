package com.example.benjamin.travelmap.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class PhotoData implements ClusterItem {


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

    @Override
    public LatLng getPosition() {
        return getPhotoPosition();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoData photoData = (PhotoData) o;

        return absolutePath.equals(photoData.absolutePath);

    }

    @Override
    public int hashCode() {
        return absolutePath.hashCode();
    }
}
