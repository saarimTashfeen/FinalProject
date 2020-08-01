package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText lat;
    EditText lon;
    Button btn;
    ListView lv;
    ProgressBar pb;
    city city;
    ArrayList<city> shownCities;

    Toolbar tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = findViewById(R.id.latitude);
        lon = findViewById(R.id.longitude);
        btn = findViewById(R.id.button);
        lv = findViewById(R.id.listView);
        pb = findViewById(R.id.progressBar);
        tb = (Toolbar) findViewById(R.id.tb);
        //setActionBar(tb);
        setSupportActionBar(tb);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tb, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        shownCities = new ArrayList<>();

        btn.setOnClickListener(l -> {
            if (lat.getText().toString() != "" || lat.getText().toString() != null) {
                if (lon.getText().toString() != "" || lon.getText().toString() != null) {
                    GeoData req = new GeoData();
                    Log.i("lat",lat.getText().toString());
                    Log.i("long",lon.getText().toString());
                    String tempLong = lon.getText().toString();
                    String tempLat = lat.getText().toString();
                    req.execute("https://api.geodatasource.com/cities?key=IFSY3SLSX1ZBEQHPP1KANIABBO6DSAQZ&format=xml&lat="+tempLat+""+"&lng="+tempLong+"");
                } else {
                    Toast.makeText(MainActivity.this, "Invalid longitude", Toast.LENGTH_SHORT);
                }
            } else {
                Toast.makeText(MainActivity.this, "Invalid Latitude", Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.AboutThisProject:
            Toast.makeText(this,"This is the Geo Data Source activity, written by Aimen Hallou",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.instructions:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Instructions");
                alert.setMessage("Please enter 2 different sets of numbers");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.create().show();
                break;
            case R.id.aboutAPI:
                Toast.makeText(this,"https://www.geodatasource.com/web-service",Toast.LENGTH_SHORT);
                break;
            case R.id.donate:
                AlertDialog.Builder alert2 = new AlertDialog.Builder(this);
                alert2.setTitle("Please give generously");
                alert2.setMessage("How much money would you like to donate?");
                alert2.setPositiveButton("THANK YOU", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert2.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert2.create().show();
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    class GeoData  extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            //create a URL object of what server to contact:
            URL url = null;
            try {
                String tempCountry= null, tempRegion= null, tempCity= null, tempCurrency= null, tempLatitude= null, tempLongitude = null;
                url = new URL(strings[0]);
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                Log.i("check","test");
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");
                String parameter = null;
                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                           if (xpp.getName().equals("country")){
                                tempCountry = xpp.getText();
                           }
                            if (xpp.getName().equals("region")){
                                tempRegion = xpp.getText();
                            }
                            if (xpp.getName().equals("city")){
                                tempCity = xpp.getText();
                            }
                            if (xpp.getName().equals("latitude")){
                                tempLatitude = xpp.getText();
                            }
                            if (xpp.getName().equals("longitude")){
                                tempLongitude = xpp.getText();
                            }
                            if (xpp.getName().equals("currency_code")){
                                tempCurrency = xpp.getText();
                            }
                            city = new city(tempCountry,tempRegion,tempCity,tempCurrency,tempLatitude,tempLongitude);
                            shownCities.add(city);
                            Log.i("makingCity",city.toString());
                    }
                    eventType = xpp.next();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pb.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pb.setVisibility(View.VISIBLE);
            pb.setProgress(values[0]);
        }
    }
    class city {
        String country;
        String region;
        String cityName;
        String currency;
        String latitude;
        String longitude;

        public city(){
        }
        public city(String country,String region,String cityName,String currency,String latitude,String longitude){
            this.country = country;
            this.region = region;
            this.cityName = cityName;
            this.currency = currency;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getCountry() {
            return country;
        }

        public String getRegion() {
            return region;
        }

        public String getCityName() {
            return cityName;
        }

        public String getCurrency() {
            return currency;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        @Override
        public String toString() {
            return "city{" +
                    "country='" + country + '\'' +
                    ", region='" + region + '\'' +
                    ", cityName='" + cityName + '\'' +
                    ", currency='" + currency + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    '}';
        }
    }
}


