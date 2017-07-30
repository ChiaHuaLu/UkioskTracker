package chiahua.ukiosktracker;

import com.orm.SugarRecord;

/**
 * Created by Daniel on 7/29/2017.
 *
 * This class is made for the many-to-many relationship between Kiosks and Posters.
 * A Kiosk can hold many posters, and a Poster can be posted at many Kiosks.
 */

public class KioskPoster extends SugarRecord {

    private Kiosk kiosk;
    private Poster poster;

    // Default constructor is necessary for SugarRecord
    public KioskPoster() {}

    public KioskPoster(Kiosk kiosk, Poster poster) {
        this.kiosk = kiosk;
        this.poster = poster;
    }

}
