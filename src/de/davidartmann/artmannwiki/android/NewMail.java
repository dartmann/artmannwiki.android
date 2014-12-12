package de.davidartmann.artmannwiki.android;

import checkIfDeletable.DBManager;
import checkIfDeletable.Entity;
import de.artmann.artmannwiki.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class NewMail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mail);
        final EditText mailEditText = (EditText) findViewById(R.id.new_mail_edittext_mailadress);
        final EditText passworEditText = (EditText) findViewById(R.id.new_mail_edittext_password);
        final EditText passwordRepeatEditText = (EditText) findViewById(R.id.new_mail_edittext_password_repeat);
        Button saveButton = (Button) findViewById(R.id.new_mail_button_save);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mailEditText.getText().toString().trim();
                String password = passworEditText.getText().toString().trim();
                String passwordRepeat = passwordRepeatEditText.getText().toString().trim();
                if (!(mail.isEmpty()&&password.isEmpty()&&passwordRepeat.isEmpty()) && (password.equals(passwordRepeat))) {
                    DBManager dbManager = new DBManager(NewMail.this);
                    dbManager.addEmail(new Entity(mail, password));
                    Toast.makeText(getApplicationContext(), R.string.prompt_saved_succesfully, Toast.LENGTH_SHORT).show();
                }
                else if (!(mail.isEmpty()&&password.isEmpty()&&passwordRepeat.isEmpty()) && !(password.equals(passwordRepeat))) {
                    Toast.makeText(getApplicationContext(), R.string.prompt_password_unidentical, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.prompt_all_fields_must_be_filled, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_mail, menu);
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
