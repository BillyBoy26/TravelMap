package com.example.benjamin.travelmap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.benjamin.travelmap.data.PhotoData;

import java.util.List;

public class PhotoList extends AppCompatActivity {

    public static final String PHOTO_KEY = "PHOTO_KEY";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        List<PhotoData> photos = (List<PhotoData>) getIntent().getExtras().get(PHOTO_KEY);
        recyclerView.setAdapter(new PhotoAdapter(photos));
    }
}
