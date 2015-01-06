package de.davidartmann.artmannwiki.android.backend;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import de.davidartmann.artmannwiki.android.database.AccountManager;
import de.davidartmann.artmannwiki.android.database.LastUpdateManager;
import de.davidartmann.artmannwiki.android.model.Account;

public class SyncManager {
	
	private AccountManager accountManager;
	private LastUpdateManager lastUpdateManager;
	
	/**
	 * Method to get the last sync timestamp from the backend and store it locally.
	 * This is done, when an C(R)UD operation has finished, to store the right sync time.
	 * @param c {@link Context}
	 */
	public void setLocalSyncTimeWithBackendResponse(final Context c) {
		StringRequest stringRequest = new StringRequest(Request.Method.GET, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_AND_SET_LAST_UPDATE,
		            new Response.Listener<String>() {
		    public void onResponse(String response) {
		    	lastUpdateManager = new LastUpdateManager(c);
		    	lastUpdateManager.openWritable(c);
		    	Long localLastUpdate = lastUpdateManager.setLastUpdate(Long.parseLong(response));
		    	System.out.println("Successfully updated local sync time: '"+lastUpdateManager.getLastUpdate()+"' with '"+localLastUpdate+"' from the backend");
		    	lastUpdateManager.close();
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	Log.e("checkLastUpdate", error.toString());
		    	System.err.println("Could not set local sync time, because backend connection failed");
		    }
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
                return headers;  
			}
		};
		VolleyRequestQueue.getInstance(c).addToRequestQueue(stringRequest);
	}
	
	/**
	 * Method to sync the {@link Account}s from the backend locally.
	 * @param c {@link Context}
	 * @param url {@link String}
	 * @param newSyncTimeStamp {@link Long}
	 */
	public void doAccountSync(final Context c, String url, final Long newSyncTimeStamp) {
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
		    @Override
		    public void onResponse(JSONArray response) {
		    	accountManager = new AccountManager(c);
		    	accountManager.openWritable(c);
		    	List<Account> localAccounts = accountManager.getAllAccounts();
		    	int j;
		        for (j = 0; j < response.length(); j++) {
		        	Account backendAcc = null;
					try {
						JSONObject jAcc = (JSONObject) response.get(j);
						System.out.println("JSON Account String: "+jAcc);
						backendAcc = new Account(jAcc.getString("owner"), jAcc.getString("iban"), jAcc.getString("bic"), jAcc.getString("pin"));
						backendAcc.setBackendId(jAcc.getLong("id"));
						backendAcc.setActive(jAcc.getBoolean("active"));
						backendAcc.setCreateTime(new Date(jAcc.getLong("createTime")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					boolean update = false;
					for (Account localAcc : localAccounts) {
						if (localAcc.getBackendId() == backendAcc.getBackendId()) {
							System.out.println("UPDATE!");
							accountManager.updateAccountByBackendId(backendAcc);
							update = true;
						}
					}
					if (!update) {
						System.out.println("CREATE!");
						Account a = accountManager.addAccount(backendAcc);
						accountManager.addBackendId(a.getId(), backendAcc.getBackendId());
					}
				}//endResponseLoop
		        accountManager.close();
		        if (j == response.length()) {
		        	Toast.makeText(c, "Update der Bankkonten erfolgreich", Toast.LENGTH_SHORT).show();
		        	lastUpdateManager = new LastUpdateManager(c);
		        	lastUpdateManager.openWritable(c);
		        	lastUpdateManager.setLastUpdate(newSyncTimeStamp);
		        	lastUpdateManager.close();
				} else {
					Toast.makeText(c, "Update der Bankkonten nicht vollständig", Toast.LENGTH_SHORT).show();
				}
		    }
		}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		        VolleyLog.e("Error: ", error.getMessage());
		        Toast.makeText(c, "Update der Bankkonten konnte nicht durchgeführt werden, Fehler bei der Verbindung", Toast.LENGTH_SHORT).show();
		    }
		}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
                return headers;  
			}
		};
		VolleyRequestQueue.getInstance(c).addToRequestQueue(jsonArrayRequest);
	}

}
