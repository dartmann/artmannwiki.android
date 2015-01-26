package de.davidartmann.artmannwiki.android.backend;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.HttpStack;

public class MyHttpStack implements HttpStack {
	
	protected final HttpClient mHttpClient;
	
	private final static String HEADER_CONTENT_TYPE = "Content-Type";
	
	public MyHttpStack(HttpClient httpClient) {
		this.mHttpClient = httpClient;
	}
	
	private static void addHeaders(HttpUriRequest httpUriRequest, Map<String, String> headers) {
		for (String key : headers.keySet()) {
			httpUriRequest.setHeader(key, headers.get(key));
		}
	}
	
	private static void setEntityIfNonEmptyBody(HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, Request<?> request) throws AuthFailureError {
		byte[] body = request.getBody();
		if (body != null) {
			HttpEntity httpEntity = new ByteArrayEntity(body);
			httpEntityEnclosingRequestBase.setEntity(httpEntity);
		}
	}
	
	@SuppressWarnings("deprecation")
	private HttpUriRequest createHttpRequest(Request<?> request, Map<String, String> additionalHeaders) throws AuthFailureError {
		switch (request.getMethod()) {
		case Method.DEPRECATED_GET_OR_POST:
			byte[] postBody = request.getPostBody();
			if (postBody != null) {
				HttpPost httpPost = new HttpPost(request.getUrl());
				httpPost.setHeader(HEADER_CONTENT_TYPE, request.getPostBodyContentType());
				HttpEntity httpEntity = new ByteArrayEntity(postBody);
				httpPost.setEntity(httpEntity);
				return httpPost;
			} else {
				return new HttpGet(request.getUrl());
			}
		case Method.GET:
			return new HttpGet(request.getUrl());
		case Method.DELETE:
			return new HttpDelete(request.getUrl());
		case Method.POST:
			HttpPost httpPost = new HttpPost(request.getUrl());
			httpPost.setHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
			setEntityIfNonEmptyBody(httpPost, request);
			return httpPost;
		case Method.PUT:
			HttpPut httpPut = new HttpPut(request.getUrl());
			httpPut.setHeader(HEADER_CONTENT_TYPE, request.getBodyContentType());
			setEntityIfNonEmptyBody(httpPut, request);
			return httpPut;
		default:
			throw new IllegalStateException("Unknown request method.");
		}
	}

	@Override
	public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
			throws IOException, AuthFailureError {
		HttpUriRequest httpUriRequest = createHttpRequest(request, additionalHeaders);
		addHeaders(httpUriRequest, additionalHeaders);
		addHeaders(httpUriRequest, request.getHeaders());
		// onPrepareRequest(); not used...
		HttpParams httpParams = httpUriRequest.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, request.getTimeoutMs());
		HttpResponse httpResponse = mHttpClient.execute(httpUriRequest);
		return httpResponse;
	}

}
