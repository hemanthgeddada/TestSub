package com.example.dell.mavride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class DriverOptions extends Activity {
    String objectid;

    protected Button pickupbtn;
    protected Button waitbtn;
    protected Button dropbtn = (Button)findViewById(R.id.btnDrop);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_options);

        Intent intent=getIntent();
        objectid=intent.getStringExtra("objectID");

        pickupbtn=(Button)findViewById(R.id.btnPick);
        waitbtn=(Button)findViewById(R.id.btnWait);


        pickupbtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");
                query.getInBackground(objectid, new GetCallback<ParseObject>() {
                    public void done(ParseObject RideRequest, ParseException e) {
                        if (e == null) {

                            String rideStatus=RideRequest.getString("Status");
                            RideRequest.put("Status","PickedUp");
                        }else{
                            //error
                        }
                    }
                });
            }

        });
        waitbtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");
                query.getInBackground(objectid, new GetCallback<ParseObject>() {
                    public void done(ParseObject RideRequest, ParseException e) {
                        if (e == null) {

                            String rideStatus=RideRequest.getString("Status");
                            RideRequest.put("Status","Waiting");
                        }else{
                            //error
                        }
                    }
                });

            }

        });
        dropbtn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");
                query.getInBackground(objectid, new GetCallback<ParseObject>() {
                    public void done(ParseObject RideRequest, ParseException e) {
                        if (e == null) {

                            String rideStatus=RideRequest.getString("Status");
                            RideRequest.put("Status","Dropped");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
