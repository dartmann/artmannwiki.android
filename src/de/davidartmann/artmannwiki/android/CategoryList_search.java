package de.davidartmann.artmannwiki.android;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.database.AccountManager;
import de.davidartmann.artmannwiki.android.model.Account;

public class CategoryList_search extends Activity {
	private Spinner spinner;
	private ListView listView;
	private AccountManager accountManager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categorylist_search);
		spinner = (Spinner) findViewById(R.id.activity_categorylist_search_spinner);
		addSpinnerOnItemSelectListener();
		
		accountManager = new AccountManager(this);
		accountManager.openWritable();
		List<Account> accountList = accountManager.getAllAccounts();
		ArrayAdapter<Account> accountAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_list_item_1, accountList);
		listView = (ListView) findViewById(R.id.activity_categorylist_search_listview);
		listView.setAdapter(accountAdapter);
	}
	
	private void addSpinnerOnItemSelectListener() {
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
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
