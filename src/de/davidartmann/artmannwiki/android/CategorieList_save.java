package de.davidartmann.artmannwiki.android;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.adapter.CategoryListSaveArrayAdapter;


public class CategorieList_save extends ListActivity {
	private List<String> listValues = new ArrayList<String>();
	private CategoryListSaveArrayAdapter categoryListAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] values = new String[] {"Bankkonto", "Gerät", "E-Mail", "Versicherung", "Login", "Diverse/Notizen"};
        for(String s : values) {
        	listValues.add(s);
        }
        categoryListAdapter = new CategoryListSaveArrayAdapter(this, listValues);
        setListAdapter(categoryListAdapter);
    }
    
    @SuppressLint("NewApi")
	@Override
	protected void onListItemClick(ListView listView, final View view, int position, long id) {
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
