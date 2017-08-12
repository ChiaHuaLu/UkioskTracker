package chiahua.ukiosktracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
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
    public ArrayList<Kiosk> allKiosks;
    public ArrayList<KioskPoster> allKPs;
    public ArrayList<KioskPoster> relevantKPs;
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
        Log.d("TAG", "Poster "+posterID + " title = " + (poster.title()==null));
        allKiosks = (ArrayList<Kiosk>) Kiosk.listAll(Kiosk.class);
        allKPs = (ArrayList<KioskPoster>) KioskPoster.listAll(KioskPoster.class);
        relevantKPs = new ArrayList<KioskPoster>();
        for (KioskPoster kp : allKPs) {
            Log.d("TAG", "kp null = " + (kp==null));
            if (kp.matchPoster(poster))
                relevantKPs.add(kp);
        }
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

        for (int kioskID = 1; kioskID <= allKiosks.size(); kioskID++) {
            Kiosk kiosk = allKiosks.get(kioskID - 1);
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(kiosk.latit(), kiosk.longit()));
            marker.title(kioskID+"");
            if (kioskMatchFound(kiosk)!=null) {
                marker.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.checkbox_on_background));
            }
            else {
                marker.icon(BitmapDescriptorFactory.fromResource(android.R.drawable.checkbox_off_background));
            }
            mMap.addMarker(marker);
        }
        mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d("TAG", "Marker title null "+(marker.getTitle()==null));
                int position = Integer.parseInt(marker.getTitle());
                Kiosk kiosk = allKiosks.get(position-1);
                KioskPoster kp = kioskMatchFound(kiosk);
                if (kp == null) {
                    Log.d("TAG", "kp is null in PCA");
                    marker.setIcon(BitmapDescriptorFactory.fromResource(android.R.drawable.checkbox_on_background));
                    kp = new KioskPoster(kiosk, poster);
                    kp.save();
                    allKPs.add(kp);
                    relevantKPs.add(kp);
                    kiosk.updatePosterCount(kiosk.getPosterCount() + 1);
                    kiosk.save();
                    poster.increaseCount();
                    poster.save();
                }
                else {
                    Log.d("TAG", "kp is not null in PCA");
                    marker.setIcon(BitmapDescriptorFactory.fromResource(android.R.drawable.checkbox_off_background));
                    allKPs.remove(kp);
                    relevantKPs.remove(kp);
                    kiosk.updatePosterCount(kiosk.getPosterCount() - 1);
                    kiosk.save();
                    KioskPoster.delete(kp);
                    poster.decreaseCount();
                    poster.save();
                }
                return true;
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(UT_AUSTIN_CAMERA));
        //Check permission for location
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            mMap.setMyLocationEnabled(true);
        }
    }

    //Checks to see if KP match is found. True if returns valid KP. False if returns null
    private KioskPoster kioskMatchFound(Kiosk kiosk) {
        for (KioskPoster kp : relevantKPs) {
            Log.d("kioskMatchFound", "KP is null "+ (kp==null));
            if (kp.matchKiosk(kiosk)) {
                return kp;
            }
        }
        return null;
    }
}
