package de.davidartmann.artmannwiki.android.backend;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import de.davidartmann.artmannwiki.android.database.AccountManager;
import de.davidartmann.artmannwiki.android.database.LastUpdateManager;
import de.davidartmann.artmannwiki.android.model.Account;

public class SyncManager {
	
	private AccountManager accountManager;
	private LastUpdateManager lastUpdateManager;
	
	public void doAccountSync(final Context c, String url, final Long newSyncTimeStamp) {
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
		    @Override
		    public void onResponse(JSONArray response) {
		    	accountManager = new AccountManager(c);
		    	accountManager.openWritable(c);
		    	List<Account> accounts = accountManager.getAllAccounts();
		    	int j;
		        for (j = 0; j < response.length(); j++) {
		        	Account backendAcc = new Account();
					try {
						JSONObject jAcc = (JSONObject) response.get(j);
						System.out.println("JSON Account String: "+jAcc);
						backendAcc.setId(jAcc.getLong("id"));
						backendAcc.setActive(jAcc.getBoolean("active"));
						backendAcc.setBic(jAcc.getString("bic"));
						backendAcc.setIban(jAcc.getString("iban"));
						backendAcc.setOwner(jAcc.getString("owner"));
						backendAcc.setPin(jAcc.getString("pin"));
						backendAcc.setCreateTime(new Date(jAcc.getLong("createTime")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					int updates = 0;
					for (Account localAcc : accounts) {
						System.out.println("Check BackendId: "+localAcc.getBackendId());
						if (localAcc.getBackendId() == backendAcc.getId()) {
							System.out.println("UPDATE!");
							accountManager.updateAccount(backendAcc);
							updates++;
						}
					}
					if (updates != response.length()) {
						System.out.println("CREATE!");
						Account a = accountManager.addAccount(backendAcc);
						accountManager.addBackendId(a.getId(), backendAcc.getId());
					}
				}
		        if (j == response.length()) {
		        	Toast.makeText(c, "Update der Bankkonten erfolgreich", Toast.LENGTH_SHORT).show();
		        	lastUpdateManager = new LastUpdateManager(c);
		        	lastUpdateManager.openWritable(c);
		        	lastUpdateManager.setLastUpdate(newSyncTimeStamp);
		        	lastUpdateManager.close();
				} else {
					Toast.makeText(c, "Update der Bankkonten nicht vollständig", Toast.LENGTH_SHORT).show();
				}
		        accountManager.close();
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
