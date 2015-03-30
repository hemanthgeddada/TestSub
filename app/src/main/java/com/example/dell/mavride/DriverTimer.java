package com.example.dell.mavride;

/**
 * Created by Hemanth on 3/29/2015.
 */
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.concurrent.TimeUnit;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("NewApi")

public class DriverTimer extends Activity {

    String objectid;

    protected Button stopButton ;

    protected TextView tV;
    protected TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_timer);

        Intent intent=getIntent();
        objectid=intent.getStringExtra("objectId");
        Toast.makeText(getApplicationContext(), objectid, Toast.LENGTH_LONG).show();


        stopButton=(Button)findViewById(R.id.stopbutton);
        tV=(TextView)findViewById(R.id.textViewTimer);

        tV.setText("00:05:00");
        final CounterClass clock=new CounterClass(300000,1000);
        Toast.makeText(getApplicationContext(), objectid, Toast.LENGTH_LONG).show();
        clock.start();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");

        query.getInBackground(objectid, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject RideRequest, com.parse.ParseException e) {
                if (e == null) {

                    RideRequest.put("Status","Waiting");
                    RideRequest.saveInBackground();
                }else{
                    //error
                }
            }
        });






        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clock.cancel();
                Intent intent = new Intent(getApplicationContext(), DriverOptions.class);
                startActivity(intent);
            }
        });

    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")

    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture,long countDownInterval) {
            super(millisInFuture,countDownInterval );
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long millis= millisUntilFinished;
            String hms=String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis)-TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            tV.setText(hms);
        }

        @Override
        public void onFinish() {
            //tV.setText("Completed");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("RideRequest");
            query.getInBackground(objectid, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject RideRequest, com.parse.ParseException e) {
                    if (e == null) {

                        //String rideStatus=RideRequest.getString("Status");
                        RideRequest.put("Status","Cancelled");
                        RideRequest.saveInBackground();
                        AlertDialog.Builder builder = new AlertDialog.Builder(DriverTimer.this);
                        builder.setMessage("Waiting time Expired, choose next Ride");
                        builder.setTitle("Ride Cancelled");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), DriverHomePage.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }else{

                        //error
                    }
                }
            });
        }
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
