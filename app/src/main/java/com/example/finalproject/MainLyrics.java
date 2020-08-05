package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainLyrics extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String lyrics;
    private String lyricsTwo;
    private EditText artistName;
    private EditText songName;
    private String artistNameEncoded;
    private String songNameEncoded;
    private String googleUrl = "https://www.google.com/search?q=";
    private String lyricsPlaceHolder = "lyricsPlaceHolder";
    private String artistPlaceHolder = "artistPlaceHolder";
    private String songPlaceHolder = "songPlaceHolder";
    SharedPreferences sharedPrefs = null;
    //  private Object FileNotFoundException;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lyrics);

        sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);

        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button googleSearch = (Button) findViewById(R.id.googleSearch);
        Button lyricsSearch = (Button) findViewById(R.id.lyricsSearch);
        Button toLyrics = (Button) findViewById(R.id.toLyrics);

        artistName = (EditText) findViewById(R.id.artistName);
        songName = (EditText) findViewById(R.id.songName);


        //User can go directly to the database page if they have no song in mind to enter
        toLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toLyrics = new Intent(MainLyrics.this, LyricsScreen.class);
                startActivity(toLyrics);

            }
        });






        // On click for google search button
        googleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                artistName = (EditText) findViewById(R.id.artistName);
                songName = (EditText) findViewById(R.id.songName);

                //goToProfile.putExtra("EMAIL", email.getText().toString());
                onPause();



                try {
                    artistNameEncoded = URLEncoder.encode(artistName.getText().toString(), "UTF-8");
                    songNameEncoded = URLEncoder.encode(songName.getText().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //  String string = "string";
                // Log.d("artist Name Encoded", artistNameEncoded);
                //Log.d("song name encoded", songNameEncoded.toString());

                Uri googlePage = Uri.parse(googleUrl+artistName.getText().toString()+" "+songName.getText().toString());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, googlePage);
                startActivity(webIntent);






            }
        });

        // On click for lyrics search
        lyricsSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                artistName = (EditText) findViewById(R.id.artistName);
                songName = (EditText) findViewById(R.id.songName);
                onPause();

                try {
                    artistNameEncoded = URLEncoder.encode(artistName.getText().toString(), "UTF-8");
                    songNameEncoded = URLEncoder.encode(songName.getText().toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                if(artistNameEncoded=="" || songNameEncoded == ""){

                    Toast.makeText(MainLyrics.this, "Song Not found", Toast.LENGTH_SHORT).show();

                } else {


                    LyricsSearch req = new LyricsSearch();
                    req.execute("https://api.lyrics.ovh/v1/" + artistNameEncoded + "/" + songNameEncoded);

                }


                // Log.i("test", "test" + lyrics);

                //      String string = "String";

            }
        });



        // Shared prefs, loading previous enteries into the edit text fields
        String savedArtist = sharedPrefs.getString("artist", "");
        String savedSong = sharedPrefs.getString("song", "");

        artistName.setText(savedArtist);
        songName.setText(savedSong);






















    }


    // Navigation Drawer Menu Item Method
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        //Goes to song search, Austin's Activity
        if (id == R.id.songSearch){

            //Toast.makeText(this, "Test Toast", Toast.LENGTH_SHORT).show();

           Intent deezerIntent = new Intent(MainLyrics.this, DeezerSearch.class);
            startActivity(deezerIntent);


        //Goes to Geo, Aimen's Activity
        } else if (id == R.id.geoHome){


            Intent geoIntent = new Intent(MainLyrics.this, GeoHome.class);
            startActivity(geoIntent);

        }

        //Closes drawer layout after item is selected

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }


    //Lyrics Search class for AsyncTask Methods
    class LyricsSearch extends AsyncTask<String, Integer, String> {





        // DO in background method to get lyrics from API
        @Override
        protected String doInBackground(String... args) {

            try {

                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //if (FileNotFoundException == true){


                //}
                InputStream response = urlConnection.getInputStream();

                // XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                //factory.setNamespaceAware(false);
                //XmlPullParser xpp = factory.newPullParser();
                //xpp.setInput(response, "UTF-8");

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;


                //String builder is building the string
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");


                }
                String result = sb.toString();

                //Fetching lyirics
                JSONObject lyricsFetch = new JSONObject(result);
                lyrics = lyricsFetch.getString("lyrics");
                Log.i("test2", "test" + result);
                //  lyricsTwo = result;




                   //Toast.makeText(getApplicationContext(),"Song not found", Toast.LENGTH_LONG).show();






                //If lyrics are found, intent to load the LyricsScreen Page
                Intent displayPage = new Intent(MainLyrics.this, LyricsScreen.class);
                displayPage.putExtra(lyricsPlaceHolder, lyrics);
                displayPage.putExtra(artistPlaceHolder, artistName.getText().toString());
                displayPage.putExtra(songPlaceHolder, songName.getText().toString());
                startActivity(displayPage);

                Log.i("TEST REAL 2", lyrics);

















            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();


            } catch (JSONException e) {
                e.printStackTrace();
            }



            return "done";
        }





    }

    // on Pause method for shared prefs
    @Override
    protected void onPause() {
        super.onPause();

        //SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs",MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("artist", artistName.getText().toString());
        editor.putString("song", songName.getText().toString());
        editor.commit();



    }


    //Inflates the menu layout for toolbar
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.main_lyrics_menu, menu);
        return true;
    }


    //Toolbar menu code
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                //message = "You clicked item 1";

                Intent deezerIntent = new Intent(MainLyrics.this, DeezerSearch.class);
                startActivity(deezerIntent);


                break;
            case R.id.item2:
              ///  message = "You clicked on the search";
                Intent geoIntent = new Intent(MainLyrics.this, GeoHome.class);
                startActivity(geoIntent);


                break;
            case R.id.helpItem:
                //message = "You clicked on mail";

                //ALERT DIALOG for the help item
                AlertDialog.Builder builder = new AlertDialog.Builder(MainLyrics.this)
                        .setTitle("Help Menu")
                        .setMessage("Enter the artist name in the first EditTExt input and the song name in the Second EditText input. LyricsSearch fetches the lyrics and Google search searches up the artist and song name on google")
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                        builder.show();





                break;
        }
       // Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }





}
