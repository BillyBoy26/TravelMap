package com.example.benjamin.travelmap;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.example.benjamin.travelmap.data.PhotoData;

import java.util.List;

public class PhotoList extends DrawerActivity {

    public static final String PHOTO_KEY = "PHOTO_KEY";

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        navigationView.setCheckedItem(R.id.nav_gallery);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_photo_list, contentFrameLayout);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        List<PhotoData> photos = (List<PhotoData>) getIntent().getExtras().get(PHOTO_KEY);
        recyclerView.setAdapter(new PhotoAdapter(photos));
    }
}
