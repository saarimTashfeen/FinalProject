package com.example.finalproject;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GeoHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    EditText lat;
    EditText lon;
    Button btn;
    ListView lv;
    ProgressBar pb;
    city city;
    ArrayList<city> shownCities;
    ArrayList<city> favouriteCities;
    MyOpener mydb;
    SQLiteDatabase db;
    Toolbar tb;
    MyListAdapter myAdapter;
    public static final String ITEM_COUNTRY = "COUNTRY";
    public static final String ITEM_REGION = "REGION";
    public static final String ITEM_CITY = "CITY";
    public static final String ITEM_CURRENCY = "CURRENCY";
    public static final String ITEM_LATITUDE = "LATITUDE";
    public static final String ITEM_LONGITUDE = "LONGITUDE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geohome_activity);
        lat = findViewById(R.id.latitude);
        lon = findViewById(R.id.longitude);
        btn = findViewById(R.id.button);
        lv = findViewById(R.id.listView);
        pb = findViewById(R.id.progressBar);
        tb = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);
        shownCities = new ArrayList<>();
        favouriteCities = new ArrayList<>();
        tb.setTitle("");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tb, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        lv.setAdapter(myAdapter = new MyListAdapter());

        mydb = new MyOpener(this);
        btn.setOnClickListener(l -> {
            if (lat.getText().toString() != "" || lat.getText().toString() != null) {
                if (lon.getText().toString() != "" || lon.getText().toString() != null) {
                    GeoData req = new GeoData();
                    Log.i("lat",lat.getText().toString());
                    Log.i("long",lon.getText().toString());
                    String tempLong = lon.getText().toString();
                    String tempLat = lat.getText().toString();
                    req.execute("https://api.geodatasource.com/cities?key=IFSY3SLSX1ZBEQHPP1KANIABBO6DSAQZ&format=xml&lat="+tempLat+""+"&lng="+tempLong+"");
                    if (shownCities.size()==0){
                        Toast.makeText(this,"No cities nearby",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GeoHome.this, "Invalid longitude", Toast.LENGTH_SHORT);
                }
            } else {
                Toast.makeText(GeoHome.this, "Invalid Latitude", Toast.LENGTH_SHORT);
            }
        });

        myAdapter.notifyDataSetChanged();
        lv.setOnItemClickListener((list,item,position,id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_COUNTRY, shownCities.get(position).getCountry());
            dataToPass.putString(ITEM_REGION, shownCities.get(position).getRegion());
            dataToPass.putString(ITEM_CITY, shownCities.get(position).getCityName());
            dataToPass.putString(ITEM_CURRENCY, shownCities.get(position).getCurrency());
            dataToPass.putString(ITEM_LATITUDE, shownCities.get(position).getLatitude());
            dataToPass.putString(ITEM_LONGITUDE, shownCities.get(position).getLongitude());


            Intent nextActivity = new Intent(GeoHome.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition

        });
        //loadDataFromDatabase();

    }
    private void loadDataFromDatabase() {
        //work in progress
        String [] columns = {mydb.COL_1, mydb.COL_2, mydb.COL_3,mydb.COL_4,mydb.COL_5,mydb.COL_6};
        db = mydb.getWritableDatabase();
        Cursor results = db.query(false, mydb.TABLE_NAME, columns, null, null, null, null, null, null);
        //mydb.printCursor(results,db.getVersion());
        int sentColumnIndex = results.getColumnIndex(mydb.COL_3);
        int messageColIndex = results.getColumnIndex(mydb.COL_2);
        int idColIndex = results.getColumnIndex(mydb.COL_1);
        if (results.getCount()==0){
            Toast.makeText(GeoHome.this,"Database is empty",Toast.LENGTH_SHORT).show();
        }
        while (results.moveToNext()){
            String sent = results.getString(sentColumnIndex);
            String text = results.getString(messageColIndex);
            long id = results.getLong(idColIndex);
            boolean temp;
            if (sent.equals("1"))
                temp = true;
            else
                temp = false;
            //city = new city(id,text,temp);
            //elements.add(message);
        }
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
                Toast.makeText(this,"https://www.geodatasource.com/web-service",Toast.LENGTH_SHORT).show();
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
    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return shownCities.size();
        }

        @Override
        public Object getItem(int position) {
            return shownCities.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflate = getLayoutInflater();
                View view = inflate.inflate(R.layout.city_list_layout, parent,false);
                TextView text = view.findViewById(R.id.id);
                text.setText((position+1)+"");
                TextView text2 = view.findViewById(R.id.cityName);
                text2.setText(shownCities.get(position).cityName.toString());
                Button favorite = (Button) view.findViewById(R.id.favourite);

                favorite.setOnClickListener( clk -> {
                boolean isInserted = mydb.add(shownCities.get(position).getCountry(),shownCities.get(position).getRegion(),shownCities.get(position).getCityName(),shownCities.get(position).getCurrency(),shownCities.get(position).getLatitude(),shownCities.get(position).getLongitude().toString());
                if (isInserted)
                    Log.i("Saved in database","yes");
                else
                    Log.i("Saved in database","no");
            });
                return view;
            }
        }
    class GeoData  extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            //create a URL object of what server to contact:
            URL url = null;
            try {
                //String tempCountry= null, tempRegion= null, tempCity= null, tempCurrency= null, tempLatitude= null, tempLongitude = null;
                url = new URL(strings[0]);
                HttpURLConnection urlConnection = null;
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                Log.i("check","test");
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");
                city tempCity = null;
                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String parameter = null;
                    switch(eventType){
                        case XmlPullParser.START_TAG:
                        parameter = xpp.getName();
                        if ("response".equals(parameter)){
                            tempCity = new city();
                            shownCities.add(tempCity);
                        } else if (tempCity != null){
                            if ("country".equals(parameter)){
                                tempCity.setCountry(xpp.nextText());
                            } else if ("region".equals(parameter)){
                                tempCity.setRegion(xpp.nextText());
                            } else if ("city".equals(parameter)){
                                tempCity.setCityName(xpp.nextText());
                            } else if ("latitude".equals(parameter)){
                                tempCity.setLatitude(xpp.nextText());
                            } else if ("longitude".equals(parameter)){
                                tempCity.setLongitude(xpp.nextText());
                            } else if ("currency_code".equals(parameter)){
                                tempCity.setCurrency(xpp.nextText());
                            }
                        }
                        break;
                    }
                    eventType = xpp.next();
                }

                Log.i("size",""+shownCities.size());
                for (int i=0;i<shownCities.size();i++){
                    Log.i("city number "+i,shownCities.get(i).toString());
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

        public void setCountry(String country) {
            this.country = country;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(String longitude) {
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
class MyOpener extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "cities.db";
    public static final String TABLE_NAME = "cities_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "country";
    public static final String COL_3 = "region";
    public static final String COL_4 = "cityName";
    public static final String COL_5 = "currency";
    public static final String COL_6 = "latitude";
    public static final String COL_7 = "longitude";
    public MyOpener(Context ct){
        super(ct,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME +
                "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_1 + " integer," +
                " "+ COL_2 +" text," +
                " "+ COL_3 + " text," +
                " "+ COL_4 +" text," +
                " "+ COL_5 +" text," +
                " "+ COL_6 +" text," +
                " "+ COL_7 +" text);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
    public boolean add(String country,String region,String cityName,String currency,String latitude,String longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2,country);
        cv.put(COL_3,region);
        cv.put(COL_4,cityName);
        cv.put(COL_5,currency);
        cv.put(COL_6,latitude);
        cv.put(COL_7,longitude);
        long result = db.insert(TABLE_NAME,null,cv);
        if (result ==-1)
            return false;
return true;
    }
}

