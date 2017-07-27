package chiahua.ukiosktracker;

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



    //public ArrayList<Kiosk> allKiosks;
    //public ArrayList<Poster> allPosters;

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
                    KioskTab kioskTab = new KioskTab();
                    return kioskTab;
                case 1:
                    NearbyTab nearbyTab = new NearbyTab();
                    return nearbyTab;
                case 2:
                    PosterTab posterTab = new PosterTab();
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
                    return "Kiosks";
                case 1:
                    return "Nearby";
                case 2:
                    return "Posters";
            }
            return null;
        }
    }

    public void aboutOnClick() {
        Log.d(TAG, "in about onClick");
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);

    }

    //Create the list of all kiosks
    private void initializeKiosks() {
        String[] kioskNames = new String[] {"Kinsolving",     "SSB",     "RLM",     "HSM", "Burdine",     "Bio", "Welch-Painter", "Guadalupe",     "FAC",   "Tower", "Waggner", "Winship", "Art Building",     "GSB",     "CBA", "Littlefield Fountain",     "PCL", "Gregory", "RecSports Center", "School of Music"};
        double[] lattitude = new double[] {    30.289869, 30.289770, 30.289276, 30.288812, 30.288546, 30.287507,       30.287310,   30.285746, 30.285957, 30.285542, 30.285520, 30.285464,      30.285396, 30.284477, 30.284560,              30.283686, 30.283365, 30.283481,          30.281584,         30.286910};
        double[] longitude = new double[] {   -97.740050,-97.738789,-97.736690,-97.740503,-97.738872,-97.740371,      -97.738325,  -97.741567,-97.740589,-97.740069,-97.737904,-97.734187,     -97.733546,-97.738564,-97.737496,             -97.739486,-97.737626,-97.737257          -97.733119,        -97.730417};
        for (int index = 0; index < kioskNames.length; index++) {
           // Kiosk
        }


    }
}
