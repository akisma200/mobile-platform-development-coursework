package com.mpd_cwk_earthquake_app.Adapters;
//Andrew Kismali S1709871

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mpd_cwk_earthquake_app.R;
import com.mpd_cwk_earthquake_app.model.Earthquake;

import java.util.ArrayList;

public class EarthquakeListAdapter extends BaseAdapter {
    //Creating private variables
    private Context context;
    private ArrayList<Earthquake> earthquakes;

    public EarthquakeListAdapter(Context context, ArrayList<Earthquake> earthquakes) {
        this.context = context;
        this.earthquakes = earthquakes;
    }

    //returns list of eqs
    @Override
    public int getCount() {

        return earthquakes.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    //sets custom layout for each row  and sets the text
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = inflater.inflate(R.layout.earthquake_list_row,null);

        TextView magnitude = convertView.findViewById(R.id.list_mag);
        TextView location = convertView.findViewById(R.id.list_loc);
        TextView date = convertView.findViewById(R.id.list_date);

        magnitude.setText(earthquakes.get(position).getMagnitude());
        location.setText(earthquakes.get(position).getLocation());
        date.setText(earthquakes.get(position).getDate());

        //setting the text to different colours for different levels of magnitude, anything less than 1 = green, 1-2 = orange, 2 and above = red
        double value  = Double.parseDouble(magnitude.getText().toString());
        if(value <= 1.0) {
            setColour(new TextView[]{magnitude,location,date}, "#4be802");
        }else if(value > 1.0 && value <= 2.0) {
            setColour(new TextView[]{magnitude,location,date}, "#e5b422");
        }else if(value > 2.0 && value <= 10.0) {
            setColour(new TextView[]{magnitude,location,date}, "#f72b02");
        }

        return convertView;
    }

    //method to set colour
    private void setColour(TextView[] textViews, String colour) {
        for(TextView textView : textViews) {
            textView.setTextColor(Color.parseColor(colour));
        }
    }
}
