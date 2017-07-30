package chiahua.ukiosktracker;

import java.util.ArrayList;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
;

import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static final String TAG = "tag";



    public static ArrayList<Kiosk> allKiosks;
    public static ArrayList<Poster> allPosters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        allKiosks = new ArrayList<Kiosk>();
        allPosters = new ArrayList<Poster>();
        initializeKiosks();

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setImageResource(R.drawable.add);*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            //Snackbar.make(getView, "Replace with your own action", Snackbar.LENGTH_LONG)
              //      .setAction("Action", null).show();
            aboutOnClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    KioskMapTab kioskMapTab = new KioskMapTab();
                    Bundle kioskMapTabBundle = new Bundle();
                    kioskMapTabBundle.putParcelableArrayList("allKiosks", allKiosks);
                    kioskMapTab.setArguments(kioskMapTabBundle);
                    return kioskMapTab;
                case 1:
                    NearbyTab nearbyTab = new NearbyTab();
                    return nearbyTab;
                case 2:
                    PosterTab posterTab = new PosterTab();
                    Bundle posterTabBundle = new Bundle();
                    posterTabBundle.putParcelableArrayList("allPosters", allPosters);
                    posterTabBundle.putParcelableArrayList("allKiosks", allKiosks);
                    posterTab.setArguments(posterTabBundle);
                    return posterTab;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.kiosk);
                case 1:
                    return getString(R.string.nearby);
                case 2:
                    return getString(R.string.posters);
            }
            return null;
        }
    }

    public void aboutOnClick() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);

    }

    //Create the list of all kiosks
    private void initializeKiosks() {
        String[] kioskNames = new String[] {"Kinsolving",     "SSB",     "RLM","Hearst Student Media", "Burdine",     "Bio", "Welch-Painter", "Guadalupe",     "FAC",   "Tower", "Waggner", "Winship", "Art Building",     "GSB",     "CBA", "Littlefield Fountain",     "PCL", "Gregory", "RecSports Center", "School of Music"};
        double[] latitude = new double[] {    30.289897, 30.289754, 30.289301,              30.288817, 30.288587, 30.287434,       30.287372,   30.285797, 30.285912, 30.285682, 30.285526, 30.285587,      30.285494, 30.284391, 30.284579,              30.283790, 30.283373, 30.283512,          30.281537,         30.286861};
        double[] longitude = new double[] {  -97.740086,-97.738839,-97.736685,             -97.740447,-97.738902,-97.740334,      -97.738315,  -97.741550,-97.740682,-97.739998,-97.737911,-97.734405,     -97.733426,-97.738599,-97.737465,             -97.739375,-97.737711,-97.737189,         -97.733091,        -97.730413};
        for (int index = 0; index < latitude.length; index++) {
            allKiosks.add(new Kiosk(index, kioskNames[index], latitude[index], longitude[index]));
        }
    }

    public ArrayList<Kiosk> getAllKiosks() {
        return allKiosks;
    }

    public ArrayList<Poster> getAllPosters() {
        return allPosters;
    }
}
