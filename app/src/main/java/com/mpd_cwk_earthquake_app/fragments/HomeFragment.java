package com.mpd_cwk_earthquake_app.fragments;
//Andrew Kismali S1709871
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mpd_cwk_earthquake_app.MainActivity;
import com.mpd_cwk_earthquake_app.R;
import com.mpd_cwk_earthquake_app.ReadRss;
import com.mpd_cwk_earthquake_app.model.Earthquake;
import java.util.ArrayList;


public class HomeFragment extends Fragment implements OnMapReadyCallback
{
    //creating private variables

    private ArrayList<Earthquake> earthquakeArrayList;
    private MainActivity mainActivity;

    // sets main activity
    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    //setting earthquake array list for maps
    public void setEarthquakeArrayList(ArrayList<Earthquake> earthquakeArrayList) {
        this.earthquakeArrayList = earthquakeArrayList;
    }

    //inflates view for google maps fragment
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);


        return v;
    }

    //checks if the earthquake array list in the class is equal to null
    //if it is it will grab the one from main activity
    //then it will loop through each earthquake in the array list and put a marker of it on the map
    //it also sets the marker click listener
    //the camera is also positioned above the uk
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        MarkerOptions option = new MarkerOptions();
        if(earthquakeArrayList == null){
            earthquakeArrayList = mainActivity.readRss.getEarthquakes();
        }

        for (Earthquake eq : earthquakeArrayList) {
          LatLng latLng = new LatLng(eq.getGeoLat(),eq.getGeoLong());
            option.position(latLng).title(eq.getDate()).snippet(eq.getTime());
            googleMap.addMarker(option);
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    DetailFragment detailFragment = new DetailFragment();
                    Earthquake earthquake = earthquakeArrayList.get(Integer.valueOf(marker.getId().split("m")[1]));
                    detailFragment.setEarthquake(earthquake);
                    mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailFragment).commit();
                    return false;
                }
            });
            LatLng latLng1 = new LatLng(54.50,-2.48);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 4));

        }

    }
}
