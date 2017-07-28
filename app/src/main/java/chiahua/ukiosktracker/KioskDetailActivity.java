package chiahua.ukiosktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class KioskDetailActivity extends AppCompatActivity {

    public Kiosk kiosk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiosk_detail);

        Intent receivedIntent = getIntent();
        kiosk = receivedIntent.getParcelableExtra("kiosk");
        TextView kioskDetailsChecker = (TextView) findViewById(R.id.kiosk_detail_name);
        kioskDetailsChecker.setText(kiosk.name() + "  ID="+kiosk.id());
        setTitle(kiosk.name() + " Kiosk");

    }
}
