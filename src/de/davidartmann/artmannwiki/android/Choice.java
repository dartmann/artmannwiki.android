package de.davidartmann.artmannwiki.android;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import de.artmann.artmannwiki.R;


public class Choice extends Activity {
	
	Button wikiSearchButton;
	Button wikiNewEntityButton;
	TextView requestresult;
	
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
            	startProgressDialog();
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


    protected void startProgressDialog() {
    	new AsyncTask<Object, Object, Object>() {
    		
    		@Override
			protected void onPreExecute() {
    			System.out.println("ASYNCTASK RUNS");
    			ProgressDialog Dialog = new ProgressDialog(Choice.this);
    			Dialog.setTitle("Hinweis");
		        Dialog.setMessage("Entschlüssle Datenbank");
		        Dialog.show();
			}

			@Override
			protected Object doInBackground(Object... params) {
				return null;
			}
    		
    	};	
	}


	public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // not necessary, so commented
    	getMenuInflater().inflate(R.menu.choice, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
		case R.id.menu_choice_action_sync:
			fireHttpRequest();
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


	private void fireHttpRequest() {
		// Instantiate the RequestQueue.
		RequestQueue queue = Volley.newRequestQueue(this);
		String url ="http://213.165.81.7:8080/ArtmannWiki/rest/login/get/all";

		// Request a string response from the provided URL.
		StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
		            new Response.Listener() {
		    public void onResponse(Object response) {
		        // Display the first 500 characters of the response string.
		    	requestresult.setText((String) response);
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	requestresult.setText("That didn't work!");
		    }
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String>  params = new HashMap<String, String>();  
                params.put("artmannwiki_headerkey", "blafoo");
                return params;  
			}
			
		};
		// Add the request to the RequestQueue.
		queue.add(stringRequest);
	}
}
