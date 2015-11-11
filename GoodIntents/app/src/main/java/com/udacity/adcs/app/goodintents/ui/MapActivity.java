package com.udacity.adcs.app.goodintents.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.udacity.adcs.app.goodintents.R;
import com.udacity.adcs.app.goodintents.content.AppProviderUtils;
import com.udacity.adcs.app.goodintents.objects.Event;

import java.util.List;

/**
 * Created by demouser on 11/10/15.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private AppProviderUtils mProvider;
    private List<Event> mEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mProvider= AppProviderUtils.Factory.get(this);
        mEvents=mProvider.getEventList();
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

        if (!mEvents.isEmpty()) {
            for (Event e : mEvents) {
                LatLng mLatLong = new LatLng(e.getLat(), e.getLong());
                mMap.addMarker(new MarkerOptions().position(mLatLong).title(e.getName()));
            }

            LatLng mDefault = new LatLng(37.7837258, -122.4213325);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mDefault));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(mDefault)
                    .zoom(mMap.getCameraPosition().zoom > 12 ? mMap.getCameraPosition().zoom
                            : 12) // Sets the zoom
                    .tilt(mMap.getCameraPosition().tilt > 12 ? mMap.getCameraPosition().tilt
                            : 12) // Sets the tilt
                            //.bearing(mCurrentLocation.getBearing()) // Set the bearing and will adjust the direction of the map
                    .build(); // Creates a CameraPosition from the builder

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
