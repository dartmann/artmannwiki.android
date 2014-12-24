package de.davidartmann.artmannwiki.android.newentities;

import java.util.Date;

import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.Choice;
import de.davidartmann.artmannwiki.android.database.AccountManager;
import de.davidartmann.artmannwiki.android.model.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
		// find components
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
					updateAccount();
				} else {
					validate(ownerEditText, ibanEditText, bicEditText, pinEditText);
				}
			}
		});
	}

	// store the new data
	protected void updateAccount() {
		Account a = (Account) getIntent().getSerializableExtra("account");
		a.setOwner(ownerEditText.getText().toString().trim());
		a.setIban(ibanEditText.getText().toString().trim());
		a.setBic(bicEditText.getText().toString().trim());
		a.setPin(pinEditText.getText().toString().trim());
		a.setLastUpdate(new Date());
		accountManager = new AccountManager(this);
		//TODO testing
		accountManager.openWritable(this);
		accountManager.updateAccount(a);
		accountManager.close();
		Toast.makeText(this, "Bankkonto erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
		goBackToMain();
	}

	// fill the fields with the data of the account to update
	private void checkIfUpdate() {
		if (getIntent().getSerializableExtra("account") != null) {
			Account a = (Account) getIntent().getSerializableExtra("account");
			ownerEditText.setText(a.getOwner());
			ibanEditText.setText(a.getIban());
			bicEditText.setText(a.getBic());
			pinEditText.setText(a.getPin());
		}
	}

	protected void validate(EditText ownerEditText2, EditText ibanEditText2, EditText bicEditText2, EditText pinEditText2) {
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
			a.setCreateTime(new Date());
			accountManager = new AccountManager(this);
			//TODO testing
			accountManager.openWritable(this);
			accountManager.addAccount(a);
			accountManager.close();
			Toast.makeText(this, "Bankkonto erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
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
