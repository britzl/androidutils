package se.springworks.android.utils.mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import se.springworks.android.utils.http.ISimpleHttpClient;

public class MockSimpleHttpClient implements ISimpleHttpClient {

	private Map<String, String> responseMap = new HashMap<String, String>();
	
	public void setResponse(String url, String response) {
		responseMap.put(url, response);
	}

	public void clear() {
		responseMap.clear();
	}
	
	@Override
	public InputStream get(String url) {
		if(!responseMap.containsKey(url)) {
			return null;
		}
		return new ByteArrayInputStream(responseMap.get(url).getBytes());
	}

	@Override
	public String getAsString(String url) {
		return responseMap.get(url);
	}

	public String basicAuthUser;
	public String basicAuthPass;
	@Override
	public void setBasicAuth(String username, String password) {
		basicAuthUser = username;
		basicAuthPass = password;
	}

}
