package de.davidartmann.artmannwiki.android;

import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.model.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SingleEntitySearch extends Activity {
	
	private TextView normalTextView;
	private Button button;
	private TextView secretTextView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_singleentity);
		// get the clicked Object deserialized
		Intent i = getIntent();
		final Account a = (Account) i.getSerializableExtra("account");
		// find the activity components
		normalTextView = (TextView) findViewById(R.id.activity_search_singleentity_textview_normal);
		button = (Button) findViewById(R.id.activity_search_singleentity_textview_button);
		secretTextView = (TextView) findViewById(R.id.activity_search_singleentity_textview_secrets);
		// fill components
		setTitle(a.getOwner());
		normalTextView.setText(a.getNormalData());
		// little security enhancement, only display critical data, when button is clicked
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (secretTextView.getVisibility() == View.GONE) {
					AlertDialog.Builder alert = new AlertDialog.Builder(SingleEntitySearch.this);
					alert.setTitle("Hinweis");
					alert.setMessage("Sind Sie sicher?\nSensible Daten werden mit Bestätigung angezeigt");
					alert.setNegativeButton("Nein", null);
					
						
					
					alert.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							secretTextView.setText(a.getPin());
							secretTextView.setVisibility(View.VISIBLE);
						}
					});
					alert.show();
				}
			}
		});
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

}
