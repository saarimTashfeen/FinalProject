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
import android.widget.ProgressBar;
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
    //Class variables
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
    private ProgressBar proBar;
    //  private Object FileNotFoundException;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lyrics);

        proBar = (ProgressBar) findViewById(R.id.progressBar);

        //proBar.setVisibility(View.VISIBLE);

        //Loading shared prefs
        sharedPrefs = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);


        //toolbar and navigation code
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


        //getting the button id's
        Button googleSearch = (Button) findViewById(R.id.googleSearch);
        Button lyricsSearch = (Button) findViewById(R.id.lyricsSearch);
        Button toLyrics = (Button) findViewById(R.id.toLyrics);

        //getiing edittext id's
        artistName = (EditText) findViewById(R.id.artistName);
        songName = (EditText) findViewById(R.id.songName);


        //to lyrics button listener
        toLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent toLyrics = new Intent(MainLyrics.this, LyricsScreen.class);
                startActivity(toLyrics);

            }
        });





        //Google search button onclick
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



                if (artistNameEncoded == "" || songNameEncoded == ""){

                    Toast.makeText(getApplicationContext(),"Song not found", Toast.LENGTH_LONG).show();
                }else {


                    LyricsSearch req = new LyricsSearch();
                    req.execute("https://api.lyrics.ovh/v1/" + artistNameEncoded + "/" + songNameEncoded);
                }

                // Log.i("test", "test" + lyrics);

                //      String string = "String";

            }
        });



        //loading shared prefs
        String savedArtist = sharedPrefs.getString("artist", "");
        String savedSong = sharedPrefs.getString("song", "");

        //setting shared prefs
        artistName.setText(savedArtist);
        songName.setText(savedSong);






















    }

    //Navigation method to sorting item clicks
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.songSearch){

            //Toast.makeText(this, "Test Toast", Toast.LENGTH_SHORT).show();

            //GOes to deezer activity
           Intent deezerIntent = new Intent(MainLyrics.this, DeezerSearch.class);
            startActivity(deezerIntent);

        } else if (id == R.id.geoHome){


            //goes to geo activity
            Intent geoIntent = new Intent(MainLyrics.this, GeoHome.class);
            startActivity(geoIntent);

        }

        //closes drawer if item is selected
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }


    //AsyncTask method for fetching lyrics from link
    class LyricsSearch extends AsyncTask<String, Integer, String> {





        @Override
        protected String doInBackground(String... args) {

            try {

                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //if (FileNotFoundException == true){


                //}
                InputStream response = urlConnection.getInputStream();

                publishProgress(25);

                // XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                //factory.setNamespaceAware(false);
                //XmlPullParser xpp = factory.newPullParser();
                //xpp.setInput(response, "UTF-8");

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;

                //String builder building the entire result
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }

                publishProgress(50);

                String result = sb.toString(); //result is the whole string

                //fetching the tag that contains the lyrics
                JSONObject lyricsFetch = new JSONObject(result);

                //setting the lyrics variable
                lyrics = lyricsFetch.getString("lyrics");
                Log.i("test2", "test" + result);
                //  lyricsTwo = result;


                publishProgress(100);

//                    Toast.makeText(getApplicationContext(),"Song not found", Toast.LENGTH_LONG).show();






                //Starting the LyricsScreen activity and passing the data to it
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

    public void onProgressUpdate (Integer ...args){

        proBar.setVisibility(View.VISIBLE);
        proBar.setProgress(args[0]);
        Log.d("test", "test in onProgUpdate");


    }





    }


    //On Pause method for commiting shared prefs
    @Override
    protected void onPause() {
        super.onPause();

        //SharedPreferences sharedPrefs = getSharedPreferences("sharedPrefs",MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("artist", artistName.getText().toString());
        editor.putString("song", songName.getText().toString());
        editor.commit();



    }


    //inflating the toolbar layout
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.main_lyrics_menu, menu);
        return true;
    }


    //Toolbar menu sorting method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;

        switch(item.getItemId())
        {

            case R.id.item1:
                //message = "You clicked item 1";

                //Goes to deezer activity
               Intent deezerIntent = new Intent(MainLyrics.this, DeezerSearch.class);
                startActivity(deezerIntent);


                break;
            case R.id.item2:
              ///  message = "You clicked on the search";

                //goes to geo activity
               Intent geoIntent = new Intent(MainLyrics.this, GeoHome.class);
                startActivity(geoIntent);


                break;
            case R.id.helpItem:
                //message = "You clicked on mail";

                //alert for if the help item is picked
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
