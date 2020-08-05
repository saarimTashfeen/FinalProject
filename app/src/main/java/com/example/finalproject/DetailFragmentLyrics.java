package com.example.finalproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragmentLyrics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragmentLyrics extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DetailFragmentLyrics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragmentLyrics.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragmentLyrics newInstance(String param1, String param2) {
        DetailFragmentLyrics fragment = new DetailFragmentLyrics();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Creating and inflating the fragment view
        View result = inflater.inflate(R.layout.fragment_detail_lyrics, container, false);

        //Getting data from empty activity
        Bundle dataFromActivity = getArguments();

        //Getting element id's
        TextView artistNameFrag = (TextView) result.findViewById(R.id.artistNameFrag);
        TextView songNameFrag = (TextView) result.findViewById(R.id.songNameFrag);
        TextView idTagFrag = (TextView) result.findViewById(R.id.idFrag);
        TextView lyricsFrag = (TextView) result.findViewById(R.id.lyricsFrag);


        //Setting textViews
        artistNameFrag.setText("Artist: " + dataFromActivity.getString(LyricsScreen.artistTag));
        songNameFrag.setText("Song: " + dataFromActivity.getString(LyricsScreen.songTag));
        idTagFrag.setText("ID: " + String.valueOf(dataFromActivity.getLong(LyricsScreen.idTag)));
        lyricsFrag.setText(dataFromActivity.getString(LyricsScreen.lyricsTag));




        // Returning view
        return result;
    }
}