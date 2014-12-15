package de.davidartmann.artmannwiki.android;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categorylist_search);
		// customize the spinner
		spinnerItems = new ArrayList<String>();
		values = new String[] {"Bankkonto", "Gerät", "E-Mail", "Versicherung", "Login", "Diverse/Notizen"};
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
			intent.putExtra("account", (Account) o);
            startActivity(intent);
		} else if (selectedSpinnerItem.equals(values[1])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			intent.putExtra("device", (Device) o);
            startActivity(intent);
		} else if (selectedSpinnerItem.equals(values[2])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			intent.putExtra("email", (Email) o);
            startActivity(intent);
		} else if (selectedSpinnerItem.equals(values[3])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			intent.putExtra("insurance", (Insurance) o);
            startActivity(intent);
		} else if (selectedSpinnerItem.equals(values[4])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			intent.putExtra("login", (Login) o);
            startActivity(intent);
		} else if (selectedSpinnerItem.equals(values[5])) {
			Intent intent = new Intent(getBaseContext(), SingleEntitySearch.class);
			intent.putExtra("miscellaneous", (Miscellaneous) o);
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
			ArrayAdapter<Account> accountAdapter = new ArrayAdapter<Account>(this, android.R.layout.simple_list_item_1, accountList);
			listView.setAdapter(accountAdapter);
			accountAdapter.notifyDataSetChanged();
		} else if(s.equals("Gerät")) {
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
        
        Email email = new Email("blafoo@test.de", "jklsadfjklsdafjklö");
        email.setActive(true);
        emailManager.addEmail(email);
        
        Insurance insurance = new Insurance("testversicherung", "bscheiser", "321321321321");
        insurance.setActive(true);
        insuranceManager.addInsurance(insurance);
        
        Login login = new Login("administrator", "123superSecure@123!", "admintestuser");
        login.setActive(true);
        loginManager.addLogin(login);
        
        Miscellaneous miscellaneous = new Miscellaneous("kasdfsdfajklsdfajklö", "asasdasdasdasdasd");
        miscellaneous.setActive(false);
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

	
}
