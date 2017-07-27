package chiahua.ukiosktracker;

/**
 * Created by ChiaHuaBladeWX on 7/27/2017.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class KioskTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.kiosk_tab, container, false);
        return rootView;
    }
}
