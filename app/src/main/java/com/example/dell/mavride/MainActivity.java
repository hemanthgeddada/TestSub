package com.example.dell.mavride;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Z1onpLDjhguG5Xerjh1sSzCm4T6o3tgQdN4TwjiM", "7WOxDqGAYaJulOnKZdLA9huezBWyB7OuOaBwjCQ0");
        SharedPreferences settings = getSharedPreferences(GlobalResources.PREFS_NAME, 0);
        if(settings.getString("username",null)!=null)
        {
            Intent intent=new Intent(this,Login.class);
            intent.putExtra("username",settings.getString("username",null));
            intent.putExtra("password",settings.getString("password",null));
            intent.putExtra("mode","persistent");
            startActivity(intent);
        }
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
        if(getIntent().getStringExtra("loginPage")!=null&&getIntent().getStringExtra("loginPage").equals("true"))
        {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.putExtra("loginUsername",getIntent().getStringExtra("loginUsername"));
            startActivity(intent);
        }
    }
    public void onClickMainHelp(View view)
    {
        startActivity(new Intent(this,HelpActivity.class));
    }
}