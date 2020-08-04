package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Class used for displaying favourited songs by the user in a database and listview
 * @author Austin
 * @version 1.0
 */

public class DeezerFavourites extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** Reference to the custom adapter */
    private DeezerAdapter adapter;

    /** Array list of songs */
    private ArrayList<DeezerArtist> artistList = new ArrayList<DeezerArtist>();
    /** Database reference */
    SQLiteDatabase db;

    /** Default string for SONG_NAME in the bundle */
    public static final String SONG_NAME = "SONG";
    /** Default string for ALBUM_NAME in the bundle */
    public static final String ALBUM_NAME = "ALBUM";
    /** Default int for DURATION in the bundle */
    public static final int DURATION = 0;
    /** Default string for IMAGE in the bundle */
    public static final String IMAGE = null;

    /** Integer code to be used by startActivityForResult() for identification */
    public static int REQUEST_CODE = 613;
    /** Integer code to be used by children for changing of data from the database */
    public static int DATABASE_CHANGED = 852;
    /** Integer code to be used by children for removal of data from the database */
    public static int REMOVE_DATA = 123;

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
        setContentView(R.layout.activity_deezer_favourites);

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

        loadDataFromDatabase();
        listViewSetUp();

    }

    /**
     * Instantiates the listview to be displayed and adds an onItemClickListener for passing data to DeezerDetails
     */
    private void listViewSetUp() {
        ListView artistListView = (ListView)findViewById(R.id.favouritesList);
        artistListView.setAdapter(adapter = new DeezerAdapter(artistList, DeezerFavourites.this));

        if(DeezerDatabase.COL_SONG.isEmpty()) {

        }

        artistListView.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            DeezerArtist artist = artistList.get(position);
            dataToPass.putString(SONG_NAME, artist.getTitle());
            dataToPass.putString(ALBUM_NAME, artist.getAlbumName());
            dataToPass.putInt(String.valueOf(DURATION), artist.getDuration());
            dataToPass.putByteArray(IMAGE, getBytes(artist.getAlbumCover()));

            Intent nextActivity = new Intent(DeezerFavourites.this, DeezerEmpty.class);
            nextActivity.putExtra("data", dataToPass);
            startActivityForResult(nextActivity, REQUEST_CODE);
        });
    }

    /**
     * Called when the activity exits
     * @param requestCode Integer code supplied to startActivityForResult() for identification
     * @param resultCode Integer code returned by child activity through its setResult()
     * @param data An intent, which can return data to the caller
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == DATABASE_CHANGED) {
            artistList.clear();
            loadDataFromDatabase();
            adapter.notifyDataSetChanged();
        }
        if(requestCode == REQUEST_CODE && resultCode == REMOVE_DATA) {
            String toBeRemoved = data.getStringExtra(SONG_NAME);
            DeezerArtist song = new DeezerArtist(toBeRemoved);
            removeSong(song); //Notify set changed is in removeSong()
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.successRemove) + " " + toBeRemoved + " " + getResources().getString(R.string.fromFavourites), Toast.LENGTH_SHORT).show();
            artistList.clear();
            loadDataFromDatabase();
        }
    }

    /**
     * Called to load newly added or removed data from the database
     */
    private void loadDataFromDatabase() {
        DeezerDatabase dbOpen = new DeezerDatabase(this);
        db = dbOpen.getWritableDatabase();

        String [] columns = {
                DeezerDatabase.COL_ID,
                DeezerDatabase.COL_SONG,
                DeezerDatabase.COL_ALBUM,
                DeezerDatabase.COL_DURATION,
                DeezerDatabase.COL_IMAGE
        };

        Cursor results = db.query(false, DeezerDatabase.TABLE_NAME, columns, null, null, null, null, null, null);
        dbOpen.printCursor(results);

        int idCol = results.getColumnIndex(DeezerDatabase.COL_ID);
        int songCol = results.getColumnIndex(DeezerDatabase.COL_SONG);
        int albumCol = results.getColumnIndex(DeezerDatabase.COL_ALBUM);
        int durCol = results.getColumnIndex(DeezerDatabase.COL_DURATION);
        int imageCol = results.getColumnIndex(DeezerDatabase.COL_IMAGE);


        while(results.moveToNext()) {
            long id = results.getLong(idCol);
            String song = results.getString(songCol);
            String album = results.getString(albumCol);
            int duration = results.getInt(durCol);
            byte[] image = results.getBlob(imageCol);

            artistList.add(new DeezerArtist(song, album, duration, getImage(image), id));
        }
    }

    /**
     * Convert Bitmap to byte
     * @param image
     * @return The bitmap of the byte array
     * https://stackoverflow.com/questions/11790104/how-to-storebitmap-image-and-retrieve-image-from-sqlite-database-in-android
     */
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    /**
     * Convert byte to Bitmap
     * @param bitmap
     * @return The byte array of the bitmap
     * https://stackoverflow.com/questions/11790104/how-to-storebitmap-image-and-retrieve-image-from-sqlite-database-in-android
     */
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    /**
     * Removes the song from the database
     * @param title The name of the song to be removed
     */

    public void removeSong(DeezerArtist title) {
        db.delete(DeezerDatabase.TABLE_NAME, DeezerDatabase.COL_SONG + "= ?",
                new String[] {title.getTitle()});
        artistList.remove(title);
        adapter.notifyDataSetChanged();
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
        Intent goToGeo = new Intent(DeezerFavourites.this, GeoTempHome.class);
        Intent goToLyrics = new Intent(DeezerFavourites.this, MainLyrics.class);
        Intent goToDeezer = new Intent(DeezerFavourites.this, DeezerSearch.class);

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

        Intent goToGeo = new Intent(DeezerFavourites.this, GeoTempHome.class);
        Intent goToLyrics = new Intent(DeezerFavourites.this, MainLyrics.class);
        Intent goToDeezer = new Intent(DeezerFavourites.this, DeezerSearch.class);

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