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
import de.davidartmann.artmannwiki.android.database.EmailManager;
import de.davidartmann.artmannwiki.android.model.Email;


public class NewEmail extends Activity {
	
	private EditText emailEditText;
	private EditText passwordEditText;
	private EditText passwordRepeatEditText;
	private Button saveButton;
	private EmailManager emailManager;
	private String pleaseFillField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_email);
        //find components
        emailEditText = (EditText) findViewById(R.id.new_mail_edittext_mailadress);
        passwordEditText = (EditText) findViewById(R.id.new_mail_edittext_password);
        passwordRepeatEditText = (EditText) findViewById(R.id.new_mail_edittext_password_repeat);
        saveButton = (Button) findViewById(R.id.new_mail_button_save);
        pleaseFillField = "Bitte ausfüllen";

        saveButton.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View view) {
                validate(emailEditText, passwordEditText, passwordRepeatEditText);
            }
        });
    }


    protected void validate(EditText emailEditText2, EditText passwordEditText2, EditText passwordRepeatEditText2) {
    	String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordRepeat = passwordRepeatEditText.getText().toString().trim();
        
        if (email.isEmpty()) {
			emailEditText2.setError(pleaseFillField);
		}
        if (password.isEmpty()) {
			passwordEditText2.setError(pleaseFillField);
		}
        if (passwordRepeat.isEmpty()) {
			passwordRepeatEditText2.setError(pleaseFillField);
		}
        if (!email.isEmpty() && !password.isEmpty() && !passwordRepeat.isEmpty() && (password.equals(passwordRepeat))) {
        	Email e = new Email(email, password);
        	e.setActive(true);
        	e.setCreateTime(new Date());
        	emailManager = new EmailManager(this);
        	emailManager.openWritable();
        	emailManager.addEmail(e);
        	emailManager.close();
        	Toast.makeText(this, "E-Mail erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
        	goBackToMain();
        }
        else if (!(email.isEmpty()&&password.isEmpty()&&passwordRepeat.isEmpty()) && !(password.equals(passwordRepeat))) {
            Toast.makeText(getApplicationContext(), R.string.prompt_password_unidentical, Toast.LENGTH_SHORT).show();
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
