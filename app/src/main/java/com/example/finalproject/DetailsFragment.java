package com.example.finalproject;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


public class DetailsFragment extends Fragment {
    private Bundle dataFromActivity;
    AppCompatActivity parentActivity;
    MyOpener temp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dataFromActivity = getArguments();
        View result =  inflater.inflate(R.layout.fragment_detail, container, false);

        TextView countryText = (TextView) result.findViewById(R.id.fragCountry);
        TextView regionText = (TextView) result.findViewById(R.id.fragRegion);
        TextView cityText = (TextView) result.findViewById(R.id.fragCity);
        TextView currencyText = (TextView) result.findViewById(R.id.fragCurrency);
        TextView latitudeText = (TextView) result.findViewById(R.id.fragLatitude);
        TextView longitudeText = (TextView) result.findViewById(R.id.fragLongitude);

        countryText.setText(dataFromActivity.getString(GeoHome.ITEM_COUNTRY));
        regionText.setText(dataFromActivity.getString(GeoHome.ITEM_REGION));
        cityText.setText(dataFromActivity.getString(GeoHome.ITEM_CITY));
        currencyText.setText(dataFromActivity.getString(GeoHome.ITEM_CURRENCY));
        latitudeText.setText(dataFromActivity.getString(GeoHome.ITEM_LATITUDE));
        longitudeText.setText(dataFromActivity.getString(GeoHome.ITEM_LONGITUDE));


        Button finishButton = (Button)result.findViewById(R.id.hide);
        finishButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}