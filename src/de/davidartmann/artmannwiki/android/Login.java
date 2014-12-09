package de.davidartmann.artmannwiki.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import de.artmann.artmannwiki.R;

public class Login extends Activity {
    //-----------------------------------------------------------------------------------
    private static final String PREFS_NAME = "sprefsfile_artmannwiki";
    //-----------------------------------------------------------------------------------
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //-----------------------------------------------------------------------------------
        final EditText passwordField = (EditText) findViewById(R.id.login_password_field);
        final EditText passwordField2 = (EditText) findViewById(R.id.login_password_field_2);
        Button loginButton = (Button) findViewById(R.id.login_button);
        final TextView textView = (TextView) findViewById(R.id.login_textview);
        final SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);    //0 == Activity.MODE_PRIVATE
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //-----------------------------------------------------------------------------------
        String passwd = sharedPreferences.getString("passwordString", "");
        textView.setText(passwd);
        if(passwd.equals("")) {
            passwordField2.setVisibility(View.VISIBLE);
            textView.setText(R.string.prompt_register);
        }
        else {
            passwordField2.setVisibility(View.GONE);
        }
        //-----------------------------------------------------------------------------------
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String passwdSp = sharedPreferences.getString("passwordString", "");
                String passField1 = passwordField.getText().toString().trim();
                String passField2 = passwordField2.getText().toString().trim();
                if(passwdSp.equals("")) {
                    if((passField1.equals(passField2)) && (passField1.length()>=6)) {
                        editor.putString("passwordString",passwordField.getText().toString()).apply();
                        Toast.makeText(Login.this, "Erfolgreiche erste Anmeldung", Toast.LENGTH_SHORT).show();
                        //setContentView(R.layout.activity_main_wiki);
                        Intent intent = new Intent(getBaseContext(), Choice.class);
                        startActivity(intent);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
                    }
                    else if((!(passField1.equals(passField2))) && (passwordField.length()>=6)) {
                        Toast.makeText(Login.this, R.string.prompt_password_unidentical, Toast.LENGTH_SHORT).show();
                   }
                    else if((passField1.equals(passField2)) && (passwordField.length()<6)) {
                        Toast.makeText(Login.this, R.string.prompt_password_too_short, Toast.LENGTH_SHORT).show();
                    }
                    else if(!(passField1.equals(passField2)) && (passwordField.length()<6)) {
                        Toast.makeText(Login.this, R.string.prompt_password_invalid+" und "
                        		+R.string.prompt_password_too_short, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Login.this, R.string.prompt_password_invalid, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(passField1.equals(passwdSp)) {
                        Toast.makeText(Login.this, "Erfolgreiche Anmeldung", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getBaseContext(), Choice.class);
                        startActivity(intent);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(passwordField.getWindowToken(), 0);
                    }
                    else {
                        Toast.makeText(Login.this, R.string.prompt_password_invalid, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });//endOnClickListener
    }//endOnCreate

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || id == R.id.action_exit || super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        Runtime.getRuntime().addShutdownHook(new Thread() {
        	@Override
        	public void run() {
        		Intent intent = new Intent(Intent.ACTION_MAIN);
        		intent.addCategory(Intent.CATEGORY_HOME);
        		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		startActivity(intent);
        	}
        });;
    }
}
