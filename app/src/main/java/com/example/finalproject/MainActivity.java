package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText lat;
    EditText lon;
    Button btn;
    ListView lv;
    ProgressBar pb;
    city city;
    ArrayList<city> shownCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = findViewById(R.id.latitude);
        lon = findViewById(R.id.longitude);
        btn = findViewById(R.id.button);
        lv = findViewById(R.id.listView);
        pb = findViewById(R.id.progressBar);

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


