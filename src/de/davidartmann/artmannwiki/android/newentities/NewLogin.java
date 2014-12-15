package de.davidartmann.artmannwiki.android.newentities;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.Choice;
import de.davidartmann.artmannwiki.android.database.LoginManager;
import de.davidartmann.artmannwiki.android.model.Login;


public class NewLogin extends Activity {
	
	private EditText usernameEditText;
	private EditText passwordEditText;
	private EditText passwordRepeatEditText;
	private EditText descriptionEditText;
	private Button saveButton;
	private LoginManager loginManager;
	private String pleaseFillField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login);
        // find components
        usernameEditText = (EditText) findViewById(R.id.activity_new_login_edittext_username);
        passwordEditText = (EditText) findViewById(R.id.activity_new_login_edittext_password);
        passwordRepeatEditText = (EditText) findViewById(R.id.activity_new_login_edittext_password_repeat);
        descriptionEditText = (EditText) findViewById(R.id.activity_new_login_edittext_info);
        saveButton = (Button) findViewById(R.id.activity_new_login_button_save);
        pleaseFillField = "Bitte ausfüllen";
        
        saveButton.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View view) {
                validate(usernameEditText, passwordEditText, passwordRepeatEditText, descriptionEditText);
            }
        });
    }

    protected void validate(EditText usernameEditText2, EditText passwordEditText2, EditText passwordRepeatEditText2, EditText descriptionEditText2) {
    	String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordRepeat = passwordRepeatEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (username.isEmpty()) {
			usernameEditText2.setError(pleaseFillField);
		}
        /*
         * logins without pw, also possible
         * 
        if (password.isEmpty()) {
			passwordEditText2.setError(pleaseFillField);
		}
        if (passwordRepeat.isEmpty()) {
			passwordRepeatEditText2.setError(pleaseFillField);
		}
        */
        if (description.isEmpty()) {
			descriptionEditText2.setError(pleaseFillField);
		}
        if ((password.equals(passwordRepeat))&&((!username.isEmpty())&&(!description.isEmpty()))) {
        	Login l = new Login(username, password, description);
        	l.setActive(true);
        	l.setCreateTime(new Date());
        	loginManager = new LoginManager(this);
        	loginManager.openWritable();
        	loginManager.addLogin(l);
        	loginManager.close();
        	Toast.makeText(this, "Login erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
			goBackToMain();
        }
        else if (!(password.equals(passwordRepeat))){
            Toast.makeText(this, R.string.prompt_password_unidentical, Toast.LENGTH_SHORT).show();
        }
	}

	private void goBackToMain() {
		Intent intent = new Intent(getBaseContext(), Choice.class);
		startActivity(intent);
	}
	
	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}
}
