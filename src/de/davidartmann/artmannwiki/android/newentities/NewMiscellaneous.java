package de.davidartmann.artmannwiki.android.newentities;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.Choice;
import de.davidartmann.artmannwiki.android.database.MiscellaneousManager;
import de.davidartmann.artmannwiki.android.model.Miscellaneous;

public class NewMiscellaneous extends Activity {

	private EditText textEditText;
	private EditText descriptionEditText;
	private Button saveButton;
	private MiscellaneousManager miscellaneousManager;
	private String pleaseFillField;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_miscellaneous);
		//find components
		textEditText = (EditText) findViewById(R.id.activity_new_miscellaneous_edittext_text);
		descriptionEditText = (EditText) findViewById(R.id.activity_new_miscellaneous_edittext_description);
		saveButton = (Button) findViewById(R.id.activity_new_miscellaneous_button_save);
		pleaseFillField = "Bitte ausfüllen";
		
		checkIfUpdate();
		
		saveButton.setOnClickListener(new View.OnClickListener() {
        	
            public void onClick(View view) {
            	if (getIntent().getBooleanExtra("update", false)) {
					updateMiscellaneous();
				} else {
					validate(textEditText, descriptionEditText);
				}
            }
        });
	}

	protected void updateMiscellaneous() {
		Miscellaneous m = (Miscellaneous) getIntent().getSerializableExtra("miscellaneous");
		m.setDescription(descriptionEditText.getText().toString().trim());
		m.setLastUpdate(new Date());
		m.setText(textEditText.getText().toString().trim());
		miscellaneousManager = new MiscellaneousManager(this);
		miscellaneousManager.openWritable(this);
		miscellaneousManager.updateMiscellaneous(m);
		miscellaneousManager.close();
		Toast.makeText(this, "Notiz erfolgreich aktualisiert", Toast.LENGTH_SHORT).show();
		goBackToMain();
	}

	private void checkIfUpdate() {
		if (getIntent().getSerializableExtra("miscellaneous") != null) {
			Miscellaneous m = (Miscellaneous) getIntent().getSerializableExtra("miscellaneous");
			textEditText.setText(m.getText());
			descriptionEditText.setText(m.getDescription());
		}
	}

	protected void validate(EditText textEditText2,	EditText descriptionEditText2) {
		String text = textEditText2.getText().toString().trim();
		String description = descriptionEditText2.getText().toString().trim();
		
		if (text.isEmpty()) {
			textEditText2.setError(pleaseFillField);
		}
		if (description.isEmpty()) {
			descriptionEditText2.setError(pleaseFillField);
		}
		if (!text.isEmpty() && !description.isEmpty()) {
			Miscellaneous m = new Miscellaneous(text, description);
			m.setActive(true);
			m.setCreateTime(new Date());
			miscellaneousManager = new MiscellaneousManager(this);
			miscellaneousManager.openWritable(this);
			miscellaneousManager.addMiscellaneous(m);
			miscellaneousManager.close();
			Toast.makeText(this, "Text erfolgreich abgespeichert", Toast.LENGTH_SHORT).show();
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
