package de.davidartmann.artmannwiki.android.newentities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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
import de.davidartmann.artmannwiki.android.database.InsuranceManager;
import de.davidartmann.artmannwiki.android.database.LastUpdateManager;
import de.davidartmann.artmannwiki.android.model.Insurance;

public class NewInsurance extends Activity {

	private EditText nameEditText;
	private EditText kindEditText;
	private EditText membershipIdEditText;
	private Button saveButton;
	private InsuranceManager insuranceManager;
	private LastUpdateManager lastUpdateManager;
	private String pleaseFillField;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_insurance);
		nameEditText = (EditText) findViewById(R.id.activity_new_insurance_edittext_name);
		kindEditText = (EditText) findViewById(R.id.activity_new_insurance_edittext_kind);
		membershipIdEditText = (EditText) findViewById(R.id.activity_new_insurance_edittext_membershipId);
		saveButton = (Button) findViewById(R.id.activity_new_insurance_button_save);
		pleaseFillField = "Bitte ausfüllen";
		
		checkIfUpdate();
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (getIntent().getBooleanExtra("update", false)) {
					updateInsurance(nameEditText, kindEditText, membershipIdEditText);
				} else {
					validate(nameEditText, kindEditText, membershipIdEditText);
				}
			}
		});
	}
	
	/**
	 * Method to send the created insurance to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param i ({@link Insurance})
	 * @param url ({@link String})
	 */
	private void createInBackend(final Insurance i, String url) {
		JSONObject jInsurance = new JSONObject();
		try {
			jInsurance.put("active", i.isActive());
			jInsurance.put("name", i.getName());
			jInsurance.put("kind", i.getKind());
			jInsurance.put("membershipId", i.getMembershipId());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jInsurance, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					insuranceManager = new InsuranceManager(NewInsurance.this);
					insuranceManager.openWritable(NewInsurance.this);
					Insurance tempIns = insuranceManager.addInsurance(i);
					try {
						insuranceManager.addBackendId(tempIns.getId(), response.getLong("id"));
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
					insuranceManager.close();
					lastUpdateManager = new LastUpdateManager(NewInsurance.this);
					lastUpdateManager.openWritable(NewInsurance.this);
		        	lastUpdateManager.setLastUpdate(new Date().getTime());
		        	lastUpdateManager.close();
					Toast.makeText(NewInsurance.this, "Versicherung erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
	           }
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
					Toast.makeText(NewInsurance.this, "Konnte Versicherung nicht speichern, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
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
	 * Method to send the updated insurance to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param i ({@link Insurance})
	 * @param url ({@link String})
	 */
	private void updateInBackend(final Insurance i, String url) {
		JSONObject jInsurance = new JSONObject();
		try {
			jInsurance.put("active", i.isActive());
			jInsurance.put("name", i.getName());
			jInsurance.put("kind", i.getKind());
			jInsurance.put("membershipId", i.getMembershipId());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jInsurance, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					insuranceManager = new InsuranceManager(NewInsurance.this);
					insuranceManager.openWritable(NewInsurance.this);
					insuranceManager.updateInsurance(i);
					insuranceManager.close();
					lastUpdateManager = new LastUpdateManager(NewInsurance.this);
					lastUpdateManager.openWritable(NewInsurance.this);
		        	lastUpdateManager.setLastUpdate(new Date().getTime());
		        	lastUpdateManager.close();
					Toast.makeText(NewInsurance.this, "Versicherung erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
		           	}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
					Toast.makeText(NewInsurance.this, "Konnte Versicherung nicht aktualisieren, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
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
	 * Method to update an existing {@link Insurance}
	 * @param nameEditText2
	 * @param kindEditText2
	 * @param membershipIdEditText2
	 */
	private void updateInsurance(EditText nameEditText2, EditText kindEditText2, EditText membershipIdEditText2) {
		String name = nameEditText2.getText().toString().trim();
		String kind = kindEditText2.getText().toString().trim();
		String membershipId = membershipIdEditText2.getText().toString().trim();
		
		if (name.isEmpty()) {
			nameEditText2.setError(pleaseFillField);
		}
		if (kind.isEmpty()) {
			kindEditText2.setError(pleaseFillField);
		}
		if (membershipId.isEmpty()) {
			membershipIdEditText2.setError(pleaseFillField);
		}
		if (!name.isEmpty() && !kind.isEmpty() && !membershipId.isEmpty()) {
			Insurance i = (Insurance) getIntent().getSerializableExtra("insurance");
			i.setKind(kind);
			i.setMembershipId(membershipId);
			i.setName(name);
//			insuranceManager = new InsuranceManager(this);
//			insuranceManager.openWritable(this);
//			i = insuranceManager.updateInsurance(i);
//			insuranceManager.close();
			updateInBackend(i, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.UPDATE_INSURANCE+i.getBackendId());
			goBackToMain();
		}
	}

	/**
	 * Method to fill the EditText fields with data. If editing was chosen before.
	 */
	private void checkIfUpdate() {
		if (getIntent().getSerializableExtra("insurance") != null) {
			setTitle("Versicherung aktualisieren");
			Insurance i = (Insurance) getIntent().getSerializableExtra("insurance");
			nameEditText.setText(i.getName());
			kindEditText.setText(i.getKind());
			membershipIdEditText.setText(i.getMembershipId());
		} else {
			setTitle("Neue Versicherung");
		}
	}

	/**
	 * Method to create a new {@link Insurance}
	 * @param nameEditText2
	 * @param kindEditText2
	 * @param membershipIdEditText2
	 */
	private void validate(EditText nameEditText2, EditText kindEditText2, EditText membershipIdEditText2) {
		String name = nameEditText2.getText().toString().trim();
		String kind = kindEditText2.getText().toString().trim();
		String membershipId = membershipIdEditText2.getText().toString().trim();
		
		if (name.isEmpty()) {
			nameEditText2.setError(pleaseFillField);
		}
		if (kind.isEmpty()) {
			kindEditText2.setError(pleaseFillField);
		}
		if (membershipId.isEmpty()) {
			membershipIdEditText2.setError(pleaseFillField);
		}
		if (!name.isEmpty() && !kind.isEmpty() && !membershipId.isEmpty()) {
			Insurance i = new Insurance(name, kind, membershipId);
			i.setActive(true);
//			insuranceManager = new InsuranceManager(this);
//			insuranceManager.openWritable(this);
//			i = insuranceManager.addInsurance(i);
//			insuranceManager.close();
			createInBackend(i, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.ADD_INSURANCE);
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
