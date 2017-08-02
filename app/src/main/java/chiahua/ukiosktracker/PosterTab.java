package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class PosterTab extends Fragment {

    private final static String TAG = "PosterTab";
    public ArrayList<Poster> allPosters;
    public ArrayList<Kiosk> allKiosks;
    private ListAdapter posterListAdapter;
    private ListView allPostersLV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.poster_tab, container, false);
        allPosters = (ArrayList<Poster>) Poster.listAll(Poster.class);
        allKiosks = (ArrayList<Kiosk>) Kiosk.listAll(Kiosk.class);
        Log.d("allPosters Check", "Poster Tab's allPosters contains "
                + allPosters.size() + " Elements");
        Log.d("allKiosks Check", "Poster Tab's allKiosks contains "
                + allKiosks.size() + " Elements");
        String debug = "";
        for (Poster posters : allPosters) {
            debug += "   " +posters.title() + "\n";
        }
        Log.d("All Posters List", "AllPosters = \n"+debug);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.posterTabfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPosterIntent = new Intent(getActivity(), EditPosterActivity.class);
                addPosterIntent.putExtra("addNew", true);
                startActivity(addPosterIntent);
            }
        });

        posterListAdapter = new PosterArrayAdapter(this.getContext(), allPosters);
        allPostersLV = (ListView) rootView.findViewById(R.id.allPostersLV);
        allPostersLV.setAdapter(posterListAdapter);
        fab.setImageResource(R.drawable.add);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: notifyDataSetChanged is preferred, but can't get it to work
        Log.d(TAG, "Notify dataset has been changed onResume");
        allPosters = (ArrayList<Poster>) Poster.listAll(Poster.class);
        posterListAdapter = new PosterArrayAdapter(this.getContext(), allPosters);
        allPostersLV = (ListView) this.getActivity().findViewById(R.id.allPostersLV);
        allPostersLV.setAdapter(posterListAdapter);
    }
}
