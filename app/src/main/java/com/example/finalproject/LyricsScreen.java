package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class LyricsScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

  //  public static String artistTag;
    private String lyricsDisplay;
    private String artistName;
    private String songName;
    private String googleUrl = "https://www.google.com/search?q=";
    ArrayList<songs> songsList = new ArrayList<>();
    SQLiteDatabase db;
    MyOwnAdapter myAdapter;
    public static String lyricsTag = "LYRICS";
    public static String songTag = "SONG";
    public static String artistTag = "ARTIST";
    public static String idTag = "IDTAG";
    public boolean longClick = false;
  //  private Object View;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_screen);

        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ListView myList = (ListView) findViewById(R.id.listView);

        loadDataFromDatabase();
        myAdapter = new MyOwnAdapter();
        myList.setAdapter(myAdapter);

        final TextView displayLyrics = (TextView) findViewById(R.id.lyricsDisplay);
        TextView artistDisplay = (TextView) findViewById(R.id.artistNameDisplay);
        TextView songDisplay = (TextView) findViewById(R.id.songNameDisplay);

        Intent getIntent = getIntent();

        lyricsDisplay = getIntent.getStringExtra("lyricsPlaceHolder");
        artistName = getIntent.getStringExtra("artistPlaceHolder");
        songName = getIntent.getStringExtra("songPlaceHolder");


        displayLyrics.setText(lyricsDisplay);
        artistDisplay.setText(artistName);
        songDisplay.setText(songName);

        Button googleSearch = (Button) findViewById(R.id.googleSearch);
        Button saveToDb = (Button) findViewById(R.id.saveToDb);

        googleSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri googlePage = Uri.parse(googleUrl+artistName+" "+songName);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, googlePage);
                startActivity(webIntent);


            }
        });

        saveToDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContentValues newRowValues = new ContentValues();
                 newRowValues.put(myOpener.COL_ARTIST, artistName);
                 newRowValues.put(myOpener.COL_SONG, songName);
                 newRowValues.put(myOpener.COL_LYRICS, lyricsDisplay);
                long newId = db.insert(myOpener.TABLE_NAME, null, newRowValues);

                songs newSong = new songs(artistName, songName, lyricsDisplay, newId);
                songsList.add(newSong);
                myAdapter.notifyDataSetChanged();


            }
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                longClick = true;


                AlertDialog.Builder builder = new AlertDialog.Builder(LyricsScreen.this)
                        .setTitle("Do you want to delete this item?")
                        .setMessage("The selected row is " + position + " The data base id is " + myAdapter.getItemId(position))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {



                                db.delete(myOpener.TABLE_NAME, myOpener.COL_ID + "= ?", new String[] {Long.toString(myAdapter.getItemId(position))});
                                songsList.remove(position);
                                myAdapter.notifyDataSetChanged();
                                Snackbar.make(myList, "Item Deleted, id: " + id, Snackbar.LENGTH_LONG).show();

                                longClick = false;



                            }
                        });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Snackbar.make(myList, "Item Not Deleted", Snackbar.LENGTH_LONG).show();

                        longClick = false;



                    }
                });
                builder.show();

              // int duration = 100;







                return false;
            }
        });

        myList.setOnItemClickListener((list, item, position, id ) -> {

            if(longClick == false) {


                songs thisRow = myAdapter.getItem(position);

                Bundle dataToPass = new Bundle();
                dataToPass.putString(artistTag, thisRow.getArtist());
                dataToPass.putString(songTag, thisRow.getSong());
                dataToPass.putString(lyricsTag, thisRow.getLyrics());
                dataToPass.putLong(idTag, thisRow.getId());
                // dataToPass.putString(messageText, messages.get(position)  );
                //dataToPass.putInt(idText, (int) aListAdapter.getItemId(position));
                //dataToPass.putInt(isSent, messagesType.get(position));
                //dataToPass.putBoolean(layoutStateData, layoutState);

                // songs thisRow = myAdapter.getItem(position);


                Intent nextActivity = new Intent(LyricsScreen.this, EmptyClassLyrics.class);
                nextActivity.putExtras(dataToPass);
                startActivity(nextActivity);
            }







        });






    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.songSearch){

            Intent deezerIntent = new Intent(LyricsScreen.this, DeezerSearch.class);
            startActivity(deezerIntent);

            Toast.makeText(this, "Test Toast", Toast.LENGTH_SHORT).show();

        } else if(id == R.id.geoHome){

            Intent geoIntent = new Intent(LyricsScreen.this, GeoHome.class);
            startActivity(geoIntent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    class songs{


        protected String artist, song, lyrics;
        protected long id;

        public songs(String artist, String song, String lyrics, long id){

            this.artist = artist;
            this.song = song;
            this.lyrics = lyrics;
            this.id = id;

        }

        public String getArtist(){

            return artist;

        }

        public String getSong(){

            return song;

        }

        public String getLyrics(){

            return lyrics;

        }

        public long getId(){

            return id;

        }

    }

    public class myOpener extends SQLiteOpenHelper{

        protected final static String DATABASE_NAME = "SongsDB";
        protected final static int VERSION_NUM = 1;
        public final static String TABLE_NAME = "SONGS";
        public final static String COL_ARTIST = "ARTIST";
        public final static String COL_SONG = "SONG";
        public final static String COL_ID = "_id";
        public final static String COL_LYRICS = "lyrics";


        public myOpener(Context ctx){

            super(ctx,DATABASE_NAME, null, VERSION_NUM );


        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COL_ARTIST + " text,"
                    + COL_LYRICS + " text, "
                    + COL_SONG  + " text);");  // add or remove columns

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

            //Create the new table:
            onCreate(db);

        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {   //Drop the old table:
            db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

            //Create the new table:
            onCreate(db);
        }
    }

    protected class MyOwnAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            return songsList.size();
        }

        @Override
        public songs getItem(int position) {
            return songsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View newView = getLayoutInflater().inflate(R.layout.songs_layout, parent, false );

            songs thisRow = getItem(position);

            TextView artistNameList = newView.findViewById(R.id.artistNameList);
            TextView songNameList = newView.findViewById(R.id.songNameList);
            TextView idList = newView.findViewById(R.id.listID);

//            Log.d("TextViewTest", artistNameList.getText().toString());

            artistNameList.setText(thisRow.getArtist() + "  ");
            songNameList.setText("  " + thisRow.getSong());
            idList.setText("    ID: " + thisRow.getId());


            return newView;
        }
    }

    private void loadDataFromDatabase(){

        myOpener dbOpener = new myOpener(this);
        db = dbOpener.getWritableDatabase();

        String [] columns = {myOpener.COL_ARTIST, myOpener.COL_SONG, myOpener.COL_LYRICS, myOpener.COL_ID};
        Cursor resultsLyrics = db.query(false, myOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        int artistColumnIndex = resultsLyrics.getColumnIndex(myOpener.COL_ARTIST);
        int songColumnIndex = resultsLyrics.getColumnIndex(myOpener.COL_SONG);
        int lyricsColumnIndex = resultsLyrics.getColumnIndex(myOpener.COL_LYRICS);
        int idColumnIndex = resultsLyrics.getColumnIndex(myOpener.COL_ID);


        while(resultsLyrics.moveToNext()){

            String artistName = resultsLyrics.getString(artistColumnIndex);
            String songName = resultsLyrics.getString(songColumnIndex);
            String songLyrics = resultsLyrics.getString(lyricsColumnIndex);
            long id = resultsLyrics.getLong(idColumnIndex);

            songsList.add(new songs(artistName, songName, songLyrics, id));

        }


    }

    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.main_lyrics_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
               // message = "You clicked item 1";


                Intent deezerIntent = new Intent(LyricsScreen.this, DeezerSearch.class);
                startActivity(deezerIntent);

                break;
            case R.id.item2:
              Intent geoIntent = new Intent(LyricsScreen.this, GeoHome.class);
              startActivity(geoIntent);
                break;
            case R.id.helpItem:
                //message = "You clicked on mail";

                AlertDialog.Builder builder = new AlertDialog.Builder(LyricsScreen.this)
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





