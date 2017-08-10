package chiahua.ukiosktracker;

import java.util.ArrayList;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
    private static final String TAG = "MainActivity";



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

        allKiosks = new ArrayList<>();
        allPosters = new ArrayList<>();

        Log.d(TAG, "test if Kiosk database is empty: " + Kiosk.count(Kiosk.class) + " entries, "
                + (Kiosk.count(Kiosk.class) <= 0));
        // build Kiosk database only if this is the first time the app has run (prevent duplicates)
        if (Kiosk.count(Kiosk.class) <= 0) {
            Log.d(TAG, "FIRST TIME RUNNING / INIT KIOSKS");
            // first time! build Kiosk database
            Kiosk.initializeKiosks();
        }
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
            aboutOnClick();
            return true;
        }
        if (id == R.id.help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }
//        if (id == R.id.clear) {
//
//        }
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
                    return kioskMapTab;
                case 1:
                    PosterTab posterTab = new PosterTab();
                    return posterTab;
                /*case 1:
                    NearbyTab nearbyTab = new NearbyTab();
                    return nearbyTab;
                case 2:
                    PosterTab posterTab = new PosterTab();
                    return posterTab;*/
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            //return 3;
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.kiosk);
                case 1:
                    return getString(R.string.posters);
                /*case 1:
                    return getString(R.string.nearby);
                case 2:
                    return getString(R.string.posters);*/
            }
            return null;
        }
    }



    public void aboutOnClick() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);

    }

    public ArrayList<Kiosk> getAllKiosks() {
        return allKiosks;
    }

    public ArrayList<Poster> getAllPosters() {
        return allPosters;
    }
}
