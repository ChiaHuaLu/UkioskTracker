package chiahua.ukiosktracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.orm.SugarRecord;

public class KioskDetailActivity extends AppCompatActivity {

    private static final String TAG = "KioskDetailActivity";
    private int kioskID;

    private Kiosk kiosk;

    private ListView kioskDetailsLV;
    private CursorAdapter kioskDetailsAdapter;

    private int mode;
    private SharedPreferences sharedPreferences;

    // workaround INNER SELECT statement
    private String whereClause;

    // Listener defined by anonymous inner class.
    public SharedPreferences.OnSharedPreferenceChangeListener spChanged =
            new SharedPreferences.OnSharedPreferenceChangeListener() {

                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    Log.d("debug", "A preference has been changed");
                    if (key.equals("SORTMODE")) {
                        Log.d(TAG, "Sort mode preferences has changed.");
                        mode = sharedPreferences.getInt("SORTMODE", 0);

                        // set how the list is sorted via ORDER BY clause in SQLite
                        // SELECT * FROM POSTER ORDER BY getSortMode(mode) based on filtered posters
                        Cursor cursor = SugarRecord.getCursor(
                                Poster.class, whereClause, null, null, getSortMode(mode), null);

                        // switch cursor to new cursor with updated database
                        kioskDetailsAdapter.swapCursor(cursor);
                        kioskDetailsAdapter.notifyDataSetChanged();
                        kioskDetailsLV.setAdapter(kioskDetailsAdapter);
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiosk_detail);
        Bundle extras = getIntent().getExtras();
        kioskID = extras.getInt("kioskID");
        whereClause = "ID IN (SELECT p.ID from KIOSK_POSTER kp INNER JOIN " +
                "POSTER p ON kp.POSTER = p.ID where kp.kiosk = " + kioskID + ")";

        kiosk = Kiosk.findById(Kiosk.class, kioskID);
        setTitle(kiosk.name() + " Kiosk");

        Log.d(TAG, "Kiosk " + kioskID + " is named " + kiosk.name());
        kioskDetailsLV = (ListView) findViewById(R.id.kioskDetailLV);

        sharedPreferences =
                getSharedPreferences("SORTMODE", Context.MODE_PRIVATE);

        sharedPreferences.registerOnSharedPreferenceChangeListener(spChanged);

        mode = sharedPreferences.getInt("SORTMODE", 0);

        // SELECT * FROM POSTER ORDER BY getSortMode(mode) based on filtered posters
        Cursor cursor = SugarRecord.getCursor(
                Poster.class, whereClause, null, null, getSortMode(mode), null);
        kioskDetailsAdapter = new PosterCursorAdapter(this, cursor);

        Log.d(TAG, whereClause);

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
        Log.d(TAG, "Notify dataset has been changed onResume");

        // set how the list is sorted via ORDER BY clause in SQLite
        Cursor cursor = SugarRecord.getCursor(
                Poster.class, whereClause, null, null, getSortMode(mode), null);

        // switch cursor to new cursor with updated database
        kioskDetailsAdapter.swapCursor(cursor);
        kioskDetailsAdapter.notifyDataSetChanged();
        kioskDetailsLV.setAdapter(kioskDetailsAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(spChanged);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kiosk_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.kiosk_sort) {
            SortByFragment sortByFragment
                    = SortByFragment.newInstance(mode);
            sortByFragment.show(getSupportFragmentManager(), "SortBy");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getSortMode(int mode) {
        String sortMode;
        switch(mode) {
            case 0:
                sortMode = "TITLE";
                break;
            case 1:
                sortMode = "ORGANIZATION, TITLE";
                break;
            case 2:
                sortMode = "COUNT, TITLE DESC";
                break;
            case 3:
                sortMode = "EVENT_TIME, TITLE";
                break;
            default:
                sortMode = null;
        }
        return sortMode;
    }
}
