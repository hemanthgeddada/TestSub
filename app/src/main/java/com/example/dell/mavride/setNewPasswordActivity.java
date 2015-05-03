package com.example.dell.mavride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class setNewPasswordActivity extends Activity {
    String email;
    String oldPassword;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        email=getIntent().getStringExtra("email");
        intent=new Intent(this,Login.class);
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
        final String newPassword=((EditText)findViewById(R.id.newPasswordEditText)).getText().toString().trim();
        String newPasswordConfirm=((EditText)findViewById(R.id.newPasswordConfirmEditText)).getText().toString().trim();
        if(newPassword.isEmpty() || newPasswordConfirm.isEmpty() || !newPassword.equals(newPasswordConfirm))
        {
            Toast t=Toast.makeText(getApplicationContext(),"Please enter same password in both fields",Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        if(newPassword.length()<6)
        {
            Toast.makeText(getApplicationContext(), "The password needs to be atleast 6 digits", Toast.LENGTH_SHORT).show();
            return;
        }
       //update in registration object-0o;'//
       ParseQuery<ParseObject> query=ParseQuery.getQuery("Registration");
        query.whereEqualTo("Email",email);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(e==null) {
                    oldPassword=parseObject.getString("Password");
                    parseObject.put("Password", newPassword);
                    parseObject.saveInBackground();
                    ParseUser.logInInBackground(email, oldPassword, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            if (e == null) {
                                parseUser.setPassword(newPassword);
                                try {
                                    parseUser.save();
                                }catch (ParseException pe)
                                {
                                    System.out.println("an error occured while saving password"+pe.getMessage());
                                }
                                ParseUser.logOut();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error logging in during password change", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //delete parse objects from forgot_password
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("forgot_password");
                    query1.whereEqualTo("email", email);
                    query1.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            int i=0;
                            while(i<parseObjects.size())
                            {
                                //System.out.println("object: "+parseObjects.get(i).get("code").toString());
                                parseObjects.get(i).deleteInBackground();
                                i++;
                            }
                        }
                    });
                    Toast.makeText(getApplicationContext(),"You can now login with your new password",Toast.LENGTH_LONG).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}