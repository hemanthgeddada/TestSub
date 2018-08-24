package com.example.dell.mavride;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
public class Login extends Activity {

    protected EditText Email;
    protected EditText nPassword;
    protected Button logBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Email = (EditText)findViewById(R.id.txtEmail);
        nPassword = (EditText)findViewById(R.id.txtPass);
        logBtn = (Button)findViewById(R.id.btnLogin2);
        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = Email.getText().toString().trim();
                final String password = nPassword.getText().toString().trim();
                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    public void done(ParseUser parseUser, com.parse.ParseException e) {
                        if(e==null){
                            final String obj = parseUser.getObjectId();
                            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Registration");
                            query1.whereEqualTo("Email", email);
                            query1.whereEqualTo("Password", password);
                            query1.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject parseObject, ParseException e) {
                                   if (e == null) {
                                        parseObject.put("UserId", obj);
                                        parseObject.saveInBackground();
                                        final String objid = parseObject.getObjectId();
                                        String type = parseObject.getString("UserType");
                                        if (type.equals("Driver")) {
                                            ParseQuery<ParseObject> query = ParseQuery.getQuery("DriverDetail");
                                            query.whereEqualTo("DriverId", obj);
                                            query.getFirstInBackground(new GetCallback<ParseObject>() {
                                                public void done(ParseObject object, ParseException e) {
                                                    if (e == null) {
                                                        object.put("DriverStatus","Online");
                                                        object.saveInBackground();
                                                    }
                                                    else {
                                                        Toast.makeText(Login.this, "An error occurred in the change status module", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                            Intent driverHome = new Intent(getApplicationContext(), DriverHomePage.class);
                                            driverHome.putExtra("objectID", objid);
                                            startActivity(driverHome);
                                        } else {
                                            Intent userHome = new Intent(getApplicationContext(), UserHome.class);
                                            userHome.putExtra("objectID", objid);
                                            if(((CheckBox)findViewById(R.id.checkBox)).isChecked())
                                            {
                                                SharedPreferences login = getSharedPreferences(GlobalResources.PREFS_NAME, 0);
                                                SharedPreferences.Editor editor = login.edit();
                                                editor.putString("username", (((EditText) findViewById(R.id.txtEmail)).getText().toString().trim()));
                                                editor.putString("password", (((EditText) findViewById(R.id.txtPass)).getText().toString().trim()));
                                                editor.commit();
                                                System.out.println("preferences savedd");
                                            }
                                            startActivity(userHome);
                                        }
                                    }
                                    else {
                                        Toast.makeText(Login.this, "Login failed because of registration table", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else {
                            // Login failed. Look at the ParseException to see what happened.
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            if(e.getCode()==ParseException.CONNECTION_FAILED)
                            {
                                builder.setMessage("Unable to connect to server. Please check your internet connection and try again");
                            }
                            else
                            {
                                builder.setMessage("Invalid Username/Password");
                            }
                            builder.setTitle("Login Failed");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
        });
        //below method referred from http://stackoverflow.com/a/9598729/2039735
        nPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(((event!=null)&&(event.getKeyCode()==KeyEvent.KEYCODE_ENTER))||(actionId== EditorInfo.IME_ACTION_DONE))
                {
                    System.out.println("button click catched");
                    logBtn.performClick();
                }
                return false;
            }
        });
        if(getIntent().getStringExtra("loginUsername")!=null)
        {
            Email.setText(getIntent().getStringExtra("loginUsername"));
            nPassword.requestFocus();
        }
        if(getIntent().getStringExtra("mode")!=null && getIntent().getStringExtra("mode").equals("persistent"))
        {
            Email.setText(getIntent().getStringExtra("username"));
            nPassword.setText(getIntent().getStringExtra("password"));
            logBtn.performClick();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

  /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // Handle action bar item clicks here. The action bar will
       // automatically handle clicks on the Home/Up button, so long
       // as you specify a parent activity in AndroidManifest.xml.
       int id = item.getItemId();

       return super.onOptionsItemSelected(item);
   } */
    public void onClickForgotPassword(View view)
    {
        Intent intent=new Intent(this, ForgotPasswordFormActivity.class);
        startActivity(intent);
    }
}