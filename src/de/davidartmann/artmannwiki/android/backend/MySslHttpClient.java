package de.davidartmann.artmannwiki.android.backend;

import java.io.InputStream;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class MySslHttpClient extends DefaultHttpClient {

	private static final int HTTP_DEFAULT_PORT = 8080;
    private static final String HTTP_SCHEME = "http";
    private static final int HTTP_DEFAULT_HTTPS_PORT = 8443;
    private static final String HTTP_SSL_SCHEME = "https";

    private InputStream mKeyStore;
    private String mKeyStorePassword;
    
    public MySslHttpClient(InputStream keyStore, String keyStorePassword) {
    	mKeyStore = keyStore;
    	mKeyStorePassword = keyStorePassword;
    }
    
    public MySslHttpClient(InputStream keyStore, String keyStorePassword, int httpsPort) {
    	mKeyStore = keyStore;
    	mKeyStorePassword = keyStorePassword;
    }
    
    public MySslHttpClient(ClientConnectionManager conman, InputStream keyStore, String keyStorePassword) {
    	super(conman, null);
    	mKeyStore = keyStore;
    	mKeyStorePassword = keyStorePassword;
    }
    
    public MySslHttpClient(ClientConnectionManager conman, HttpParams params, InputStream keyStore, String keyStorePassword) {
    	super(conman, params);
    	mKeyStore = keyStore;
    	mKeyStorePassword = keyStorePassword;
    }

	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		PlainSocketFactory psf = PlainSocketFactory.getSocketFactory();
		registry.register(new Scheme(HTTP_SCHEME, psf, HTTP_DEFAULT_PORT));
		registry.register(new Scheme(HTTP_SSL_SCHEME, new MySslSocketFactory(mKeyStore, mKeyStorePassword), HTTP_DEFAULT_HTTPS_PORT));
		HttpParams httpParams = new BasicHttpParams();
		httpParams.setParameter(ConnManagerParams.MAX_TOTAL_CONNECTIONS, 1);
		httpParams.setParameter(ConnManagerParams.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(1));
		httpParams.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, "utf8");
		ClientConnectionManager clientConnectionManager = new ThreadSafeClientConnManager(httpParams, registry);
		return clientConnectionManager;
	}
}
