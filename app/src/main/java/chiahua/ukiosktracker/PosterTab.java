package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        //TextView debugTV = (TextView) getActivity().findViewById(R.id.debugList);
        //debugTV.setText(debug);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.posterTabfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent addPosterIntent = new Intent(getActivity(), AddPosterActivity.class);
//                startActivityForResult(addPosterIntent,
//                        getResources().getInteger(R.integer.add_new_poster_reqCode));

                Intent addPosterIntent = new Intent(getActivity(), EditPosterActivity.class);
                addPosterIntent.putExtra("addNew", true);
                startActivity(addPosterIntent);
            }
        });

        posterListAdapter = new PosterArrayAdapter(this.getContext(), allPosters);
        allPostersLV = (ListView) rootView.findViewById(R.id.allPostersLV);
        allPostersLV.setAdapter(posterListAdapter);
//        allPostersLV.setClickable(true);
//        allPostersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Poster posterToDetail = (Poster) allPostersLV.getItemAtPosition(position);
//                long posterID = posterToDetail.getId();
//                Intent intent = new Intent(getActivity(), PosterChecklistActivity.class);
//                intent.putExtra("PosterID", posterID);
//                startActivity(intent);
//            }
//        });

        fab.setImageResource(R.drawable.add);
        return rootView;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult called");
//        ((BaseAdapter) posterListAdapter).notifyDataSetChanged();
//        allPostersLV = (ListView) this.getActivity().findViewById(R.id.allPostersLV);
//        allPostersLV.setAdapter(posterListAdapter);
//    }
}
