package com.example.benjamin.travelmap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.travelmap.data.PhotoData;
import com.example.benjamin.travelmap.utils.BitmapUtils;
import com.example.benjamin.travelmap.utils.GpsUtils;
import com.example.benjamin.travelmap.utils.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnCameraIdleListener,
        GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static final int EXTERNAL_STORAGE_REQUEST_CODE = 2;
    private static final int DEFAULT_ZOOM = 15;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private GoogleMap googleMap;
    private boolean locationPermissionGranted;
    private CameraPosition cameraPosition;
    private List<PhotoData> photos = new ArrayList<>();
    private List<Marker> photoMarkers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleMap = null;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }

        if (savedInstanceState != null) {
            lastLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        tryLoadPhotos();

    }

    private void tryLoadPhotos() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            loadPhotos();
        } else {
            PermissionUtils.requestPermission(this,EXTERNAL_STORAGE_REQUEST_CODE,Manifest.permission.READ_EXTERNAL_STORAGE,true);
        }
    }


    /**
     * Pour copier des fichiers sur l'émulateur, juste lancer l'emulateur avec Android Virtual Device Manager, sans débugger l'appli.
     * Utiliser Android Device Monitor pour copier les photos dans sdcard.
     */
    private void loadPhotos() {
        String[] mProjection = {MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA};
        //TODO je pense que ca ne récupère que les photos de la sdcard.
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mProjection, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    String absolutePath = cursor.getString(index);
                    String fileName = absolutePath.substring(absolutePath.lastIndexOf("/") + 1, absolutePath.length());
                    ExifInterface exifInterface = new ExifInterface(absolutePath);
                    float[] latLong = new float[2];
                    exifInterface.getLatLong(latLong);
                    LatLng photoPosition = GpsUtils.getLatLng(latLong);
                    if (photoPosition != null) {
                        PhotoData photoData = new PhotoData(absolutePath, fileName, photoPosition);
                        if (!photos.contains(photoData)) {
                            photos.add(photoData);
                        }
                    } else {
                        Log.i("PHOTO","No LatLng for " + fileName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }

    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,android.Manifest.permission.ACCESS_FINE_LOCATION,true);
        }
        if (locationPermissionGranted) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            //didnt found a location on my desktop
            if(lastLocation != null){
                lastLocation.setLatitude(48.826080);
                lastLocation.setLongitude(2.360522);
            }

        }
        if (cameraPosition != null) {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else if (lastLocation != null) {
            LatLng currentLocationLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, DEFAULT_ZOOM));
        } else {
            Log.d("Location", "Current location is null");
        }

    }

    private void addMarkerCurrentPosition() {
        if (lastLocation != null) {
            double latitude = lastLocation.getLatitude();
            double longitude = lastLocation.getLongitude();

            // Creating a LatLng object for the current location
            LatLng myPosition = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(myPosition).title("Start"));
        }
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnCameraIdleListener(this);

//        View photoMarkerView = getLayoutInflater().inflate(R.layout.photo_marker_card, null);
//        googleMap.setInfoWindowAdapter(new PhotoMarker(photoMarkerView));
        updateLocationUI();
        getDeviceLocation();
    }



    //TODO quand plein de photo, faire pleins de petites photos dans la zone ?
    //https://developers.google.com/maps/documentation/android-api/utility/marker-clustering?hl=fr
    private void drawPhotoMarkers() {
        cleanPhotoMarker();
        List<PhotoData> photosToDraw = new ArrayList<>();
        LatLngBounds currentBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
        for (PhotoData photo : photos) {
            if (currentBounds.contains(photo.getPhotoPosition())) {
                photosToDraw.add(photo);
            }

        }
        for (PhotoData photo : photosToDraw) {
            View photo_marker = buildPhotoMarkerView(photo);
            BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(
                    BitmapUtils.getMarkerBitmapFromView(photo_marker));
            Marker marker = googleMap.addMarker(new MarkerOptions().position(photo.getPhotoPosition()).icon(bitmapDescriptor));
            marker.setTag(photo);

            photoMarkers.add(marker);
        }
    }

    private void cleanPhotoMarker() {
        for (Marker marker : photoMarkers) {
            marker.remove();
        }
        photoMarkers.clear();
    }

    private View buildPhotoMarkerView(PhotoData photoData) {
        View photo_marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.photo_marker_card, null);
        TextView lblTitle = (TextView) photo_marker.findViewById(R.id.marker_title);
        lblTitle.setText(photoData.getFileName());
        ImageView img = (ImageView) photo_marker.findViewById(R.id.marker_image);
        img.setImageBitmap(BitmapFactory.decodeFile(photoData.getAbsolutePath()));
        return photo_marker;
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        updateLocationUI();
        getDeviceLocation();
        addMarkerCurrentPosition();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    locationPermissionGranted = true;
                }
            }
            case EXTERNAL_STORAGE_REQUEST_CODE:{
                if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    loadPhotos();
                }
            }
        }

        updateLocationUI();
    }

    private void updateLocationUI() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,android.Manifest.permission.ACCESS_FINE_LOCATION,true);
        }
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (locationPermissionGranted) {
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
        } else  {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            lastLocation = null;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("GoogleApi", "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("GoogleApi", "Connection failed");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastLocation);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onCameraIdle() {
        drawPhotoMarkers();
    }
}
