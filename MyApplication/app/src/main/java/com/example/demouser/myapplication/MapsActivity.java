package com.example.demouser.myapplication;

import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng san_francisco = new LatLng(37.7837258, -122.4213325);
        LatLng san_francisco1 = new LatLng(37.7838088, -122.588994);
        LatLng san_francisco2 = new LatLng(37.7710917, -122.4087604);
        LatLng san_francisco3 = new LatLng(37.8029932, -122.4165789);
        LatLng san_francisco4 = new LatLng(37.7943583, -122.3980391);
        LatLng san_francisco5 = new LatLng(37.7592243, -122.4594192);
        LatLng san_francisco6 = new LatLng(37.7755016, -122.4143532);
        LatLng san_francisco7 = new LatLng(37.7918212, -122.4021527);
        LatLng san_francisco8 = new LatLng(37.8662102, -122.2643627);
        LatLng san_francisco9 = new LatLng(37.7834637, -122.3980157);
        LatLng san_francisco10 = new LatLng(37.7781789, -122.3929135);
        LatLng san_francisco11 = new LatLng(37.7763668, -122.4237366);
        LatLng san_francisco12 = new LatLng(37.7214928, -122.4477123);
        LatLng san_francisco13 = new LatLng(37.7785993, -122.3914585);

        //37.7785993	-122.3914585
        //37.8571652	-122.2777907
        //37.9682242	-122.6066029
        //37.9172406	-122.2949671
        //37.7891342	-122.4094387
        //37.8437005	-122.2936917
        mMap.addMarker(new MarkerOptions().position(san_francisco).title("san_francisco"));
        mMap.addMarker(new MarkerOptions().position(san_francisco1).title("san_francisco1"));
        mMap.addMarker(new MarkerOptions().position(san_francisco2).title("san_francisco2"));
        mMap.addMarker(new MarkerOptions().position(san_francisco3).title("san_francisco3"));
        mMap.addMarker(new MarkerOptions().position(san_francisco4).title("san_francisco4"));
        mMap.addMarker(new MarkerOptions().position(san_francisco5).title("san_francisco5"));
        mMap.addMarker(new MarkerOptions().position(san_francisco6).title("san_francisco6"));
        mMap.addMarker(new MarkerOptions().position(san_francisco7).title("san_francisco7"));
        mMap.addMarker(new MarkerOptions().position(san_francisco8).title("san_francisco8"));
        mMap.addMarker(new MarkerOptions().position(san_francisco9).title("san_francisco9"));
        mMap.addMarker(new MarkerOptions().position(san_francisco10).title("san_francisco10"));
        mMap.addMarker(new MarkerOptions().position(san_francisco11).title("san_francisco11"));
        mMap.addMarker(new MarkerOptions().position(san_francisco12).title("san_francisco12"));
        mMap.addMarker(new MarkerOptions().position(san_francisco13).title("san_francisco13"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(san_francisco));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(37.7837258, -122.4213325))
                .zoom(mMap.getCameraPosition().zoom > 12 ? mMap.getCameraPosition().zoom
                        : 12) // Sets the zoom
                .tilt(mMap.getCameraPosition().tilt > 12 ? mMap.getCameraPosition().tilt
                        : 12) // Sets the tilt
                //.bearing(mCurrentLocation.getBearing()) // Set the bearing and will adjust the direction of the map
                .build(); // Creates a CameraPosition from the builder

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
