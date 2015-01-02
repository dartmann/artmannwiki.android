package de.davidartmann.artmannwiki.android.backend;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestQueue /*extends Application*/{

    private RequestQueue mRequestQueue;
	private static Context mCtx;
	/**
     * A singleton instance of the application class for easy access in other places
     */
	private static VolleyRequestQueue sInstance;

	/**
	 * Private constructor
	 */
	private VolleyRequestQueue(Context c) {
		// Context needs to be initialized first!
		mCtx = c;
		// ...So the RequestQueue does not run in a NP
		mRequestQueue = getRequestQueue();
	}
    
    /**
     * @return VolleyRequestQueue singleton instance
     */
    public static synchronized VolleyRequestQueue getInstance(Context c) {
    	if (sInstance == null) {
    		sInstance = new VolleyRequestQueue(c);
		}
        return sInstance;
    }
    
    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
