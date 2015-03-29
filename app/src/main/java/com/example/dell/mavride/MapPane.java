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
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class MapPane extends FragmentActivity implements OnMapReadyCallback, OnMarkerClickListener, OnMarkerDragListener {

    private static final LatLng ERB = new LatLng(32.7330729,-97.1130619);
    private static final LatLng NH = new LatLng(32.7324781,-97.1138454);
    private static final LatLng UC = new LatLng(32.731654,-97.110998);
    private static final LatLng CL = new LatLng(32.729659,-97.112958);
    private static final LatLng UH = new LatLng(32.7290307,-97.1138804);
    private static final LatLng KC = new LatLng(32.728002,-97.109622);
    private static final LatLng PL = new LatLng(32.730395,-97.111811);
    private static final LatLng LH = new LatLng(32.7290878,-97.1082488);
    private static final LatLng WH = new LatLng(32.731606,-97.112588);
    private static final LatLng AH = new LatLng(32.7310998,-97.1093611);

    private GoogleMap googleMap;

    protected Button request;
    protected Spinner NoOfRider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        addGoogleMap();
        // addLines();
        addMarkers();
       // Parse.initialize(this, "Z1onpLDjhguG5Xerjh1sSzCm4T6o3tgQdN4TwjiM", "7WOxDqGAYaJulOnKZdLA9huezBWyB7OuOaBwjCQ0");
        MapFragment mapfragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
                mapfragment.getMapAsync(this);
        Toast.makeText(getApplicationContext(), "Select Your Source and Destination On the Map", Toast.LENGTH_LONG).show();
        Spinner dropdown = (Spinner)findViewById(R.id.NoOfRider);
        String[] items = new String[]{"1", "2", "3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
        Spinner dropdown1 = (Spinner)findViewById(R.id.select);
        String[] items1 = new String[]{"Source", "Destination"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items1);
        dropdown1.setAdapter(adapter1);
        request= (Button) findViewById(R.id.request_button);
        NoOfRider= (Spinner) findViewById(R.id.NoOfRider);

    }

    private void addGoogleMap() {

        // check if we have got the googleMap already
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            googleMap.setOnMarkerClickListener(this);
            googleMap.setOnMarkerDragListener(this);
        }
    }
    private void addMarkers() {
        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);

            // a draggable marker with title and snippet
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ERB, 16));
            googleMap.addMarker(new MarkerOptions().position(ERB)
                    .title("ERB").snippet("Engineering Research Building")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NH, 16));
            googleMap.addMarker(new MarkerOptions().position(NH)
                    .title("NH").snippet("Nedderman Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UC, 16));
            googleMap.addMarker(new MarkerOptions().position(UC)
                    .title("UC").snippet("University Center")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CL, 16));
            googleMap.addMarker(new MarkerOptions().position(CL)
                    .title("CL").snippet("Central Library")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AH, 16));
            googleMap.addMarker(new MarkerOptions().position(AH)
                    .title("AH").snippet("Arlington Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UH, 16));
            googleMap.addMarker(new MarkerOptions().position(UH)
                    .title("UH").snippet("University Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KC, 16));
            googleMap.addMarker(new MarkerOptions().position(KC)
                    .title("KC").snippet("Kalpana Chawla Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(WH, 16));
            googleMap.addMarker(new MarkerOptions().position(WH)
                    .title("WH").snippet("Woolf Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PL, 16));
            googleMap.addMarker(new MarkerOptions().position(PL)
                    .title("PL").snippet("Planeterium")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LH, 16));
            googleMap.addMarker(new MarkerOptions().position(LH)
                    .title("LH").snippet("Lipscomb Hall")
                    .flat(true)
                    .draggable(true));

        }




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
        LatLng fromPosition = marker.getPosition();
        Log.d(getClass().getSimpleName(), "Drag start at: " + fromPosition);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        LatLng toPosition = marker.getPosition();
        LatLng fromPosition = marker.getPosition();

        Toast.makeText(
                getApplicationContext(),
                "Marker " + marker.getTitle() + " dragged from " + fromPosition
                        + " to " + toPosition, Toast.LENGTH_LONG).show();

    }

    @Override
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

    }@Override
    public void onMapReady(GoogleMap googleMap) {

    }
}

