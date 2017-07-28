package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */

import java.util.ArrayList;

public class Poster {

    private String title;
    private String organization;
    private String eventLocation;
    private String eventTime;
    private String details;
    private int count;

    public boolean[] locations;  //Do not modify this from other classes
    public ArrayList<Kiosk> allKiosks;


    //Basic Constructor
    //Precondition: All Kiosks must have already been instanciated and added to list
    public Poster(String title, String details, ArrayList<Kiosk> kiosks) {
        initializePoster(title, details, kiosks);
    }

    //Detailed Constructor
    //Precondition: All Kiosks must have already been instanciated and added to list
    public Poster(String title, String organization, String location,
                  String time, String details, ArrayList<Kiosk> kiosks) {
        initializePoster(title, details, kiosks);
        this.organization = organization;
        eventLocation = location;
        eventTime = time;
        if (time == null)
            eventTime = "";
    }

    //Constructor helper method
    private void initializePoster(String title, String details, ArrayList<Kiosk> kiosks) {
        this.title = title;
        this.details = details;
        this.allKiosks = kiosks;
        organization = "";
        eventLocation = "";
        eventTime = "";
        locations = new boolean[kiosks.size()];
        count = 0;
    }

    //Number of this poster posted around campus
    public int count() {
        return count;
    }

    //Modify a poster's data. Use null for fields that don't change
    public void modify(String title, String organization, String location,
                       String time, String details) {
        if (title != null)
            this.title = title;
        if (organization != null)
            this.organization = organization;
        if (location != null)
            this.eventLocation = location;
        if (time != null)
            this.eventTime = time;
        if (details != null)
            this.details = details;
    }

    //Get a poster's title
    public String title() {
        return title;
    }

    //Get a poster's organization
    public String organization() {
        return organization;
    }

    //Get a poster's event location
    public String eventLocation() {
        return eventLocation;
    }

    //Get a poster's event time
    public String eventTime() {
        return eventTime;
    }

    //Get a poster's details
    public String details() {
        return details;
    }

    //Add this poster to a kiosk location
    public boolean add(int kioskNumber) {
        if (!locations[kioskNumber]) {
            locations[kioskNumber] = true;
            allKiosks.get(kioskNumber).add(this);
            count++;
            return true;
        }
        return false;
    }

    //Remove this poster from a kiosk location
    public boolean remove(int kioskNumber) {
        if (locations[kioskNumber]) {
            locations[kioskNumber] = false;
            allKiosks.get(kioskNumber).remove(this);
            count--;
            return true;
        }
        return false;
    }
}




