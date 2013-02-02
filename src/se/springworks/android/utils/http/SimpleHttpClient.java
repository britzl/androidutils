package se.springworks.android.utils.http;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class SimpleHttpClient implements ISimpleHttpClient {

	private HttpClient client = new DefaultHttpClient();
	
	@Override
	public InputStream get(String url) {
		try {
			HttpResponse response = client.execute(new HttpGet(url));
			HttpEntity entity = response.getEntity();
			if(entity != null) {
				return entity.getContent();
			}
		}
		catch (Exception e) {
		}
		return null;
	}
}
