package com.mpd_cwk_earthquake_app.fragments;
//Andrew Kismali S1709871
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mpd_cwk_earthquake_app.MainActivity;
import com.mpd_cwk_earthquake_app.R;
import com.mpd_cwk_earthquake_app.model.Earthquake;


public class DetailFragment extends Fragment
{
    //Creating variables
    private Earthquake earthquake = null;

    public void setEarthquake( Earthquake earthquake) {
        this.earthquake = earthquake;
    }

    //this expands view for the details of an earthquake
    @Nullable
    @Override
     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.earthquake_details, container, false);

        //creates map view
        final MapView mapDetailView = view.findViewById(R.id.mapDetailView);

        //catching a rotation change bug, program crashes when rotating from detail view
        if(earthquake != null){

            mapDetailView.onCreate(savedInstanceState);
            mapDetailView.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    LatLng coordinates = new LatLng(DetailFragment.this.earthquake.getGeoLat(), DetailFragment.this.earthquake.getGeoLong());
                    googleMap.addMarker(new MarkerOptions().position(coordinates));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 12));
                    mapDetailView.onResume();
                }
            });

            //getting text views for the details
            TextView location = view.findViewById(R.id.locTxt);
            TextView coordinate = view.findViewById(R.id.cordTxt);
            TextView time = view.findViewById(R.id.timeTxt);
            TextView date = view.findViewById(R.id.dateTxt);
            TextView magnitude = view.findViewById(R.id.magTxt);
            TextView depth = view.findViewById(R.id.depthTxt);

            //setting them here
            location.setText(this.earthquake.getLocation());
            coordinate.setText(this.earthquake.getGeoLat()+"," + this.earthquake.getGeoLong());
            time.setText(this.earthquake.getTime());
            date.setText(this.earthquake.getDate());
            magnitude.setText(this.earthquake.getMagnitude());
            depth.setText(this.earthquake.getDepth());
        }




        return view;
    }

}
