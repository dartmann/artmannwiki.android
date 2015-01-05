package de.davidartmann.artmannwiki.android.backend;

import java.util.ArrayList;
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
		    	// put all accounts from the db into a list so we only need a single query
		    	List<Account> accounts = accountManager.getAllAccounts();
		    	// for every account in the response...
		        for (int j = 0; j < response.length(); j++) {
		        	Account backendAcc = new Account();
					try {
						// ...get one
						JSONObject jAcc = (JSONObject) response.get(j);
						System.out.println("JSON Account String: "+jAcc);
						backendAcc.setId(jAcc.getLong("id"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					List<Account> tempList = new ArrayList<Account>();
					// get an local account from the list
					for (Account localAcc : accounts) {
						// and compare his id with the one from the response
						if (localAcc.getId() == backendAcc.getId()) {
							// if it matches, update
							System.out.println("UPDATE!");
							//accountManager.updateAccount(a);
							tempList.add(localAcc);
						}
					}
					accounts.removeAll(tempList);
					// the remaining accounts in the list did not match...
					tempList = new ArrayList<Account>();
					for (Account localAcc : accounts) {
						// (double check)
						if (localAcc.getId() != backendAcc.getId()) {
							// ... so they need to be newly created
							System.out.println("CREATE!");
							//accountManager.addAccount(tempAcc);
							tempList.add(localAcc);
						}
					}
					accounts.removeAll(tempList);
				}
		        if (accounts.isEmpty()) {
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
