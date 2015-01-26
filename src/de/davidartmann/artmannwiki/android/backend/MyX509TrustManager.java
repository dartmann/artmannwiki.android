package de.davidartmann.artmannwiki.android.backend;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MyX509TrustManager implements X509TrustManager {
	
	private X509TrustManager mX509TrustManager = null;
	
	public MyX509TrustManager(InputStream keyStore, String keyStorePassword) throws GeneralSecurityException {
		super();
		TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		KeyStore ks = KeyStore.getInstance("BKS");
		try {
            ks.load(keyStore, keyStorePassword.toCharArray());
        } catch (IOException e) {
            throw new GeneralSecurityException("Problem reading keystore stream", e);
        }
		factory.init(ks);
		TrustManager[] trustManagers = factory.getTrustManagers();
		if (trustManagers.length == 0) {
			throw new NoSuchAlgorithmException("No TrustManager found");
		}
		this.mX509TrustManager = (X509TrustManager) trustManagers[0];
	}

	public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
		mX509TrustManager.checkClientTrusted(certificates, authType);
	}

	public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
		if ((certificates != null) && (certificates.length == 1)) {
			certificates[0].checkValidity();
		} else {
			mX509TrustManager.checkServerTrusted(certificates, authType);
		}
	}

	public X509Certificate[] getAcceptedIssuers() {
		return this.mX509TrustManager.getAcceptedIssuers();
	}

}
