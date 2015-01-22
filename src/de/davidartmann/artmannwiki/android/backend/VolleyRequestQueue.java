package de.davidartmann.artmannwiki.android.backend;

import java.io.InputStream;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import de.artmann.artmannwiki.R;

public class VolleyRequestQueue /*extends Application*/{

    private RequestQueue mRequestQueue;
	private static Context mCtx;
	@SuppressWarnings("unused")
	private HurlStack hurlStack;
	
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
        	/*
        	KeyStore trustedKeyStore = null;
        	InputStream keyStoreInputStream = null;
        	try {
	        	trustedKeyStore = KeyStore.getInstance("BKS");
	        	keyStoreInputStream = mCtx.getResources().openRawResource(R.raw.tomcat);
	        	trustedKeyStore.load(keyStoreInputStream, "T0mcat2015K3y".toCharArray());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					keyStoreInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        	try {
        		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            	tmf.init(trustedKeyStore);
            	
            	KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            	kmf.init(trustedKeyStore, "T0mcat2015K3y".toCharArray());
            	
            	SSLContext sslContext = null;
            	sslContext = SSLContext.getInstance("TLS");
    			sslContext.init(null, tmf.getTrustManagers(), null);
    			
            	// will provide the client certificate from the keystore during handshake
            	SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            	hurlStack = new HurlStack(null, sslSocketFactory);
            	mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), hurlStack);
        	} catch (Exception e) {
        		e.printStackTrace();
        	}
        	*/
        	InputStream keyStoreInputStream = mCtx.getResources().openRawResource(R.raw.tomcat);
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext(), 
            		new ExtHttpClientStack(
            				new SslHttpClient(keyStoreInputStream, "T0mcat2015K3y", 8443)));
            
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
