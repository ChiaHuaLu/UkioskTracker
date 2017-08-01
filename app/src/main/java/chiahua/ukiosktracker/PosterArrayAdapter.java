package chiahua.ukiosktracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
        Log.d("TAG", "Event time is "+ posterItem.eventTime());
        if (posterItem.eventTime().length()>0) {
            if (Integer.parseInt(posterItem.eventTime())+1 < Integer.parseInt(systemTime())) {
                customView.setBackgroundColor(Color.parseColor("#fca9a9"));
            }
        }



        return customView;
    }

    private String systemTime() {
        long systemTime = System.currentTimeMillis();
        long year = systemTime / (525600*60*1000)+997;
        long dayOfYear = systemTime % (525600*60*1000) / (1000 * 3600 * 24) +203;
        int month = 0;
        if (dayOfYear < 31) {
            month = 1;
        }
        else {
            dayOfYear -= 31;
            if ((isLeapYear(year) && dayOfYear<30) || !isLeapYear(year) && dayOfYear<29) {
                month = 2;
            }
            else {
                dayOfYear-=28;
                if (isLeapYear(year))
                    dayOfYear--;
                if ((dayOfYear-=31)<1) {
                    month=3;
                    dayOfYear+=31;
                } else if ((dayOfYear-=30)<1) {
                    month=4;
                    dayOfYear+=30;
                } else if ((dayOfYear-=31)<1) {
                    month=5;
                    dayOfYear+=31;
                } else if ((dayOfYear-=30)<1) {
                    month=6;
                    dayOfYear+=30;
                } else if ((dayOfYear-=31)<1) {
                    month=7;
                    dayOfYear+=31;
                } else if ((dayOfYear-=31)<1) {
                    month=8;
                    dayOfYear+=31;
                } else if ((dayOfYear-=30)<1) {
                    month=9;
                    dayOfYear+=30;
                } else if ((dayOfYear-=31)<1) {
                    month=10;
                    dayOfYear+=31;
                } else if ((dayOfYear-=30)<1) {
                    month=11;
                    dayOfYear+=30;
                } else {
                    month=12;
                }

            }

        }
        Log.d("TIME", systemTime + " = " + month+ "/"+dayOfYear+"/"+year);
        return padDateInts((int) year, 4) + padDateInts(month, 2) + padDateInts((int)dayOfYear, 2);
    }
    private String padDateInts(int number, int length) {
        StringBuilder result = new StringBuilder("");
        result.append(number);
        while (result.length()<length) {
            result.insert(0, "0");
        }
        return result.toString();
    }

    private boolean isLeapYear(long year) {
        if ((year % 4 == 0) && (year % 100 != 0) ||(year % 400 == 0))
            return true;
        else
            return false;
    }
}
