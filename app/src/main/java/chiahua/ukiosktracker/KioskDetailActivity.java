package chiahua.ukiosktracker;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class KioskDetailActivity extends AppCompatActivity {

    private static final String TAG = "KioskDetailActivity";
    private int kioskID;

    private Kiosk kiosk;
    private ArrayList<Poster> relevantPosters;
    private List<KioskPoster> allKPs;

    private ListView kioskDetailsLV;
    private ListAdapter kioskDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiosk_detail);
        Bundle extras = getIntent().getExtras();
        kioskID = extras.getInt("kioskID");

        kiosk = Kiosk.findById(Kiosk.class, kioskID);
        setTitle(kiosk.name() + " Kiosk");

        Log.d(TAG, "Kiosk " + kioskID + " is named " + kiosk.name());
        allKPs = KioskPoster.listAll(KioskPoster.class);
        relevantPosters = new ArrayList<>();
        Log.d("TAG", "allKPs size: " + allKPs.size());
        for (KioskPoster kp : allKPs) {
            if (kp.matchKiosk(kiosk)) {
                relevantPosters.add(kp.getPoster());
            }
        }
        Log.d("Kiosk Detail", "Relevant posters size = "+relevantPosters.size());
        kioskDetailsAdapter = new PosterArrayAdapter(this, relevantPosters);
        kioskDetailsLV = (ListView) findViewById(R.id.kioskDetailLV);
        ImageView kioskImage = (ImageView) findViewById(R.id.kioskImageIV);
        TypedArray kioskImages = getResources().obtainTypedArray(R.array.kioskImages);
        Log.d("SetImage KioskID", kioskID + "");
        kioskImage.setImageResource(kioskImages.getResourceId(kioskID, -1));
        kioskImages.recycle();
        kioskDetailsLV.setAdapter(kioskDetailsAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.KioskDetailfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPosterIntent = new Intent(getBaseContext(), EditPosterActivity.class);
                addPosterIntent.putExtra("addNew", true);
                addPosterIntent.putExtra("KioskID", kioskID);
                Log.d("TAG", "Intent adding kioskID " + kioskID);
                startActivity(addPosterIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: notifyDataSetChanged is preferred, but can't get it to work
        Log.d(TAG, "Notify dataset has been changed onResume");

        relevantPosters = new ArrayList<>();
        Log.d("TAG", "allKPs size: " + allKPs.size());
        for (KioskPoster kp : allKPs) {
            if (kp.matchKiosk(kiosk)) {
                if (kp.getPoster() == null) {
                    Log.d("TAG", "KDA: getPoster is null.");
                } else {
                    relevantPosters.add(kp.getPoster());
                }
            }
        }
        Log.d("Kiosk Detail", "Relevant posters size = "+relevantPosters.size());

        kioskDetailsAdapter = new PosterArrayAdapter(this, relevantPosters);
        kioskDetailsLV = (ListView) findViewById(R.id.kioskDetailLV);
        kioskDetailsLV.setAdapter(kioskDetailsAdapter);
    }
}
