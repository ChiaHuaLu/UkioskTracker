package chiahua.ukiosktracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ChiaHuaBladeWX on 8/5/2017.
 */

public class SortByFragment extends DialogFragment {
    private static final String TAG = "SortBy Dialog";
    private static final String SORT_MODE = "sortBy";

    public static SortByFragment newInstance(int sortMode) {
        SortByFragment result = new SortByFragment();
        // Supply sortMode input as an argument.
        Bundle args = new Bundle();
        args.putInt(SORT_MODE , sortMode);
        result.setArguments(args);
        return result;
    }
    public int getSelectedMode() {
        return getArguments().getInt(SORT_MODE, 0);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int currentMode = getSelectedMode();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle(R.string.sort_mode_choose)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(R.array.sortBy, currentMode,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // save the mode by:
                                // Poster Title, Organization, Number Posted, Expiration Date
                                getArguments().putInt(SORT_MODE, which);
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // user pressed okay, so we are going to change the mode
                        int newMode = getArguments().getInt(SORT_MODE, 0);
                        Log.d(TAG, "User clicked okay. Changing mode to: " + newMode);
                        SharedPreferences pref = getActivity().
                                getSharedPreferences("SORTMODE", Context.MODE_PRIVATE);
                        pref.edit().putInt("SORTMODE", newMode).apply();
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}