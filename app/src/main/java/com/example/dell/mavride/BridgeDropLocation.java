package com.example.dell.mavride;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
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
import com.parse.SaveCallback;

import java.util.Date;


public class BridgeDropLocation extends Activity {
    String objectid;
    protected Button btnNHBridge;
    public String source;
    public String destination;
    public Date requestCreated;
    public int riders;
    public String riderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_drop_location);
        btnNHBridge = (Button)findViewById(R.id.btnBridgeNH);
        Intent intent=getIntent();
        objectid=intent.getStringExtra("objectID");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");

        query.getInBackground(objectid, new GetCallback<ParseObject>() {
            public void done(ParseObject Riderequest, ParseException e) {
                if (e == null) {
                    source = Riderequest.getString("Source");
                    destination = Riderequest.getString("destination");
                    requestCreated = Riderequest.getCreatedAt();
                    riders = Riderequest.getInt("NoRiders");
                    riderId = Riderequest.getString("RiderId");
                    Toast.makeText(getApplicationContext(), objectid, Toast.LENGTH_LONG).show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "error in main", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnNHBridge.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");

                query.getInBackground(objectid, new GetCallback<ParseObject>() {
                    public void done(ParseObject Riderequest, ParseException e) {
                        if (e == null) {
                            Riderequest.put("Destination","NHBridge");
                            Riderequest.saveInBackground();
                            ParseObject newRequest = new ParseObject("RideRequest");
                            newRequest.put("Source", "NHBridge");
                            newRequest.put("Destination", destination);
                            newRequest.put("createdAt", requestCreated);
                            newRequest.put("NoRiders", riders);
                            newRequest.put("RiderId", riderId);
                            newRequest.put("Status", "Unallocated");
                            newRequest.put("BridgeUsage", true);
                            newRequest.put("CampusChange", true);
                            newRequest.put("Priority", 1);
                            newRequest.saveInBackground();
                            Toast.makeText(getApplicationContext(), objectid, Toast.LENGTH_LONG).show();
                            ParseQuery<ParseObject> loc = ParseQuery.getQuery("Location");
                            loc.whereEqualTo("LocName",destination);
                            loc.getFirstInBackground(new GetCallback<ParseObject>() {
                                                         public void done(ParseObject object, ParseException e) {
                                                            String area = object.getString("CampusType");
                                                             DriverAllocationPriority allocationPriority = new DriverAllocationPriority();
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



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bridge_drop_location, menu);
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
