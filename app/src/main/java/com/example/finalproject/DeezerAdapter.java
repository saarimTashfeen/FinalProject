package com.example.finalproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Object to act as a bridge between AdapterView and the data for that view
 * @author Austin
 * @version 1.0
 */

public class DeezerAdapter extends BaseAdapter {

    /** A list of songs by the artist */
    private List<DeezerArtist> list;
    /** The current state of the application/object */
    private Context context;
    /** Variable for instantiating a layout XML file into its corresponding view */
    private LayoutInflater inflater;

    /**
     * Turns the data received into a list and inflates it to be viewable
     * @param list The list of songs retrieved
     * @param context The state of the application
     */

    public DeezerAdapter(List<DeezerArtist> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * How many items are represented by this adapter
     * @return Count of items
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Get the data item associated with the specified position in the data set
     * @param position Position of the item whose data is wanted within the adapter's dataset
     * @return The data at the specified position
     */

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * Returns the number of types of Views that will be created by getView(int, View, ViewGroup)
     * @return The number of types of Views that will be created by this adapter
     */

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    /**
     * Get the row id associated with the specified position in the list
     * @param position The position of the item within the adapter's data whose row id is wanted
     * @return The id of the item at the specified position
     */

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set
     * @param position The position of the item within the adapter's data set of the item whose view is wanted
     * @param convertView The old view to reuse, if possible
     * @param parent The parent that this view will eventually be attached to
     * @return A view corresponding to the data at the specified position
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TextView songText;
        //inflate the view
        view = inflater.inflate(R.layout.activity_deezer_list, null);

        songText = (TextView)view.findViewById(R.id.textViewSong);
        songText.setTextColor(Color.parseColor("#800080"));
        songText.setText(list.get(position).getTitle());
        songText.setText(list.get(position).getTitle());
        return view;
    }
}
