package de.davidartmann.artmannwiki.android.newentities;

import java.util.Date;

import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.Choice;
import de.davidartmann.artmannwiki.android.database.InsuranceManager;
import de.davidartmann.artmannwiki.android.model.Insurance;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewInsurance extends Activity {

	private EditText nameEditText;
	private EditText kindEditText;
	private EditText membershipIdEditText;
	private Button saveButton;
	private InsuranceManager insuranceManager;
	private String pleaseFillField;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_insurance);
		// find components
		nameEditText = (EditText) findViewById(R.id.activity_new_insurance_edittext_name);
		kindEditText = (EditText) findViewById(R.id.activity_new_insurance_edittext_kind);
		membershipIdEditText = (EditText) findViewById(R.id.activity_new_insurance_edittext_membershipId);
		saveButton = (Button) findViewById(R.id.activity_new_insurance_button_save);
		pleaseFillField = "Bitte ausfüllen";
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				validate(nameEditText, kindEditText, membershipIdEditText);
			}
		});
	}

	protected void validate(EditText nameEditText2, EditText kindEditText2,	EditText membershipIdEditText2) {
		String name = nameEditText2.getText().toString().trim();
		String kind = kindEditText2.getText().toString().trim();
		String membershipId = membershipIdEditText2.getText().toString().trim();
		
		if (name.isEmpty()) {
			nameEditText2.setError(pleaseFillField);
		}
		if (kind.isEmpty()) {
			kindEditText2.setError(pleaseFillField);
		}
		if (membershipId.isEmpty()) {
			membershipIdEditText2.setError(pleaseFillField);
		}
		if (!name.isEmpty() && !kind.isEmpty() && !membershipId.isEmpty()) {
			Insurance i = new Insurance(name, kind, membershipId);
			i.setActive(true);
			i.setCreateTime(new Date());
			insuranceManager = new InsuranceManager(this);
			insuranceManager.openWritable();
			insuranceManager.addInsurance(i);
			insuranceManager.close();
			Toast.makeText(this, "Versicherung erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
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
