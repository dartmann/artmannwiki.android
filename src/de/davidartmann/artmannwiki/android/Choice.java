package de.davidartmann.artmannwiki.android;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import de.artmann.artmannwiki.R;
import de.davidartmann.artmannwiki.android.backend.BackendConstants;
import de.davidartmann.artmannwiki.android.backend.VolleyRequestQueue;
import de.davidartmann.artmannwiki.android.database.LastUpdateManager;


public class Choice extends Activity {
	
	private Button wikiSearchButton;
	private Button wikiNewEntityButton;
	private TextView requestresult;
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
        requestresult = (TextView) findViewById(R.id.requestresult);

        wikiSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	Intent intent = new Intent(getBaseContext(), CategoryListSearch.class);
            	//TODO: implement a progressdialog while loading new activity with db content
            	//startProgressDialog();
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


    private void startProgressDialog() {
    	new AsyncTask<Object, Object, Object>() {
    		
    		@Override
			protected void onPreExecute() {
    			System.out.println("ASYNCTASK RUNS");
    			ProgressDialog dialog = new ProgressDialog(Choice.this);
    			dialog.setTitle("Hinweis");
		        dialog.setMessage("Entschlüssle Datenbank");
		        dialog.show();
			}

			@Override
			protected Object doInBackground(Object... params) {
				return null;
			}
    		
    	};	
	}


	public boolean onCreateOptionsMenu(Menu menu) {
    	getMenuInflater().inflate(R.menu.choice, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
		case R.id.menu_choice_action_sync:
			getOrSetLastUpdate(Request.Method.POST);
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


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void getOrSetLastUpdate(int method) {
		String url ="http://213.165.81.7:8080/ArtmannWiki/rest/lastupdate";

		StringRequest stringRequest = new StringRequest(method, url,
		            new Response.Listener() {
		    public void onResponse(Object response) {
		    	requestresult.setText((String) response);
		    	lastUpdateManager = new LastUpdateManager(Choice.this);
		    	lastUpdateManager.openWritable(Choice.this);
		    	Long localLastUpdate = lastUpdateManager.setLastUpdate(Long.parseLong((String) response));
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	requestresult.setText("That didn't work!");
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
