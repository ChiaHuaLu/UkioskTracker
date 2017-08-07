package chiahua.ukiosktracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        Log.d("TAG", "Posteritem is null: " + (posterItem==null));
        Log.d("TAG", "Poster position is: " + position);

        TextView titleTV = (TextView) customView.findViewById(R.id.poster_item_title);
        TextView subtitleTV = (TextView) customView.findViewById(R.id.poster_item_subtitle);
        Button editPosterButton = (Button) customView.findViewById(R.id.poster_item_edit);
        titleTV.setText(posterItem.title());
        subtitleTV.setText("Poster Locations: " + posterItem.count() + "    " +
                convertMMDDYYYY(posterItem) + "    " + posterItem.organization());
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
        if (posterItem.eventTime().length()>0) {
            if (Integer.parseInt(posterItem.eventTime())+1 < Integer.parseInt(new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date()))) {
                customView.setBackgroundColor(Color.parseColor("#fca9a9"));
            }
        }
        Date currentDate = new Date();
        currentDate.getYear();

        return customView;
    }

    private String convertMMDDYYYY(Poster poster) {
        String result = "";
        String posterDate = poster.eventTime();
        if (posterDate.length()>0) {
            result += posterDate.substring(4, 6) +"/"+ posterDate.substring(6)
                    +"/"+ posterDate.substring(0, 4);
        }
        return result;
    }

}
