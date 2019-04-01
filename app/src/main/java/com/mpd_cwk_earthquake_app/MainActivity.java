package com.mpd_cwk_earthquake_app;
//Andrew Kismali S1709871
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.mpd_cwk_earthquake_app.fragments.FiltersFragment;
import com.mpd_cwk_earthquake_app.fragments.HomeFragment;
import com.mpd_cwk_earthquake_app.fragments.ListFragment;
import com.mpd_cwk_earthquake_app.model.Earthquake;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{
    //creating variables
    public static ReadRss readRss;
    public static ArrayList<Earthquake> filteredEarthquakes = null;
    public Context context = this;
    private MainActivity mainActivity = this;
    public static Earthquake detailEarthquake;


    //oncreate method for application, runs the application
    //checks if network connection is available
    //if not requests user connects then restarts the app
    //schedules the app to refresh according to the number of ms set (in this case every 20 minutes)
    //sets default fragment to home fragment (maps)

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(isNetworkAvailable(context)){
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            readRss = new ReadRss(mainActivity,mainActivity);
                            readRss.execute();

                            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
                            bottomNav.setOnNavigationItemSelectedListener(navListener);
                        }
                    });
                }
            },0,1200000);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("No Connection!");
            builder.setMessage("Connection required. Please connect to internet before opening application")
                    .setCancelable(false)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }


        }

        //Listener for bottom navigation menu, when a menu item is clicked on this tells the app what to do for that menu option
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
                {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_home:
                            HomeFragment homeFragment = new HomeFragment();
                            homeFragment.setMainActivity(mainActivity);
                            homeFragment.setEarthquakeArrayList( filteredEarthquakes == null ? readRss.getEarthquakes() : filteredEarthquakes);
                            selectedFragment = homeFragment;
                            break;
                        case R.id.nav_list:
                            ListFragment listFragment = new ListFragment();
                            listFragment.setMainActivity(mainActivity);
                            selectedFragment = listFragment;
                            break;
                        case R.id.nav_filters:
                            FiltersFragment filtersFragment = new FiltersFragment();
                            filtersFragment.setReadRss(readRss);
                            filtersFragment.setContext(context);
                            filtersFragment.setMainActivity(mainActivity);
                            selectedFragment = filtersFragment;
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    //sets the filtered earthquakes array list to the filtered earthquakes, after a filter has been used this passes in the earthquakes that matched the query/condition
    public static void setFilteredEarthquakes(ArrayList<Earthquake> filteredEarthquakes) {
        MainActivity.filteredEarthquakes = filteredEarthquakes;
    }

    //checks for internet connection
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
