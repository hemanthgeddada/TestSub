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
        // Initialise three buttons
        pickupbtn=(Button)findViewById(R.id.btnPick);
        waitbtn=(Button)findViewById(R.id.btnWait);
        dropbtn = (Button)findViewById(R.id.btnDrop);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");
        query.getInBackground(objectid,new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                String status = parseObject.getString("Status");
                //Disabling the buttons based on conditions
                if(status.equals("Pending")){
                    dropbtn.setEnabled(false);
                }
                if(status.equals("PickedUp")){
                    waitbtn.setEnabled(false);
                    pickupbtn.setEnabled(false);
                }
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_options, menu);
        return true;
    }
    // Back is restricted
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
