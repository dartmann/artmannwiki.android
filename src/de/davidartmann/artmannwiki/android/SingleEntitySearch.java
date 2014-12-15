package de.davidartmann.artmannwiki.android;

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
		// style the button
		//button.getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
		//button.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
		button.setBackgroundResource(R.layout.button_sensible);
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
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.single_entity_search, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
		case R.id.single_entity_action_delete:
			checkTypeAndSoftDelete();
			return true;
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }

	private void checkTypeAndSoftDelete() {
		switch (intentSerializableExtra) {
		case 0:
			AlertDialog.Builder alert = new AlertDialog.Builder(SingleEntitySearch.this)
				.setTitle("Hinweis")
				.setMessage("Sind Sie sicher?\nDas Bankkonto wird mit Bestätigung gelöscht")
				.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						accountManager = new AccountManager(SingleEntitySearch.this);
						accountManager.openWritable();
						long id = intent.getLongExtra("entityId", 0);
						Account a = accountManager.getAccountById(id);
						deletedSuccessfully = accountManager.softDeleteAccount(a);
						accountManager.close();
						Intent intent = new Intent(getBaseContext(), CategoryListSearch.class);
						startActivity(intent);
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
						deviceManager = new DeviceManager(SingleEntitySearch.this);
						deviceManager.openWritable();
						long id = intent.getLongExtra("entityId", 0);
						Device d = deviceManager.getDeviceById(id);
						deletedSuccessfully = deviceManager.softDeleteDevice(d);
						deviceManager.close();
						Intent intent = new Intent(getBaseContext(), CategoryListSearch.class);
						startActivity(intent);
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
						emailManager = new EmailManager(SingleEntitySearch.this);
						emailManager.openWritable();
						long id = intent.getLongExtra("entityId", 0);
						Email e = emailManager.getEmailById(id);
						deletedSuccessfully = emailManager.softDeleteEmail(e);
						emailManager.close();
						Intent intent = new Intent(getBaseContext(), CategoryListSearch.class);
						startActivity(intent);
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
						insuranceManager = new InsuranceManager(SingleEntitySearch.this);
						insuranceManager.openWritable();
						long id = intent.getLongExtra("entityId", 0);
						Insurance i = insuranceManager.getInsuranceById(id);
						deletedSuccessfully = insuranceManager.softDeleteEmail(i);
						insuranceManager.close();
						Intent intent = new Intent(getBaseContext(), CategoryListSearch.class);
						startActivity(intent);
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
						loginManager = new LoginManager(SingleEntitySearch.this);
						loginManager.openWritable();
						long id = intent.getLongExtra("entityId", 0);
						Login l = loginManager.getLoginById(id);
						deletedSuccessfully = loginManager.softDeleteLogin(l);
						loginManager.close();
						Intent intent = new Intent(getBaseContext(), CategoryListSearch.class);
						startActivity(intent);
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
						miscellaneousManager = new MiscellaneousManager(SingleEntitySearch.this);
						miscellaneousManager.openWritable();
						long id = intent.getLongExtra("entityId", 0);
						Miscellaneous m = miscellaneousManager.getMiscellaneousById(id);
						deletedSuccessfully = miscellaneousManager.softDeleteLogin(m);
						miscellaneousManager.close();
						Intent intent = new Intent(getBaseContext(), CategoryListSearch.class);
						startActivity(intent);
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

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

}
