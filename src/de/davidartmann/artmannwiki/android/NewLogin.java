package de.davidartmann.artmannwiki.android;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import checkIfDeletable.DBManager;
import checkIfDeletable.Entity;
import de.artmann.artmannwiki.R;


public class NewLogin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        final EditText usernameEditText = (EditText) findViewById(R.id.new_login_edittext_username);
        final EditText passwordEditText = (EditText) findViewById(R.id.new_login_edittext_password);
        final EditText passwordRepeatEditText = (EditText) findViewById(R.id.new_login_edittext_password_repeat);
        final EditText infoEditText = (EditText) findViewById(R.id.new_login_edittext_info);
        Button saveButton = (Button) findViewById(R.id.new_login_button_save);
        //
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String passwordRepeat = passwordRepeatEditText.getText().toString().trim();
                String info = infoEditText.getText().toString().trim();

                if ((password.equals(passwordRepeat))&&((!username.isEmpty())&&(!info.isEmpty()))) {
                    DBManager dbManager = new DBManager(NewLogin.this);
                    dbManager.addLogin(new Entity(username, password, info));
                    Toast.makeText(getApplicationContext(), R.string.prompt_saved_succesfully, Toast.LENGTH_SHORT).show();
                }
                else if (!(password.equals(passwordRepeat))){
                    Toast.makeText(getApplicationContext(), R.string.prompt_password_unidentical, Toast.LENGTH_SHORT).show();
                }
                else if (username.isEmpty()||(info.isEmpty())) {
                    Toast.makeText(getApplicationContext(), R.string.prompt_new_login_info_required, Toast.LENGTH_SHORT).show();
                    usernameEditText.setBackgroundResource(R.drawable.mistyrose);
                    infoEditText.setBackgroundResource(R.drawable.mistyrose);
                    new CountDownTimer(5000, 50) {
                        @Override
                        public void onTick(long l) {
                            //nothing
                        }

                        @Override
                        public void onFinish() {
                            usernameEditText.setBackgroundResource(R.drawable.screen_background_holo_light);
                            infoEditText.setBackgroundResource(R.drawable.screen_background_holo_light);
                        }
                    }.start();
                }
                clearEditTexts();
            }
        });
    }

    private void clearEditTexts() {
        EditText usernameEditText = (EditText) findViewById(R.id.new_login_edittext_username);
        EditText passwordEditText = (EditText) findViewById(R.id.new_login_edittext_password);
        EditText passwordRepeatEditText = (EditText) findViewById(R.id.new_login_edittext_password_repeat);
        usernameEditText.setText("");
        passwordEditText.setText("");
        passwordRepeatEditText.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
