package se.springworks.android.utils.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import se.springworks.android.utils.stream.StreamUtils;
import android.util.Base64;

public class SimpleHttpUrlConnectionClient implements ISimpleHttpClient {

	private String basicAuth = null;
	
	@Override
	public InputStream get(String url) {
		InputStream in = null;
		try {
			URLConnection urlConnection = openConnection(url);
			in = new BufferedInputStream(urlConnection.getInputStream());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return in;
	}

	@Override
	public String getAsString(String url) {
		InputStream in = get(url);
		if(in == null) {
			return null;
		}
		String string = null;
		try {
			byte[] data = StreamUtils.getAsBytes(in);
			string = new String(data, "UTF-8");
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return string;
	}
	
	/**
	 * Opens a URLConnection and sets basic auth
	 * @param url
	 * @return
	 */
	private URLConnection openConnection(String url) {
		URLConnection urlConnection = null;
		try {
			URL u = new URL(url);
			urlConnection = u.openConnection();	
			if(basicAuth != null) {
				urlConnection.setRequestProperty("Authorization", basicAuth);
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urlConnection;
	}

	@Override
	public void setBasicAuth(String username, String password) {
		basicAuth = "Basic " + Base64.encode((username + ":" + password).getBytes(), Base64.NO_WRAP);
	}

}
