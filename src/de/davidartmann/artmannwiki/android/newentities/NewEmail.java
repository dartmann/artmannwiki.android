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
        
        checkIfUpdate();

        saveButton.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View view) {
            	if (getIntent().getBooleanExtra("update", false)) {
					updateEmail();
				} else {
					validate(emailEditText, passwordEditText, passwordRepeatEditText);
				}
            }
        });
    }


    protected void updateEmail() {
		Boolean success = false;
    	Email e = (Email) getIntent().getSerializableExtra("email");
		String email = emailEditText.getText().toString().trim();
		if (!email.isEmpty()) {
			e.setEmailaddress(email);
		} else {
			emailEditText.setError(pleaseFillField);
		}
		String pw = passwordEditText.getText().toString().trim();
		String pw2 = passwordRepeatEditText.getText().toString().trim();
		if (!pw.isEmpty() && !pw2.isEmpty() && pw.equals(pw2)) {
			e.setPassword(pw);
			success = true;
		} else {
			if (pw.isEmpty()) {
				passwordEditText.setError(pleaseFillField);
			}
			if (pw2.isEmpty()) {
				passwordRepeatEditText.setError(pleaseFillField);
			}
			if (!pw.equals(pw2)) {
				Toast.makeText(this, R.string.prompt_password_unidentical, Toast.LENGTH_SHORT).show();
			}
		}
		if (success) {
			e.setLastUpdate(new Date());
			emailManager = new EmailManager(this);
			emailManager.openWritable(this);
			emailManager.updateEmail(e);
			emailManager.close();
			Toast.makeText(this, "E-Mail erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
			goBackToMain();
		}
	}


	private void checkIfUpdate() {
		if (getIntent().getSerializableExtra("email") != null) {
			Email e = (Email) getIntent().getSerializableExtra("email");
			emailEditText.setText(e.getEmailaddress());
			passwordEditText.setText(e.getPassword());
			//TODO: check if this feature is wanted
			//passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		}
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
        	emailManager.openWritable(this);
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
