package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PosterTab extends Fragment {

    private final static String TAG = "PosterTab";
    public ArrayList<Poster> allPosters;
    public ArrayList<Kiosk> allKiosks;
    private ListAdapter posterListAdapter;
    private ListView allPostersLV;
    private Spinner sortBy;

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
//        pref = getContext().getSharedPreferences("SORTMODE", Context.MODE_PRIVATE);
//        sortMode = Integer.parseInt(pref.getString("SORTMODE", "0"));
//        sort(sortMode);
//        posterListAdapter = new PosterArrayAdapter(this.getContext(), allPosters);
        allPostersLV = (ListView) rootView.findViewById(R.id.allPostersLV);
//        allPostersLV.setAdapter(posterListAdapter);
        update();
        fab.setImageResource(R.drawable.add);
        return rootView;
    }

    public void update() {

        sort();
        allPostersLV.deferNotifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: notifyDataSetChanged is preferred, but can't get it to work
        Log.d(TAG, "Notify dataset has been changed onResume");
        update();
//        sort(sortMode);
//        posterListAdapter = new PosterArrayAdapter(this.getContext(), allPosters);
//        allPostersLV = (ListView) this.getActivity().findViewById(R.id.allPostersLV);
//        allPostersLV.setAdapter(posterListAdapter);
    }

    public void sort() {
        SharedPreferences pref = getContext().getSharedPreferences("SORTMODE", Context.MODE_PRIVATE);
        int mode = Integer.parseInt(pref.getString("SORTMODE", "0"));
        allPosters = (ArrayList<Poster>) Poster.listAll(Poster.class);
        //Sort by poster name
        if (mode == 0) {
            Collections.sort(allPosters, new Comparator<Poster>() {
                @Override
                public int compare(Poster p1, Poster p2) {
                    return p1.title().compareTo(p2.title());
                }
            });
        }
        //Sort by Org
        else if (mode == 1) {
            Collections.sort(allPosters, new Comparator<Poster>() {
                @Override
                public int compare(Poster p1, Poster p2) {
                    String org1 = p1.organization();
                    String org2 = p2.organization();
                    if (org1.equals(org2)) {
                        return p1.title().compareTo(p2.title());
                    }
                    else {
                       return p1.organization().compareTo(p2.organization());
                    }
                }
            });
        }
        //Sort by number posted
        else if (mode == 2) {
            Collections.sort(allPosters, new Comparator<Poster>() {
                @Override
                public int compare(Poster p1, Poster p2) {
                    if (p1.count()==p2.count())
                        return p1.title().compareTo(p2.title());
                    return p1.count()-p2.count();
                }
            });
        }
        //Sort by expiration
        else {
            Collections.sort(allPosters, new Comparator<Poster>() {
                @Override
                public int compare(Poster p1, Poster p2) {
                    String exp1 = p1.eventTime();
                    String exp2 = p2.eventTime();
                    if (exp1.equals("")) {
                        if (exp2.equals("")) {
                            return p1.title().compareTo(p2.title());
                        }
                        else {
                            return 1;
                        }
                    }
                    else if (!exp1.equals(""))
                        return -1;
                    return p1.eventTime().compareTo(p2.eventTime());
                }
            });
        }
        posterListAdapter = new PosterArrayAdapter(this.getContext(), allPosters);
//        allPostersLV = (ListView) getActivity().findViewById(R.id.allPostersLV);
        allPostersLV.setAdapter(posterListAdapter);
    }

}


