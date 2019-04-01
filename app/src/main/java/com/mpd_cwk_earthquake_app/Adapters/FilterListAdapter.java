package com.mpd_cwk_earthquake_app.Adapters;
//Andrew Kismali S1709871
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mpd_cwk_earthquake_app.R;


public class FilterListAdapter extends BaseAdapter {

    private String[] filters;
    private Context context;

    /**
     *
     * @param filters gets a list of filters
     * @param context context of app
     */
    public FilterListAdapter(String[] filters, Context context){
        this.context = context;
        this.filters = filters;

    }
    // gets number of filters
    @Override
    public int getCount() {
        return filters.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    //sets custom layout for for the filters and sets the text to what the filter is
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        convertView = inflater.inflate(R.layout.filteritems,null);
        TextView textView = convertView.findViewById(R.id.filter);

        textView.setText(filters[position]);

        return convertView;
    }
}
