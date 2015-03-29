package com.example.dell.mavride;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class MapPane extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,GoogleMap.OnMarkerDragListener {

    protected Spinner NoOfRider;

    protected Button request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       // Parse.initialize(this, "Z1onpLDjhguG5Xerjh1sSzCm4T6o3tgQdN4TwjiM", "7WOxDqGAYaJulOnKZdLA9huezBWyB7OuOaBwjCQ0");
        MapFragment mapfragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
                mapfragment.getMapAsync(this);
        Toast.makeText(getApplicationContext(), "Select Your Source and Destination On the Map", Toast.LENGTH_LONG).show();
        Spinner dropdown = (Spinner)findViewById(R.id.NoOfRider);
        String[] items = new String[]{"1", "2", "3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng erb = new LatLng(32.7330729,-97.1130619);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(erb,16));
        googleMap.addMarker(new MarkerOptions()
                .title("ERB")
                .snippet("ERB")
                .position(erb));

        LatLng nh = new LatLng(32.7324781,-97.1138454);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nh,16));
        googleMap.addMarker(new MarkerOptions()
                .title("NH")
                .snippet("Nedderman Hall")
                .position(nh));


        LatLng uc = new LatLng(32.731654,-97.110998);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uc,16));
        googleMap.addMarker(new MarkerOptions()
                .title("UC")
                .snippet("University Center")
                .position(uc));

        LatLng cl = new LatLng(32.729659,-97.112958);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cl,16));
        googleMap.addMarker(new MarkerOptions()
                .title("CL")
                .snippet("Central library")
                .position(cl));

        LatLng uh = new LatLng(32.7290307,-97.1138804);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uh,16));
        googleMap.addMarker(new MarkerOptions()
                .title("UH")
                .snippet("University Hall")
                .position(uh));

        LatLng kc = new LatLng(32.728002,-97.109622);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kc,16));
        googleMap.addMarker(new MarkerOptions()
                .title("KCH")
                .snippet("Kalpana Chawla Hall")
                .position(kc));

        LatLng pl = new LatLng(32.730395,-97.111811);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pl,16));
        googleMap.addMarker(new MarkerOptions()
                .title("PL")
                .snippet("Planeterium")
                .position(pl));


        LatLng lh = new LatLng(32.7290878,-97.1082488);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lh,16));
        googleMap.addMarker(new MarkerOptions()
                .title("LH")
                .snippet("Lipscomb Hall")
                .position(lh));


        LatLng wh = new LatLng(32.731606,-97.112588);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wh,16));
        googleMap.addMarker(new MarkerOptions()
                .title("WH")
                .snippet("Woolf Hall")
                .position(wh));


        LatLng ah = new LatLng(32.7310998,-97.1093611);
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ah,16));
        googleMap.addMarker(new MarkerOptions()
                .title("AH")
                .snippet("Arlington Hall")
                .position(ah));



         googleMap.addMarker(new MarkerOptions()
         .position(new LatLng(32.7310,-97.1150))
         .title("Marker"));
         googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13))
         googleMap.setMapType(googleMap.MAP_TYPE_TERRAIN);

    }



    public boolean onMarkerClick(Marker marker) {

        Log.i("MapPane", "onMarkerClick");
        final LatLng src = marker.getPosition();

        Toast.makeText(getApplicationContext(),
                "Your current location is " + marker.getTitle(), Toast.LENGTH_LONG)
                .show();
        final LatLng dest = marker.getPosition();

        Toast.makeText(getApplicationContext(),
                "Your destination is  " + marker.getTitle(), Toast.LENGTH_LONG)
                .show();

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject map = new ParseObject("RideRequest");
                map.put("Source", src);
                map.put("Destination", dest);
                map.put("NoRider", NoOfRider);



            }

        });

        return false;

    }


    public void startActivity(View view) {
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Logout) {
            ParseUser.logOut();
            Intent userhome = new Intent(getApplicationContext(), Login.class);
            startActivity(userhome);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
