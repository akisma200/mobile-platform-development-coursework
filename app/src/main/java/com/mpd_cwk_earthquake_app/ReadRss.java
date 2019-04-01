package com.mpd_cwk_earthquake_app;
//Andrew Kismali S1709871
import android.content.Context;
import android.os.AsyncTask;

import com.mpd_cwk_earthquake_app.fragments.HomeFragment;
import com.mpd_cwk_earthquake_app.model.Earthquake;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ReadRss extends AsyncTask<Void, Void, Void> {

    // setting variables
    public Context context = null;
    public static ArrayList<Earthquake> earthquakeArrayList = new ArrayList<>();
    private URL url = null;
    private MainActivity mainActivity;

    /**

     * @param context - context of main activity
     */
    public ReadRss(Context context, MainActivity mainActivity) {
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //opens the homefragment once the earthquake list has been parsed
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setMainActivity(mainActivity);
        homeFragment.setEarthquakeArrayList( mainActivity.filteredEarthquakes == null ? getEarthquakes() : mainActivity.filteredEarthquakes);
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commitAllowingStateLoss();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        processFeed();
        return null;
    }

    //main parser method
    //loops through each line in the xml document from the website
    //gets each node and parses it, then creates an earthquake object
    //then adds it to the earthquakes array list
    private void processFeed() {

        try {
            url = new URL("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedUrlConnectionReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            boolean itemFound = false;
            ArrayList<Earthquake> earthquakesArrayList = new ArrayList<>();
            String line;
            while ((line = bufferedUrlConnectionReader.readLine()) != null) {

                if (line.contains("<item>")) {
                    itemFound = true;
                } else if (line.contains("<description>") && itemFound) {
                    Earthquake earthquake = new Earthquake();
                    line = line.replace("<description>", "").replace("</description>", "");
                    earthquake.parseDescription(line);
                    earthquakesArrayList.add(earthquake);
                    itemFound = false;

                }

            }
            this.earthquakeArrayList = earthquakesArrayList;
            bufferedUrlConnectionReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //method to get the earthquakes array
    public ArrayList<Earthquake> getEarthquakes() {
        return earthquakeArrayList;
    }

}