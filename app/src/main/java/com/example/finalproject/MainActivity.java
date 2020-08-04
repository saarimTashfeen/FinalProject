package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button goToGeo = (Button)findViewById(R.id.GoToGeo);
        Button goToLyrics = (Button)findViewById(R.id.GoToLyricsSearch);
        Button goToDeezer = (Button)findViewById(R.id.GoToDeezerSearch);

        goToGeo.setOnClickListener(c -> {
            Intent geoDataSource = new Intent(MainActivity.this, GeoTempHome.class);
            startActivity(geoDataSource);
        });

        goToLyrics.setOnClickListener(c -> {
            Intent lyricsSearch = new Intent(MainActivity.this, MainLyrics.class);
            startActivity(lyricsSearch);
        });

        goToDeezer.setOnClickListener(c -> {
            Intent deezerSongSearch = new Intent(MainActivity.this, DeezerSearch.class);
            startActivity(deezerSongSearch);
        });
    }
}