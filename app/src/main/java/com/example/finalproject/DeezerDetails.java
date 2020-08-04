package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

/**
 * Page to show details of the song clicked in DeezerResults
 * @author Austin
 * @version 1.0
 */
public class DeezerDetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** The bundle being passed to this class */
    Bundle bundle;
    /** The song name & album name being passed from DeezerResults */
    String dataPassedSongName, dataPassedAlbumName;
    /** The song's duration being passed from DeezerResults */
    int dataPassedDuration;
    /** The song's album artwork being passed from DeezerResults */
    Bitmap dataPassedAlbumCover;

    /** The checkbox to add the song to favourites */
    CheckBox favouriteCheck;
    /** The toolbar for accessing other activities */
    Toolbar tBar;
    /** The custom title of the toolbar */
    TextView toolbarTitle;

    /** The SQLite database reference */
    SQLiteDatabase db;

    /**
     * Called when the activity is starting or restarting
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_details);

        Log.i("", "In onCreate DeezerDetails");

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

        /** Instantiating the database */
        DeezerDatabase dbOpener = new DeezerDatabase(this);
        db = dbOpener.getWritableDatabase();

        bundle = getIntent().getExtras();
        dataPassedSongName = bundle.getString("songTitle");
        dataPassedAlbumName = bundle.getString("albumName");
        dataPassedDuration = bundle.getInt("duration", 0);
        byte[] byteArray = getIntent().getByteArrayExtra("albumCover");
        dataPassedAlbumCover = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        TextView songName = (TextView)findViewById(R.id.songName);
        TextView albumName = (TextView)findViewById(R.id.albumName);
        TextView duration = (TextView)findViewById(R.id.songDuration);
        ImageView albumCover = (ImageView)findViewById(R.id.albumCover);

        songName.setText(getResources().getString(R.string.songName) + dataPassedSongName);
        albumName.setText(getResources().getString(R.string.albumName) + dataPassedAlbumName);
        duration.setText(getResources().getString(R.string.duration) + dataPassedDuration + " " + getResources().getString(R.string.seconds));
        albumCover.setImageBitmap(dataPassedAlbumCover);

        favouriteCheck = (CheckBox)findViewById(R.id.favouriteButton);

        if(DeezerDatabase.TABLE_NAME.contains(dataPassedSongName)) {
            favouriteCheck.isChecked();
        }

        favouriteCheck.setOnCheckedChangeListener((cb, isChecked) -> {

            if (isChecked) {
                addToFavourites();
                setResult(DeezerFavourites.DATABASE_CHANGED);
            } else {
                removeFromFavourites();
                setResult(DeezerFavourites.REMOVE_DATA);

            }
        });
    }

    /**
     * Adds the current detail to the database
     */
    private void addToFavourites() {
        ContentValues rowValues = new ContentValues();
        rowValues.put(DeezerDatabase.COL_SONG, dataPassedSongName);
        rowValues.put(DeezerDatabase.COL_ALBUM, dataPassedAlbumName);
        rowValues.put(DeezerDatabase.COL_DURATION, dataPassedDuration);
        byte[] byteArray = getIntent().getByteArrayExtra("albumCover");
        rowValues.put(DeezerDatabase.COL_IMAGE, byteArray);
        long newId = db.insert(DeezerDatabase.TABLE_NAME, null, rowValues);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.successAdd) + " " +  dataPassedSongName + " " +  getResources().getString(R.string.toFavourites), Toast.LENGTH_SHORT).show();
    }

    /**
     * Removes current detail from database
     */
    private void removeFromFavourites() {
        db.delete(DeezerDatabase.TABLE_NAME, DeezerDatabase.COL_SONG + "= ?", new String[] {dataPassedSongName});
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.successRemove) + " " +  dataPassedSongName + " " +  getResources().getString(R.string.fromFavourites), Toast.LENGTH_SHORT).show();
    }

    /**
     * Closes the database when the details page is stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
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
        Intent goToGeo = new Intent(DeezerDetails.this, GeoTempHome.class);
        Intent goToLyrics = new Intent(DeezerDetails.this, MainLyrics.class);
        Intent goToDeezer = new Intent(DeezerDetails.this, DeezerSearch.class);

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

        Intent goToGeo = new Intent(DeezerDetails.this, GeoTempHome.class);
        Intent goToLyrics = new Intent(DeezerDetails.this, MainLyrics.class);
        Intent goToDeezer = new Intent(DeezerDetails.this, DeezerSearch.class);

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

}