package chiahua.ukiosktracker;

import android.content.Intent;
import android.content.res.TypedArray;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class KioskDetailActivity extends AppCompatActivity {

    private static final String TAG = "KioskDetailActivity";
    public int kioskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiosk_detail);
        Bundle extras = getIntent().getExtras();
        kioskID = extras.getInt("kioskID");

        Kiosk kiosk = Kiosk.findById(Kiosk.class, kioskID);
        setTitle(kiosk.name() + " Kiosk");
        ImageView kioskImage = (ImageView) findViewById(R.id.kioskImage);
        TypedArray kioskImages = getResources().obtainTypedArray(R.array.kioskImages);
        kioskImage.setImageResource(kioskImages.getResourceId(kioskID, -1));
        kioskImages.recycle();
        Log.d(TAG, "Kiosk " + kioskID + " is named " + kiosk.name());
        List<KioskPoster> allKPs = KioskPoster.listAll(KioskPoster.class);
        ArrayList<Poster> relevantPosters = new ArrayList<Poster>();
        for (KioskPoster kp : allKPs) {
            if (kp.matchKiosk(kiosk)) {
                relevantPosters.add(kp.getPoster());
            }
        }
        Log.d("Kiosk Detail", "Relevant posters size = "+relevantPosters.size());
        ListAdapter kioskDetailsAdapter = new PosterArrayAdapter(this, relevantPosters);
        ListView kioskDetailsLV = (ListView) findViewById(R.id.kioskDetailLV);
        kioskDetailsLV.setAdapter(kioskDetailsAdapter);
        //TODO: Something wrong with the KioskDetails Listview.
    }
}
