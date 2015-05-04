package com.example.dell.mavride;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import javax.xml.transform.Source;


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
    private static final LatLng UCB = new LatLng(32.731223, -97.111211);
    private static final LatLng GCP = new LatLng(32.729232, -97.107203);
    private static final LatLng CPC = new LatLng(32.731077, -97.107890);
    private static final LatLng B = new LatLng(32.729462, -97.110389);
    private static final LatLng ARCH = new LatLng(32.731358, -97.116001);
    private static final LatLng BOOK = new LatLng(32.733436, -97.109278);
    private static final LatLng CARH = new LatLng(32.730745, -97.112594);
    private static final LatLng CH = new LatLng(32.730815, -97.111521);
    private static final LatLng DH = new LatLng(32.729378, -97.115116);
    private static final LatLng ELAB = new LatLng(32.732515, -97.113101);
    private static final LatLng SWEET = new LatLng(32.733802, -97.121975);
    private static final LatLng FA = new LatLng(32.730530, -97.115261);
    private static final LatLng GS = new LatLng(32.731745, -97.113834);
    private static final LatLng HLTH = new LatLng(32.730442, -97.110806);
    private static final LatLng LS = new LatLng(32.728912, -97.112887);
    private static final LatLng MAC = new LatLng(32.731913, -97.116611);
    private static final LatLng NANO = new LatLng(32.732262, -97.115333);
    private static final LatLng PKH = new LatLng(32.729089, -97.111888);
    private static final LatLng PH = new LatLng(32.730882, -97.112848);
    private static final LatLng RH = new LatLng(32.730943, -97.112369);
    private static final LatLng SH = new LatLng(32.730412, -97.114111);
    private static final LatLng SWCA = new LatLng(32.734491, -97.114210);
    private static final LatLng SC = new LatLng(32.734058, -97.121287);
    private static final LatLng TEX = new LatLng(32.729723, -97.115132);
    private static final LatLng TH = new LatLng(32.729942, -97.111748);
    private static final LatLng ARBOR = new LatLng(32.730354, -97.121618);
    private static final LatLng AUT = new LatLng(32.732834, -97.110585);
    private static final LatLng BH = new LatLng(32.730901, -97.110818);
    private static final LatLng CENTI = new LatLng(32.726786, -97.115629);
    private static final LatLng CP = new LatLng(32.728116, -97.106819);
    private static final LatLng CC = new LatLng(32.733224, -97.114988);
    private static final LatLng GC = new LatLng(32.733233, -97.110858);
    private static final LatLng MS = new LatLng(32.733246, -97.110036);
    private static final LatLng MR = new LatLng(32.731656, -97.121560);
    private static final LatLng OAK = new LatLng(32.732847, -97.110250);
    private static final LatLng PECAN = new LatLng(32.726636, -97.109033);
    private static final LatLng HEIGHTS = new LatLng(32.724203, -97.108727);
    private static final LatLng LOFTS = new LatLng(32.724203, -97.108727);
    private static final LatLng TIMBER = new LatLng(32.733456, -97.119660);
    private static final LatLng TRINITY = new LatLng(32.724203, -97.108727);
    private static final LatLng UV = new LatLng(32.730571, -97.119578);
    private static final LatLng VH = new LatLng(32.731384, -97.108573);
    private static final LatLng WC = new LatLng(32.732589, -97.110748);
    private static final LatLng WS = new LatLng(32.727089, -97.109650);
    private static final LatLng ZEN = new LatLng(32.729019, -97.106350);




    private GoogleMap googleMap;

    protected Button request;
    protected Spinner NoOfRider;
    public String source;
    public String destination;
    public String ridersCount;
    public String sourceArea;
    public String destinationArea;
    protected String place;
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
        String[] items = new String[]{"0","1", "2", "3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
        Spinner dropdown1 = (Spinner)findViewById(R.id.select);
        String[] items1 = new String[]{"Source", "Destination"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items1);
        dropdown1.setAdapter(adapter1);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String riders = (String) parent.getItemAtPosition(position);
               // Toast.makeText(getApplicationContext(),"No Of Riders:  " + riders, Toast.LENGTH_LONG).show();
                ridersCount = riders;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapPane.this);
                builder.setMessage("Please select no of riders");
                builder.setTitle("Select Riders");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        dropdown1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                             @Override
                                             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                 String location = (String) parent.getItemAtPosition(position);
                                                 place = location;
                                             }

                                             @Override
                                             public void onNothingSelected(AdapterView<?> parent) {
                                                 AlertDialog.Builder builder = new AlertDialog.Builder(MapPane.this);
                                                 builder.setMessage("Please select Source and Destination");
                                                 builder.setTitle("Select Location ");
                                                 builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialog, int i) {
                                                         dialog.dismiss();
                                                     }
                                                 });
                                                 AlertDialog dialog = builder.create();
                                                 dialog.show();
                                             }
                                         });
        request= (Button) findViewById(R.id.request);
       // NoOfRider= (Spinner) findViewById(R.id.NoOfRider);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int riders = Integer.parseInt(ridersCount);
                if(riders == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MapPane.this);
                    builder.setMessage("Please select No of Riders");
                    builder.setTitle("Select Riders Count ");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    if(source == null){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapPane.this);
                        builder.setMessage("Please select Source");
                        builder.setTitle("Select Location ");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else {
                        if (destination == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MapPane.this);
                            builder.setMessage("Please select Destination");
                            builder.setTitle("Select Location ");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else{
                            if(source.equals(destination)){
                                AlertDialog.Builder builder = new AlertDialog.Builder(MapPane.this);
                                builder.setMessage("Source and Destination are same. Please select different Locations");
                                builder.setTitle("Select Correct Location ");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(MapPane.this);
                                builder.setMessage("Thanks for requesting the ride. Your ride is from "+source+" to "+destination+". Please Confirm");
                                builder.setTitle("Request Accepted");
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                        ParseUser currentUser = ParseUser.getCurrentUser();
                                        String userid = currentUser.getObjectId();
                                        boolean locationDiff;
                                        if(sourceArea.equals(destinationArea)){
                                            locationDiff = false;
                                        }
                                        else{
                                            locationDiff = true;
                                        }
                    ParseObject map = new ParseObject("RideRequest");
                    map.put("Status", "Unallocated");
                    map.put("Source", source);
                    map.put("Destination", destination);
                    map.put("NoRiders", riders);
                    map.put("RiderId", userid);
                    map.put("CampusChange",locationDiff);
                    map.saveInBackground();
                    Intent intent = new Intent(getApplicationContext(), UserHome.class);
                    startActivity(intent);
                }
            });
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(), MapPane.class);
                                        startActivity(intent);
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }

                        }
                    }
                }

            }

        });

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
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UCB, 16));
            googleMap.addMarker(new MarkerOptions().position(UCB)
                    .title("UC(B)").snippet("University Center Back")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GCP, 16));
            googleMap.addMarker(new MarkerOptions().position(GCP)
                    .title("GCP").snippet("Green at College Park")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CPC, 16));
            googleMap.addMarker(new MarkerOptions().position(CPC)
                    .title("CPC").snippet("College Park Center")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(B, 16));
            googleMap.addMarker(new MarkerOptions().position(LH)
                    .title("B").snippet("Business Building")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ARCH, 16));
            googleMap.addMarker(new MarkerOptions().position(ARCH)
                    .title("ARCH").snippet("Architecture and Fine Arts Library")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BOOK, 16));
            googleMap.addMarker(new MarkerOptions().position(BOOK)
                    .title("BOOK").snippet("Bookstore")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CARH, 16));
            googleMap.addMarker(new MarkerOptions().position(CARH)
                    .title("CARH").snippet("Carlisle Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CH, 16));
            googleMap.addMarker(new MarkerOptions().position(CH)
                    .title("CH").snippet("College Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DH, 16));
            googleMap.addMarker(new MarkerOptions().position(DH)
                    .title("DH").snippet("Davis Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ELAB, 16));
            googleMap.addMarker(new MarkerOptions().position(ELAB)
                    .title("ELAB").snippet("Engineering and Research Lab")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SWEET, 16));
            googleMap.addMarker(new MarkerOptions().position(SWEET)
                    .title("SWEET").snippet("Sweet Center")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(FA, 16));
            googleMap.addMarker(new MarkerOptions().position(FA)
                    .title("FA").snippet("Fine Arts Building")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GS, 16));
            googleMap.addMarker(new MarkerOptions().position(GS)
                    .title("GS").snippet("Geo Science")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HLTH, 16));
            googleMap.addMarker(new MarkerOptions().position(HLTH)
                    .title("HLTH").snippet("Health Center")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LS, 16));
            googleMap.addMarker(new MarkerOptions().position(LS)
                    .title("LS").snippet("Life Science")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MAC, 16));
            googleMap.addMarker(new MarkerOptions().position(MAC)
                    .title("MAC").snippet("Maverick Activity Center")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NANO, 16));
            googleMap.addMarker(new MarkerOptions().position(NANO)
                    .title("NANO").snippet("Nano Fab Building")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PKH, 16));
            googleMap.addMarker(new MarkerOptions().position(PKH)
                    .title("PKH").snippet("Pickard Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PH, 16));
            googleMap.addMarker(new MarkerOptions().position(PH)
                    .title("PH").snippet("Preston Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(RH, 16));
            googleMap.addMarker(new MarkerOptions().position(RH)
                    .title("RH").snippet("Ransom Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SH, 16));
            googleMap.addMarker(new MarkerOptions().position(SH)
                    .title("SH").snippet("Science Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SC, 16));
            googleMap.addMarker(new MarkerOptions().position(SC)
                    .title("SC").snippet("Swift Center")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TEX, 16));
            googleMap.addMarker(new MarkerOptions().position(TEX)
                    .title("TEX").snippet("Texas Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TH, 16));
            googleMap.addMarker(new MarkerOptions().position(TH)
                    .title("TH").snippet("Trimble Hall")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ARBOR, 16));
            googleMap.addMarker(new MarkerOptions().position(ARBOR)
                    .title("ARBOR").snippet("Arbor Oaks")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AUT, 16));
            googleMap.addMarker(new MarkerOptions().position(AUT)
                    .title("AUT").snippet("Autumn Hollow")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SC, 16));
            googleMap.addMarker(new MarkerOptions().position(SC)
                    .title("SC").snippet("SC")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BH, 16));
            googleMap.addMarker(new MarkerOptions().position(BH)
                    .title("BH").snippet("Brazos House")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTI, 16));
            googleMap.addMarker(new MarkerOptions().position(CENTI)
                    .title("CENTI").snippet("Centinnial Court ")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CP, 16));
            googleMap.addMarker(new MarkerOptions().position(CP)
                    .title("CP").snippet("Center Point")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CC, 16));
            googleMap.addMarker(new MarkerOptions().position(CC)
                    .title("CC").snippet("Cooper Chase")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(GC, 16));
            googleMap.addMarker(new MarkerOptions().position(GC)
                    .title("GC").snippet("Garden CLub")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MS, 16));
            googleMap.addMarker(new MarkerOptions().position(MS)
                    .title("MS").snippet("Maple Square ")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MR, 16));
            googleMap.addMarker(new MarkerOptions().position(MR)
                    .title("MR").snippet("Meadow Run")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(OAK, 16));
            googleMap.addMarker(new MarkerOptions().position(OAK)
                    .title("OAK").snippet("Oak Landing")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PECAN, 16));
            googleMap.addMarker(new MarkerOptions().position(PECAN)
                    .title("PECAN").snippet("Pecan Place")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HEIGHTS, 16));
            googleMap.addMarker(new MarkerOptions().position(HEIGHTS)
                    .title("HEIGHTS").snippet("The Heights on Pecan")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LOFTS, 16));
            googleMap.addMarker(new MarkerOptions().position(LOFTS)
                    .title("LOFTS").snippet("The Lofts at College Park")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TIMBER, 16));
            googleMap.addMarker(new MarkerOptions().position(TIMBER)
                    .title("TIMBER").snippet("Timber Brook")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TRINITY, 16));
            googleMap.addMarker(new MarkerOptions().position(TRINITY)
                    .title("TRINITY").snippet("Trinity House ")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(UV, 16));
            googleMap.addMarker(new MarkerOptions().position(UV)
                    .title("UV").snippet("University Village ")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(VH, 16));
            googleMap.addMarker(new MarkerOptions().position(VH)
                    .title("VH").snippet("Vandergriff Hall ")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(WC, 16));
            googleMap.addMarker(new MarkerOptions().position(WC)
                    .title("WC").snippet("West Crossing")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(WS, 16));
            googleMap.addMarker(new MarkerOptions().position(WS)
                    .title("WS").snippet("Woodland Springs")
                    .flat(true)
                    .draggable(true));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ZEN, 16));
            googleMap.addMarker(new MarkerOptions().position(ZEN)
                    .title("ZEN").snippet("Zen Apartments")
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
        if(place.equals("Source")) {
            source = marker.getTitle();
            AlertDialog.Builder builder = new AlertDialog.Builder(MapPane.this);
            builder.setMessage("Selected Pick up from "+marker.getTitle());
            builder.setTitle("Pick Up Location ");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            ParseQuery<ParseObject> querySource = ParseQuery.getQuery("Location");
            querySource.whereEqualTo("LocName", source);
            querySource.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    sourceArea = object.getString("CampusType");
                }
            });
        }
      // final LatLng dest = marker.getPosition();

        if (place.equals("Destination")) {
            destination = marker.getTitle();
            AlertDialog.Builder builder = new AlertDialog.Builder(MapPane.this);
            builder.setMessage("Selected Dropping at "+marker.getTitle());
            builder.setTitle("Drop Location ");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            ParseQuery<ParseObject> queryDestination = ParseQuery.getQuery("Location");
            queryDestination.whereEqualTo("LocName", destination);
            queryDestination.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    destinationArea = object.getString("CampusType");
                }
            });
        }


        return false;

    }@Override
    public void onMapReady(GoogleMap googleMap) {

    }
    /*
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapPane.this);
        builder.setMessage("Back is restricted");
        builder.setTitle("Caution");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    */
}

