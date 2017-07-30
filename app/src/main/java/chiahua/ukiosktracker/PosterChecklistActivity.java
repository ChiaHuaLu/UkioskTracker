package chiahua.ukiosktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PosterChecklistActivity extends AppCompatActivity {

    public Poster poster;
    private boolean[] location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_checklist);

        Intent receivedIntent = getIntent();
        long posterID = receivedIntent.getLongExtra("PosterID", -1);
        poster = Poster.findById(Poster.class, posterID);
        setTitle(poster.title());
        location = new boolean[20];
        for (int kioskID = 1; kioskID <= 20; kioskID++) {
            location[kioskID-1] = poster.checkKiosk(kioskID);
        }
    }
}
