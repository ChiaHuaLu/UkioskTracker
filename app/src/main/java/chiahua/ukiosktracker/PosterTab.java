package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.orm.SugarRecord;

public class PosterTab extends Fragment {

    private final static String TAG = "PosterTab";
    private CursorAdapter posterListAdapter;
    private ListView allPostersLV;
    private Button buttonSort;
    private int mode;
    private SharedPreferences sharedPreferences;
    private String[] sortMode;

    // Listener defined by anonymous inner class.
    public SharedPreferences.OnSharedPreferenceChangeListener spChanged =
            new SharedPreferences.OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.d("debug", "A preference has been changed");
            if (key.equals("SORTMODE")) {
                Log.d(TAG, "Sort mode preferences has changed.");
                mode = sharedPreferences.getInt("SORTMODE", 0);
                buttonSort.setText(getString(R.string.sortby) + " " + sortMode[mode]);

                // set how the list is sorted via ORDER BY clause in SQLite
                // SELECT * FROM POSTER ORDER BY getSortMode(mode)
                Cursor cursor = SugarRecord.getCursor(
                        Poster.class, null, null, null, getSortMode(mode), null);

                // switch cursor to new cursor with updated database
                posterListAdapter.swapCursor(cursor);
                posterListAdapter.notifyDataSetChanged();
                allPostersLV.setAdapter(posterListAdapter);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.poster_tab, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.posterTabfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPosterIntent = new Intent(getActivity(), EditPosterActivity.class);
                addPosterIntent.putExtra("addNew", true);
                startActivity(addPosterIntent);
            }
        });

        sortMode = getResources().getStringArray(R.array.sortBy);

        sharedPreferences =
                getActivity().getSharedPreferences("SORTMODE", Context.MODE_PRIVATE);

        sharedPreferences.registerOnSharedPreferenceChangeListener(spChanged);

        mode = sharedPreferences.getInt("SORTMODE", 0);

        buttonSort = (Button) rootView.findViewById(R.id.sort_button);

        buttonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortByFragment sortByFragment
                        = SortByFragment.newInstance(mode);
                sortByFragment.show(getActivity().getSupportFragmentManager(), "SortBy");
            }
        });

        buttonSort.setText(getString(R.string.sortby) + " " + sortMode[mode]);

        // SELECT * FROM POSTER ORDER BY getSortMode(mode)
        Cursor cursor = SugarRecord.getCursor(
                Poster.class, null, null, null, getSortMode(mode), null);
        posterListAdapter = new PosterCursorAdapter(getActivity(), cursor);

        allPostersLV = (ListView) rootView.findViewById(R.id.allPostersLV);
        fab.setImageResource(R.drawable.add);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Notify dataset has been changed onResume");

        // set how the list is sorted via ORDER BY clause in SQLite
        Cursor cursor =
                SugarRecord.getCursor(Poster.class, null, null, null, getSortMode(mode), null);

        // switch cursor to new cursor with updated database
        posterListAdapter.swapCursor(cursor);
        posterListAdapter.notifyDataSetChanged();
        allPostersLV.setAdapter(posterListAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(spChanged);
    }

    public String getSortMode(int mode) {
        String sortMode;
        switch(mode) {
            case 0:
                sortMode = "TITLE";
                break;
            case 1:
                sortMode = "ORGANIZATION";
                break;
            case 2:
                sortMode = "COUNT";
                break;
            case 3:
                sortMode = "EVENT_TIME";
                break;
            default:
                sortMode = null;
        }
        return sortMode;
    }
}
