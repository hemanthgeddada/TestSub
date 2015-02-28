package com.example.dell.mavride;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


public class ForgotPasswordFormActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_form);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_password_form, menu);
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
    public void onClickHaveCode(View view)
    {
        //System.out.println("have code clicked");
        EditText editTextResetCode=(EditText)findViewById(R.id.editTextResetCode);
        editTextResetCode.setVisibility(View.VISIBLE);
        if(((Button)findViewById(R.id.buttonHaveCode)).getText().equals("Validate Code"))
        {
            //toast and return if email empty
            //validate code and email
            Toast.makeText(getApplicationContext(),"Code Validation",Toast.LENGTH_SHORT).show();
        }
        ((Button)findViewById(R.id.buttonHaveCode)).setText("Validate Code");
    }
    public void onClickSendCode(View view)
    {
        //EditText editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        if(((EditText)findViewById(R.id.editTextEmail)).getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new SendEmailAsyncTask().execute();
        Toast.makeText(getApplicationContext(),"Mail sent successfully",Toast.LENGTH_SHORT).show();
    }
    class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Mail m = new Mail("mavrideuta@gmail.com", "team2csallner");
        public SendEmailAsyncTask() {
            if (BuildConfig.DEBUG)
                Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");
            String[] toArr = {((EditText)findViewById(R.id.editTextEmail)).getText().toString()};
            m.setTo(toArr);
            m.setFrom("mavrideuta@gmail.com");
            m.setSubject("MavRide App Password Reset Code");
            int code=(int)(Math.random()*9000)+1000;
            //ALSO STORE THIS CODE IN PARSE AGAINST THE USER'S EMAIL TO VALIDATE WHEN USER ENTERS THIS IN APP
            m.setBody("Hello,\nYour password reset code for MavRide App is "+code+".\n\nRegards,\n-Team MavRide");
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            if (BuildConfig.DEBUG) Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
            try {
                m.send();
                return true;
            } catch (AuthenticationFailedException e) {
                Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
                e.printStackTrace();
                return false;
            } catch (MessagingException e) {
                Log.e(SendEmailAsyncTask.class.getName(), /*m.getTo(null)*/"something god" + "failed");
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
