package chiahua.ukiosktracker;

import android.util.Log;
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

    public boolean matchPoster(Poster p) {
        Log.d("TAG", "poster is null = " + (poster==null));
        if (!poster.title().equals(p.title()))
            return false;
        if (!poster.organization().equals(p.organization()))
            return false;
        if (!poster.details().equals(p.details()))
            return false;
        return poster.eventLocation().equals(p.eventLocation());
    }

    public boolean matchKiosk(Kiosk k) {
        return k.name().equals(kiosk.name());
    }

    public Poster getPoster() {
        return poster;
    }

}
