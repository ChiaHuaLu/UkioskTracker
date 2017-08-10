package chiahua.ukiosktracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class KioskMapTab extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "KioskMapTab";
    private static View rootView;
    private static final float DEFAULT_MIN_ZOOM = 14.0f;
    private static final float DEFAULT_MAX_ZOOM = 19.0f;
    private static final float DEFAULT_ZOOM = 15.3f;
    private static final LatLngBounds UT_AUSTIN_BOUNDS = new LatLngBounds(
            new LatLng(30.280890, -97.741699), new LatLng(30.291044, -97.727639));
    private static final CameraPosition UT_AUSTIN_CAMERA = new CameraPosition.Builder()
            .target(new LatLng(30.285967, -97.736179)).zoom(DEFAULT_ZOOM)
            .bearing(0).tilt(0).build();
    private GoogleMap mMap;
    public ArrayList<Kiosk> allKiosks;
    public HashMap<Marker, Kiosk> markerKioskHashMap;

    public static final int LOCATION_FINE_REQ_CODE = 24;
    public static final int LOCATION_COARSE_REQ_CODE = 25;
    private boolean locationFineAccess = false;
    private boolean locationCoarseAccess = false;


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
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d(TAG, "test if Kiosk database is empty: " + Kiosk.count(Kiosk.class) + " entries");
        allKiosks = (ArrayList<Kiosk>) Kiosk.listAll(Kiosk.class);
        markerKioskHashMap = new HashMap<>();
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

        for (Kiosk kiosk : allKiosks) {
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(kiosk.latit(), kiosk.longit()))
                    .title(kiosk.name())
                    .snippet("Posters: " + kiosk.posterCount()));
            markerKioskHashMap.put(marker, kiosk);
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent kioskDetailsIntent =
                        new Intent(KioskMapTab.this.getActivity(), KioskDetailActivity.class);
                // send in the kioskID ID (based on database)
                int kioskID = markerKioskHashMap.get(marker).getId().intValue();
                Log.d(TAG, "testing - send in kioskID [" + kioskID + "] to kioskDetails");
                kioskDetailsIntent.putExtra("kioskID", kioskID);
                startActivity(kioskDetailsIntent);
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(UT_AUSTIN_CAMERA));
        //Check permission for location
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("TAG", "Locations Permissions Granted, Enable Current Location");
                mMap.setMyLocationEnabled(true);
            } else {
                Log.d("TAG", "Request Fine Location Permissions");
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_FINE_REQ_CODE);
            }
        } else {
            Log.d("TAG", "Request Coarse Location Permissions");
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_COARSE_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_FINE_REQ_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationFineAccess = true;
                    ActivityCompat.requestPermissions(this.getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_FINE_REQ_CODE);
                    Log.d("TAG", "Locations Permissions Granted, Enable Current Location");
                    if (ActivityCompat.checkSelfPermission(this.getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this.getActivity(),
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    break;
                }
                else {
                    locationFineAccess = false;
                }
                Log.d("TAG", "fine location is: " + locationFineAccess);
            }
            case LOCATION_COARSE_REQ_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationCoarseAccess = true;
                    ActivityCompat.requestPermissions(this.getActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            LOCATION_COARSE_REQ_CODE);
                    break;
                }
                else {
                    locationCoarseAccess = false;
                }
                Log.d("TAG", "coarse location is: " + locationCoarseAccess);
            }
        }
    }

}
