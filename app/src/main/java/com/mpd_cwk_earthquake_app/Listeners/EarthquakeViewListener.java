package com.mpd_cwk_earthquake_app.Listeners;
//Andrew Kismali S1709871
import android.view.View;
import android.widget.AdapterView;

import com.mpd_cwk_earthquake_app.MainActivity;
import com.mpd_cwk_earthquake_app.R;
import com.mpd_cwk_earthquake_app.fragments.DetailFragment;
import com.mpd_cwk_earthquake_app.model.Earthquake;

import java.util.ArrayList;

public class EarthquakeViewListener implements android.widget.AdapterView.OnItemClickListener {
    //creating private variables
    private ArrayList<Earthquake> earthquakes;
    private MainActivity activity;

    //setting private variables to the constructor
    public EarthquakeViewListener(ArrayList<Earthquake> earthquakes, MainActivity mainActivity){
        this.earthquakes = earthquakes;
        this.activity = mainActivity;
    }

    //onclick listener that opens the detail fragment view of the earthquake that was clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setEarthquake(earthquakes.get(position));
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailFragment).commit();
    }
}
