package com.mpd_cwk_earthquake_app.fragments;
//Andrew Kismali S1709871
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.mpd_cwk_earthquake_app.Adapters.EarthquakeListAdapter;
import com.mpd_cwk_earthquake_app.Listeners.EarthquakeViewListener;
import com.mpd_cwk_earthquake_app.MainActivity;
import com.mpd_cwk_earthquake_app.R;


public class ListFragment extends Fragment
{
    //creating private variables
    private MainActivity mainActivity;

    //sets the main activity
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    //this gets list fragment layout
    //grabs the list inside the layout
    // creates an earthquake list adapter and passes in the application context and list of earthquakes
    //then adds a click listener to the list view
    @Nullable
    @Override
     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ListView list = view.findViewById(R.id.eqlist);
        EarthquakeListAdapter listAdapter = new EarthquakeListAdapter(mainActivity.readRss.context,mainActivity.readRss.getEarthquakes());
        list.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        list.setOnItemClickListener(new EarthquakeViewListener(MainActivity.readRss.getEarthquakes(),mainActivity));

        return view;
    }
}
