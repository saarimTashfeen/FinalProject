package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

/**
 * Fragment class to be inflated into DeezerEmpty
 * @author Austin
 * @version 1.0
 */

public class DeezerFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{

    /** Bundle being passed from the activity */
    private Bundle dataFromActivity;
    /** The parent activity that the bundle is being passed from */
    private AppCompatActivity parentActivity;

    /** The ImageView in the xml file that will display the album artwork */
    private ImageView albumCoverImage;
    /** The TextViews in the xml file that will display the song name, album name, and duration */
    private TextView titleText, albumNameText, durationText;
    /** The Button in the xml file that will display the unfavourite button */
    private Button unfavouriteButton;

    /** The title of the song, and album */
    private String title, albumName;
    /** The duration of the song in seconds */
    private int duration;
    /** The Bitmap of the album artwork */
    private Bitmap albumCover;
    /** The byte array that is passed from the bundle to create the album cover Bitmap */
    private byte[] albumCoverByteArray;

    /** The toolbar for accessing other activities */
    Toolbar tBar;
    /** The custom title of the toolbar */
    TextView toolbarTitle;

    /**
     * Called when the activity is starting or restarting
     * @param savedInstanceState  If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        View result = inflater.inflate(R.layout.activity_deezer_fragment, container, false);

        albumCoverImage = (ImageView)result.findViewById(R.id.albumCoverFrag);
        titleText = (TextView)result.findViewById(R.id.songNameFrag);
        albumNameText = (TextView)result.findViewById(R.id.albumNameFrag);
        durationText = (TextView)result.findViewById(R.id.songDurationFrag);
        unfavouriteButton = (Button)result.findViewById(R.id.unfavouriteButtonFrag);

        //Retrieving data from favourites
        title = dataFromActivity.getString(DeezerFavourites.SONG_NAME);
        albumName = dataFromActivity.getString(DeezerFavourites.ALBUM_NAME);
        duration = dataFromActivity.getInt(String.valueOf(DeezerFavourites.DURATION));
        albumCoverByteArray = dataFromActivity.getByteArray(DeezerFavourites.IMAGE); //Image comes in as byte array
        albumCover = DeezerFavourites.getImage(albumCoverByteArray); //Convert to bitmap

        //toolbar stuff
        tBar = parentActivity.findViewById(R.id.tool_bar);
        parentActivity.setSupportActionBar(tBar);
        parentActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle = (TextView) tBar.findViewById(R.id.toolbar_title);

        //navigation drawer stuff:
        DrawerLayout drawer = parentActivity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this.parentActivity,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = parentActivity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set GUI
        albumCoverImage.setImageBitmap(albumCover);
        titleText.setText(title);
        albumNameText.setText(albumName);
        durationText.setText(String.valueOf(duration) + getResources().getString(R.string.seconds));

        unfavouriteButton.setOnClickListener(click -> {
            DeezerEmpty parent = (DeezerEmpty) getActivity();
            Intent closeFragment = new Intent();
            closeFragment.putExtra(DeezerFavourites.SONG_NAME, title);
            parent.setResult(DeezerFavourites.REMOVE_DATA, closeFragment);
            parent.finish();
        });

        return result;
    }

    /**
     * Inflates the toolbar to be visible on the page
     * @param menu The menu file being inflated
     * @return True that the menu file is being inflated
     */

    public boolean onCreateOptionsMenu(Menu menu) {
        parentActivity.getMenuInflater().inflate(R.menu.menu_deezer_toolbar, menu);
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
        Intent goToGeo = new Intent(this.parentActivity, GeoHome.class);
        Intent goToLyrics = new Intent(this.parentActivity, MainLyrics.class);
        Intent goToDeezer = new Intent(this.parentActivity, DeezerSearch.class);

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

        Intent goToGeo = new Intent(this.parentActivity, GeoHome.class);
        Intent goToLyrics = new Intent(this.parentActivity, MainLyrics.class);
        Intent goToDeezer = new Intent(this.parentActivity, DeezerSearch.class);

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

        DrawerLayout drawerLayout = parentActivity.findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    /**
     * Called when the fragment is first attached to its context
     * @param context The context
     */

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentActivity = (AppCompatActivity) context;
    }


}
