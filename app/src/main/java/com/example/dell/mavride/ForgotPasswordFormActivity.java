package com.example.dell.mavride;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;


public class ForgotPasswordFormActivity extends Activity {

    protected String email1=null;
    protected int code=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_form);
        ((EditText)findViewById(R.id.editTextEmail)).addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                (findViewById(R.id.buttonSendCode)).setEnabled(true);
                ((Button)findViewById(R.id.buttonSendCode)).setText("Send Email");
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
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
            //Toast.makeText(getApplicationContext(),"Code Validation",Toast.LENGTH_SHORT).show();
            if(editTextResetCode.getText().toString().trim().isEmpty() || editTextResetCode.getText().toString().trim().length()<4)
            {
                Toast.makeText(getApplicationContext(),"The code needs to be of 4 digits",Toast.LENGTH_SHORT).show();
                return;
            }
            int code=Integer.parseInt(editTextResetCode.getText().toString());
            email1=((EditText)findViewById(R.id.editTextEmail)).getText().toString();
            if(email1.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
                Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }
            //if the email string doesn't end with @mavs.uta.edu
            if (!email1.endsWith("@mavs.uta.edu"))
            {
                //email is validated to be correct
                Toast.makeText(getApplicationContext(),"You need to enter your @mavs.uta.edu email address",Toast.LENGTH_SHORT).show();
                return;
            }
            ParseQuery<ParseObject> query=ParseQuery.getQuery("forgot_password");
            query.whereEqualTo("email",email1);
            query.whereEqualTo("code",code);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if(e==null)
                    {
                        //code found
                        System.out.print("to new intent");
                        Intent intent=new Intent(getApplicationContext(), setNewPasswordActivity.class);
                        intent.putExtra("email",email1);
                        startActivity(intent);
                    }
                    else
                    {
                        //code and email not found
                        Toast.makeText(getApplicationContext(),"Your code is invalid. Please check or send email to your email id for the code first",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        ((Button)findViewById(R.id.buttonHaveCode)).setText("Validate Code");
    }
        public void onClickSendCode(View view)
    {
        email1=((EditText)findViewById(R.id.editTextEmail)).getText().toString();
        if(email1.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }
        //if the email string doesn't end with @mavs.uta.edu
        if (!email1.endsWith("@mavs.uta.edu"))
        {
            //email is validated to be correct
            Toast.makeText(getApplicationContext(),"You need to enter your @mavs.uta.edu email address",Toast.LENGTH_SHORT).show();
            return;
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Registration");
        query.whereEqualTo("Email", email1);
        (findViewById(R.id.buttonSendCode)).setEnabled(false);
        ((Button)findViewById(R.id.buttonSendCode)).setText("Code Being Sent");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                //getfirstinbackground will return exactly one result instead of find, which i think will return all results.
                if (e == null) {
                    //no exception, result found with email
                    //so send the email
                    ParseObject fpwd = new ParseObject("forgot_password");
                    fpwd.put("email", email1);
                    code = (int) (Math.random() * 9000) + 1000;
                    fpwd.put("code", code);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    new SendEmailAsyncTask().execute();
                    Toast.makeText(getApplicationContext(), "Mail sent successfully", Toast.LENGTH_SHORT).show();
                    fpwd.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //success
                                //Toast
                            } else {
                                Toast.makeText(getApplicationContext(),"Unfortunately, an error occurred. Please try again",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    // Login failed. Look at the ParseException to see what happened.
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordFormActivity.this);
                    if(e.getCode()==ParseException.CONNECTION_FAILED)
                    {
                        builder.setMessage("Unable to connect to server. Please check your internet connection and try again");
                    }
                    else
                    {
                        builder.setMessage("The Email ID is not registered.");
                    }
                    builder.setTitle("Error");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    (findViewById(R.id.buttonSendCode)).setEnabled(true);
                    ((Button)findViewById(R.id.buttonSendCode)).setText("Send Email");
                }
            }
        });
        /*THE SECTION FROM
        **START**
         *  TO
          *  **END**
          *  WAS COPIED/REFERRED FROM
          *  http://stackoverflow.com/a/13470775/2039735
          *
           *  Javamail-android api libraries activation.jar, additionnal.jar and mail.jar
           *  are taken from
           *  https://code.google.com/p/javamail-android/downloads/list
         */
        //**START**
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
            //ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Registration");
            //query2.whereEqualTo("Email", email1);
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
    //**END**
}
