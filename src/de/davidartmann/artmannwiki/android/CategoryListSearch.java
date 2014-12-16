package de.davidartmann.artmannwiki.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
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

public class CategoryListSearch extends Activity {
	private Spinner spinner;
	private ListView listView;
	private AccountManager accountManager;
	private DeviceManager deviceManager;
	private EmailManager emailManager;
	private InsuranceManager insuranceManager;
	private LoginManager loginManager;
	private MiscellaneousManager miscellaneousManager;
	private List<String> spinnerItems;
	private String selectedSpinnerItem;
	private String[] values;
	private ArrayAdapter<Account> accountAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categorylist_search);
		// customize the spinner
		spinnerItems = new ArrayList<String>();
		values = new String[] {"Bankkonto", "Ger�t", "E-Mail", "Versicherung", "Login", "Diverse/Notizen"};
		for(String s : values) {
        	spinnerItems.add(s);
        }
		// Instantiate the manager classes
		accountManager = new AccountManager(this);
		deviceManager = new DeviceManager(this);
		emailManager = new EmailManager(this);
		insuranceManager = new InsuranceManager(this);
		loginManager = new LoginManager(this);
		miscellaneousManager = new MiscellaneousManager(this);
		// find the components
		spinner = (Spinner) findViewById(R.id.activity_categorylist_search_spinner);
		listView = (ListView) findViewById(R.id.activity_categorylist_search_listview);
		// add features
			//TODO: customize the spinner (images like the save list)
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, spinnerItems);
		spinner.setAdapter(spinnerAdapter);
		addSpinnerOnItemSelectListener();
		addListViewOnItemClickListener();
			//TODO delete if testing finished
		makeTestData();
	}
	
	private void addListViewOnItemClickListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				Object o = listView.getItemAtPosition(position);
				checkWhichKindOfEntity(o);
			}
		});
	}

	// helper method to check which kind of item was clicked
	private void checkWhichKindOfEntity(Object o) {
		if (selectedSpinnerItem.equals(values[0])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			Account a = (Account) o;
			intent.putExtra("account", a);
			intent.putExtra("entityId", a.getId());
            startActivity(intent);
		} else if (selectedSpinnerItem.equals(values[1])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			Device d = (Device) o;
			intent.putExtra("device", d);
			intent.putExtra("entityId", d.getId());
            startActivity(intent);
		} else if (selectedSpinnerItem.equals(values[2])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			Email e = (Email) o;
			intent.putExtra("email", e);
			intent.putExtra("entityId", e.getId());
            startActivity(intent);
		} else if (selectedSpinnerItem.equals(values[3])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			Insurance i = (Insurance) o;
			intent.putExtra("insurance", i);
			intent.putExtra("entityId", i.getId());
            startActivity(intent);
		} else if (selectedSpinnerItem.equals(values[4])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			Login l = (Login) o;
			intent.putExtra("login", l);
			intent.putExtra("entityId", l.getId());
            startActivity(intent);
		} else if (selectedSpinnerItem.equals(values[5])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			Miscellaneous m = (Miscellaneous) o;
			intent.putExtra("miscellaneous", m);
			intent.putExtra("entityId", m.getId());
            startActivity(intent);
		}
		
	}

	private void addSpinnerOnItemSelectListener() {
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String selectedItem = (String) parent.getItemAtPosition(position);
				showSelectedEntities(selectedItem);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// nothing to do here
			}
		});
	}

	//TODO: implement the viewholder pattern
	private void showSelectedEntities(String s) {
		if (s.equals("Bankkonto")) {
			selectedSpinnerItem = values[0];
			accountManager.openReadable();
			List<Account> accountList = accountManager.getAllAccounts();
			/*ArrayAdapter<Account> */accountAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_list_item_1, accountList);
			listView.setAdapter(accountAdapter);
			accountAdapter.notifyDataSetChanged();
		} else if(s.equals("Ger�t")) {
			selectedSpinnerItem = values[1];
			deviceManager.openReadable();
			List<Device> deviceList = deviceManager.getAllDevices();
			ArrayAdapter<Device> deviceAdapter = new ArrayAdapter<Device>(this, android.R.layout.simple_list_item_1, deviceList);
			listView.setAdapter(deviceAdapter);
			deviceAdapter.notifyDataSetChanged();
		} else if (s.equals("E-Mail")) {
			selectedSpinnerItem = values[2];
			emailManager.openReadable();
			List<Email> emailList = emailManager.getAllEmails();
			ArrayAdapter<Email> emailAdapter = new ArrayAdapter<Email>(this, android.R.layout.simple_list_item_1, emailList);
			listView.setAdapter(emailAdapter);
			emailAdapter.notifyDataSetChanged();
		} else if (s.equals("Versicherung")) {
			selectedSpinnerItem = values[3];
			insuranceManager.openReadable();
			List<Insurance> insuranceList = insuranceManager.getAllInsurances();
			ArrayAdapter<Insurance> insuranceAdapter = new ArrayAdapter<Insurance>(this, android.R.layout.simple_list_item_1, insuranceList);
			listView.setAdapter(insuranceAdapter);
			insuranceAdapter.notifyDataSetChanged();
		} else if (s.equals("Login")) {
			selectedSpinnerItem = values[4];
			loginManager.openReadable();
			List<Login> loginList = loginManager.getAllLogins();
			ArrayAdapter<Login> loginAdapter = new ArrayAdapter<Login>(this, android.R.layout.simple_list_item_1, loginList);
			listView.setAdapter(loginAdapter);
			loginAdapter.notifyDataSetChanged();
		} else if (s.equals("Diverse/Notizen")) {
			selectedSpinnerItem = values[5];
			miscellaneousManager.openReadable();
			List<Miscellaneous> miscellaneousList = miscellaneousManager.getAllMiscellaneous();
			ArrayAdapter<Miscellaneous> miscArrayAdapter = new ArrayAdapter<Miscellaneous>(this, android.R.layout.simple_list_item_1, miscellaneousList);
			listView.setAdapter(miscArrayAdapter);
			miscArrayAdapter.notifyDataSetChanged();
		}
	}
	
	private void makeTestData() {		
		accountManager.openWritable();
		deviceManager.openWritable();
		emailManager.openWritable();
		insuranceManager.openWritable();
		loginManager.openWritable();
		miscellaneousManager.openWritable();
		for(int i = 0; i<10; i++) {
			Account account = new Account(String.valueOf(i), "123456123", "BYLADMNIEA", "1234");
	        account.setActive(true);
	        accountManager.addAccount(account);
		}		
        
        Device device = new Device("Handy123", "017526661654", "1234", "13245678");
        device.setActive(true);
        deviceManager.addDevice(device);
        
        Email email = new Email("blafoo@test.de", "jklsadfjklsdafjkl�");
        email.setActive(true);
        emailManager.addEmail(email);
        
        Insurance insurance = new Insurance("testversicherung", "bscheiser", "321321321321");
        insurance.setActive(true);
        insuranceManager.addInsurance(insurance);
        
        Login login = new Login("administrator", "123superSecure@123!", "admintestuser");
        login.setActive(true);
        loginManager.addLogin(login);
        
        Miscellaneous miscellaneous = new Miscellaneous("kasdfsdfajklsdfajkl�", "asasdasdasdasdasd");
        miscellaneous.setActive(true);
        miscellaneousManager.addMiscellaneous(miscellaneous);
	}

	protected void onPause() {
		accountManager.close();
		deviceManager.close();
		emailManager.close();
		insuranceManager.close();
		loginManager.close();
		miscellaneousManager.close();
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.categorie_list_search, menu);
		MenuItem menuItem = menu.findItem(R.id.menu_categorylist_action_search);
		SearchView searchView = (SearchView) menuItem.getActionView();
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		if (searchManager != null) {
			searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		}
		// show only icon by default
		searchView.setIconifiedByDefault(true);
		searchView.setQueryHint(getString(R.string.categorylist_hint_search));
		addOnQueryTextListener(searchView);
		return true;
        //return super.onCreateOptionsMenu(menu);
	}

	//TODO: test filter
	private void addOnQueryTextListener(SearchView searchView) {
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			public boolean onQueryTextSubmit(String s) {
				CategoryListSearch.this.accountAdapter.getFilter().filter(s);
				return true;
			}
			
			public boolean onQueryTextChange(String s) {
				CategoryListSearch.this.accountAdapter.getFilter().filter(s);
				return true;
			}
		});
		
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
        switch (id) {
		case R.id.menu_categorylist_action_search:
			return true;
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
	}
}