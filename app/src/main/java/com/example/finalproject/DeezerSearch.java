package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

/**
 * Main/Title page for searching or accessing favourites
 * @author Austin
 * @version 1.0
 */

public class DeezerSearch extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /** The first part of the api url */
    final static String URL_START = "https://api.deezer.com/search/artist/?q=";
    /** The last part of the api url */
    final static String URL_END = "&output=xml";

    /** Reference to SharedPreferences for saving details */
    SharedPreferences prefs;
    /** The String to be saved as SharedPreferences */
    private String search;
    /** The EditText in the xml file for searching the api */
    EditText apiSearch;
    /** The Button for executing the search, the Button for accessing the user's favourites, the Button for accessing the help dialog */
    Button searchButton, favouritesButton, helpButton;
    /** The ImageButton for the logo and secret */
    ImageButton deezerLogo;

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
        setContentView(R.layout.activity_deezer_search);

        apiSearch = (EditText) findViewById(R.id.artistSearch);
        searchButton = (Button) findViewById(R.id.searchButton);
        favouritesButton = (Button) findViewById(R.id.favourites);
        helpButton = (Button) findViewById(R.id.helpButton);
        deezerLogo = (ImageButton) findViewById(R.id.deezerImage);

        prefs = getSharedPreferences("DeezerSongAPI", Context.MODE_PRIVATE);
        search = prefs.getString("Search", getResources().getString(R.string.defaultPref));

        apiSearch.setHint(getResources().getString(R.string.ex) + search.replace("+", " "));

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

        searchButton.setOnClickListener(c -> {
            search = apiSearch.getText().toString().replace(" ", "+");

            Intent goToResults = new Intent(DeezerSearch.this, DeezerResults.class);
            goToResults.putExtra("artistName", search);
            goToResults.putExtra("URL", URL_START + search + URL_END);
            startActivityForResult(goToResults, 951);
        });

        favouritesButton.setOnClickListener(c -> {
            Intent goToFavourites = new Intent(DeezerSearch.this, DeezerFavourites.class);
            startActivity(goToFavourites);
        });

        helpButton.setOnClickListener(c -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(DeezerSearch.this);
            builder.setTitle(getResources().getString(R.string.HelpTitleDetails))
                    .setMessage(getResources().getString(R.string.HelpScreen1)
//                            "Welcome to the Deezer API searcher." +
//                            "\n\nThe search page contains four different options:" +
//                            "\n1. The SEARCH BAR can be used to enter any artist's name in the field provided." +
//                            "\n2. The SEARCH button is used to confirm your SEARCH BAR String." +
//                            "\n3. The FAVOURITES button is used to take you to your favourited songs (if you have any)." +
//                            "\n4. You already know what the HELP button does."
                    )
                    .setPositiveButton(getResources().getString(R.string.nextButton), new DialogInterface.OnClickListener() {
                        /**
                         * Invoked when the positive button is clicked
                         * @param dialog The dialog that received the click
                         * @param which The button that was clicked
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DeezerSearch.this);
                            builder.setTitle(getResources().getString(R.string.HelpTitleFavourites))
                                    .setMessage(getResources().getString(R.string.HelpScreen2)
//                                            "The favourites page contains two options:" +
//                                            "\n1. View the details of your favourite song by clicking on it!" +
//                                            "\n2. When in the details of your song, press UNFAVOURITE to remove it from your list."
                                    )
                                    .setPositiveButton(getResources().getString(R.string.nextButton), new DialogInterface.OnClickListener() {
                                        /**
                                         * Invoked when the positive button is clicked
                                         * @param dialog The dialog that received the click
                                         * @param which The button that was clicked
                                         */
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(DeezerSearch.this);
                                            builder.setTitle(getResources().getString(R.string.HelpTitleResults))
                                                    .setMessage(getResources().getString(R.string.HelpScreen3)
//                                                            "The results page contains one option:" +
//                                                            "\n1. Click a song to view the details of it."
                                                    )
                                                    .setPositiveButton(getResources().getString(R.string.nextButton), new DialogInterface.OnClickListener() {
                                                        /**
                                                         * Invoked when the positive button is clicked
                                                         * @param dialog The dialog that received the click
                                                         * @param which The button that was clicked
                                                         */
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(DeezerSearch.this);
                                                            builder.setTitle(getResources().getString(R.string.HelpTitleDetails))
                                                                    .setMessage(getResources().getString(R.string.HelpScreen4)
//                                                                            "The details page contains one option:" +
//                                                                            "\n1. Click the FAVOURITE button to add it to your favourites list (And again to remove it)."
                                                                    )
                                                                    .setNegativeButton(getResources().getString(R.string.nextButton), new DialogInterface.OnClickListener() {
                                                                        /**
                                                                         * Invoked when the negative button is clicked
                                                                         * @param dialog The dialog that received the click
                                                                         * @param which The button that was clicked
                                                                         */
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {

                                                                        }
                                                                    });
                                                            builder.show();
                                                        }
                                                    })
                                                    .setNegativeButton(getResources().getString(R.string.endButton), new DialogInterface.OnClickListener() {
                                                        /**
                                                         * Invoked when the negative button is clicked
                                                         * @param dialog The dialog that received the click
                                                         * @param which The button that was clicked
                                                         */
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });
                                            builder.show();
                                        }
                                    })
                                    .setNegativeButton(getResources().getString(R.string.endButton), new DialogInterface.OnClickListener() {
                                        /**
                                         * Invoked when the negative button is clicked
                                         * @param dialog The dialog that received the click
                                         * @param which The button that was clicked
                                         */
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                            builder.show();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.goBack), new DialogInterface.OnClickListener() {
                        /**
                         * Invoked when the negative button is clicked
                         * @param dialog The dialog that received the click
                         * @param which The button that was clicked
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();
        });

        deezerLogo.setOnLongClickListener(new AdapterView.OnLongClickListener() {
            /**
             * Called when the ImageView is clicked and held
             * @param v The view that was clicked and held
             * @return True if the callback consumed the long click, false otherwise
             */
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(deezerLogo, getResources().getString(R.string.snackbarText), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.goSnackbar), new View.OnClickListener() {
                            //https://stackoverflow.com/questions/48032841/how-to-add-clickable-website-link-into-snackbar
                            @Override
                            public void onClick(View view) {
                                Intent browserIntent = new
                                        Intent(Intent.ACTION_VIEW,
                                        Uri.parse(getString(R.string.page_address)));
                                startActivity(browserIntent);
                            }
                        }).show();
                return false;
            }
        });
    }

    /**
     * Called when the user no longer actively interacts with the activity, but it is still visible on screen
     */

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor ed = prefs.edit();
        ed.putString("Search", search);
        ed.commit();
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
        Intent goToGeo = new Intent(DeezerSearch.this, GeoTempHome.class);
        Intent goToLyrics = new Intent(DeezerSearch.this, MainLyrics.class);
        Intent goToDeezer = new Intent(DeezerSearch.this, DeezerSearch.class);

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
        return false;
    }

    /**
     * Called when an item in the navigation menu is selected
     * @param item The selected item
     * @return False to display the item as the selected item
     */

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Intent goToGeo = new Intent(DeezerSearch.this, GeoTempHome.class);
        Intent goToLyrics = new Intent(DeezerSearch.this, MainLyrics.class);
        Intent goToDeezer = new Intent(DeezerSearch.this, DeezerSearch.class);

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