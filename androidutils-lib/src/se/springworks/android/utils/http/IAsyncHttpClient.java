package se.springworks.android.utils.http;

import java.util.Map;

/**
 * Interface for an asynchronous http client. This client is expected to have all the bells and
 * whistles of an async client with thread pools, calls being made outside the ui thread and so
 * on 
 * @author bjornritzl
 */
public interface IAsyncHttpClient {

	public interface IAsyncHttpResponseHandler {
		void onSuccess(String response);

		void onFailure(Throwable e, String response, int statusCode);
	}
	
	void get(String url, IAsyncHttpResponseHandler responseHandler);
	
	void delete(String url, IAsyncHttpResponseHandler responseHandler);
	
	void post(String url, Map<String, String> params, IAsyncHttpResponseHandler responseHandler);
	
	void post(String url, String data, String contentType, IAsyncHttpResponseHandler responseHandler);
	
	void cancelRequests(boolean mayInterruptIfRunning);
	
	void setPreemptiveBasicAuth(String user, String pass);
	
	void setHeader(String header, String value);
	
	void setTimeout(int timeoutMillis);
	
	void removeHeader(String header);
	
	void clearCookies();
}
