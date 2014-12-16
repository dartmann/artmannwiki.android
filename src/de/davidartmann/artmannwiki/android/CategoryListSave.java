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

    @Override
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
    	/*
    	accountManager = new AccountManager(CategorieList.this);
        accountManager.openWritable();
    	final Account account = (Account) listView.getItemAtPosition(position);
    	//accountManager.softDeleteAccount(account);
    	account.setOwner("Pumuckel");
    	accountManager.updateAccount(account);
        view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
            public void run() {
                //accountList.remove(account);
                categoryListAdapter.notifyDataSetChanged();
                view.setAlpha(1);
            }
        });
        */
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	// Not necessary, so commented
        //getMenuInflater().inflate(R.menu.categorie_list, menu);
        //return true;
        return super.onCreateOptionsMenu(menu);
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_exit || super.onOptionsItemSelected(item);
    }


	@Override
	protected void onPause() {
		super.onPause();
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

}
