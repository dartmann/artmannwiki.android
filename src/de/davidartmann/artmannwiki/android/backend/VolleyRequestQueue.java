package de.davidartmann.artmannwiki.android.backend;

import java.io.InputStream;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import de.artmann.artmannwiki.R;

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
        	InputStream keyStoreInputStream = mCtx.getResources().openRawResource(R.raw.tomcat);
        	/*
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), 
            		new ExtHttpClientStack(
            				new SslHttpClient(keyStoreInputStream, "T0mcat2015K3y", 8443)));
            */
        	mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), 
        			new MyHttpStack(
        					new MySslHttpClient(keyStoreInputStream, "T0mcat2015K3y")));
        }
        return mRequestQueue;
    }

    /**
     * Adds a Request to the Volley {@link RequestQueue}
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
