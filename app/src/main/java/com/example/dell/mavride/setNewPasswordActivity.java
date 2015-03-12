package com.example.dell.mavride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class setNewPasswordActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_new_password, menu);
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
    public void onClickCancel(View view)
    {
        finish();
    }
    public void onClickSetPassword(View view)
    {
        String newPassword=((EditText)findViewById(R.id.newPasswordEditText)).getText().toString().trim();
        String newPasswordConfirm=((EditText)findViewById(R.id.newPasswordConfirmEditText)).getText().toString().trim();
        if(newPassword.isEmpty() || newPasswordConfirm.isEmpty() || !newPassword.equals(newPasswordConfirm))
        {
            Toast t=Toast.makeText(getApplicationContext(),"Please enter same password in both fields",Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        //delete parse objects from forgot_password
        //update in registration object
        Toast.makeText(getApplicationContext(),"You can now login with your new password",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
