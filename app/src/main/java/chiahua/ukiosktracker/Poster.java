package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */

import com.orm.SugarRecord;

public class Poster extends SugarRecord {

    private String title;
    private String organization;
    private String eventLocation;
    private String eventTime;
    private String details;
    private int count;

    // Leave empty constructor for SugarRecords
    public Poster() {}

    //Basic Constructor
    //Precondition: All Kiosks must have already been instantiated and added to list
    public Poster(String title, String details) {
        initializePoster(title, details);
    }

    //Detailed Constructor
    //Precondition: All Kiosks must have already been instantiated and added to list
    public Poster(String title, String organization, String location,
                  String time, String details) {
        initializePoster(title, details);
        this.organization = organization;
        eventLocation = location;
        eventTime = time;
        if (time == null)
            eventTime = "";
    }

    //Constructor helper method
    private void initializePoster(String title, String details) {
        this.title = title;
        this.details = details;
        organization = "";
        eventLocation = "";
        eventTime = "";
        count = 0;
    }

    //Number of this poster posted around campus
    public int count() {
        return count;
    }

    public void increaseCount() {
        count++;
    }

    public void decreaseCount() {
        count--;
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

    public String[] getDetailArray() {
        String[] detailsArray =
                new String[] {title, organization, eventLocation, eventTime, details};
        return detailsArray;
    }
}




