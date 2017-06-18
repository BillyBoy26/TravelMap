package com.example.benjamin.travelmap.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

public class PhotoData implements ClusterItem,Serializable,Parcelable {


    private final String absolutePath;
    private final String fileName;
    private final LatLng photoPosition;

    public PhotoData(String absolutePath, String fileName, LatLng photoPosition) {
        this.absolutePath = absolutePath;
        this.fileName = fileName;
        this.photoPosition = photoPosition;
    }

    protected PhotoData(Parcel in) {
        absolutePath = in.readString();
        fileName = in.readString();
        photoPosition = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<PhotoData> CREATOR = new Creator<PhotoData>() {
        @Override
        public PhotoData createFromParcel(Parcel in) {
            return new PhotoData(in);
        }

        @Override
        public PhotoData[] newArray(int size) {
            return new PhotoData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(absolutePath);
        dest.writeString(fileName);
        dest.writeParcelable(photoPosition, flags);
    }
}
