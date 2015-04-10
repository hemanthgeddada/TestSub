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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
//import android.widget.Toast;


public class UserHome extends Activity {
    String objectid;
    //protected TextView UserLoggedName;
    protected Button btnr;
    protected Button btnc;
    protected Button btns;
    //private int backButtonCount=0;
    //private Toast backExitToast=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        btnr = (Button)findViewById(R.id.btnride);
        btnc = (Button)findViewById(R.id.btncride);
        btns = (Button)findViewById(R.id.btnstatus);

        Intent intent=getIntent();
        objectid = intent.getStringExtra("objectID");
        ParseUser userLogged = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Registration");
        query.whereEqualTo("UserId", userLogged.getObjectId());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    final String UserLogged = object.getString("First_Name");
                    Toast.makeText(getApplicationContext(), "Welcome " + UserLogged, Toast.LENGTH_LONG).show();
                    //UserName.setText("Welcome "+ UserLogged);
                } else {
                    // something went wrong
                }
            }
        });
        ParseQuery<ParseObject> query_pending = ParseQuery.getQuery("RideRequest");
        query_pending.whereEqualTo("RiderId", userLogged.getObjectId());
        query_pending.whereEqualTo("Status", "Pending");

        ParseQuery<ParseObject> query_pickedup = ParseQuery.getQuery("RideRequest");
        query_pickedup.whereEqualTo("RiderId", userLogged.getObjectId());
        query_pickedup.whereEqualTo("Status", "PickedUp");

        ParseQuery<ParseObject> query_waiting = ParseQuery.getQuery("RideRequest");
        query_waiting.whereEqualTo("RiderId", userLogged.getObjectId());
        query_waiting.whereEqualTo("Status", "Waiting");

        ParseQuery<ParseObject> query_unallocated = ParseQuery.getQuery("RideRequest");
        query_unallocated.whereEqualTo("RiderId", userLogged.getObjectId());
        query_unallocated.whereEqualTo("Status", "Unallocated");

        final List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query_pending);
        queries.add(query_pickedup);
        queries.add(query_waiting);
        queries.add(query_unallocated);
        btnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> compoundQuery = ParseQuery.or(queries);
                compoundQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserHome.this);
                            builder.setMessage("Can request one ride at a time");
                            builder.setTitle("Already Requested");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MapPane.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        btnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> compoundQuery = ParseQuery.or(queries);
                compoundQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(final ParseObject object, ParseException e) {
                        if (e == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserHome.this);
                            builder.setMessage("Confirm cancel ?");
                            builder.setTitle("Request Status");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    object.put("Status","Cancelled");
                                    object.saveInBackground();
                                    dialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UserHome.this);
                                    builder.setMessage("Your request has been cancelled. Thank you for using Mav Ride");
                                    builder.setTitle("Request Cancelled");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog subDialog = builder.create();
                                    subDialog.show();
                                }
                            });
                            builder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserHome.this);
                            builder.setMessage("Sorry, you don't have any pending requests");
                            builder.setTitle("No Pending Requests");
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
        });

        btns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseQuery<ParseObject> compoundQuery = ParseQuery.or(queries);
                compoundQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserHome.this);
                            if(object.get("Status").equals("Unallocated")){
                                builder.setMessage("Your Request (Id: "+object.getObjectId()+") is under process and driver will be allocated soon");
                                builder.setTitle("Request Status");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                            if(object.get("Status").equals("Pending")){
                                builder.setMessage("Your request (Id: "+object.getObjectId()+") is processed and driver is on the way");
                                builder.setTitle("Request Status");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                            if(object.get("Status").equals("Waiting")){
                                builder.setMessage("Driver is waiting for you at "+object.get("Source")+". Please reach your driver");
                                builder.setTitle("Request Status");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                            if(object.get("Status").equals("PickedUp")){
                                builder.setMessage("You are picked by a driver. Enjoy your safe ride to your destination");
                                builder.setTitle("Request Status");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserHome.this);
                            builder.setMessage("Sorry, you don't have any pending requests");
                            builder.setTitle("No Pending Requests");
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
        });


}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_home, menu);
        return true;
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
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserHome.this);
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

    //@Override
    /**
     * Back button listener.
     * Will close the application if the back button pressed twice.
     */
    //copied as it is from http://stackoverflow.com/a/16383385/2039735

    //public void onBackPressed()
    //{
        //super.onBackPressed();
        //finish();
        /*
        if(backButtonCount >= 1)
        {
            backExitToast.cancel();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            backExitToast=Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_LONG);
            backExitToast.show();
            backButtonCount++;
        }
        */
    //}
    /*
    @Override
    public void onDestroy()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }*/
    }