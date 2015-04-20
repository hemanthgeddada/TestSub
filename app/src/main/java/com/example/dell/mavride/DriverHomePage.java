package com.example.dell.mavride;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class DriverHomePage extends ListActivity {
    String objectid;
    protected List<ParseObject> request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home_page);
        // Calling Driver allocation class to allocate the drivers to the requests
        DriverAllocation allocate = new DriverAllocation();
        allocate.allocation();
        Intent intent=getIntent();
        objectid = intent.getStringExtra("objectID");
        ParseUser userLogged = ParseUser.getCurrentUser();
        // Retrieving the requests of the driver to send to request adapter
        ParseQuery<ParseObject> status_Pending = ParseQuery.getQuery("RideRequest");
        status_Pending.whereEqualTo("DriverId", userLogged.getObjectId());
        status_Pending.whereEqualTo("Status", "Pending");

        ParseQuery<ParseObject> status_waiting = ParseQuery.getQuery("RideRequest");
        status_waiting.whereEqualTo("DriverId", userLogged.getObjectId());
        status_waiting.whereEqualTo("Status", "Waiting");

        ParseQuery<ParseObject> status_Pickedup = ParseQuery.getQuery("RideRequest");
        status_Pickedup.whereEqualTo("DriverId", userLogged.getObjectId());
        status_Pickedup.whereEqualTo("Status", "PickedUp");

        List<ParseQuery<ParseObject>> compoundQuery = new ArrayList<ParseQuery<ParseObject>>();
        compoundQuery.add(status_Pending);
        compoundQuery.add(status_waiting);
        compoundQuery.add(status_Pickedup);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(compoundQuery);
        mainQuery.orderByAscending("createdAt");
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    if(parseObjects.size()>0){
                        request = parseObjects;
                        //Calling request adapter class
                        RequestAdapter adapter = new RequestAdapter(getListView().getContext(), request);
                        setListAdapter(adapter);
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomePage.this);
                        builder.setMessage("No Pending Requests. Please refresh after some time");
                        builder.setTitle("No Requests");
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
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_home_page, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Logging out and setting the default values for the driver
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
            Intent loginActivity = new Intent(getApplicationContext(), Login.class);
            loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginActivity);

            return true;
        }
        if (id == R.id.Refresh) {
            Intent intent = getIntent();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long Id){
        super.onListItemClick(l, v, position, Id);

        ParseObject statusObject = request.get(position);
        String objectId = statusObject.getObjectId();

        //Toast.makeText(getApplicationContext(), objectId, Toast.LENGTH_LONG).show();

        Intent options = new Intent(DriverHomePage.this, DriverOptions.class);
       options.putExtra("objectID",objectId);

        startActivity(options);
    }
    // Restrict the back button
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomePage.this);
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
