package com.mpd_cwk_earthquake_app.model;

//earthquake model to be used by the application
public class Earthquake
{
    private String depth;
    private String time;
    private String location;
    private String Date;
    private String geoLat;
    private String geoLong;
    private String magnitude;

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getDate()
    {
        return Date;
    }

    public void setDate(String date)
    {
        this.Date = date;
    }

    public double getGeoLat()
    {
        return Double.parseDouble(geoLat);
    }

    public void setGeoLat(String geoLat)
    {
        this.geoLat = geoLat;
    }

    public double getGeoLong()
    {
        return Double.parseDouble(geoLong);
    }

    public void setGeoLong(String geoLong)
    {
        this.geoLong = geoLong;
    }


    //parser that takes in the description from the rss feed item and splits each node into the corresponding value required for the model
    public void parseDescription(String nodeDescription){
        String[] segment = nodeDescription.split(";");
        int segmentNumber = 0;
        for(String data : segment){

            String[] earthquake = data.split(": ");

            if(segmentNumber == 0){
                String date = earthquake[1].substring(0,17).trim();
                setDate(date);
                String time = earthquake[1].substring(17, 22).trim();
                setTime(time);
            }else if(segmentNumber == 1){
                setLocation(earthquake[1]);
            }else if(segmentNumber == 2){
                String[] Coordinates = earthquake[1].split(",");
                setGeoLat(Coordinates[0].trim());
                setGeoLong(Coordinates[1].trim());
            }else if(segmentNumber == 3){
                setDepth(earthquake[1].trim());
            }else if(segmentNumber == 4){
                setMagnitude(earthquake[1].trim());
            }
            segmentNumber++;
        }

    }
}


