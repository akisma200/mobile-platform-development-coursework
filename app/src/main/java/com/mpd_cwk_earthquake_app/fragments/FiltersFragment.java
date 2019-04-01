package com.mpd_cwk_earthquake_app.fragments;
//Andrew Kismali S1709871
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.mpd_cwk_earthquake_app.Adapters.FilterListAdapter;
import com.mpd_cwk_earthquake_app.Listeners.FilterOnClickListener;
import com.mpd_cwk_earthquake_app.MainActivity;
import com.mpd_cwk_earthquake_app.R;
import com.mpd_cwk_earthquake_app.ReadRss;



public class FiltersFragment extends Fragment
{
    //creating private variables
    private ReadRss readRss;
    private Context context;
    private MainActivity mainActivity;

    //setting the context of the application
    public void setContext(Context context) {
        this.context = context;
    }

    //setting the ReadRss parser
    public void setReadRss( ReadRss readRss) {
        this.readRss = readRss;
    }

    //setting the main activity
    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    //inflates the custom layout onto the list view
    // then creates a list adapter with a custom onclicklistener
    // then sets the adapter and notifies the adapter of change
    @Nullable
    @Override
     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_filters, container, false);

        ListView list = v.findViewById(R.id.filters_list);

        FilterListAdapter adapter = new FilterListAdapter(new String[]{"Show All Earthquakes","Date","Location","Most Northern","Most Southern",
                                                                        "Most Eastern","Most Western","Highest Magnitude",
                                                                        "Deepest Earthquake","Shallowest Earthquake"},v.getContext());

        list.setOnItemClickListener(new FilterOnClickListener(mainActivity,context,readRss));
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return v;
    }
}
