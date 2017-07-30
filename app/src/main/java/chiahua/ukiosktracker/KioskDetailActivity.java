package chiahua.ukiosktracker;

import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class KioskDetailActivity extends AppCompatActivity {

    public Kiosk kiosk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiosk_detail);

        Intent receivedIntent = getIntent();
        kiosk = receivedIntent.getParcelableExtra("kiosk");
        setTitle(kiosk.name() + " Kiosk");
        ImageView kioskImage = (ImageView) findViewById(R.id.kioskIV);
        TypedArray imageArray = getResources().obtainTypedArray(R.array.kiosk_images);
        kioskImage.setImageResource(imageArray.getResourceId(kiosk.id(), -1));
        imageArray.recycle();

    }
}
