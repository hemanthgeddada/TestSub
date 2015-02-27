package com.example.dell.mavride;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseObject;


public class RegistrationActivity extends Activity {

    //declaring
    protected EditText username;
    protected EditText email;
    protected EditText phone;
    protected EditText password;
    protected EditText confirmpassword;
    protected Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Initializing
        username = (EditText) findViewById(R.id.username_reg);
        email= (EditText) findViewById(R.id.email_reg);
        phone = (EditText) findViewById(R.id.phone_reg);
        password = (EditText) findViewById(R.id.password_reg);

        signup= (Button) findViewById(R.id.signup_reg);

        //Listen to register button click
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username1 = username.getText().toString().trim();
                String email1 = email.getText().toString().trim();
                String password1 = password.getText().toString().trim();

               String phone1 = phone.getText().toString().trim();

                //get user's values and convert them to string

                //store user in parse
                ParseObject signup = new ParseObject("Sign_Up");
                signup.put("User_name", username1);
                signup.put("Password", password1);
                signup.put("Email", email1);
                signup.put("Phone no", phone1);


                signup.saveInBackground();

                //Toast
                //Toast.makeText(RegistrationActivity.this, "You have been Successfully Registered", Toast.LENGTH_LONG).show();

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
