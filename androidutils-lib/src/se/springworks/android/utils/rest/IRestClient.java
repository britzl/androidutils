package se.springworks.android.utils.rest;

import java.util.Map;

public interface IRestClient {
	
	public interface OnHttpResponseHandler {
		
		public void onSuccess(String response);
		
		public void onFailure(Throwable t, String response, int code);
	}
	
	/**
	 * Clears any stored cookies
	 */
	void clearCookies();
	
	/**
	 * Clears all cached responses
	 */
	void clearCache();
	
	/**
	 * Cancels any active requests
	 */
	void cancelRequests();
	
	/**
	 * Perform a synchronous GET 
	 * @param url
	 * @param params
	 * @return
	 */
	String get(final String url, Map<String, String> params);
	
	/**
	 * Perform an asynchronous GET
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	void get(String url, Map<String, String> params, OnHttpResponseHandler responseHandler);
	
	/**
	 * Perform an asynchronous POST
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	void post(String url, Map<String, String> params, OnHttpResponseHandler responseHandler);
	
	/**
	 * Perform an asynchronous POST
	 * @param url
	 * @param json
	 * @param responseHandler
	 */
	void post(String url, String json, OnHttpResponseHandler responseHandler);
	
	/**
	 * Perform an asynchronous DELETE
	 * @param url
	 * @param responseHandler
	 */
	void delete(String url, OnHttpResponseHandler responseHandler);
	
	/**
	 * Set base url to use for all requests
	 * @param baseUrl
	 */
	void setBaseUrl(String baseUrl);
	
	/**
	 * Set basic auth credentials for use
	 * @param username
	 * @param password
	 */
	void setBasicAuth(final String username, final String password);
	
	/**
	 * Enables response caching
	 */
	void enableCaching();
	
	/**
	 * Disables response caching
	 */
	void disableCaching();
	
	/**
	 * Check if caching is enabled 
	 * @return
	 */
	boolean isCachingEnabled();
}
