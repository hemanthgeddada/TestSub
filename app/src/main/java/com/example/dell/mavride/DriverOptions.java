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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class DriverOptions extends Activity {
    String objectid;

    protected Button pickupbtn;
    protected Button waitbtn;
    protected Button dropbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_options);

        Intent intent=getIntent();
        objectid=intent.getStringExtra("objectID");
        //Toast.makeText(getApplicationContext(), objectid, Toast.LENGTH_LONG).show();
        pickupbtn=(Button)findViewById(R.id.btnPick);
        waitbtn=(Button)findViewById(R.id.btnWait);
        dropbtn = (Button)findViewById(R.id.btnDrop);
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
        waitbtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(), DriverTimer.class);
                intent.putExtra("objectId",objectid);
                startActivity(intent);
            }

        });


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
                                        Intent intent = new Intent(getApplicationContext(), DriverHomePage.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "update failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            //error
                        }
                    }
                });

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_options, menu);
        return true;
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Logout) {
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
            Intent userhome = new Intent(getApplicationContext(), Login.class);
            startActivity(userhome);
            return true;
        }

        return super.onOptionsItemSelected(item);
    } */
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
}
