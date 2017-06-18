package com.example.benjamin.travelmap;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benjamin.travelmap.data.PhotoData;

public class PhotoViewHolder  extends RecyclerView.ViewHolder{

    private final ImageView imageView;
    private final TextView textView;

    public PhotoViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        textView = (TextView) itemView.findViewById(R.id.text);
    }

    public void bind(PhotoData photoData) {
        imageView.setImageBitmap(BitmapFactory.decodeFile(photoData.getAbsolutePath()));
        textView.setText(photoData.getFileName());
    }
}
