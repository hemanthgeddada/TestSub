package com.example.dell.mavride;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class DriverOptions extends Activity {
    String objectid;

    protected Button pickupbtn;
    protected Button waitbtn;
    protected Button dropbtn;
    protected Button brdgebtn;
    public String source;
    public String destination;
    public String cartArea;
    public String sourceArea;
    public String destinationArea;
    public String status;
    public boolean campusChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_options);

        Intent intent = getIntent();
        objectid = intent.getStringExtra("objectID");
        // Initialise three buttons
        pickupbtn = (Button) findViewById(R.id.btnPick);
        waitbtn = (Button) findViewById(R.id.btnWait);
        dropbtn = (Button) findViewById(R.id.btnDrop);
        brdgebtn = (Button) findViewById(R.id.btnBridge);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");
        query.getInBackground(objectid, new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                status = parseObject.getString("Status");
                source = parseObject.getString("Source");
                campusChange = parseObject.getBoolean("CampusChange");
                destination = parseObject.getString("Destination");
                //Disabling the buttons based on conditions
                if (status.equals("Pending")) {
                    dropbtn.setEnabled(false);

                }
                if (status.equals("PickedUp")) {
                    waitbtn.setEnabled(false);
                    pickupbtn.setEnabled(false);
                    //brdgebtn.setEnabled(false);
                }
                boolean locationArea = parseObject.getBoolean("CampusChange");
                ParseUser currentUser = ParseUser.getCurrentUser();
                String obj = currentUser.getObjectId();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("DriverDetail");
                query.whereEqualTo("DriverId", obj);
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        String currentLocation = object.getString("CurrentLocation");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Location");
                        query.whereEqualTo("LocName", currentLocation);
                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                            public void done(ParseObject object, ParseException e) {
                                final String cartArea = object.getString("CampusType");
                                ParseQuery<ParseObject> destinationquery = ParseQuery.getQuery("Location");
                                destinationquery.whereEqualTo("LocName", destination);
                                destinationquery.getFirstInBackground(new GetCallback<ParseObject>() {
                                    public void done(ParseObject object, ParseException e) {
                                        String destinationArea = object.getString("CampusType");
                                        if (campusChange) {
                                           // Toast.makeText(getApplicationContext(), "campus success", Toast.LENGTH_LONG).show();
                                            brdgebtn.setEnabled(false);
                                        } else {
                                            if (status.equals("Pending")) {
                                                ParseQuery<ParseObject> avalDrivers = ParseQuery.getQuery("DriverDetail");
                                                avalDrivers.whereEqualTo("DriverStatus", "Online");
                                                avalDrivers.findInBackground(new FindCallback<ParseObject>() {
                                                    @Override
                                                    public void done(List<ParseObject> unAllocate, ParseException e) {
                                                        if (e == null) {
                                                            if (unAllocate.size() > 1) {
                                                                for (int i = 0; i < unAllocate.size(); i++) {
                                                                    ParseObject cLocationArea = unAllocate.get(i);
                                                                    String cLocation = cLocationArea.getString("CurrentLocation");
                                                                    ParseQuery<ParseObject> area = ParseQuery.getQuery("Location");
                                                                    area.whereEqualTo("LocName", cLocation);
                                                                    area.getFirstInBackground(new GetCallback<ParseObject>() {
                                                                        public void done(ParseObject object, ParseException e) {
                                                                            if (e == null) {
                                                                                String dvr2Area = object.getString("CampusType");
                                                                                if (dvr2Area.equals(cartArea)) {
                                                                                    //do nothing
                                                                                } else {
                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(DriverOptions.this);
                                                                                    builder.setMessage("There is a driver available at the bridge. Do you want to drop at bridge and raise a request for Pick Up?");
                                                                                    builder.setTitle("Request Status");
                                                                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int i) {
                                                                                            pickupbtn.setEnabled(false);
                                                                                            waitbtn.setEnabled(false);
                                                                                        }
                                                                                    });
                                                                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            dialog.dismiss();
                                                                                        }
                                                                                    });
                                                                                    AlertDialog dialog = builder.create();
                                                                                    dialog.show();
                                                                                }

                                                                            } else {
                                                                                Toast.makeText(getApplicationContext(), "error in bridge dialog box part", Toast.LENGTH_LONG).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "error in taking online driver part", Toast.LENGTH_LONG).show();
                                                                brdgebtn.setEnabled(false);
                                                            }
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "error in taking online driver part", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                brdgebtn.setEnabled(false);
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });



        // Changing the status to pickedup
        pickupbtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");

                query.getInBackground(objectid, new GetCallback<ParseObject>() {
                    public void done(ParseObject Riderequest, ParseException e) {
                        if (e == null) {

                            // String rideStatus=RideRequest.getString("Status");
                            Riderequest.put("Status","PickedUp");
                            Riderequest.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        Toast.makeText(getApplicationContext(), "Rider is Picked", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), DriverHomePage.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "update failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                        else{
                            //error
                        }
                    }
                });
            }

        });
        // Change the status to waiting and timer starts
        waitbtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(), DriverTimer.class);
                intent.putExtra("objectId",objectid);
                startActivity(intent);
            }

        });

        // Change the status to dropped
        dropbtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");
                query.getInBackground(objectid, new GetCallback<ParseObject>() {
                    public void done(ParseObject RideRequest, ParseException e) {
                        if (e == null) {
                            RideRequest.put("Status","Dropped");
                            RideRequest.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e == null){
                                        Toast.makeText(getApplicationContext(), "Rider is Dropped", Toast.LENGTH_LONG).show();
                                        ParseUser currentUser = ParseUser.getCurrentUser();
                                        String cUser = currentUser.getObjectId();
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("DriverDetail");
                                        query.whereEqualTo("DriverId", cUser);
                                        query.getFirstInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject parseObject, ParseException e) {
                                                if(e==null){
                                                    parseObject.put("CurrentLocation",destination);
                                                    parseObject.saveInBackground();
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });
                                        Intent intent = new Intent(getApplicationContext(), DriverHomePage.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "update failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });

            }

        });

        brdgebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bridgeLocation = new Intent(getApplicationContext(), BridgeDropLocation.class);
                //bridgeLocation.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                bridgeLocation.putExtra("objectID",objectid);
                startActivity(bridgeLocation);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_options, menu);
        return true;
    }
    /*/ Back is restricted
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverOptions.this);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Logging out and setting the default values for the driver
        /*if (id == R.id.Logout) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            String obj = currentUser.getObjectId();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("DriverDetail");
            query.whereEqualTo("DriverId", obj);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        object.put("DriverStatus","Offline");
                        object.put("NoAssigned",0);
                        object.put("CurrentLocation","UC");
                        object.saveInBackground();
                    } else {
                        // something went wrong
                        Toast.makeText(getApplicationContext(), "status changed", Toast.LENGTH_LONG).show();
                    }
                }
            });
            ParseUser.logOut();
            Intent loginActivity = new Intent(getApplicationContext(), Login.class);
            loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginActivity);
            return true;
        }*/
        if(id==R.id.override_buttons)
        {
            ((Button)findViewById(R.id.btnPick)).setEnabled(true);
            ((Button)findViewById(R.id.btnWait)).setEnabled(true);
            ((Button)findViewById(R.id.btnDrop)).setEnabled(true);
        }
        return super.onOptionsItemSelected(item);
    }
}
