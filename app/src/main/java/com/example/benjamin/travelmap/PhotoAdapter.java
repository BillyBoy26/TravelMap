package com.example.benjamin.travelmap;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.benjamin.travelmap.data.PhotoData;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {


    private final List<PhotoData> photos;

    public PhotoAdapter(List<PhotoData> photos) {
        this.photos = photos;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_card, parent,false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder photoHolder, int position) {
        PhotoData photoData = photos.get(position);
        photoHolder.bind(photoData);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
