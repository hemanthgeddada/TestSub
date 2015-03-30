package com.example.dell.mavride;

import android.app.Activity;
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
        btnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapPane.class);
                startActivity(intent);
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