package com.tourngen.droid;

import java.security.KeyStore;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class CustomHttpClient {
	
	private static final int TIMEOUT = 0;
	private static final int HTTPS_PORT = 8080;
	
	public static HttpClient getHttpClient() {

	    DefaultHttpClient client = null;

	    try {

	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        // Setting up parameters
	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, "utf-8");
	        params.setBooleanParameter("http.protocol.expect-continue", false);

	        // Setting timeout
	        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
	        HttpConnectionParams.setSoTimeout(params, TIMEOUT);

	        // Registering schemes for both HTTP and HTTPS
	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, HTTPS_PORT));

	        // Creating thread safe client connection manager
	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        // Creating HTTP client
	        client = new DefaultHttpClient(ccm, params);
	        /*
	        // Registering user name and password for authentication
	        client.getCredentialsProvider().setCredentials(
	                new AuthScope(null, -1),
	                new UsernamePasswordCredentials(mUsername, mPassword));*/

	    } catch (Exception e) {
	        client = new DefaultHttpClient();
	    }

	    return client;

	}
}
