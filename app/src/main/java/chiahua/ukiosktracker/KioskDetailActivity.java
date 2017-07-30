package chiahua.ukiosktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        TextView kioskDetailsChecker = (TextView) findViewById(R.id.kiosk_detail_name);
        kioskDetailsChecker.setText(kiosk.name() + "\n    ID="+ kioskID);
        setTitle(kiosk.name() + " Kiosk");

    }
}
