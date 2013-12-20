package se.springworks.android.utils.mock;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import se.springworks.android.utils.http.ISimpleHttpClient;
import se.springworks.android.utils.stream.StreamUtils;

public class MockSimpleHttpClient implements ISimpleHttpClient {

	private Map<String, InputStream> responseMap = new HashMap<String, InputStream>();
	
	public void setResponse(String url, InputStream response) {
		responseMap.put(url, response);
	}
	
	public void setResponse(String url, String response) {
		setResponse(url, new ByteArrayInputStream(response.getBytes()));
	}

	public void clear() {
		responseMap.clear();
	}
	
	@Override
	public InputStream get(String url) {
		if(!responseMap.containsKey(url)) {
			return null;
		}
		return responseMap.get(url);
//		return new ByteArrayInputStream(responseMap.get(url).getBytes());
	}

	@Override
	public String getAsString(String url) {
		return StreamUtils.getAsString(responseMap.get(url));
//		return responseMap.get(url);
	}

	public String basicAuthUser;
	public String basicAuthPass;
	@Override
	public void setBasicAuth(String username, String password) {
		basicAuthUser = username;
		basicAuthPass = password;
	}

}
