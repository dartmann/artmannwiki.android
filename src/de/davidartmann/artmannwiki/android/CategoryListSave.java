package de.davidartmann.artmannwiki.android;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.adapter.CategoryListSaveArrayAdapter;
import de.davidartmann.artmannwiki.android.newentities.NewAccount;
import de.davidartmann.artmannwiki.android.newentities.NewDevice;
import de.davidartmann.artmannwiki.android.newentities.NewEmail;
import de.davidartmann.artmannwiki.android.newentities.NewInsurance;
import de.davidartmann.artmannwiki.android.newentities.NewLogin;
import de.davidartmann.artmannwiki.android.newentities.NewMiscellaneous;


public class CategoryListSave extends ListActivity {
	private List<String> listValues = new ArrayList<String>();
	private CategoryListSaveArrayAdapter categoryListAdapter;
	private String[] values;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        values = new String[] {"Bankkonto", "Gerät", "E-Mail", "Versicherung", "Login", "Diverse/Notizen"};
        for(String s : values) {
        	listValues.add(s);
        }
        categoryListAdapter = new CategoryListSaveArrayAdapter(this, listValues);
        setListAdapter(categoryListAdapter);
    }

	protected void onListItemClick(ListView listView, final View view, int position, long id) {
		String s = (String) listView.getItemAtPosition(position);
		if (s.equals(values[0])) {
			Intent intent = new Intent(getBaseContext(), NewAccount.class);
			startActivity(intent);
		} else if(s.equals(values[1])) {
			Intent intent = new Intent(getBaseContext(), NewDevice.class);
			startActivity(intent);
		} else if(s.equals(values[2])) {
			Intent intent = new Intent(getBaseContext(), NewEmail.class);
			startActivity(intent);
		} else if(s.equals(values[3])) {
			Intent intent = new Intent(getBaseContext(), NewInsurance.class);
			startActivity(intent);
		} else if(s.equals(values[4])) {
			Intent intent = new Intent(getBaseContext(), NewLogin.class);
			startActivity(intent);
		} else if(s.equals(values[5])) {
			Intent intent = new Intent(getBaseContext(), NewMiscellaneous.class);
			startActivity(intent);
		}
	}

    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_exit || super.onOptionsItemSelected(item);
    }

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

}
