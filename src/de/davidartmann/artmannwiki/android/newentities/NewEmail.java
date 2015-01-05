package de.davidartmann.artmannwiki.android.newentities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.Choice;
import de.davidartmann.artmannwiki.android.backend.BackendConstants;
import de.davidartmann.artmannwiki.android.backend.VolleyRequestQueue;
import de.davidartmann.artmannwiki.android.database.EmailManager;
import de.davidartmann.artmannwiki.android.database.LastUpdateManager;
import de.davidartmann.artmannwiki.android.model.Email;


public class NewEmail extends Activity {
	
	private EditText emailEditText;
	private EditText passwordEditText;
	private EditText passwordRepeatEditText;
	private Button saveButton;
	private EmailManager emailManager;
	private LastUpdateManager lastUpdateManager;
	private String pleaseFillField;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_email);
        emailEditText = (EditText) findViewById(R.id.new_mail_edittext_mailadress);
        passwordEditText = (EditText) findViewById(R.id.new_mail_edittext_password);
        passwordRepeatEditText = (EditText) findViewById(R.id.new_mail_edittext_password_repeat);
        saveButton = (Button) findViewById(R.id.new_mail_button_save);
        pleaseFillField = "Bitte ausfüllen";
        
        checkIfUpdate();

        saveButton.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View view) {
            	if (getIntent().getBooleanExtra("update", false)) {
					updateEmail(emailEditText, passwordEditText, passwordRepeatEditText);
				} else {
					createEmail(emailEditText, passwordEditText, passwordRepeatEditText);
				}
            }
        });
    }

    /**
	 * Method to send the created email to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param e ({@link Email})
	 * @param url ({@link String})
	 */
	private void createInBackend(final Email e, String url) {
		JSONObject jEmail = new JSONObject();
		try {
			jEmail.put("active", e.isActive());
			jEmail.put("emailaddress", e.getEmailaddress());
			jEmail.put("password", e.getPassword());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jEmail, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					emailManager = new EmailManager(NewEmail.this);
		        	emailManager.openWritable(NewEmail.this);
		        	Email tempEmail = emailManager.addEmail(e);
		        	try {
						emailManager.addBackendId(tempEmail.getId(), response.getLong("id"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
		        	emailManager.close();
		        	lastUpdateManager = new LastUpdateManager(NewEmail.this);
		        	lastUpdateManager.openWritable(NewEmail.this);
		        	lastUpdateManager.setLastUpdate(new Date().getTime());
		        	lastUpdateManager.close();
		        	Toast.makeText(NewEmail.this, "E-Mail erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
	           	}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
					Toast.makeText(NewEmail.this, "Konnte E-Mail nicht speichern, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
				}
			}) 	{
		       	public Map<String, String> getHeaders() throws AuthFailureError {
		           	HashMap<String, String> headers = new HashMap<String, String>();
		           	headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
		       		headers.put(BackendConstants.CONTENT_TYPE, BackendConstants.APPLICATION_JSON);
		       		return headers;
		       	}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
	}
	
	/**
	 * Method to send the updated email to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param e ({@link Email})
	 * @param url ({@link String})
	 */
	private void updateInBackend(final Email e, String url) {
		JSONObject jEmail = new JSONObject();
		try {
			jEmail.put("active", e.isActive());
			jEmail.put("emailaddress", e.getEmailaddress());
			jEmail.put("password", e.getPassword());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jEmail, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					emailManager = new EmailManager(NewEmail.this);
					emailManager.openWritable(NewEmail.this);
					emailManager.updateEmail(e);
					emailManager.close();
					lastUpdateManager = new LastUpdateManager(NewEmail.this);
					lastUpdateManager.openWritable(NewEmail.this);
		        	lastUpdateManager.setLastUpdate(new Date().getTime());
		        	lastUpdateManager.close();
					Toast.makeText(NewEmail.this, "E-Mail erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
		           	}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
					Toast.makeText(NewEmail.this, "Konnte E-Mail nicht aktualisieren, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
				}
			}) 	{
		       	public Map<String, String> getHeaders() throws AuthFailureError {
		           	HashMap<String, String> headers = new HashMap<String, String>();
		           	headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
		       		headers.put(BackendConstants.CONTENT_TYPE, BackendConstants.APPLICATION_JSON);
		       		return headers;
		       	}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(jsonObjectRequest);
	}

    /**
     * Method to update an existing {@link Email}
     * @param emailEditText2
     * @param passwordEditText2
     * @param passwordRepeatEditText2
     */
    private void updateEmail(EditText emailEditText2, EditText passwordEditText2, EditText passwordRepeatEditText2) {
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
        	Email e = (Email) getIntent().getSerializableExtra("email");
        	e.setEmailaddress(email);
        	e.setPassword(password);
//			emailManager = new EmailManager(this);
//			emailManager.openWritable(this);
//			e = emailManager.updateEmail(e);
//			emailManager.close();
			updateInBackend(e, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.UPDATE_EMAIL+e.getBackendId());
			goBackToMain();
		}
        else if (!(email.isEmpty()&&password.isEmpty()&&passwordRepeat.isEmpty()) && !(password.equals(passwordRepeat))) {
            Toast.makeText(getApplicationContext(), R.string.prompt_password_unidentical, Toast.LENGTH_SHORT).show();
        }
	}


    /**
	 * Method to fill the EditText fields with data. If editing was chosen before.
	 */
	private void checkIfUpdate() {
		if (getIntent().getSerializableExtra("email") != null) {
			setTitle("E-Mail aktualisieren");
			Email e = (Email) getIntent().getSerializableExtra("email");
			emailEditText.setText(e.getEmailaddress());
			passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			passwordRepeatEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			passwordEditText.setText(e.getPassword());
		} else {
			setTitle("Neue E-Mail");
		}
	}

	/**
	 * Method to create a new {@link Email}
	 * @param emailEditText2
	 * @param passwordEditText2
	 * @param passwordRepeatEditText2
	 */
	private void createEmail(EditText emailEditText2, EditText passwordEditText2, EditText passwordRepeatEditText2) {
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
//        	emailManager = new EmailManager(this);
//        	emailManager.openWritable(this);
//        	e = emailManager.addEmail(e);
//        	emailManager.close();
        	createInBackend(e, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.ADD_EMAIL);
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
