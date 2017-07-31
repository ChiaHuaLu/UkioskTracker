package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */

import android.util.Log;

import com.orm.SugarRecord;
import java.util.ArrayList;
import java.util.List;

public class Poster extends SugarRecord {

    private String title;
    private String organization;
    private String eventLocation;
    private String eventTime;
    private String details;
    private int count;
    //private String location;

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
        //location = "00000000000000000000"; //Represents a boolean[] where 1 = present at a kiosk
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

    // Create the list of all kiosks (please only run this once!)
    public static void initializePoster() {
       // List<Kiosk> posters = new ArrayList<>();
        //Log.d("TEST","Saving kiosks! count: " + posters.size());
        //SugarRecord.saveInTx(posters);
    }
    /*
    //Check to see if poster is at a location
    public boolean checkKiosk(int kioskID) {
        if (location.charAt(kioskID-1) == '1')
            return true;
        else {
            return false;
        }
    }

    //Set if poster is at a location
    public void setKiosk(int kioskID, boolean present) {
        StringBuilder result = new StringBuilder(location);
        if (present) {
            result.setCharAt(kioskID - 1, '1');
        }
        else {
            result.setCharAt(kioskID - 1, '0');
        }
        location = result.toString();
        this.save();
    }  */

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

    //Add this poster to a kioskID location
    /*public boolean add(int kioskNumber) {
        if (!locations[kioskNumber]) {
            locations[kioskNumber] = true;
            allKiosks.get(kioskNumber).add(this);
            count++;
            return true;
        }
        return false;
    }*/

    //Remove this poster from a kioskID location
    /*public boolean remove(int kioskNumber) {
        if (locations[kioskNumber]) {
            locations[kioskNumber] = false;
            allKiosks.get(kioskNumber).remove(this);
            count--;
            return true;
        }
        return false;
    }*/

    public String[] getDetailArray() {
        String[] detailsArray =
                new String[] {title, organization, eventLocation, eventTime, details};
        return detailsArray;
    }
}




