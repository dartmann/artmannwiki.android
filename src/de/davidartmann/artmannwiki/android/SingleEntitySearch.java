package de.davidartmann.artmannwiki.android;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

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
	private boolean deletedSuccessfully;
	private AccountManager accountManager;
	private DeviceManager deviceManager;
	private EmailManager emailManager;
	private InsuranceManager insuranceManager;
	private LoginManager loginManager;
	private MiscellaneousManager miscellaneousManager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_singleentity);
		// find the activity components
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
						accountManager = new AccountManager(SingleEntitySearch.this);
						accountManager.openWritable(SingleEntitySearch.this);
						deletedSuccessfully = accountManager.softDeleteAccount(a);
						accountManager.close();
						softDeleteInBackend(a, "http://213.165.81.7:8080/ArtmannWiki/rest/account/delete/"+a.getBackendId());
						goBackToCategoryListSearch();
						if (deletedSuccessfully) {
							Toast.makeText(getBaseContext(), "Eintrag erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getBaseContext(), "Eintrag konnte nicht gelöscht werden", Toast.LENGTH_SHORT).show();
						}
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
						deviceManager = new DeviceManager(SingleEntitySearch.this);
						deviceManager.openWritable(SingleEntitySearch.this);
						deletedSuccessfully = deviceManager.softDeleteDevice(d);
						deviceManager.close();
						goBackToCategoryListSearch();
						if (deletedSuccessfully) {
							Toast.makeText(getBaseContext(), "Eintrag erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getBaseContext(), "Eintrag konnte nicht gelöscht werden", Toast.LENGTH_SHORT).show();
						}
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
						emailManager = new EmailManager(SingleEntitySearch.this);
						emailManager.openWritable(SingleEntitySearch.this);
						deletedSuccessfully = emailManager.softDeleteEmail(e);
						emailManager.close();
						goBackToCategoryListSearch();
						if (deletedSuccessfully) {
							Toast.makeText(getBaseContext(), "Eintrag erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getBaseContext(), "Eintrag konnte nicht gelöscht werden", Toast.LENGTH_SHORT).show();
						}
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
						insuranceManager = new InsuranceManager(SingleEntitySearch.this);
						insuranceManager.openWritable(SingleEntitySearch.this);
						deletedSuccessfully = insuranceManager.softDeleteEmail(i);
						insuranceManager.close();
						goBackToCategoryListSearch();
						if (deletedSuccessfully) {
							Toast.makeText(getBaseContext(), "Eintrag erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getBaseContext(), "Eintrag konnte nicht gelöscht werden", Toast.LENGTH_SHORT).show();
						}
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
						loginManager = new LoginManager(SingleEntitySearch.this);
						loginManager.openWritable(SingleEntitySearch.this);
						deletedSuccessfully = loginManager.softDeleteLogin(l);
						loginManager.close();
						goBackToCategoryListSearch();
						if (deletedSuccessfully) {
							Toast.makeText(getBaseContext(), "Eintrag erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getBaseContext(), "Eintrag konnte nicht gelöscht werden", Toast.LENGTH_SHORT).show();
						}
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
						miscellaneousManager = new MiscellaneousManager(SingleEntitySearch.this);
						miscellaneousManager.openWritable(SingleEntitySearch.this);
						deletedSuccessfully = miscellaneousManager.softDeleteLogin(m);
						miscellaneousManager.close();
						goBackToCategoryListSearch();
						if (deletedSuccessfully) {
							Toast.makeText(getBaseContext(), "Eintrag erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getBaseContext(), "Eintrag konnte nicht gelöscht werden", Toast.LENGTH_SHORT).show();
						}
					}
				}).setNegativeButton("Nein", null);
				alert5.show();
			break;
		default:
			break;
		}
	}
	
	private void softDeleteInBackend(final Account a, String url) {
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
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, jAccount, 
				new Response.Listener<JSONObject>() {
					public void onResponse(JSONObject response) {
						// void controller, so nothing to do here
		           	}
				}, 	new Response.ErrorListener() {
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
