package de.davidartmann.artmannwiki.android;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.backend.BackendConstants;
import de.davidartmann.artmannwiki.android.backend.VolleyRequestQueue;
import de.davidartmann.artmannwiki.android.database.AccountManager;
import de.davidartmann.artmannwiki.android.database.DeviceManager;
import de.davidartmann.artmannwiki.android.database.EmailManager;
import de.davidartmann.artmannwiki.android.database.InsuranceManager;
import de.davidartmann.artmannwiki.android.database.LoginManager;
import de.davidartmann.artmannwiki.android.database.MiscellaneousManager;
import de.davidartmann.artmannwiki.android.model.Account;
import de.davidartmann.artmannwiki.android.model.Device;
import de.davidartmann.artmannwiki.android.model.Email;
import de.davidartmann.artmannwiki.android.model.Insurance;
import de.davidartmann.artmannwiki.android.model.Login;
import de.davidartmann.artmannwiki.android.model.Miscellaneous;
import de.davidartmann.artmannwiki.android.newentities.NewAccount;
import de.davidartmann.artmannwiki.android.newentities.NewDevice;
import de.davidartmann.artmannwiki.android.newentities.NewEmail;
import de.davidartmann.artmannwiki.android.newentities.NewInsurance;
import de.davidartmann.artmannwiki.android.newentities.NewLogin;
import de.davidartmann.artmannwiki.android.newentities.NewMiscellaneous;

public class SingleEntitySearch extends Activity {
	
	private TextView normalTextView;
	private Button button;
	private TextView secretTextView;
	private Intent intent;
	private int intentSerializableExtra;
	private AccountManager accountManager;
	private DeviceManager deviceManager;
	private EmailManager emailManager;
	private InsuranceManager insuranceManager;
	private LoginManager loginManager;
	private MiscellaneousManager miscellaneousManager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_singleentity);
		normalTextView = (TextView) findViewById(R.id.activity_search_singleentity_textview_normal);
		button = (Button) findViewById(R.id.activity_search_singleentity_textview_button);
		secretTextView = (TextView) findViewById(R.id.activity_search_singleentity_textview_secrets);
		// get the clicked Object deserialized
		intent = getIntent();
		performCheckOfIntentExtra(intent);
	}

	private void performCheckOfIntentExtra(Intent intent) {
		if (intent.getSerializableExtra("account") != null) {
			intentSerializableExtra = 0;
			final Account a = (Account) intent.getSerializableExtra("account");
			// fill components
			setTitle("Bankkonto");
			normalTextView.setText(a.getNormalData());		
			// little security enhancement, only display critical data, when button "yes" is clicked
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (secretTextView.getVisibility() == View.GONE) {
						AlertDialog.Builder alert = new AlertDialog.Builder(SingleEntitySearch.this)
							.setTitle("Hinweis")
							.setMessage("Sind Sie sicher?\nSensible Daten werden mit Bestätigung angezeigt")
							.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									secretTextView.setText("Pin: "+a.getPin());
									secretTextView.setVisibility(View.VISIBLE);
								}
							}).setNegativeButton("Nein", null);
						alert.show();
					}
				}
			});
		} else if(intent.getSerializableExtra("device") != null) {
			intentSerializableExtra = 1;
			final Device d = (Device) intent.getSerializableExtra("device");
			setTitle("Gerät");
			normalTextView.setText(d.toString());
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (secretTextView.getVisibility() == View.GONE) {
						AlertDialog.Builder alert = new AlertDialog.Builder(SingleEntitySearch.this)
							.setTitle("Hinweis")
							.setMessage("Sind Sie sicher?\nSensible Daten werden mit Bestätigung angezeigt")
							.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									secretTextView.setText("Pin: "+d.getPin()+"\n"+"Puk: "+d.getPuk());
									secretTextView.setVisibility(View.VISIBLE);
								}
							}).setNegativeButton("Nein", null);
						alert.show();
					}
				}
			});
		} else if(intent.getSerializableExtra("email") != null) {
			intentSerializableExtra = 2;
			final Email e = (Email) intent.getSerializableExtra("email");
			setTitle("E-Mail");
			normalTextView.setText(e.toString());
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (secretTextView.getVisibility() == View.GONE) {
						AlertDialog.Builder alert = new AlertDialog.Builder(SingleEntitySearch.this)
							.setTitle("Hinweis")
							.setMessage("Sind Sie sicher?\nSensible Daten werden mit Bestätigung angezeigt")
							.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									secretTextView.setText("Passwort: "+e.getPassword());
									secretTextView.setVisibility(View.VISIBLE);
								}
							}).setNegativeButton("Nein", null);
						alert.show();
					}
				}
			});
		} else if (intent.getSerializableExtra("insurance") != null) {
			intentSerializableExtra = 3;
			final Insurance i = (Insurance) intent.getSerializableExtra("insurance");
			setTitle("Versicherung");
			normalTextView.setText(i.getNormalData());
			button.setVisibility(View.GONE);
		} else if(intent.getSerializableExtra("login") != null) {
			intentSerializableExtra = 4;
			final Login l = (Login) intent.getSerializableExtra("login");
			setTitle("Login");
			normalTextView.setText(l.getNormalData());
			button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if (secretTextView.getVisibility() == View.GONE) {
						AlertDialog.Builder alert = new AlertDialog.Builder(SingleEntitySearch.this)
							.setTitle("Hinweis")
							.setMessage("Sind Sie sicher?\nSensible Daten werden mit Bestätigung angezeigt")
							.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									secretTextView.setText("Passwort: "+l.getPassword());
									secretTextView.setVisibility(View.VISIBLE);
								}
							}).setNegativeButton("Nein", null);
						alert.show();
					}
				}
			});
		} else if(intent.getSerializableExtra("miscellaneous") != null) {
			intentSerializableExtra = 5;
			final Miscellaneous m = (Miscellaneous) intent.getSerializableExtra("miscellaneous");
			setTitle("Diverse/Notizen");
			normalTextView.setText(m.toString());
			button.setVisibility(View.GONE);
		}
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.single_entity_search, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
		case R.id.single_entity_action_delete:
			checkTypeAndSoftDelete();
			return true;
		case R.id.single_entity_action_edit:
			openEditActivity();
			return true;
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }

	private void openEditActivity() {
		switch (intentSerializableExtra) {
		case 0:
			if (intent.getSerializableExtra("account") != null) {
				Account a = (Account) intent.getSerializableExtra("account");
				Intent intent = new Intent(getBaseContext(), NewAccount.class);
				intent.putExtra("account", a);
				intent.putExtra("update", true);
				startActivity(intent);
			}
			break;
		case 1:
			if (intent.getSerializableExtra("device") != null) {
				Device d = (Device) intent.getSerializableExtra("device");
				Intent intent = new Intent(getBaseContext(), NewDevice.class);
				intent.putExtra("device", d);
				intent.putExtra("update", true);
				startActivity(intent);
			}
			break;
		case 2:
			if (intent.getSerializableExtra("email") != null) {
				Email e = (Email) intent.getSerializableExtra("email");
				Intent intent = new Intent(getBaseContext(), NewEmail.class);
				intent.putExtra("email", e);
				intent.putExtra("update", true);
				startActivity(intent);
			}
			break;
		case 3:
			if (intent.getSerializableExtra("insurance") != null) {
				Insurance i = (Insurance) intent.getSerializableExtra("insurance");
				Intent intent = new Intent(getBaseContext(), NewInsurance.class);
				intent.putExtra("insurance", i);
				intent.putExtra("update", true);
				startActivity(intent);
			}
			break;
		case 4:
			if (intent.getSerializableExtra("login") != null) {
				Login l = (Login) intent.getSerializableExtra("login");
				Intent intent = new Intent(getBaseContext(), NewLogin.class);
				intent.putExtra("login", l);
				intent.putExtra("update", true);
				startActivity(intent);
			}
			break;
		case 5:
			if (intent.getSerializableExtra("miscellaneous") != null) {
				Miscellaneous m = (Miscellaneous) intent.getSerializableExtra("miscellaneous");
				Intent intent = new Intent(getBaseContext(), NewMiscellaneous.class);
				intent.putExtra("miscellaneous", m);
				intent.putExtra("update", true);
				startActivity(intent);
			}
		default:
			break;
		}
	}

	private void checkTypeAndSoftDelete() {
		switch (intentSerializableExtra) {
		case 0:
			AlertDialog.Builder alert = new AlertDialog.Builder(SingleEntitySearch.this)
				.setTitle("Hinweis")
				.setMessage("Sind Sie sicher?\nDas Bankkonto wird mit Bestätigung gelöscht")
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Account a = (Account) intent.getSerializableExtra("account");
						softDeleteAccountInBackend(a, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.SOFT_DELETE_ACCOUNT_BY_ID+a.getBackendId());
						goBackToCategoryListSearch();
					}
				}).setNegativeButton("Nein", null);
				alert.show();
			break;
		case 1:
			AlertDialog.Builder alert1 = new AlertDialog.Builder(SingleEntitySearch.this)
				.setTitle("Hinweis")
				.setMessage("Sind Sie sicher?\nDas Gerät wird mit Bestätigung gelöscht")
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Device d = (Device) intent.getSerializableExtra("device");
						softDeleteDeviceInBackend(d, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.SOFT_DELETE_DEVICE_BY_ID+d.getBackendId());
						goBackToCategoryListSearch();
					}
				}).setNegativeButton("Nein", null);
				alert1.show();
			break;
		case 2:
			AlertDialog.Builder alert2 = new AlertDialog.Builder(SingleEntitySearch.this)
				.setTitle("Hinweis")
				.setMessage("Sind Sie sicher?\nDie E-Mail wird mit Bestätigung gelöscht")
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Email e = (Email) intent.getSerializableExtra("email");
						softDeleteEmailInBackend(e, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.SOFT_DELETE_EMAIL_BY_ID+e.getBackendId());
						goBackToCategoryListSearch();
					}
				}).setNegativeButton("Nein", null);
				alert2.show();
			break;
		case 3:
			AlertDialog.Builder alert3 = new AlertDialog.Builder(SingleEntitySearch.this)
				.setTitle("Hinweis")
				.setMessage("Sind Sie sicher?\nDie Versicherung wird mit Bestätigung gelöscht")
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Insurance i = (Insurance) intent.getSerializableExtra("insurance");
						softDeleteInsuranceInBackend(i, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.SOFT_DELETE_INSURANCE_BY_ID+i.getBackendId());
						goBackToCategoryListSearch();
					}
				}).setNegativeButton("Nein", null);
				alert3.show();
			break;
		case 4:
			AlertDialog.Builder alert4 = new AlertDialog.Builder(SingleEntitySearch.this)
				.setTitle("Hinweis")
				.setMessage("Sind Sie sicher?\nDer Login wird mit Bestätigung gelöscht")
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Login l = (Login) intent.getSerializableExtra("login");
						softDeleteLoginInBackend(l, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.SOFT_DELETE_LOGIN_BY_ID+l.getBackendId());
						goBackToCategoryListSearch();
					}
				}).setNegativeButton("Nein", null);
				alert4.show();
			break;
		case 5:
			AlertDialog.Builder alert5 = new AlertDialog.Builder(SingleEntitySearch.this)
				.setTitle("Hinweis")
				.setMessage("Sind Sie sicher?\nDie Notiz wird mit Bestätigung gelöscht")
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Miscellaneous m = (Miscellaneous) intent.getSerializableExtra("miscellaneous");
						softDeleteMiscellaneousInBackend(m, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.SOFT_DELETE_MISCELLANEOUS_BY_ID+m.getBackendId());
						goBackToCategoryListSearch();
					}
				}).setNegativeButton("Nein", null);
				alert5.show();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Method to <i>softdelete</i> an {@link Account} in the backend.
	 * @param a ({@link Account})
	 * @param url ({@link String})
	 */
	private void softDeleteAccountInBackend(final Account a, String url) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
		    public void onResponse(String response) {
		    	accountManager = new AccountManager(SingleEntitySearch.this);
            	accountManager.openWritable(SingleEntitySearch.this);
            	accountManager.softDeleteAccount(a);
            	accountManager.close();
		    }
			}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		    	VolleyLog.e("Error: ", error.getMessage());
				Toast.makeText(SingleEntitySearch.this, "Konnte Konto nicht löschen, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
		    }
			}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
                return headers;  
			}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
	}
	
	/**
	 * Method to <i>softdelete</i> an {@link Device} in the backend.
	 * @param d ({@link Device})
	 * @param url ({@link String})
	 */
	private void softDeleteDeviceInBackend(final Device d, String url) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
		    public void onResponse(String response) {
		    	deviceManager = new DeviceManager(SingleEntitySearch.this);
		    	deviceManager.openWritable(SingleEntitySearch.this);
		    	deviceManager.softDeleteDevice(d);
		    	deviceManager.close();
		    }
			}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		    	VolleyLog.e("Error: ", error.getMessage());
				Toast.makeText(SingleEntitySearch.this, "Konnte Gerät nicht löschen, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
		    }
			}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
                return headers;  
			}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
	}
	
	/**
	 * Method to <i>softdelete</i> an {@link Email} in the backend.
	 * @param e ({@link Email})
	 * @param url ({@link String})
	 */
	private void softDeleteEmailInBackend(final Email e, String url) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
		    public void onResponse(String response) {
		    	emailManager = new EmailManager(SingleEntitySearch.this);
		    	emailManager.openWritable(SingleEntitySearch.this);
		    	emailManager.softDeleteEmail(e);
		    	emailManager.close();
		    }
			}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		    	VolleyLog.e("Error: ", error.getMessage());
				Toast.makeText(SingleEntitySearch.this, "Konnte E-Mail nicht löschen, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
		    }
			}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
                return headers;  
			}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
	}
	
	/**
	 * Method to <i>softdelete</i> an {@link Insurance} in the backend.
	 * @param i ({@link Insurance})
	 * @param url ({@link String})
	 */
	private void softDeleteInsuranceInBackend(final Insurance i, String url) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
		    public void onResponse(String response) {
		    	insuranceManager = new InsuranceManager(SingleEntitySearch.this);
		    	insuranceManager.openWritable(SingleEntitySearch.this);
		    	insuranceManager.softDeleteInsurance(i);
		    	insuranceManager.close();
		    }
			}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		    	VolleyLog.e("Error: ", error.getMessage());
				Toast.makeText(SingleEntitySearch.this, "Konnte Versicherung nicht löschen, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
		    }
			}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
                return headers;  
			}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
	}
	
	/**
	 * Method to <i>softdelete</i> an {@link Login} in the backend.
	 * @param l ({@link Login})
	 * @param url ({@link String})
	 */
	private void softDeleteLoginInBackend(final Login l, String url) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
		    public void onResponse(String response) {
		    	loginManager = new LoginManager(SingleEntitySearch.this);
		    	loginManager.openWritable(SingleEntitySearch.this);
		    	loginManager.softDeleteLogin(l);
		    	loginManager.close();
		    }
			}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		    	VolleyLog.e("Error: ", error.getMessage());
				Toast.makeText(SingleEntitySearch.this, "Konnte Login nicht löschen, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
		    }
			}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
                return headers;  
			}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
	}
	
	/**
	 * Method to <i>softdelete</i> an {@link Miscellaneous} in the backend.
	 * @param m ({@link Miscellaneous})
	 * @param url ({@link String})
	 */
	private void softDeleteMiscellaneousInBackend(final Miscellaneous m, String url) {
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
		            new Response.Listener<String>() {
		    public void onResponse(String response) {
		    	miscellaneousManager = new MiscellaneousManager(SingleEntitySearch.this);
		    	miscellaneousManager.openWritable(SingleEntitySearch.this);
		    	miscellaneousManager.softDeleteLogin(m);
		    	miscellaneousManager.close();
		    }
			}, new Response.ErrorListener() {
		    public void onErrorResponse(VolleyError error) {
		    	VolleyLog.e("Error: ", error.getMessage());
				Toast.makeText(SingleEntitySearch.this, "Konnte Notiz nicht löschen, Fehler bei Übertragung zum Server", Toast.LENGTH_SHORT).show();
		    }
			}) {
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
                return headers;  
			}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
	}
	
	private void goBackToCategoryListSearch() {
		Intent intent = new Intent(getBaseContext(), CategoryListSearch.class);
		startActivity(intent);
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

}
