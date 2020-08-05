package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class EmptyClassLyrics extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_class_lyrics);

        //Getting data to load from prev activity
        Bundle dataToPass = getIntent().getExtras();

        //Loading and inflating fragment
        DetailFragmentLyrics dFragment = new DetailFragmentLyrics();
        dFragment.setArguments(dataToPass);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, dFragment)
                .commit();






    }
}
