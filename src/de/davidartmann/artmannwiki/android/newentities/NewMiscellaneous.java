package de.davidartmann.artmannwiki.android.newentities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import de.davidartmann.artmannwiki.android.backend.SyncManager;
import de.davidartmann.artmannwiki.android.backend.VolleyRequestQueue;
import de.davidartmann.artmannwiki.android.database.MiscellaneousManager;
import de.davidartmann.artmannwiki.android.model.Miscellaneous;

public class NewMiscellaneous extends Activity {

	private EditText textEditText;
	private EditText descriptionEditText;
	private Button saveButton;
	private MiscellaneousManager miscellaneousManager;
	private String pleaseFillField;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_miscellaneous);
		textEditText = (EditText) findViewById(R.id.activity_new_miscellaneous_edittext_text);
		descriptionEditText = (EditText) findViewById(R.id.activity_new_miscellaneous_edittext_description);
		saveButton = (Button) findViewById(R.id.activity_new_miscellaneous_button_save);
		pleaseFillField = "Bitte ausfüllen";
		
		checkIfUpdate();
		
		saveButton.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View view) {
            	if (getIntent().getBooleanExtra("update", false)) {
					updateMiscellaneous(textEditText, descriptionEditText);
				} else {
					createMiscellaneous(textEditText, descriptionEditText);
				}
            }
        });
	}
	
	/**
	 * Method to send the created miscellaneous to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param m ({@link Miscellaneous})
	 * @param url ({@link String})
	 */
	private void createInBackend(final Miscellaneous m, String url) {
		JSONObject jMiscellaneous = new JSONObject();
		try {
			jMiscellaneous.put("active", m.isActive());
			jMiscellaneous.put("text", m.getText());
			jMiscellaneous.put("description", m.getDescription());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jMiscellaneous, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					miscellaneousManager = new MiscellaneousManager(NewMiscellaneous.this);
					miscellaneousManager.openWritable(NewMiscellaneous.this);
					Miscellaneous tempMisc = miscellaneousManager.addMiscellaneous(m);
					try {
						miscellaneousManager.addBackendId(tempMisc.getId(), response.getLong("id"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					miscellaneousManager.close();
//					lastUpdateManager = new LastUpdateManager(NewMiscellaneous.this);
//					lastUpdateManager.openWritable(NewMiscellaneous.this);
//		        	lastUpdateManager.setLastUpdate(new Date().getTime());
//		        	lastUpdateManager.close();
					new SyncManager().setLocalSyncTimeWithBackendResponse(NewMiscellaneous.this);
					Toast.makeText(NewMiscellaneous.this, "Notiz erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
					Toast.makeText(NewMiscellaneous.this, "Konnte Notiz nicht speichern, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
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
	 * Method to send the updated account to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param m ({@link Miscellaneous})
	 * @param url ({@link String})
	 */
	private void updateInBackend(final Miscellaneous m, String url) {
		JSONObject jMiscellaneous = new JSONObject();
		try {
			jMiscellaneous.put("active", m.isActive());
			jMiscellaneous.put("text", m.getText());
			jMiscellaneous.put("description", m.getDescription());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jMiscellaneous, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					miscellaneousManager = new MiscellaneousManager(NewMiscellaneous.this);
					miscellaneousManager.openWritable(NewMiscellaneous.this);
					miscellaneousManager.updateMiscellaneous(m);
					miscellaneousManager.close();
//					lastUpdateManager = new LastUpdateManager(NewMiscellaneous.this);
//					lastUpdateManager.openWritable(NewMiscellaneous.this);
//		        	lastUpdateManager.setLastUpdate(new Date().getTime());
//		        	lastUpdateManager.close();
					new SyncManager().setLocalSyncTimeWithBackendResponse(NewMiscellaneous.this);
					Toast.makeText(NewMiscellaneous.this, "Notiz erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
	           	}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
					Toast.makeText(NewMiscellaneous.this, "Konnte Notiz nicht aktualisieren, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
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
	 * Method to update an existing {@link Miscellaneous}
	 * @param textEditText2
	 * @param descriptionEditText2
	 */
	private void updateMiscellaneous(EditText textEditText2,	EditText descriptionEditText2) {
		String text = textEditText2.getText().toString().trim();
		String description = descriptionEditText2.getText().toString().trim();
		
		if (text.isEmpty()) {
			textEditText2.setError(pleaseFillField);
		}
		if (description.isEmpty()) {
			descriptionEditText2.setError(pleaseFillField);
		}
		if (!text.isEmpty() && !description.isEmpty()) {
			Miscellaneous m = (Miscellaneous) getIntent().getSerializableExtra("miscellaneous");
			m.setDescription(description);
			m.setText(text);
//			miscellaneousManager = new MiscellaneousManager(this);
//			miscellaneousManager.openWritable(this);
//			m = miscellaneousManager.updateMiscellaneous(m);
//			miscellaneousManager.close();
			updateInBackend(m, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.UPDATE_MISCELLANEOUS+m.getBackendId());
			goBackToMain();
		}
	}

	/**
	 * Method to fill the EditText fields with data. If editing was chosen before.
	 */
	private void checkIfUpdate() {
		if (getIntent().getSerializableExtra("miscellaneous") != null) {
			setTitle("Notiz aktualisieren");
			Miscellaneous m = (Miscellaneous) getIntent().getSerializableExtra("miscellaneous");
			textEditText.setText(m.getText());
			descriptionEditText.setText(m.getDescription());
		} else {
			setTitle("Neue Notiz");
		}
	}

	/**
	 * Method to create a new {@link Miscellaneous}
	 * @param textEditText2
	 * @param descriptionEditText2
	 */
	private void createMiscellaneous(EditText textEditText2,	EditText descriptionEditText2) {
		String text = textEditText2.getText().toString().trim();
		String description = descriptionEditText2.getText().toString().trim();
		
		if (text.isEmpty()) {
			textEditText2.setError(pleaseFillField);
		}
		if (description.isEmpty()) {
			descriptionEditText2.setError(pleaseFillField);
		}
		if (!text.isEmpty() && !description.isEmpty()) {
			Miscellaneous m = new Miscellaneous(text, description);
			m.setActive(true);
//			miscellaneousManager = new MiscellaneousManager(this);
//			miscellaneousManager.openWritable(this);
//			m = miscellaneousManager.addMiscellaneous(m);
//			miscellaneousManager.close();
			createInBackend(m, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.ADD_MISCELLANEOUS);
        	goBackToMain();
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
