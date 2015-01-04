package de.davidartmann.artmannwiki.android.newentities;

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
        usernameEditText = (EditText) findViewById(R.id.activity_new_login_edittext_username);
        passwordEditText = (EditText) findViewById(R.id.activity_new_login_edittext_password);
        passwordRepeatEditText = (EditText) findViewById(R.id.activity_new_login_edittext_password_repeat);
        descriptionEditText = (EditText) findViewById(R.id.activity_new_login_edittext_info);
        saveButton = (Button) findViewById(R.id.activity_new_login_button_save);
        pleaseFillField = "Bitte ausfüllen";
        
        checkIfUpdate();
        
        saveButton.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View view) {
            	if (getIntent().getBooleanExtra("update", false)) {
					updateLogin(usernameEditText, passwordEditText, passwordRepeatEditText, descriptionEditText);
				} else {
					createLogin(usernameEditText, passwordEditText, passwordRepeatEditText, descriptionEditText);
				}
            }
        });
    }
    
    /**
	 * Method to send the created login to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param l ({@link Login})
	 * @param url ({@link String})
	 */
	private void createInBackend(final Login l, String url) {
		JSONObject jLogin = new JSONObject();
		try {
			jLogin.put("active", l.isActive());
			jLogin.put("username", l.getUsername());
			jLogin.put("password", l.getPassword());
			jLogin.put("description", l.getDescription());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jLogin, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
		               try {
		            	   VolleyLog.v("Response:%n %s", response.toString(4));
		            	   loginManager = new LoginManager(NewLogin.this);
		            	   loginManager.openWritable(NewLogin.this);
		            	   loginManager.addBackendId(l.getId(), response.getLong("id"));
		            	   loginManager.close();
		               } catch (JSONException e) {
		            	   e.printStackTrace();
		               }
		           }
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());					
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
	 * Method to send the updated login to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param l ({@link Login})
	 * @param url ({@link String})
	 */
	private void updateInBackend(final Login l, String url) {
		JSONObject jLogin = new JSONObject();
		try {
			jLogin.put("active", l.isActive());
			jLogin.put("username", l.getUsername());
			jLogin.put("password", l.getPassword());
			jLogin.put("description", l.getDescription());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jLogin, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
		               try {
		            	   VolleyLog.v("Response:%n %s", response.toString(4));
		               } catch (JSONException e) {
		            	   e.printStackTrace();
		               }
		           }
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());					
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
     * Method to update an existing {@link Login}
     * @param usernameEditText2
     * @param passwordEditText2
     * @param passwordRepeatEditText2
     * @param descriptionEditText2
     */
    private void updateLogin(EditText usernameEditText2, EditText passwordEditText2, EditText passwordRepeatEditText2, EditText descriptionEditText2) {
    	String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordRepeat = passwordRepeatEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (username.isEmpty()) {
			usernameEditText2.setError(pleaseFillField);
		}
        if (password.isEmpty()) {
			passwordEditText2.setError(pleaseFillField);
		}
        if (passwordRepeat.isEmpty()) {
			passwordRepeatEditText2.setError(pleaseFillField);
		}
        if (description.isEmpty()) {
			descriptionEditText2.setError(pleaseFillField);
		}
        if ((password.equals(passwordRepeat))&&((!username.isEmpty())&&(!description.isEmpty()))) {
        	Login l = (Login) getIntent().getSerializableExtra("login");
        	l.setDescription(description);
        	l.setPassword(password);
        	l.setUsername(username);
			loginManager = new LoginManager(this);
			loginManager.openWritable(this);
			l = loginManager.updateLogin(l);
			loginManager.close();
			createInBackend(l, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.UPDATE_LOGIN+l.getBackendId());
			Toast.makeText(this, "Login erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
			goBackToMain();
		}
	}

    /**
	 * Method to fill the EditText fields with data. If editing was chosen before.
	 */
	private void checkIfUpdate() {
    	if (getIntent().getSerializableExtra("login") != null) {
    		setTitle("Login aktualisieren");
			Login l = (Login) getIntent().getSerializableExtra("login");
			usernameEditText.setText(l.getUsername());
			passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			passwordRepeatEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			passwordEditText.setText(l.getPassword());
		} else {
			setTitle("Neuer Login");
		}
	}

	/**
	 * Method to create a new {@link Login}
	 * @param usernameEditText2
	 * @param passwordEditText2
	 * @param passwordRepeatEditText2
	 * @param descriptionEditText2
	 */
	private void createLogin(EditText usernameEditText2, EditText passwordEditText2, EditText passwordRepeatEditText2, EditText descriptionEditText2) {
    	String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordRepeat = passwordRepeatEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        if (username.isEmpty()) {
			usernameEditText2.setError(pleaseFillField);
		}
        if (password.isEmpty()) {
			passwordEditText2.setError(pleaseFillField);
		}
        if (passwordRepeat.isEmpty()) {
			passwordRepeatEditText2.setError(pleaseFillField);
		}
        if (description.isEmpty()) {
			descriptionEditText2.setError(pleaseFillField);
		}
        if ((password.equals(passwordRepeat))&&((!username.isEmpty())&&(!description.isEmpty()))) {
        	Login l = new Login(username, password, description);
        	l.setActive(true);
        	loginManager = new LoginManager(this);
        	loginManager.openWritable(this);
        	l = loginManager.addLogin(l);
        	loginManager.close();
        	updateInBackend(l, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.ADD_LOGIN);
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
