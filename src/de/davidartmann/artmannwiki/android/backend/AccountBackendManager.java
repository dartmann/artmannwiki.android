package de.davidartmann.artmannwiki.android.backend;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import de.davidartmann.artmannwiki.android.database.AccountManager;
import de.davidartmann.artmannwiki.android.model.Account;
import de.davidartmann.artmannwiki.android.newentities.NewAccount;

public class AccountBackendManager extends Application{
	
	private AccountManager accountManager;

	/**
	 * Method to send the created account to the backend.
	 * @param a ({@link Account})
	 */
	public void createOrUpdateInBackend(final Account a, String url) {
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
		               try {
		                   	VolleyLog.v("Response:%n %s", response.toString(4));
		                   	accountManager = new AccountManager(getApplicationContext());
		           			accountManager.openWritable(getApplicationContext());
		           			accountManager.addBackendId(a.getId(), response.getLong("id"));
		           			accountManager.close();
		               } catch (JSONException e) {
		                   e.printStackTrace();
		               }
		           }
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					VolleyLog.e("Error: ", error.getMessage());					
				}
			}) {
		       public Map<String, String> getHeaders() throws AuthFailureError {
		           HashMap<String, String> headers = new HashMap<String, String>();
		           headers.put("artmannwiki_headerkey", "blafoo");
		           headers.put("Content-Type", "application/json");
		           return headers;
		       }
		};
	}
}
