package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */

import android.util.Log;
import com.orm.SugarRecord;
import java.util.ArrayList;
import java.util.List;

public class Kiosk extends SugarRecord {

    //Coordinates of the Kiosks
    private double latitude;
    private double longitude;

    // Short name / name for kioskID
    private String name;

    private int posterCount;

    // Leave empty constructor for SugarRecords
    public Kiosk() {}

    //Constructor method for Kiosks
    public Kiosk(String name, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.posterCount = 0;
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

    public int getPosterCount() {
        return this.posterCount;
    }

    // Create the list of all kiosks (please only run this once!)
    // This is gacky, but it'll do (should be in a file)
    public static void initializeKiosks() {
        String[] kioskNames = new String[] {"Kinsolving",     "SSB",     "RLM","Hearst Student Media", "Burdine",     "Bio", "Welch-Painter", "Guadalupe",     "FAC",   "Tower", "Waggner", "Winship", "Art Building",     "GSB", "Littlefield Fountain",     "PCL", "Gregory", "RecSports Center", "School of Music"};
        double[] latitude = new double[] {    30.289897,  30.289754, 30.289301,              30.288817, 30.288587, 30.287434,       30.287372,   30.285797, 30.285912, 30.285683, 30.285526, 30.285587,      30.285494, 30.284337,              30.283790, 30.283373, 30.283512,          30.281537,         30.286861};
        double[] longitude = new double[] {  -97.740086, -97.738839,-97.736685,             -97.740447,-97.738902,-97.740334,      -97.738315,  -97.741550,-97.740682,-97.739996,-97.737911,-97.734405,     -97.733426,-97.738590,             -97.739375,-97.737711,-97.737189,         -97.733091,        -97.730413};
        List<Kiosk> kiosks = new ArrayList<>();
        for (int index = 0; index < latitude.length; index++) {
            kiosks.add(new Kiosk(kioskNames[index], latitude[index], longitude[index]));
            Kiosk.saveInTx(kiosks);
        }
        // Save all kiosks en-masse
        Log.d("TEST","Saving kiosks! count: " + kiosks.size());
        SugarRecord.saveInTx(kiosks);
    }

    //Calculates distance from a given location in meters
    public double distance(double userLatitude, double userLongitude) {
        double latitudeDelta = latitude - userLatitude;
        double longitudeDelta = longitude - userLongitude;
        double result = (latitudeDelta * latitudeDelta) +
                (longitudeDelta * longitudeDelta);
        return Math.sqrt(result) * 111000;
    }

    public void updatePosterCount(int result) {
        Log.d("Kiosk", "Kiosk" + this.getId() + " had " + posterCount + " posters.");
        posterCount = result;
        Log.d("Kiosk", "Kiosk" + this.getId() + " now has " + posterCount + " posters.");
    }
}

