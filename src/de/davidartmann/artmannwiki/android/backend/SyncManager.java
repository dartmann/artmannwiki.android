package de.davidartmann.artmannwiki.android.backend;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import de.davidartmann.artmannwiki.android.database.AccountManager;
import de.davidartmann.artmannwiki.android.database.DeviceManager;
import de.davidartmann.artmannwiki.android.database.EmailManager;
import de.davidartmann.artmannwiki.android.database.InsuranceManager;
import de.davidartmann.artmannwiki.android.database.LastUpdateManager;
import de.davidartmann.artmannwiki.android.database.LoginManager;
import de.davidartmann.artmannwiki.android.database.MiscellaneousManager;
import de.davidartmann.artmannwiki.android.model.Account;
import de.davidartmann.artmannwiki.android.model.Device;
import de.davidartmann.artmannwiki.android.model.Email;
import de.davidartmann.artmannwiki.android.model.Insurance;
import de.davidartmann.artmannwiki.android.model.Login;
import de.davidartmann.artmannwiki.android.model.Miscellaneous;

/**
 * Class which extends the {@link AsyncTask} to perform several update tasks in the background.
 */
public class SyncManager extends AsyncTask<Long, Integer, Long> {
	
	private AccountManager accountManager;
	private DeviceManager deviceManager;
	private EmailManager emailManager;
	private InsuranceManager insuranceManager;
	private LoginManager loginManager;
	private MiscellaneousManager miscellaneousManager;
	private LastUpdateManager lastUpdateManager;
	private Context context;
	private ProgressDialog progressDialog;
//	private int createdItem;
//	private int updatedItem;
	private boolean updateError = false;
	
	/**
	 * Constructor without parameters for the NewAccount, NewDevice, (...) Classes
	 */
	public SyncManager() {
		//nothing
	}
	
	/**
	 * Contructor to pass the {@link Context} for the doInBackground() of the AsyncTask
	 * @param c
	 */
	public SyncManager(Context c) {
		this.context = c;
	}
	
	protected void onPreExecute() {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMax(6);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}
	
	protected Long doInBackground(Long... params) {
		doAccountSync(context, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_ACCOUNTS_SINCE+params[0], params[1]);
		publishProgress(1);
		doDeviceSync(context, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_DEVICES_SINCE+params[0], params[1]);
		publishProgress(2);
		doEmailSync(context, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_EMAILS_SINCE+params[0], params[1]);
		publishProgress(3);
		doInsuranceSync(context, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_INSURANCES_SINCE+params[0], params[1]);
		publishProgress(4);
		doLoginSync(context, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_LOGINS_SINCE+params[0], params[1]);
		publishProgress(5);
		doMiscellaneousSync(context, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_MISCELLANEOUS_SINCE+params[0], params[1]);
		publishProgress(6);
		return null;
	}
	
	protected void onPostExecute(Long result) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (!updateError) {
			setLocalSyncTimeWithBackendResponse(context);
			/*
			 * Unfortunately this is not realizable at the moment, because the VolleyRequestQueue handles its work in an own Thread
			 * and i don't get feedback from it, so i can not say how much entities are updated/created.
			 * 
			AlertDialog.Builder alert = new AlertDialog.Builder(context)
				.setTitle("Update Übersicht")
				.setMessage("Es wurden "+createdItem+" neue Einträge erstellt und "+updatedItem+" aktualisiert")
				.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
			alert.show();
			*/
		}
	}

	protected void onProgressUpdate(Integer... values) {
		progressDialog.setProgress(values[0]);
	}

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
		    	lastUpdateManager.setLastUpdate(Long.parseLong(response));
		    	lastUpdateManager.close();
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	Log.e("checkLastUpdate", error.toString());
		    	updateError = true;
		    	//Toast.makeText(c, "Setzen der lokalen Update Zeit konnte nicht durchgeführt werden, Fehler bei der Verbindung", Toast.LENGTH_SHORT).show();
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
	private void doAccountSync(final Context c, String url, final Long newSyncTimeStamp) {
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
							accountManager.updateAccountByBackendId(backendAcc);
							update = true;
//							updatedItem++;
						}
					}
					if (!update) {
						Account a = accountManager.addAccount(backendAcc);
						accountManager.addBackendId(a.getId(), backendAcc.getBackendId());
//						createdItem++;
					}
				}//endResponseLoop
		        accountManager.close();
		    }
		}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		        VolleyLog.e("Error: ", error.getMessage());
		        updateError = true;
		        //Toast.makeText(c, "Update der Bankkonten konnte nicht durchgeführt werden, Fehler bei der Verbindung", Toast.LENGTH_SHORT).show();
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
	
	/**
	 * Method to sync the {@link Device}s from the backend locally.
	 * @param c {@link Context}
	 * @param url {@link String}
	 * @param newSyncTimeStamp {@link Long}
	 */
	private void doDeviceSync(final Context c, String url, final Long newSyncTimeStamp) {
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
		    @Override
		    public void onResponse(JSONArray response) {
		    	deviceManager = new DeviceManager(c);
		    	deviceManager.openWritable(c);
		    	List<Device> localDevices = deviceManager.getAllDevices();
		    	int j;
		        for (j = 0; j < response.length(); j++) {
		        	Device backendDev = null;
					try {
						JSONObject jDev = (JSONObject) response.get(j);
						backendDev = new Device(jDev.getString("name"), jDev.getString("number"), 
								jDev.getString("pin"), jDev.getString("puk"));
						backendDev.setBackendId(jDev.getLong("id"));
						backendDev.setActive(jDev.getBoolean("active"));
						backendDev.setCreateTime(new Date(jDev.getLong("createTime")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					boolean update = false;
					for (Device localDev : localDevices) {
						if (localDev.getBackendId() == backendDev.getBackendId()) {
							deviceManager.updateDeviceByBackendId(backendDev);
							update = true;
//							updatedItem++;
						}
					}
					if (!update) {
						Device d = deviceManager.addDevice(backendDev);
						deviceManager.addBackendId(d.getId(), backendDev.getBackendId());
//						createdItem++;
					}
				}//endResponseLoop
		        deviceManager.close();
		    }
		}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		        VolleyLog.e("Error: ", error.getMessage());
		        updateError = true;
		        //Toast.makeText(c, "Update der Geräte konnte nicht durchgeführt werden, Fehler bei der Verbindung", Toast.LENGTH_SHORT).show();
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
	
	/**
	 * Method to sync the {@link Email}s from the backend locally.
	 * @param c {@link Context}
	 * @param url {@link String}
	 * @param newSyncTimeStamp {@link Long}
	 */
	private void doEmailSync(final Context c, String url, final Long newSyncTimeStamp) {
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
		    @Override
		    public void onResponse(JSONArray response) {
		    	emailManager = new EmailManager(c);
		    	emailManager.openWritable(c);
		    	List<Email> localEmails = emailManager.getAllEmails();
		    	int j;
		        for (j = 0; j < response.length(); j++) {
		        	Email backendEmail = null;
					try {
						JSONObject jEmail = (JSONObject) response.get(j);
						backendEmail = new Email(jEmail.getString("emailaddress"), jEmail.getString("password"));
						backendEmail.setBackendId(jEmail.getLong("id"));
						backendEmail.setActive(jEmail.getBoolean("active"));
						backendEmail.setCreateTime(new Date(jEmail.getLong("createTime")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					boolean update = false;
					for (Email localEmail : localEmails) {
						if (localEmail.getBackendId() == backendEmail.getBackendId()) {
							emailManager.updateEmailByBackendId(backendEmail);
							update = true;
//							updatedItem++;
						}
					}
					if (!update) {
						Email e = emailManager.addEmail(backendEmail);
						emailManager.addBackendId(e.getId(), backendEmail.getBackendId());
//						createdItem++;
					}
				}//endResponseLoop
		        emailManager.close();
		    }
		}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		        VolleyLog.e("Error: ", error.getMessage());
		        updateError = true;
		        //Toast.makeText(c, "Update der E-Mails konnte nicht durchgeführt werden, Fehler bei der Verbindung", Toast.LENGTH_SHORT).show();
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
	
	/**
	 * Method to sync the {@link Insurance}s from the backend locally.
	 * @param c {@link Context}
	 * @param url {@link String}
	 * @param newSyncTimeStamp {@link Long}
	 */
	private void doInsuranceSync(final Context c, String url, final Long newSyncTimeStamp) {
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
		    @Override
		    public void onResponse(JSONArray response) {
		    	insuranceManager = new InsuranceManager(c);
		    	insuranceManager.openWritable(c);
		    	List<Insurance> localInsurances = insuranceManager.getAllInsurances();
		    	int j;
		        for (j = 0; j < response.length(); j++) {
		        	Insurance backendInsurance = null;
					try {
						JSONObject jIns = (JSONObject) response.get(j);
						backendInsurance = new Insurance(jIns.getString("name"), 
								jIns.getString("kind"), jIns.getString("membershipId"));
						backendInsurance.setBackendId(jIns.getLong("id"));
						backendInsurance.setActive(jIns.getBoolean("active"));
						backendInsurance.setCreateTime(new Date(jIns.getLong("createTime")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					boolean update = false;
					for (Insurance localInsurance : localInsurances) {
						if (localInsurance.getBackendId() == backendInsurance.getBackendId()) {
							insuranceManager.updateInsuranceByBackendId(backendInsurance);
							update = true;
//							updatedItem++;
						}
					}
					if (!update) {
						Insurance i = insuranceManager.addInsurance(backendInsurance);
						insuranceManager.addBackendId(i.getId(), backendInsurance.getBackendId());
//						createdItem++;
					}
				}//endResponseLoop
		        insuranceManager.close();
		    }
		}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		        VolleyLog.e("Error: ", error.getMessage());
		        updateError = true;
		        //Toast.makeText(c, "Update der Versicherungen konnte nicht durchgeführt werden, Fehler bei der Verbindung", Toast.LENGTH_SHORT).show();
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
	
	/**
	 * Method to sync the {@link Login}s from the backend locally.
	 * @param c {@link Context}
	 * @param url {@link String}
	 * @param newSyncTimeStamp {@link Long}
	 */
	private void doLoginSync(final Context c, String url, final Long newSyncTimeStamp) {
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
		    @Override
		    public void onResponse(JSONArray response) {
		    	loginManager = new LoginManager(c);
		    	loginManager.openWritable(c);
		    	List<Login> localLogins = loginManager.getAllLogins();
		    	int j;
		        for (j = 0; j < response.length(); j++) {
		        	Login backendLogin = null;
					try {
						JSONObject jLog = (JSONObject) response.get(j);
						backendLogin = new Login(jLog.getString("username"), jLog.getString("password"), 
								jLog.getString("description"));
						backendLogin.setBackendId(jLog.getLong("id"));
						backendLogin.setActive(jLog.getBoolean("active"));
						backendLogin.setCreateTime(new Date(jLog.getLong("createTime")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					boolean update = false;
					for (Login localLogin : localLogins) {
						if (localLogin.getBackendId() == backendLogin.getBackendId()) {
							loginManager.updateLoginByBackendId(backendLogin);
							update = true;
//							updatedItem++;
						}
					}
					if (!update) {
						Login l = loginManager.addLogin(backendLogin);
						loginManager.addBackendId(l.getId(), backendLogin.getBackendId());
//						createdItem++;
					}
				}//endResponseLoop
		        loginManager.close();
		    }
		}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		        VolleyLog.e("Error: ", error.getMessage());
		        updateError = true;
		        //Toast.makeText(c, "Update der Logins konnte nicht durchgeführt werden, Fehler bei der Verbindung", Toast.LENGTH_SHORT).show();
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
	
	/**
	 * Method to sync the {@link Miscellaneous} from the backend locally.
	 * @param c {@link Context}
	 * @param url {@link String}
	 * @param newSyncTimeStamp {@link Long}
	 */
	private void doMiscellaneousSync(final Context c, String url, final Long newSyncTimeStamp) {
		JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
		    @Override
		    public void onResponse(JSONArray response) {
		    	miscellaneousManager = new MiscellaneousManager(c);
		    	miscellaneousManager.openWritable(c);
		    	List<Miscellaneous> localMiscellaneouses = miscellaneousManager.getAllMiscellaneous();
		    	int j;
		        for (j = 0; j < response.length(); j++) {
		        	Miscellaneous backendMiscellaneous = null;
					try {
						JSONObject jMisc = (JSONObject) response.get(j);
						backendMiscellaneous = new Miscellaneous(jMisc.getString("text"), jMisc.getString("description"));
						backendMiscellaneous.setBackendId(jMisc.getLong("id"));
						backendMiscellaneous.setActive(jMisc.getBoolean("active"));
						backendMiscellaneous.setCreateTime(new Date(jMisc.getLong("createTime")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					boolean update = false;
					for (Miscellaneous localMiscellaneous : localMiscellaneouses) {
						if (localMiscellaneous.getBackendId() == backendMiscellaneous.getBackendId()) {
							miscellaneousManager.updateMiscellaneousById(backendMiscellaneous);
							update = true;
//							updatedItem++;
						}
					}
					if (!update) {
						Miscellaneous m = miscellaneousManager.addMiscellaneous(backendMiscellaneous);
						miscellaneousManager.addBackendId(m.getId(), backendMiscellaneous.getBackendId());
//						createdItem++;
					}
				}//endResponseLoop
		        miscellaneousManager.close();
		    }
		}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		        VolleyLog.e("Error: ", error.getMessage());
		        updateError = true;
		        //Toast.makeText(c, "Update der Notizen konnte nicht durchgeführt werden, Fehler bei der Verbindung", Toast.LENGTH_SHORT).show();
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
