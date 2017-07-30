package chiahua.ukiosktracker;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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

        Poster posterItem = getItem(position);
        TextView titleTV = (TextView) customView.findViewById(R.id.poster_item_title);
        TextView subtitleTV = (TextView) customView.findViewById(R.id.poster_item_subtitle);
        Button editPosterButton = (Button) customView.findViewById(R.id.poster_item_edit);
        titleTV.setText(posterItem.title());
        subtitleTV.setText("Poster Locations: " + posterItem.count());
        return customView;
    }
}