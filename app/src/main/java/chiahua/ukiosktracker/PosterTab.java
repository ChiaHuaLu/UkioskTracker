package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class PosterTab extends Fragment {

    public ArrayList<Poster> allPosters;
    public ArrayList<Kiosk> allKiosks;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.poster_tab, container, false);
        allPosters = this.getArguments().getParcelableArrayList("allPosters");
        allKiosks = this.getArguments().getParcelableArrayList("allKiosks");
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

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent addPosterIntent = new Intent(getActivity(), AddPosterActivity.class);
//                int index = 0;
//                for (Poster poster: allPosters) {
//                    addPosterIntent.putExtra("Poster"+index+"Bool[]", poster.locations);
//                    addPosterIntent.putExtra("Poster"+index+"String[]", poster.getDetailArray());
//                    index++;
//                }
//                addPosterIntent.putExtra("NumPosters", index);

                startActivityForResult(addPosterIntent, getResources().getInteger(R.integer.add_new_poster_reqCode));
                //startActivity(addPosterIntent);




//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        fab.setImageResource(R.drawable.add);

        ListAdapter posterListAdapter = new PosterArrayAdapter(this.getContext(), allPosters);
        ListView allPostersLV = (ListView) rootView.findViewById(R.id.allPostersLV);
        allPostersLV.setAdapter(posterListAdapter);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    // TODO Extract the data returned from the child Activity.
                    String[] returnValue = data.getStringArrayExtra("New_Poster_Info");
                    Log.d("TAG", "New poster info: "
                            + returnValue[0] + returnValue[1] + returnValue[2] + returnValue[3]);
                    allPosters.add(new Poster(returnValue[0], returnValue[1], returnValue[2],
                            "", returnValue[3], allKiosks));
                }
                break;
            }
        }
    }
}
