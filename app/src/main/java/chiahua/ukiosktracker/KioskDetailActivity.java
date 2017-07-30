package chiahua.ukiosktracker;

import android.content.Intent;
import android.content.res.TypedArray;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

    }
}
