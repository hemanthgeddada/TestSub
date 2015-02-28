package com.example.dell.mavride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.Parse;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Z1onpLDjhguG5Xerjh1sSzCm4T6o3tgQdN4TwjiM", "7WOxDqGAYaJulOnKZdLA9huezBWyB7OuOaBwjCQ0");
        //ParseUser user = new ParseUser();
        //user.setUsername("Hemanth");
        //user.setPassword("12345");
        //user.setEmail("hemanthgeddada@gmail.com");

        //user.signUpInBackground(new SignUpCallback() {
            //@Override
          //  public void done(com.parse.ParseException e) {
            //    if (e == null) {
                    // Hooray! Let them use the app now.
              //  } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                //}
            //}
        //});

        Button btn = (Button) findViewById(R.id.btnLogin);
        Button btn1 = (Button) findViewById(R.id.btnRegister);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}
