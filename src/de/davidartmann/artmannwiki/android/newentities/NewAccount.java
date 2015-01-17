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
import de.davidartmann.artmannwiki.android.backend.SyncManager;
import de.davidartmann.artmannwiki.android.backend.VolleyRequestQueue;
import de.davidartmann.artmannwiki.android.database.AccountManager;
import de.davidartmann.artmannwiki.android.model.Account;

public class NewAccount extends Activity {
	
	private EditText ownerEditText;
	private EditText ibanEditText;
	private EditText bicEditText;
	private EditText pinEditText;
	private Button saveButton;
	private AccountManager accountManager;
	private String pleaseFillField;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_account);
		ownerEditText = (EditText) findViewById(R.id.activity_new_account_edittext_owner);
		ibanEditText = (EditText) findViewById(R.id.activity_new_account_edittext_iban);
		bicEditText = (EditText) findViewById(R.id.activity_new_account_edittext_bic);
		pinEditText = (EditText) findViewById(R.id.activity_new_account_edittext_pin);
		saveButton = (Button) findViewById(R.id.activity_new_account_button_save);
		pleaseFillField = "Bitte ausfüllen";
		
		checkIfUpdate();
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (getIntent().getBooleanExtra("update", false)) {
					updateAccount(ownerEditText, ibanEditText, bicEditText, pinEditText);
				} else {
					createAccount(ownerEditText, ibanEditText, bicEditText, pinEditText);
				}
			}
		});
	}
	
	/**
	 * Method to send the created account to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param a ({@link Account})
	 * @param url ({@link String})
	 */
	private void createInBackend(final Account a, String url) {
		JSONObject jAccount = new JSONObject();
		try {
			jAccount.put("active", a.isActive());
			jAccount.put("owner", a.getOwner());
			jAccount.put("iban", a.getIban());
			jAccount.put("bic", a.getBic());
			jAccount.put("pin", a.getPin());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jAccount, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					accountManager = new AccountManager(NewAccount.this);
	            	accountManager.openWritable(NewAccount.this);
	            	Account tempAcc = accountManager.addAccount(a);
					try {
		            	accountManager.addBackendId(tempAcc.getId(), response.getLong("id"));
	               	} catch (JSONException e) {
	               		e.printStackTrace();
	               	}
					accountManager.close();
					new SyncManager().setLocalSyncTimeWithBackendResponse(NewAccount.this);
					Toast.makeText(NewAccount.this, "Bankkonto erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
		        }
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
					Toast.makeText(NewAccount.this, "Konnte Bankkonto nicht speichern, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
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
	 * Method to send the created account to the backend.
	 * Via a Volley {@link JsonObjectRequest}
	 * @param a ({@link Account})
	 * @param url ({@link String})
	 */
	private void updateInBackend(final Account a, String url) {
		JSONObject jAccount = new JSONObject();
		try {
			jAccount.put("active", a.isActive());
			jAccount.put("owner", a.getOwner());
			jAccount.put("iban", a.getIban());
			jAccount.put("bic", a.getBic());
			jAccount.put("pin", a.getPin());
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jAccount, 
			new Response.Listener<JSONObject>() {
				public void onResponse(JSONObject response) {
					accountManager = new AccountManager(NewAccount.this);
					accountManager.openWritable(NewAccount.this);
					accountManager.updateAccountById(a);
					accountManager.close();
					new SyncManager().setLocalSyncTimeWithBackendResponse(NewAccount.this);
					Toast.makeText(NewAccount.this, "Bankkonto erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
				}
			}, new Response.ErrorListener() {
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());
					Toast.makeText(NewAccount.this, "Konnte Bankkonto nicht aktualisieren, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
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
	 * Method to update an existing {@link Account}
	 * @param ownerEditText2
	 * @param ibanEditText2
	 * @param bicEditText2
	 * @param pinEditText2
	 */
	private void updateAccount(EditText ownerEditText2, EditText ibanEditText2, EditText bicEditText2, EditText pinEditText2) {
		String owner = ownerEditText2.getText().toString().trim();
		String iban = ibanEditText2.getText().toString().trim();
		String bic = bicEditText2.getText().toString().trim();
		String pin = pinEditText2.getText().toString().trim();
		
		if (owner.isEmpty()) {
			ownerEditText2.setError(pleaseFillField);
		}
		if (iban.isEmpty()) {
			ibanEditText2.setError(pleaseFillField);
		}
		if (bic.isEmpty()) {
			bicEditText2.setError(pleaseFillField);
		}
		if (pin.isEmpty()) {
			pinEditText2.setError(pleaseFillField);
		}
		if (!owner.isEmpty() && !iban.isEmpty() && !bic.isEmpty() && !pin.isEmpty()) {
			Account a = (Account) getIntent().getSerializableExtra("account");
			a.setOwner(owner);
			a.setIban(iban);
			a.setBic(bic);
			a.setPin(pin);
			updateInBackend(a, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.UPDATE_ACCOUNT+a.getBackendId());
			goBackToMain();
		}
	}

	/**
	 * Method to fill the EditText fields with data. If editing was chosen before.
	 */
	private void checkIfUpdate() {
		if (getIntent().getSerializableExtra("account") != null) {
			setTitle("Bankkonto aktualisieren");
			Account a = (Account) getIntent().getSerializableExtra("account");
			ownerEditText.setText(a.getOwner());
			ibanEditText.setText(a.getIban());
			bicEditText.setText(a.getBic());
			pinEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			pinEditText.setText(a.getPin());
		} else {
			setTitle("Neues Bankkonto");
		}
	}

	/**
	 * Method to create a new {@link Account}
	 * @param ownerEditText2
	 * @param ibanEditText2
	 * @param bicEditText2
	 * @param pinEditText2
	 */
	private void createAccount(EditText ownerEditText2, EditText ibanEditText2, EditText bicEditText2, EditText pinEditText2) {
		String owner = ownerEditText2.getText().toString().trim();
		String iban = ibanEditText2.getText().toString().trim();
		String bic = bicEditText2.getText().toString().trim();
		String pin = pinEditText2.getText().toString().trim();
		
		if (owner.isEmpty()) {
			ownerEditText2.setError(pleaseFillField);
		}
		if (iban.isEmpty()) {
			ibanEditText2.setError(pleaseFillField);
		}
		if (bic.isEmpty()) {
			bicEditText2.setError(pleaseFillField);
		}
		if (pin.isEmpty()) {
			pinEditText2.setError(pleaseFillField);
		}
		if (!owner.isEmpty() && !iban.isEmpty() && !bic.isEmpty() && !pin.isEmpty()) {
			Account a = new Account(owner,iban, bic, pin);
			a.setActive(true);
			createInBackend(a, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.ADD_ACCOUNT);
			goBackToMain();
		}
		
	}

	private void goBackToMain() {
		Intent intent = new Intent(getBaseContext(), Choice.class);
		finish();
		startActivity(intent);
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

}
