package com.example.benjamin.travelmap.markers;

import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.example.benjamin.travelmap.R;
import com.example.benjamin.travelmap.data.PhotoData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class PhotoMarker implements GoogleMap.InfoWindowAdapter {

    private View infoWindow = null;

    public PhotoMarker(View infoWindow) {
        this.infoWindow = infoWindow;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (marker.getTag() != null) {
            PhotoData photoData = (PhotoData) marker.getTag();
            return buildPhotoMarkerView(photoData);
        }
        return infoWindow;
    }

    private View buildPhotoMarkerView(PhotoData photoData) {
//        TextView lblTitle = (TextView) infoWindow.findViewById(R.id.marker_title);
//        lblTitle.setText(photoData.getFileName());
        ImageView img = (ImageView) infoWindow.findViewById(R.id.marker_image);
        img.setImageBitmap(BitmapFactory.decodeFile(photoData.getAbsolutePath()));
        return infoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
