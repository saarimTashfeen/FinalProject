package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

/**
 * Fragment page to be used for inflation
 */

public class DeezerEmpty extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
        setContentView(R.layout.activity_deezer_empty);

        Intent fromFavourites = getIntent();
        Bundle dataToPass = fromFavourites.getBundleExtra("data");

        //Unsure if anything from here  {

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

        // } To here does anything

        DeezerFragment dFragment = new DeezerFragment();
        dFragment.setArguments(dataToPass);
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentEmpty, dFragment).commit();
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
        Intent goToGeo = new Intent(DeezerEmpty.this, GeoTempHome.class);
        Intent goToLyrics = new Intent(DeezerEmpty.this, MainLyrics.class);
        Intent goToDeezer = new Intent(DeezerEmpty.this, DeezerSearch.class);

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

        Intent goToGeo = new Intent(DeezerEmpty.this, GeoTempHome.class);
        Intent goToLyrics = new Intent(DeezerEmpty.this, MainLyrics.class);
        Intent goToDeezer = new Intent(DeezerEmpty.this, DeezerSearch.class);

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