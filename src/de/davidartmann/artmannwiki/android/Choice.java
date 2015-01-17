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
        if (getIntent().getBooleanExtra("firstLogin", false)) {
        	checkLastUpdate();
        } else if (getIntent().getBooleanExtra("login", false)) {
        	checkLastUpdate();
        }
        
        wikiSearchButton = (Button) findViewById(R.id.choice_wiki_search);
        wikiNewEntityButton = (Button) findViewById(R.id.choice_wiki_new_entity);

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
    
    @Override
	protected void onPause() {
		super.onPause();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.choice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
		case R.id.menu_choice_action_sync:
			checkLastUpdate();
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

    /**
	 * Method to check the backend for the last update time
	 * @param method
	 * @param url
	 */
	private void checkLastUpdate() {
		StringRequest stringRequest = new StringRequest(Request.Method.GET, BackendConstants.ARTMANNWIKI_ROOT+BackendConstants.GET_AND_SET_LAST_UPDATE,
		            new Response.Listener<String>() {
		    public void onResponse(String response) {
		    	Long responseTime = Long.parseLong(response);
		    	lastUpdateManager = new LastUpdateManager(Choice.this);
		    	lastUpdateManager.openReadable(Choice.this);
		    	Long localLastUpdate = lastUpdateManager.getLastUpdate();
		    	lastUpdateManager.close();
		    	if (localLastUpdate < responseTime) {
		    		new SyncManager(Choice.this).execute(localLastUpdate, responseTime);
				}
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	Log.e("checkLastUpdate", error.toString());
		    	Toast.makeText(Choice.this, "Fehler beim check der letzten Updatezeit - Server nicht erreichbar", Toast.LENGTH_SHORT).show();
		    }
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				headers.put(BackendConstants.HEADER_KEY, BackendConstants.HEADER_VALUE);
                return headers;  
			}
		};
		VolleyRequestQueue.getInstance(Choice.this).addToRequestQueue(stringRequest);
	}
}
