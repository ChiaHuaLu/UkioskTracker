package chiahua.ukiosktracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class PosterChecklistActivity extends AppCompatActivity implements OnMapReadyCallback {

    public Poster poster;
    private boolean[] location;
    public ArrayList<Kiosk> allKiosks;


    private static final float DEFAULT_MIN_ZOOM = 14.0f;
    private static final float DEFAULT_MAX_ZOOM = 19.0f;
    private static final float DEFAULT_ZOOM = 15.3f;

    private static final LatLngBounds UT_AUSTIN_BOUNDS = new LatLngBounds(
            new LatLng(30.280890, -97.741699), new LatLng(30.291044, -97.727639));
    private static final CameraPosition UT_AUSTIN_CAMERA = new CameraPosition.Builder()
            .target(new LatLng(30.285967, -97.736179)).zoom(DEFAULT_ZOOM)
            .bearing(0).tilt(0).build();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_checklist);

        Intent receivedIntent = getIntent();
        long posterID = receivedIntent.getLongExtra("PosterID", -1);
        poster = Poster.findById(Poster.class, posterID);
        setTitle(poster.title());
        location = new boolean[20];
        allKiosks = (ArrayList<Kiosk>) Kiosk.listAll(Kiosk.class);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.checklistMap);
        mapFragment.getMapAsync(this);
    }

    /**
     * Before the map is ready many calls will fail.
     * This should be called on all entry points that call methods on the Google Maps API.
     */
    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
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

        for (int kioskID = 1; kioskID <= 20; kioskID++) {
            Kiosk kiosk = allKiosks.get(kioskID - 1);
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(kiosk.latit(), kiosk.longit()));
//            if (poster.checkKiosk(kioskID)) {
//                marker.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.checkbox_on_background));
//            }
//            else {
                marker.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.checkbox_off_background));
//            }

            mMap.addMarker(marker);

        }
        mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //int position = (int)(marker.getTag());
                //Using position get Value from arraylist
                //Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                marker.setIcon(BitmapDescriptorFactory.fromResource(android.R.drawable.checkbox_on_background));
                return false;
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(UT_AUSTIN_CAMERA));
    }
}
