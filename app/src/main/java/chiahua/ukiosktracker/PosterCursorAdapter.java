package chiahua.ukiosktracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Daniel on 8/7/2017.
 */

public class PosterCursorAdapter extends CursorAdapter {

    private static final String TAG = "PosterCursorAdapter";

    public PosterCursorAdapter(@NonNull Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.poster_row, parent, false);
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        String result = "[ ";
        for (String s : cursor.getColumnNames()
             ) {
            result += s + " ";
        }
        Log.d(TAG, "Cursor getColumns: " + result + "]");

        TextView titleTV = (TextView) view.findViewById(R.id.poster_item_title);
        TextView subtitleTV = (TextView) view.findViewById(R.id.poster_item_subtitle);
        Button editPosterButton = (Button) view.findViewById(R.id.poster_item_edit);

        String title = cursor.getString(cursor.getColumnIndexOrThrow("TITLE"));
        int count = cursor.getInt(cursor.getColumnIndexOrThrow("COUNT"));
        String organization = cursor.getString(cursor.getColumnIndexOrThrow("ORGANIZATION"));
        String subtitle = "Poster Locations: " + count + "    " +
                convertMMDDYYYY(cursor) + "    " + organization;

        titleTV.setText(title);
        subtitleTV.setText(subtitle);

        final long posterIDToEdit = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
        editPosterButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editPosterIntent = new Intent(v.getContext(), EditPosterActivity.class);
                editPosterIntent.putExtra("PosterID", posterIDToEdit);
                editPosterIntent.putExtra("addNew", false);
                v.getContext().startActivity(editPosterIntent);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long posterID = posterIDToEdit;
                Intent intent = new Intent(v.getContext(), PosterChecklistActivity.class);
                intent.putExtra("PosterID", posterID);
                v.getContext().startActivity(intent);
            }
        });
        String eventTime = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_TIME"));
        if (eventTime.length() > 0) {
            if (Integer.parseInt(eventTime)+1 < Integer.parseInt(
                    new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date()))) {
                view.setBackgroundColor(Color.parseColor("#fca9a9"));
            }
        }
        Date currentDate = new Date();
        currentDate.getYear();
    }

    private String convertMMDDYYYY(Cursor cursor) {
        String result = "";
        String posterDate = cursor.getString(cursor.getColumnIndexOrThrow("EVENT_TIME"));
        if (posterDate.length()>0) {
            result += posterDate.substring(4, 6) +"/"+ posterDate.substring(6)
                    +"/"+ posterDate.substring(0, 4);
        }
        return result;
    }
}
