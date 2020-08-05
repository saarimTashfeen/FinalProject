package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to display the results from the search window
 * @author Austin
 * @version 1.0
 */
public class DeezerResults extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** ListView used in the xml file to display the songs from the search */
    private ListView songListView;
    /** The url from the <tracklist></tracklist> of the api, the name of the artist */
    private String trackListUrl, artistName;
    /** An array list of the songs by the artist searched */
    List<DeezerArtist> listArtist = new ArrayList<>();

    /** The toolbar for accessing other activities */
    Toolbar tBar;
    /** The custom title of the toolbar */
    TextView toolbarTitle;

    /**
     * Called when the activity is starting or restarting
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_results);
        songListView = (ListView)findViewById(R.id.SongListView);


        Intent goToResults = getIntent();
        Bundle bundle = goToResults.getExtras();
        trackListUrl = (String) bundle.get("URL");
        artistName = (String) bundle.get("artistName");

        //toolbar stuff
        tBar = findViewById(R.id.tool_bar);
        setSupportActionBar(tBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = (TextView) tBar.findViewById(R.id.toolbar_title);

        //navigation drawer stuff:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SongSearchQuery req = new SongSearchQuery();
        //Had issue where async task wasn't stopping so new one wouldn't get called
        if(req.getStatus().equals(AsyncTask.Status.RUNNING))
        {
            req.cancel(true);
        }
        req.execute(trackListUrl);

        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Callback method to be invoked when an item in this AdapterView is clicked
             * @param parent The AdapterView where the click happened
             * @param view THe view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that was clicked
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //https://stackoverflow.com/questions/41485882/passing-value-from-listview-to-another-activity-class
                String songTitle = listArtist.get(position).getTitle();
                String albumName = listArtist.get(position).getAlbumName();
                int duration = listArtist.get(position).getDuration();
                Bitmap albumCover = listArtist.get(position).getAlbumCover();

                Intent goToDetailsPage = new Intent(DeezerResults.this, DeezerDetails.class);

                goToDetailsPage.putExtra("artistName", artistName);
                goToDetailsPage.putExtra("songTitle", songTitle);
                goToDetailsPage.putExtra("albumName", albumName);
                goToDetailsPage.putExtra("duration", duration);
                //https://stackoverflow.com/questions/11010386/passing-android-bitmap-data-within-activity-using-intent-in-android
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                albumCover.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                goToDetailsPage.putExtra("albumCover", byteArray);
                startActivityForResult(goToDetailsPage, 111);
            }
        });
    }

    /**
     * Inflates the toolbar to be visible on the page
     * @param menu The menu file being inflated
     * @return True that the menu file is being inflated
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_deezer_toolbar, menu);
        return true;
    }

    /**
     * Handles the selection bar on the toolbar
     * @param item The item being selected
     * @return True that the item has been selected
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle presses on the action bar items
        Intent goToGeo = new Intent(DeezerResults.this, GeoHome.class);
        Intent goToLyrics = new Intent(DeezerResults.this, MainLyrics.class);
        Intent goToDeezer = new Intent(DeezerResults.this, DeezerSearch.class);

        switch (item.getItemId()) {
            case R.id.toolbarGeo:
                startActivity(goToGeo);
                break;
            case R.id.toolBarLyrics:
                startActivity(goToLyrics);
                break;
            case R.id.toolBarDeezer:
                startActivity(goToDeezer);
                break;
        }
        return true;
    }

    /**
     * Called when an item in the navigation menu is selected
     * @param item The selected item
     * @return False to display the item as the selected item
     */

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Intent goToGeo = new Intent(DeezerResults.this, GeoHome.class);
        Intent goToLyrics = new Intent(DeezerResults.this, MainLyrics.class);
        Intent goToDeezer = new Intent(DeezerResults.this, DeezerSearch.class);

        switch (item.getItemId()) {
            case R.id.drawerGeo:
                startActivity(goToGeo);
                break;
            case R.id.drawerLyrics:
                startActivity(goToLyrics);
                break;
            case R.id.drawerDeezer:
                startActivity(goToDeezer);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }



    /**
     * Async task that converts strings to JSON from the api and draws the progress bar.
     * Displays results once bar is complete.
     */
    private class SongSearchQuery extends AsyncTask<String, Integer, String> {
        String trackList, title,  albumName, imgUrl;
        int duration;
        Bitmap albumCover;
        ProgressBar progressBar;
        DeezerArtist deezerArtist;

        /**
         * Scrapes data
         * @param params The parameters of the task
         * @return The information provided by the API
         */
        @Override
        public String doInBackground(String... params) {
            try {

                Log.i("Async Task","In SongSearchQuery");
                URL urlTrackList = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection)urlTrackList.openConnection();
                InputStream is = urlConnection.getInputStream();

                Log.i("Async Task", "URL: " + urlTrackList.toString());
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(is, "UTF-8");

                int eventType = xpp.getEventType(); //Parser currently at START_DOCUMENT

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xpp.getName().equalsIgnoreCase("tracklist")) {
                            Log.i("Async Task", "Pointing at <tracklist>");
                                xpp.next();
                                trackList = xpp.getText(); //Should return tracklist
                                Log.i("AsyncTask", "Tracklist is: " + trackList);
                            break;
                        }
                    }
                    eventType = xpp.next();
                }

                URL url = new URL(trackList);
                HttpURLConnection urlConnection2 = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection2.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                //convert to JSON
                JSONObject apiData = new JSONObject(result);
                JSONArray artistArray = apiData.getJSONArray("data");
                for(int i = 0; i < artistArray.length(); i++) {
                    JSONObject artistData = artistArray.getJSONObject(i);
                    deezerArtist = new DeezerArtist(title, albumName, duration);
                    deezerArtist.setTitle(artistData.getString("title"));
                    publishProgress(20);
                    JSONObject albumNameObj = artistData.getJSONObject("album");
                    deezerArtist.setAlbumName(albumNameObj.getString("title"));
                    publishProgress(40);
                    deezerArtist.setDuration(artistData.getInt("duration"));
                    publishProgress(60);
                    //get bitmap for images
                    imgUrl = albumNameObj.getString("cover_big");
                    String imgPath = albumNameObj.getString("title").replace("/", "") + ".png";

                    //get image from internet or download to local
                    if(fileExist(imgPath)) { //if file is local, use it
                        FileInputStream fis = null;
                        try {
                            fis = openFileInput(imgPath);
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        }
                        albumCover = BitmapFactory.decodeStream(fis);
                    } else { //download from internet
                        URL imageUrl = new URL(imgUrl);
                        HttpURLConnection urlImageConnection = (HttpURLConnection) imageUrl.openConnection();
                        urlImageConnection.connect();
                        InputStream resp = urlImageConnection.getInputStream();
                        albumCover = BitmapFactory.decodeStream(resp);

                        //save to local
                        FileOutputStream fos = openFileOutput(imgPath, Context.MODE_PRIVATE);
                        albumCover.compress(Bitmap.CompressFormat.PNG, 80, fos);
                        fos.flush();
                        fos.close();
                        urlImageConnection.disconnect();
                    }
                    deezerArtist.setAlbumCover(albumCover);
                    publishProgress(80);

                    listArtist.add(deezerArtist);
                    publishProgress(100);
                    Log.i("AsyncTask", "Song Info: " + deezerArtist.getTitle() + " " + deezerArtist.getAlbumName() + " " + deezerArtist.getDuration());
                }
                publishProgress(100);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Exception", "doInBackgroundException");
            }

            return "Done";
        }

        /**
         * Called when setProgress is called, updates the progress bar
         * @param args The values indicating progress
         */

        public void onProgressUpdate(Integer... args) {
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            if(trackListUrl == "https://api.deezer.com/search/artist/?q=&output=xml") {
                progressBar.setProgress(1);
            } else
            progressBar.setProgress(args[0]);
        }

        /**
         * Called when async task is completed, creates the adapter with a list and removes the progress bar's visibility
         * @param fromDoInBackground The result of the operation computed by doInBackground
         */

        protected void onPostExecute(String fromDoInBackground) {
            Log.i("AsyncTask", "in onPostExecute");
            DeezerAdapter adapter = new DeezerAdapter(listArtist, DeezerResults.this);
            songListView.setAdapter(adapter);
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);


        }

        /**
         * Checks whether the file exists in the local path
         * @param name The path of the file
         * @return True if the file exists locally
         */

        private boolean fileExist(String name){
            File file = getBaseContext().getFileStreamPath(name);
            return file.exists();
        }
    }



}