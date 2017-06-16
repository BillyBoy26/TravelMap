package com.example.benjamin.travelmap;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.benjamin.travelmap.data.PhotoData;
import com.example.benjamin.travelmap.utils.BitmapUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.List;

public class PhotoClusterManager extends DefaultClusterRenderer<PhotoData> {

    public static final int MAX_IMAGES = 2;
    private final LayoutInflater layoutInflater;
    private List<ImageView> imageViews = new ArrayList<>();
    private final View clusterView;

    public PhotoClusterManager(Context context, GoogleMap map, ClusterManager<PhotoData> clusterManager, LayoutInflater layoutInflater) {
        super(context, map, clusterManager);
        this.layoutInflater = layoutInflater;
        clusterView = this.layoutInflater.inflate(R.layout.cluster_photo_marker, null);
        imageViews.add((ImageView) clusterView.findViewById(R.id.marker_image_left));
        imageViews.add((ImageView) clusterView.findViewById(R.id.marker_image_rigt));

    }

    @Override
    protected void onBeforeClusterItemRendered(PhotoData photo, MarkerOptions markerOptions) {
        View photoMarkerView = buildPhotoMarkerView(photo);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(
                    BitmapUtils.getMarkerBitmapFromView(photoMarkerView));
        markerOptions.icon(bitmapDescriptor);
    }


    /**
     * https://tympanus.net/Development/FolderPreviewIdeas/
     */
    @Override
    protected void onBeforeClusterRendered(Cluster<PhotoData> photos, MarkerOptions markerOptions) {
        int cpt = 0;
        for (PhotoData photoData : photos.getItems()) {
            if (cpt == MAX_IMAGES) {
                break;
            }
            ImageView imageView = imageViews.get(cpt);
            imageView.setImageBitmap(BitmapFactory.decodeFile(photoData.getAbsolutePath()));

            cpt++;
        }
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtils.getMarkerBitmapFromView(clusterView)));
    }



    @Override
    protected boolean shouldRenderAsCluster(Cluster<PhotoData> cluster) {
        return cluster.getSize() > 1;
    }

    private View buildPhotoMarkerView(PhotoData photoData) {
        View photo_marker = layoutInflater.inflate(R.layout.photo_marker_card, null);
//        TextView lblTitle = (TextView) photo_marker.findViewById(R.id.marker_title);
//        lblTitle.setText(photoData.getFileName());
        ImageView img = (ImageView) photo_marker.findViewById(R.id.marker_image);
        img.setImageBitmap(BitmapFactory.decodeFile(photoData.getAbsolutePath()));
        return photo_marker;
    }
}
