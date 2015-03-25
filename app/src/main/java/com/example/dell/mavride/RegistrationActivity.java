package com.example.dell.mavride;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
//import com.parse.Parse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class RegistrationActivity extends Activity {

    //declaring
    protected EditText firstname;
    protected EditText lastname;
    protected EditText email;
    protected EditText phone;
    protected EditText password;
    //protected EditText confirmpassword;
    protected Button signup;
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
                final String password1 = password.getText().toString().trim();
                if(password1.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter your Password", Toast.LENGTH_SHORT).show();
                    return;
                }

               final String phone1 = phone.getText().toString().trim();
                if(phone1.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter your Phone Number", Toast.LENGTH_SHORT).show();
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
                            builder.setTitle("Email id is already registered");
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
                            ParseUser user = new ParseUser();
                            user.setUsername(email1);
                            user.setPassword(password1);
                            user.signUpInBackground(new SignUpCallback() {
                            @Override
                              public void done(com.parse.ParseException e) {
                                if (e == null) {
                                    Toast.makeText(RegistrationActivity.this, "inserted in user", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    //error
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
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(RegistrationActivity.this, "Some Error Occured, Try Again", Toast.LENGTH_LONG).show();
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
