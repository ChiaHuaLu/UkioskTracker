package chiahua.ukiosktracker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ChiaHuaBladeWX on 7/28/2017.
 */

class PosterArrayAdapter extends ArrayAdapter<Poster> {



    public PosterArrayAdapter(@NonNull Context context, ArrayList<Poster> posterList) {
        super(context, R.layout.poster_row, posterList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.poster_row, parent, false);

        final Poster posterItem = getItem(position);
        TextView titleTV = (TextView) customView.findViewById(R.id.poster_item_title);
        TextView subtitleTV = (TextView) customView.findViewById(R.id.poster_item_subtitle);
        Button editPosterButton = (Button) customView.findViewById(R.id.poster_item_edit);
        titleTV.setText(posterItem.title());
        subtitleTV.setText("Poster Locations: " + posterItem.count() + "    " + posterItem.organization());
        final long posterIDToEdit = posterItem.getId();
        editPosterButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editPosterIntent = new Intent(getContext(), EditPosterActivity.class);
                editPosterIntent.putExtra("PosterID", posterIDToEdit);
                editPosterIntent.putExtra("addNew", false);
                getContext().startActivity(editPosterIntent);
            }
        });
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ListView", "Poster is null = " + (posterItem == null));
                long posterID = posterItem.getId();
                Intent intent = new Intent(getContext(), PosterChecklistActivity.class);
                intent.putExtra("PosterID", posterID);
                getContext().startActivity(intent);
            }
        });


        return customView;
    }
}
