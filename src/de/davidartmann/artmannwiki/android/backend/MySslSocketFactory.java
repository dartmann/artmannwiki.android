package de.davidartmann.artmannwiki.android.backend;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class MySslSocketFactory implements SocketFactory, LayeredSocketFactory {
	
	private SSLContext mSslContext;
	private static InputStream mKeyStore;
	private static String mKeyStorePassword;
	
	public MySslSocketFactory(InputStream keyStore, String keyStorePassword) {
		mKeyStore = keyStore;
		mKeyStorePassword = keyStorePassword;
	}
	
	private static SSLContext createSSLContext() throws IOException {
		try {
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new TrustManager[] {new MyX509TrustManager(mKeyStore, mKeyStorePassword)}, null);
			return context;
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}
	
	private SSLContext getSSLContext() throws IOException {
		if (mSslContext == null) {
			this.mSslContext = createSSLContext();
		}
		return this.mSslContext;
	}

	public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
			throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	public Socket connectSocket(Socket socket, String host, int port,
			InetAddress localAddress, int localPort, HttpParams params) throws IOException,
			UnknownHostException, ConnectTimeoutException {
		int connTimeout = HttpConnectionParams.getConnectionTimeout(params);
		int soTimeout = HttpConnectionParams.getSoTimeout(params);
		InetSocketAddress remoteAddress = new InetSocketAddress(host, port);
		SSLSocket sslSocket = (SSLSocket) ((socket != null) ? socket : createSocket());
		if ((localAddress != null) || (localPort > 0)) {
			if (localPort < 0) {
				localPort = 0; // indicates any port
			}
			InetSocketAddress isa = new InetSocketAddress(localAddress, localPort);
			sslSocket.bind(isa);
		}
		sslSocket.connect(remoteAddress, connTimeout);
		sslSocket.setSoTimeout(soTimeout);
		return sslSocket;
	}

	public Socket createSocket() throws IOException {
		return getSSLContext().getSocketFactory().createSocket();
	}

	public boolean isSecure(Socket socket) throws IllegalArgumentException {
		return true;
	}

	public boolean equals(Object o) {
		return ((o != null) && o.getClass().equals(MySslSocketFactory.class));
	}

	public int hashCode() {
		return MySslSocketFactory.class.hashCode();
	}

}
