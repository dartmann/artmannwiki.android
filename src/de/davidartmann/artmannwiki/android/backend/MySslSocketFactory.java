package de.davidartmann.artmannwiki.android.backend;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

//import javax.net.ssl.HostnameVerifier;

import org.apache.http.conn.ssl.SSLSocketFactory;

public class MySslSocketFactory extends SSLSocketFactory {
	
	public MySslSocketFactory(InputStream keyStore, String keyStorePassword) 
			throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, GeneralSecurityException {
		super(isToKs(keyStore, keyStorePassword));
		KeyStore ks = KeyStore.getInstance("BKS");
		try {
            ks.load(keyStore, keyStorePassword.toCharArray());
        } catch (IOException e) {
            throw new GeneralSecurityException("Problem reading keystore stream", e);
        }
//		HostnameVerifier hostnameVerifier = SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
		
	}
	
	private static KeyStore isToKs(InputStream isKeyStore, String keyStorePassword) throws GeneralSecurityException {
		KeyStore ks = KeyStore.getInstance("BKS");
		try {
            ks.load(isKeyStore, keyStorePassword.toCharArray());
        } catch (IOException e) {
            throw new GeneralSecurityException("Problem reading keystore stream", e);
        } catch (NoSuchAlgorithmException e) {
        	throw new IllegalStateException("No such Algorithm", e);
		} catch (CertificateException e) {
			throw new IllegalStateException("Problem with certificate", e);
		}
		return ks;
	}
}
