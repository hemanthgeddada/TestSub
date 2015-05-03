package com.example.dell.mavride;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

//import com.parse.Parse;


public class RegistrationActivity extends Activity {

    //declaring
    protected EditText firstname;
    protected EditText lastname;
    protected EditText email;
    protected EditText phone;
    protected EditText password;
    protected EditText code;
    //protected EditText confirmpassword;
    protected Button signup;
    protected static int cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
       // Parse.initialize(this, "Z1onpLDjhguG5Xerjh1sSzCm4T6o3tgQdN4TwjiM", "7WOxDqGAYaJulOnKZdLA9huezBWyB7OuOaBwjCQ0");

        //Initializing
        firstname = (EditText) findViewById(R.id.firstname_reg);
        lastname = (EditText) findViewById(R.id.lastname_reg);
        email= (EditText) findViewById(R.id.email_reg);
        phone = (EditText) findViewById(R.id.phone_reg);
        password = (EditText) findViewById(R.id.password_reg);
        signup= (Button) findViewById(R.id.signup_reg);
        code=(EditText) findViewById(R.id.email_code);
        //Listen to register button click
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String firstname1 = firstname.getText().toString().trim();
                if(firstname1.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter your First Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String lastname1 = lastname.getText().toString().trim();
                if(lastname1.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter your Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String email1 = email.getText().toString().trim();
                if(email1.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                //if the email string is not a valid email id
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
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
                final String code1 = code.getText().toString().trim();
                if(code1.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter the validation code from email", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String password1 = password.getText().toString().trim();
                if(password1.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter the password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password1.length()<6)
                {
                    Toast.makeText(getApplicationContext(), "The password has to be minimum 6 digits.", Toast.LENGTH_SHORT).show();
                    return;
                }
               final String phone1 = phone.getText().toString().trim();
                if(phone1.isEmpty()||phone1.length()<10)
                {
                    Toast.makeText(getApplicationContext(), "Please enter a valid Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                //get user's values and convert them to string
                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Registration");
                query2.whereEqualTo("Email", email1);
                query2.whereEqualTo("UserType", "Rider");
                //whereEqualTo specifies a selection condition for a particular tuple like where Email = email1 in table Registration
                query2.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        //getfirstinbackground will return exactly one result instead of find, which i think will return all results.
                        if (e == null) {
                            String objectId = parseObject.getObjectId();
                            Toast.makeText(getApplicationContext(), objectId, Toast.LENGTH_LONG).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                            //builder.setMessage(e.getMessage());
                            builder.setTitle("Email id is already registered. Use Forgot Password option.");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        //store user in parse
                        else{
                            if(!(Integer.parseInt(code1)==cd))
                            {
                                Toast.makeText(getApplicationContext(), "The validation code is invalid", Toast.LENGTH_LONG).show();
                                return;
                            }
                            ParseUser user = new ParseUser();
                            user.setUsername(email1);
                            user.setPassword(password1);
                            user.signUpInBackground(new SignUpCallback() {
                            @Override
                              public void done(com.parse.ParseException e) {
                                if (e == null) {
                                    System.out.println("inserted in user");
                                }
                                else {
                                    Toast.makeText(RegistrationActivity.this, "error inserting in user", Toast.LENGTH_LONG).show();
                                 }
                            }
                            });
                            //String objid = user.getObjectId();
                            //Toast.makeText(RegistrationActivity.this, objid, Toast.LENGTH_LONG).show();
                            ParseObject registration = new ParseObject("Registration");
                            registration.put("First_Name", firstname1);
                            registration.put("Last_Name", lastname1);
                            registration.put("Password", password1);
                            registration.put("Email", email1);
                            registration.put("PhoneNo", phone1);
                            registration.put("UserType", "Rider");
                           // registration.put("UserId",objid);
                            registration.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null){
                                        //Toast
                                        Toast.makeText(RegistrationActivity.this, "You have been Successfully Registered", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.putExtra("loginPage","true");
                                        intent.putExtra("loginUsername",email1);
                                        startActivity(intent);
                                    }else{
                                        // Login failed. Look at the ParseException to see what happened.
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                                        if(e.getCode()==ParseException.CONNECTION_FAILED)
                                        {
                                            builder.setMessage("Unable to connect to server. Please check your internet connection and try again");
                                        }
                                        else
                                        {
                                            builder.setMessage("An error occurred. Please try again or contact developers.");
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
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);

    }*/
    public void sendEmail(View view)
    {
        String email1=((EditText)findViewById(R.id.email_reg)).getText().toString().trim();
        if(email1.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Please enter an email id to send the code to.",Toast.LENGTH_LONG).show();
            return;
        }
        //if the email string is not a valid email id
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email1).matches()) {
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
        cd = (int) (Math.random() * 9000) + 1000;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        new SendEmailAsyncTask().execute();
        Toast.makeText(getApplicationContext(), "Mail sent successfully", Toast.LENGTH_LONG).show();
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
            String[] toArr = {((EditText)findViewById(R.id.email_reg)).getText().toString()};
            m.setTo(toArr);
            m.setFrom("mavrideuta@gmail.com");
            m.setSubject("MavRide App Validation Code");
            m.setBody("Hello,\nYour validation code for MavRide App is "+cd+".\n\nRegards,\n-Team MavRide");
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
