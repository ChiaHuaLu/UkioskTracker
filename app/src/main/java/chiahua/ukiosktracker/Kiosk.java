package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */

import android.util.Log;

import com.orm.SugarRecord;

import java.util.ArrayList;

public class Kiosk extends SugarRecord {

    //Coordinates of the Kiosks
    private double latitude;
    private double longitude;

    // Short name / name for kioskID
    private String name;

    // Leave empty constructor for SugarRecords
    public Kiosk() {}

    //Constructor method for Kiosks
    public Kiosk(String name, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    //Get latitude coordinate
    public double latit() {
        return this.latitude;
    }

    //Get longitude coordinate
    public double longit() {
        return this.longitude;
    }

    //Get Kiosk Name
    public String name() { return this.name; }

    //Get Kiosk ID
    /*public int id() { return this.kioskNumber; }*/


    //Adds a poster to this kioskID's list of posters
    //Only call this from the Poster class
    /*public boolean add(Poster poster) {
        return posterList.add(poster);
    }*/

    //Removes a poster from this kioskID's list of posters
    //Only call this from the Poster class
    /*public boolean remove(Poster poster) {
        return posterList.remove(poster);
    }*/

    //Returns the list of posters posted at this kioskID
    /*public ArrayList<Poster> getPosterList() {
        return posterList;
    }*/

    /*public void printAsString() {
        System.out.println("Kiosk number " + kioskNumber + ": "+ name);
        System.out.println(posterList.size() + " Posters are here:");
        for (Poster poster: posterList) {
            System.out.println("   "+poster.title());
        }
    }*/

    // Create the list of all kiosks (please only run this once!)
    public static void initializeKiosks() {
        String[] kioskNames = new String[] {"Kinsolving",     "SSB",     "RLM",     "HSM", "Burdine",     "Bio", "Welch-Painter", "Guadalupe",     "FAC",   "Tower", "Waggner", "Winship", "Art Building",     "GSB",     "CBA", "Littlefield Fountain",     "PCL", "Gregory", "RecSports Center", "School of Music"};
        double[] latitude = new double[] {    30.289869, 30.289770, 30.289276, 30.288812, 30.288546, 30.287507,       30.287310,   30.285746, 30.285957, 30.285542, 30.285520, 30.285464,      30.285396, 30.284477, 30.284560,              30.283686, 30.283365, 30.283481,          30.281584,         30.286910};
        double[] longitude = new double[] {   -97.740050,-97.738789,-97.736690,-97.740503,-97.738872,-97.740371,      -97.738325,  -97.741567,-97.740589,-97.740069,-97.737904,-97.734187,     -97.733546,-97.738564,-97.737496,             -97.739486,-97.737626,-97.737257,         -97.733119,        -97.730417};

        ArrayList<Kiosk> kiosks = new ArrayList<>();
        for (int index = 0; index < latitude.length; index++) {
            //allKiosks.add(new Kiosk(index, kioskNames[index], latitude[index], longitude[index]));
            kiosks.add(new Kiosk(kioskNames[index], latitude[index], longitude[index]));
        }
        // Save all kiosks en-masse
        Log.d("TEST","Saving kiosks! count: " + kiosks.size());
    }

    //Calculates distance from a given location in meters
    public double distance(double userLatitude, double userLongitude) {
        //Distance = sqrt{ (y2-y1)^2 + (x2-x1)^2 }
        double latitudeDelta = latitude - userLatitude;
        double longitudeDelta = longitude - userLongitude;
        double result = (latitudeDelta * latitudeDelta) +
                (longitudeDelta * longitudeDelta);
        return Math.sqrt(result) * 111000;
    }
}

