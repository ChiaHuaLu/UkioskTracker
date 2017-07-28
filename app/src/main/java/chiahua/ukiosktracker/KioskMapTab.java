package chiahua.ukiosktracker;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class KioskMapTab extends Fragment implements OnMapReadyCallback {

    private static View rootView;

    private final LatLng UT_AUSTIN = new LatLng(30.2859305,-97.7395813);

    private static final float DEFAULT_MIN_ZOOM = 14.0f;
    private static final float DEFAULT_MAX_ZOOM = 18.0f;

    private static final LatLngBounds UT_AUSTIN_BOUNDS = new LatLngBounds(
            new LatLng(30.277979,-97.7428474), new LatLng(30.290262, -97.726700));
    private static final CameraPosition UT_AUSTIN_CAMERA = new CameraPosition.Builder()
            .target(new LatLng(30.2859305,-97.7395813)).zoom(DEFAULT_MIN_ZOOM)
            .bearing(0).tilt(0).build();

    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.kiosk_map_tab, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }

    /**
     * Before the map is ready many calls will fail.
     * This should be called on all entry points that call methods on the Google Maps API.
     */
    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(getActivity(), R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (!checkReady()) {
            return;
        }

        mMap.setLatLngBoundsForCameraTarget(UT_AUSTIN_BOUNDS);
        mMap.setMinZoomPreference(DEFAULT_MIN_ZOOM);
        mMap.setMaxZoomPreference(DEFAULT_MAX_ZOOM);

        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(UT_AUSTIN).title("UT Tower"));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(UT_AUSTIN_CAMERA));
    }
}
