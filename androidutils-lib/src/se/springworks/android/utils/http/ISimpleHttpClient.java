package se.springworks.android.utils.http;

import java.io.InputStream;

/**
 * Simple synchronous http client with calls made on the ui thread, no thread pooling or
 * anything else fancy
 * @author bjornritzl
 *
 */
public interface ISimpleHttpClient {

	/**
	 * Make an HTTP GET
	 * @param url
	 * @return
	 */
	InputStream get(String url);
	
	/**
	 * Make an HTTP GET and return the response as a string
	 * @param url
	 * @return
	 */
	String getAsString(String url);
	
	/**
	 * Set basic authentication to use for consecutive calls
	 * @param username
	 * @param password
	 */
	void setBasicAuth(final String username, final String password);
}
