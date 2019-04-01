package com.mpd_cwk_earthquake_app.Listeners;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.mpd_cwk_earthquake_app.MainActivity;
import com.mpd_cwk_earthquake_app.R;
import com.mpd_cwk_earthquake_app.ReadRss;
import com.mpd_cwk_earthquake_app.fragments.HomeFragment;
import com.mpd_cwk_earthquake_app.model.Earthquake;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FilterOnClickListener implements android.widget.AdapterView.OnItemClickListener
{

    //creating a list of filters to choose from
    public String[] filters = new String[]{"Show All Earthquakes","Date", "Location","Most Northern", "Most Southern", "Most Eastern", "Most Western", "Highest Magnitude", "Deepest Earthquake", "Shallowest Earthquake"};
    private Context context;
    private ReadRss readRss;
    private MainActivity activity;

    //constructor to set variables
    public FilterOnClickListener(MainActivity activity, Context context, ReadRss readRss)
    {
        this.context = context;
        this.readRss = readRss;
        this.activity = activity;
    }

    //switch case to launch a filter that is selected, by default show all earthquakes is selected
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {

        switch (getFilter(position))
        {

            case "Show All Earthquakes":
                filterToGetAllMarkers();
                break;

            case "Date":
                filterByDate();
                break;

            case "Location":
                filterByLocation();
                break;

            case "Most Northern":
                filterByMostNorthern();
                break;

            case "Most Southern":
                filterByMostSouthern();
                break;

            case "Most Eastern":
                filterByMostEastern();
                break;

            case "Most Western":
                filterByMostWestern();
                break;

            case "Highest Magnitude":
                filterByHighestMagnitude();
                break;

            case "Deepest Earthquake":
                filterByDeepestDepth();
                break;

            case "Shallowest Earthquake":
                filterByShallowestDepth();
                break;

        }

    }

    //this is the default filter and shows all earthquakes and then returns to the home fragment
    private void filterToGetAllMarkers()
    {

        activity.setFilteredEarthquakes(readRss.getEarthquakes());
        returnToMap(readRss.getEarthquakes());

    }
    //gets all earthquakes between two dates
    public void filterByDate(){

        //array list for the dates
        final String[] dates = new String[]{null,null};

        // brings up a calender
        final Calendar newCalendar = Calendar.getInstance();

        //calender date picker
        DatePickerDialog startDatePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                final Date startDate = calendar.getTime();
                String start = simpleDateFormat.format(startDate);

                dates[0] = start;

                DatePickerDialog endDatePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
                        final Date startDate = calendar.getTime();
                        String end = simpleDateFormat.format(startDate);

                        dates[1] = end;

                        try{
                            ArrayList<Earthquake> earthquakes = new ArrayList<>();
                            for(Earthquake earthquake : readRss.getEarthquakes()){
                                Date dateStart = new SimpleDateFormat("dd MMM yyyy").parse(dates[0]);
                                Date dateEnd = new SimpleDateFormat("dd MMM yyyy").parse(dates[1]);
                                Date earthquakeDate = new SimpleDateFormat("dd MMM yyyy").parse(earthquake.getDate().substring(4,earthquake.getDate().length()));
                                //checks if the earthquake is after the start date and before the end date
                                if(earthquakeDate.after(dateStart) && earthquakeDate.before(dateEnd)){
                                    earthquakes.add(earthquake);
                                }
                            }

                            if(earthquakes.size() != 0){
                                activity.setFilteredEarthquakes(earthquakes);
                                returnToMap(earthquakes);
                            }else{
                                displayAlert("No Matches","It appears there are no earthquakes to display for this period of time");
                            }
                        }catch(ParseException e){
                            e.printStackTrace();
                        }


                        //sets the max date to the current date so dates in the future cannot be selected
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                endDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                endDatePickerDialog.show();
                //same thing for the second datepicker
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        startDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        startDatePickerDialog.show();

    }

    //Opens dialog box for the user to select by earthquake date and displays the earthquake with the highest magnitude for that date
    private void filterByHighestMagnitude()
    {

        //creates a new arraylist that will store dates of earthquakes
        final ArrayList<String> dates = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //loop through each earthquake from the feed and get dates and adds them to array list if the array list doesn't contain that date
        for (Earthquake eq: readRss.getEarthquakes())
        {
            if (!dates.contains(eq.getDate())) dates.add(eq.getDate());
        }

        //this brings up a spinner with the dates to choose from
        View v = inflater.inflate(R.layout.dialoguespinner, null);
        final Spinner dateSpinner = v.findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dates.toArray(new String[dates.size()]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(spinnerArrayAdapter);

        //creates an alert dialog with the list of dates
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // this does a comparison with the date selected
        builder.setTitle("Date Picker");
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ArrayList<Earthquake> earthquakes = new ArrayList<>();
                Earthquake Magnitude = null;
                double maxMagnitude = -10.00;

                //comparison here calculation = each time the loop finds a magnitude with a higher number, it sets that to the new maximum magnitude
                for (Earthquake earthquake : readRss.getEarthquakes())
                {
                    double magnitude = Double.parseDouble(earthquake.getMagnitude());
                    if (earthquake.getDate().equals(dateSpinner.getSelectedItem().toString()) && magnitude > maxMagnitude)
                    {
                        maxMagnitude = magnitude;
                        Magnitude = earthquake;
                    }
                }
                //adds the highest magnitude earthquake to the earthquakes array list and returns to home fragment
                earthquakes.add(Magnitude);
                if (earthquakes.size() != 0)
                {
                    activity.setFilteredEarthquakes(earthquakes);
                    returnToMap(earthquakes);
                } else
                {
                    // error handling if for any reason a date malfunctions, but this shouldn't happen
                    displayAlert("No Matches", "It appears there are no earthquakes to display");
                }

            }
        });

        //display dialog box
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    //Opens dialog box for the user to select by earthquake date and displays the earthquake with the deepest depth for that date
    private void filterByDeepestDepth()
    {
        //same pattern as filterByHighestMagnitude()
        final ArrayList<String> dates = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Earthquake eq: readRss.getEarthquakes())
        {
            if (!dates.contains(eq.getDate())) dates.add(eq.getDate());
        }
        View v = inflater.inflate(R.layout.dialoguespinner, null);
        final Spinner dateSpinner = v.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dates.toArray(new String[dates.size()]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(spinnerArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Date Picker");
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ArrayList<Earthquake> earthquakes = new ArrayList<>();
                Earthquake Deepest = null;
                double deepest = 0;

                //comparison checks for the deepest depth for the date selected. calculation = checks if the depth is greater than the deepest depth, if it is higher then it makes that the new deepest depth
                for (Earthquake earthquake : readRss.getEarthquakes())
                {
                    double depth = Double.parseDouble(earthquake.getDepth().split("\\s+")[0]);
                    if (earthquake.getDate().equals(dateSpinner.getSelectedItem().toString()) && depth > deepest)
                    {
                        deepest = depth;
                        Deepest = earthquake;
                    }
                }

                earthquakes.add(Deepest);

                if (earthquakes.size() != 0)
                {
                    activity.setFilteredEarthquakes(earthquakes);
                    returnToMap(earthquakes);
                } else
                {
                    displayAlert("No Matches", "It appears there are no earthquakes to display");
                }
            }
        });

        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    //Opens dialog box for the user to select by earthquake date and displays the earthquake with the shallowest depth for that date
    private void filterByShallowestDepth()
    {
        //same pattern as filterByHighestMagnitude()
        final ArrayList<String> dates = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Earthquake eq: readRss.getEarthquakes())
        {
            if (!dates.contains(eq.getDate())) dates.add(eq.getDate());
        }

        View v = inflater.inflate(R.layout.dialoguespinner, null);

        final Spinner dateSpinner = v.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dates.toArray(new String[dates.size()]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(spinnerArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Date Picker");
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ArrayList<Earthquake> earthquakes = new ArrayList<>();
                Earthquake Shallowest = null;
                double shallowest = 100;
                //comparison checks for the shallowest depth for the date selected.  calculation = checks if the depth is less than the shallowest depth, if it is lower then it makes that the new shallowest depth
                for (Earthquake earthquake : readRss.getEarthquakes())
                {
                    double depth = Double.parseDouble(earthquake.getDepth().split("\\s+")[0]);
                    if (earthquake.getDate().equals(dateSpinner.getSelectedItem().toString()) && depth < shallowest)
                    {
                        shallowest = depth;
                        Shallowest = earthquake;
                    }
                }

                earthquakes.add(Shallowest);
                if (earthquakes.size() != 0)
                {
                    activity.setFilteredEarthquakes(earthquakes);
                    returnToMap(earthquakes);
                } else
                {
                    displayAlert("No Matches", "It appears there are no earthquakes to display");
                }
            }
        });

        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    //Opens dialog box for the user to select by earthquake date and displays the earthquake with the MostNorthern for that date
    private void filterByMostNorthern()
    {
        //same pattern as filterByHighestMagnitude()
        final ArrayList<String> dates = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Earthquake eq: readRss.getEarthquakes())
        {
            if (!dates.contains(eq.getDate())) dates.add(eq.getDate());
        }
        View v = inflater.inflate(R.layout.dialoguespinner, null);
        final Spinner dateSpinner = v.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dates.toArray(new String[dates.size()]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(spinnerArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Date Picker");
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ArrayList<Earthquake> earthquakes = new ArrayList<>();
                Earthquake Northern = null;
                double maxLatitude = -90;
                //comparison checks for the shallowest depth for the date selected
                for (Earthquake earthquake : readRss.getEarthquakes())
                {
                    double lat = earthquake.getGeoLat();
                    //checks if the date is the same and then performs the calculation below: if latitude is greater then the max latitude then that latitude gets set to the new max latitude
                    if (earthquake.getDate().equals(dateSpinner.getSelectedItem().toString()) && lat > maxLatitude)
                    {
                        maxLatitude = lat;
                        Northern = earthquake;
                    }
                }
                earthquakes.add(Northern);
                if (earthquakes.size() != 0)
                {
                    activity.setFilteredEarthquakes(earthquakes);
                    returnToMap(earthquakes);
                } else
                {
                    displayAlert("No Matches", "It appears there are no earthquakes to display");
                }
            }
        });
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    //Opens dialog box for the user to select by earthquake date and displays the earthquake with the MostEastern for that date
    private void filterByMostEastern()
    {
        //same pattern as filterByHighestMagnitude()
        final ArrayList<String> dates = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Earthquake eq: readRss.getEarthquakes())
        {
            if (!dates.contains(eq.getDate())) dates.add(eq.getDate());
        }
        View v = inflater.inflate(R.layout.dialoguespinner, null);
        final Spinner dateSpinner = v.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dates.toArray(new String[dates.size()]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(spinnerArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Date Picker");
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ArrayList<Earthquake> earthquakes = new ArrayList<>();
                Earthquake Eastern = null;
                double maxLong = -180;
                for (Earthquake earthquake : readRss.getEarthquakes())
                {
                    double lon = earthquake.getGeoLong();
                    //checks if the date is the same and then performs the calculation below: if longitude is greater then the maximum longitude then that longitude gets set to the new maximum longitude
                    if (earthquake.getDate().equals(dateSpinner.getSelectedItem().toString()) && lon > maxLong)
                    {
                        maxLong = lon;
                        Eastern = earthquake;
                    }
                }
                earthquakes.add(Eastern);
                if (earthquakes.size() != 0)
                {
                    activity.setFilteredEarthquakes(earthquakes);
                    returnToMap(earthquakes);
                } else
                {
                    displayAlert("No Matches", "It appears there are no earthquakes to display");
                }
            }
        });
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Opens dialog box for the user to select by earthquake date and displays the earthquake with the MostWestern for that date
    private void filterByMostWestern()
    {
        //same pattern as filterByHighestMagnitude()
        final ArrayList<String> dates = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Earthquake eq: readRss.getEarthquakes())
        {
            if (!dates.contains(eq.getDate())) dates.add(eq.getDate());
        }
        View v = inflater.inflate(R.layout.dialoguespinner, null);
        final Spinner dateSpinner = v.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dates.toArray(new String[dates.size()]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(spinnerArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Date Picker");
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ArrayList<Earthquake> earthquakes = new ArrayList<>();
                Earthquake Western = null;
                double minLong = 180;
                //checks if the date is the same and then performs the calculation below: if longitude is greater than the minimum longitude then that longitude gets set to the new min longitude
                for (Earthquake earthquake : readRss.getEarthquakes())
                {
                    double lon = earthquake.getGeoLong();
                    if (earthquake.getDate().equals(dateSpinner.getSelectedItem().toString()) && lon < minLong)
                    {
                        minLong = lon;
                        Western = earthquake;
                    }
                }
                earthquakes.add(Western);
                if (earthquakes.size() != 0)
                {
                    activity.setFilteredEarthquakes(earthquakes);
                    returnToMap(earthquakes);
                } else
                {
                    displayAlert("No Matches", "It appears there are no earthquakes to display");
                }
            }
        });
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Opens dialog box for the user to select by earthquake date and displays the earthquake with the MostSouthern for that date
    private void filterByMostSouthern()
    {
        //same pattern as filterByHighestMagnitude()
        final ArrayList<String> dates = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Earthquake eq: readRss.getEarthquakes())
        {
            if (!dates.contains(eq.getDate())) dates.add(eq.getDate());
        }
        View v = inflater.inflate(R.layout.dialoguespinner, null);
        final Spinner dateSpinner = v.findViewById(R.id.spinner);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dates.toArray(new String[dates.size()]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(spinnerArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Date Picker");
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ArrayList<Earthquake> earthquakes = new ArrayList<>();
                Earthquake Southern = null;
                double minLat = 90;
                for (Earthquake earthquake : readRss.getEarthquakes())
                {
                    double lat = earthquake.getGeoLat();
                    //checks if the date is the same and then performs the calculation below: if latitude is lesser than the minimum latitude then that latitude gets set to the new minimum latitude
                    if (earthquake.getDate().equals(dateSpinner.getSelectedItem().toString()) && lat < minLat)
                    {
                        minLat = lat;
                        Southern = earthquake;
                    }
                }
                earthquakes.add(Southern);
                if (earthquakes.size() != 0)
                {
                    activity.setFilteredEarthquakes(earthquakes);
                    returnToMap(earthquakes);
                } else
                {
                    displayAlert("No Matches", "It appears there are no earthquakes to display");
                }
            }
        });
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Opens a dialog bx with a spinner where the user can click on a location to see where an earthquake has occurred
    public void filterByLocation()
    {
        final ArrayList<String> locations = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (Earthquake eq: readRss.getEarthquakes())
        {
            if (!locations.contains(eq.getLocation())) locations.add(eq.getLocation());
        }

        View v = inflater.inflate(R.layout.dialoguespinner, null);
        final Spinner locationSpinner = v.findViewById(R.id.spinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, locations.toArray(new String[locations.size()]));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(spinnerArrayAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Location Picker");
        builder.setPositiveButton("Select", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ArrayList<Earthquake> earthquakes = new ArrayList<>();
                for (Earthquake earthquake : readRss.getEarthquakes())
                {
                    //checks location of each earthquake against the selected location
                    if (earthquake.getLocation().equals(locationSpinner.getSelectedItem().toString()))
                    {
                        earthquakes.add(earthquake);
                    }
                }

                //checks if earthquakes are there if not then will display an alert saying no earthquakes
                if (earthquakes.size() != 0)
                {
                    activity.setFilteredEarthquakes(earthquakes);
                    returnToMap(earthquakes);
                } else
                {
                    displayAlert("No Matches", "It appears there are no earthquakes to display");
                }
            }
        });

        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //method to return to map after filter is selected, parameter takes filtered earthquakes array list
    public void returnToMap(ArrayList<Earthquake> earthquakes)
    {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setMainActivity(activity);
        homeFragment.setEarthquakeArrayList(earthquakes);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
    }

    //gets selected filter
    public String getFilter(int i)
    {
        return filters[i];
    }

    //creates dialog with a title and a message for use by the filters
    public void displayAlert(String title, String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(readRss.context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}
