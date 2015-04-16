package com.example.dell.mavride;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class DriverHomePage extends ListActivity {
    String objectid;
    protected List<ParseObject> request;
    //protected TextView UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home_page);
        DriverAllocation allocate = new DriverAllocation();
        allocate.allocation();
        Intent intent=getIntent();
        objectid = intent.getStringExtra("objectID");
        ParseUser userLogged = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Registration");
        query.whereEqualTo("UserId", userLogged.getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    final String UserLogged = object.getString("First_Name");
                    //Toast.makeText(getApplicationContext(), "Welcome " + UserLogged, Toast.LENGTH_LONG).show();
                    //UserName.setText("Welcome "+ UserLogged);
                } else {
                    // something went wrong
                    Toast.makeText(getApplicationContext(), "Severe error", Toast.LENGTH_LONG).show();
                }
            }
        });

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("RideRequest");
        query1.whereEqualTo("DriverId", userLogged.getObjectId());
        query1.whereEqualTo("Status", "Pending");

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("RideRequest");
        query2.whereEqualTo("DriverId", userLogged.getObjectId());
        query2.whereEqualTo("Status", "PickedUp");

        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("RideRequest");
        query3.whereEqualTo("DriverId", userLogged.getObjectId());
        query3.whereEqualTo("Status", "Waiting");

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);
        queries.add(query3);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByAscending("createdAt");
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    if(parseObjects.size()>0){
                        request = parseObjects;
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
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomePage.this);
                    builder.setMessage(e.getMessage());
                    builder.setTitle("Login Failed");
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

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.Logout) {
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
        }*/
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
