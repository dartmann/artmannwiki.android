package de.davidartmann.artmannwiki.android;

import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.database.AccountManager;
import de.davidartmann.artmannwiki.android.model.Account;


public class CategorieList extends ListActivity {
	private AccountManager accountManager;
	private List<Account> accountList;
	private ArrayAdapter<Account> adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_categorie_list);
        
        //final ListView listview = (ListView) findViewById(R.id.categoryList);
        accountManager = new AccountManager(CategorieList.this);
        accountManager.openWritable();
        //creating testdata
        Account account = new Account("bla", "foo", "bar", "shit");
        account.setActive(true);
        account.setCreateTime(new Date());
        account.setLastUpdate(new Date());
        accountManager.addAccount(account);
        accountList = accountManager.getAllAccounts();
        // use custom layout
        adapter = new ArrayAdapter<Account>(this,
            R.layout.activity_categorie_list, R.id.label, accountList);
        setListAdapter(adapter);
        //Toast.makeText(this, accountList.size(), Toast.LENGTH_LONG).show();
    }
    
    @SuppressLint("NewApi")
	@Override
	protected void onListItemClick(ListView listView, final View view, int position, long id) {
    	accountManager = new AccountManager(CategorieList.this);
        accountManager.openWritable();
    	final Account account = (Account) listView.getItemAtPosition(position);
    	boolean test = accountManager.deleteAccount(account);
    	Toast.makeText(this, String.valueOf(test), Toast.LENGTH_LONG).show();;
        view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                accountList.remove(account);
                adapter.notifyDataSetChanged();
                view.setAlpha(1);
            }
        });
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.categorie_list, menu);
        return true;
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


	@Override
	protected void onPause() {
		accountManager.close();
		super.onPause();
	}


	@Override
	protected void onResume() {
		accountManager.openWritable();
		super.onResume();
	}

}
