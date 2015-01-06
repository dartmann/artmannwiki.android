package de.davidartmann.artmannwiki.android;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.backend.BackendConstants;
import de.davidartmann.artmannwiki.android.backend.SyncManager;
import de.davidartmann.artmannwiki.android.backend.VolleyRequestQueue;
import de.davidartmann.artmannwiki.android.database.LastUpdateManager;


public class Choice extends Activity {
	
	private Button wikiSearchButton;
	private Button wikiNewEntityButton;
	private LastUpdateManager lastUpdateManager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_choice_landscape);
        }
        else {
            setContentView(R.layout.activity_choice_portrait);
        }
        
        wikiSearchButton = (Button) findViewById(R.id.choice_wiki_search);
        wikiNewEntityButton = (Button) findViewById(R.id.choice_wiki_new_entity);
        
        //TODO: make an automatic check if update is needed and inform the user -> after first login (extra from mainLogin activity) perform a sync always

        wikiSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent intent = new Intent(getBaseContext(), CategoryListSearch.class);
                startActivity(intent);
            }
        });
        wikiNewEntityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent intent = new Intent(getBaseContext(), CategoryListSave.class);
                startActivity(intent);
            }
        });
        
    }

	public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.choice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
		case R.id.menu_choice_action_sync:
			checkLastUpdate(Request.Method.GET, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_AND_SET_LAST_UPDATE);
			return true;
		case R.id.menu_choice_action_exit:
			finish();
	        Runtime.getRuntime().addShutdownHook(new Thread() {
	        	@Override
	        	public void run() {
	        		Intent intent = new Intent(Intent.ACTION_MAIN);
	        		intent.addCategory(Intent.CATEGORY_HOME);
	        		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        		startActivity(intent);
	        	}
	        });;
			return true;
		default:
			break;
		}
        return super.onOptionsItemSelected(item);
    }

	private void checkLastUpdate(int method, String url) {
		StringRequest stringRequest = new StringRequest(method, url,
		            new Response.Listener<String>() {
		    public void onResponse(String response) {
		    	Long responseTime = Long.parseLong(response);
		    	lastUpdateManager = new LastUpdateManager(Choice.this);
		    	lastUpdateManager.openReadable(Choice.this);
		    	Long localLastUpdate = lastUpdateManager.getLastUpdate();
		    	lastUpdateManager.close();
		    	System.out.println("Lokale Zeit: "+localLastUpdate+" Backend Zeit: "+responseTime);
		    	if (responseTime > localLastUpdate) {
		    		Toast.makeText(Choice.this, "Update wird durchgeführt", Toast.LENGTH_SHORT).show();
					new SyncManager().doAccountSync(Choice.this, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_ACCOUNTS_SINCE+localLastUpdate, responseTime);
					new SyncManager().doDeviceSync(Choice.this, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_DEVICES_SINCE+localLastUpdate, responseTime);
					new SyncManager().doEmailSync(Choice.this, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_EMAILS_SINCE+localLastUpdate, responseTime);
					new SyncManager().doInsuranceSync(Choice.this, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_INSURANCES_SINCE+localLastUpdate, responseTime);
					//TODO: other syncings and the local sync time should be setted one time after all syncs have passed 
					//-> async task with post progress and every sync could hold a percentage to display in progressbar
				} else {
					Toast.makeText(Choice.this, "Kein Update nötig", Toast.LENGTH_SHORT).show();
				}
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	Log.e("checkLastUpdate", error.toString());
		    }
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
                return headers;  
			}
		};
		VolleyRequestQueue.getInstance(this).addToRequestQueue(stringRequest);
	}
}
