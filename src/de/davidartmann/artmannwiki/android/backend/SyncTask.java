package de.davidartmann.artmannwiki.android.backend;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.AsyncTask;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class SyncTask extends AsyncTask<String, Integer, Void> {

	@Override
	protected Void doInBackground(String... strings) {
		for (int i = 0; i < strings.length; i++) {			
			@SuppressWarnings("unused")
			JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(strings[i], new Response.Listener<JSONArray> () {
			    @Override
			    public void onResponse(JSONArray response) {
			        try {
			            VolleyLog.v("Response:%n %s", response.toString(4));
			        } catch (JSONException e) {
			            e.printStackTrace();
			        }
			    }
			}, new Response.ErrorListener() {
			    @Override
			    public void onErrorResponse(VolleyError error) {
			        VolleyLog.e("Error: ", error.getMessage());
			    }
			});
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

}
