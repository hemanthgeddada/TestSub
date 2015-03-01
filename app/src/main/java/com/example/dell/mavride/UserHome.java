package com.example.dell.mavride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class UserHome extends Activity {
    protected Button btnr;
    protected Button btnc;
    protected Button btns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        btnr = (Button)findViewById(R.id.btnLogin2);
        btnc = (Button)findViewById(R.id.btnLogin2);
        btns = (Button)findViewById(R.id.btnLogin2);

        btnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MapPane.class);
                startActivity(intent);
            }


    });


}
    }
