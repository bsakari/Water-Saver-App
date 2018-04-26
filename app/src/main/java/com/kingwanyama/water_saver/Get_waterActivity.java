package com.kingwanyama.water_saver;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Get_waterActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_water);
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
        LatLng Nairobi = new LatLng(-1.268384, 36.7718421);
        mMap.addMarker(new MarkerOptions().position(Nairobi).title("Nairobi Water Company Pangani"));

        LatLng Nairobi1 = new LatLng(-1.2683831, 36.771842);
        mMap.addMarker(new MarkerOptions().position(Nairobi1).title("Nairobi Water & Sewarage Company Limited"));

        LatLng Nairobi2 = new LatLng(-1.2683822, 36.7718419);
        mMap.addMarker(new MarkerOptions().position(Nairobi2).title("Nairobi Water Company:Off Tom Mboya Street"));

        LatLng Nairobi3 = new LatLng(-1.2683817, 36.7718418);
        mMap.addMarker(new MarkerOptions().position(Nairobi3).title("Nairobi Water Suppliers"));



        LatLng Nairobi4 = new LatLng(-1.2633575, 36.8759369);
        mMap.addMarker(new MarkerOptions().position(Nairobi4).title("Ole Mara Water Distributors"));


        LatLng Nakuru = new LatLng(-0.2819336, 36.0452361);
        mMap.addMarker(new MarkerOptions().position(Nakuru).title("NAWASSCO (Nakuru Water And Sanitation Service Company)"));


        LatLng Nakuru1 = new LatLng(-0.2819336, 36.0452361);
        mMap.addMarker(new MarkerOptions().position(Nakuru1).title("Prison Road Pumping Station"));


        LatLng Nakuru3 = new LatLng(-0.3020583, 36.0555478);
        mMap.addMarker(new MarkerOptions().position(Nakuru3).title("Nawassco Co.Ltd Western Zone Office"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Nairobi,6.0f));
    }
}
