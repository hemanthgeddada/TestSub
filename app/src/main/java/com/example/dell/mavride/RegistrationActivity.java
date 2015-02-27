package com.example.dell.mavride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;


public class RegistrationActivity extends Activity {

    //declaring
    protected EditText firstname;
    protected EditText lastname;
    protected EditText email;
    protected EditText phone;
    protected EditText password;
    protected EditText confirmpassword;
    protected Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Parse.initialize(this, "Z1onpLDjhguG5Xerjh1sSzCm4T6o3tgQdN4TwjiM", "7WOxDqGAYaJulOnKZdLA9huezBWyB7OuOaBwjCQ0");

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
                String firstname1 = firstname.getText().toString().trim();
                String lastname1 = lastname.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String password1 = password.getText().toString().trim();

               String phone1 = phone.getText().toString().trim();

                //get user's values and convert them to string

                //store user in parse
                ParseObject registration = new ParseObject("Registration");
                registration.put("First_Name", firstname1);
                registration.put("Last_Name", lastname1);
                registration.put("Password", password1);
                registration.put("Email", email1);
                registration.put("Phone no", phone1);


                registration.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                        if(e==null){
                            //Toast
                            Toast.makeText(RegistrationActivity.this, "You have been Successfully Registered", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MapPane.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(RegistrationActivity.this, "Some Error Occured, Try Again", Toast.LENGTH_LONG).show();
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
