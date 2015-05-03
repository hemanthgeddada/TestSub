package com.example.dell.mavride;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;


public class DriverHomePage extends ListActivity {
    //String objectid;
    protected static List<ParseObject> request;
    public static Thread refreshThread;
    public String[] currentAllocated;
    public DriverHomePage()
    {
        if(GlobalResources.REFRESH_THREAD==0) {
            GlobalResources.REFRESH_THREAD=1;
            refresh();
            asyncRefresh();
        }
        currentAllocated=new String[3];
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home_page);
        // Calling Driver allocation class to allocate the drivers to the requests
        //Intent intent=getIntent();
        //objectid = intent.getStringExtra("objectID");
        //asyncRefresh();
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
           int i=0;
           while(i<currentAllocated.length) {
               ParseQuery<ParseObject> query2 = ParseQuery.getQuery("RideRequest");
               try {
                   ParseObject po = query2.get(currentAllocated[i]);
                   if (po.getString("Status").equals("PickedUp")) {
                       AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomePage.this);
                       builder.setMessage("You cannot logout when you have picked up some riders. Please drop them first.");
                       builder.setTitle("Error");
                       builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int i) {
                               dialog.dismiss();
                           }
                       });
                       AlertDialog dialog = builder.create();
                       dialog.show();
                       return false;
                   }
               } catch (ParseException ei) {
                   System.out.println(ei);
               }
               i++;
           }
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
           GlobalResources.REFRESH_THREAD=0;
           refreshThread.interrupt();
            Intent loginActivity = new Intent(getApplicationContext(), Login.class);
            loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginActivity);
            return true;
        }
        /*if (id == R.id.Refresh) {
            asyncRefresh();
        }
        */
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
        refreshThread.interrupt();
        GlobalResources.REFRESH_THREAD = 0;
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
    public void asyncRefresh() {
        // do something long
            Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int i=0;
                while(GlobalResources.REFRESH_THREAD!=0){
                    System.out.println("i ki value: "+i);
                    //Toast.makeText(getApplicationContext(),"Refreshing list",Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(5000);
                        refresh();
                    }catch(InterruptedException e)
                    {
                        System.out.println("Refresh Thread Interrupted");
                    }
                    i++;
                }
            }
        };
        refreshThread=new Thread(runnable);
        refreshThread.start();
    }
    public void refresh()
    {
        DriverAllocation allocate = new DriverAllocation();
        allocate.allocation();
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
                    int i=0;
                    while(i<currentAllocated.length)
                    {
                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("RideRequest");
                        try {
                            ParseObject po = query2.get(currentAllocated[i]);
                            if(po.getString("Status").equals("Cancelled"))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomePage.this);
                                builder.setMessage("A request was cancelled.");
                                builder.setTitle("Alert");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }catch(ParseException ei)
                        {
                            System.out.println(ei);
                        }
                        //whereEqualTo specifies a selection condition for a particular tuple like where Email = email1 in table Registration
                        i++;
                    }
                    i=0;
                    currentAllocated=new String[3];
                    while(i<parseObjects.size())
                    {
                        //if(parseObjects.get(i).getString("Status").equals("Pending")||parseObjects.get(i).getString("Status").equals("Waiting"))
                        currentAllocated[i]=(parseObjects.get(i).getObjectId());
                        i++;
                    }
                    i=0;
                    while(i<currentAllocated.length)
                    {
                        System.out.println("currentAllocated["+i+"]="+currentAllocated[i]);
                        i++;
                    }
                    request = parseObjects;
                    //System.out.println("size: "+currentAllocated.size()+"-"+currentAllocated.containsKey("SbTxSj8Tyo"));
                    //Calling request adapter class
                    RequestAdapter adapter = new RequestAdapter(getListView().getContext(), request);
                    setListAdapter(adapter);
                    if(parseObjects.size()>0){

                    }
                    else{
                        Toast.makeText(DriverHomePage.this,"No requests. Checking again in some time",Toast.LENGTH_SHORT).show();
                        /*AlertDialog.Builder builder = new AlertDialog.Builder(DriverHomePage.this);
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
                        */
                    }
                }
            }
        });
    }
}
