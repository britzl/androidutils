package se.springworks.android.utils.http;

import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class SimpleDefaultHttpClient implements ISimpleHttpClient {
	
	private DefaultHttpClient client = new DefaultHttpClient();
	
	@Override
	public InputStream get(String url) {
		try {
			HttpResponse response = client.execute(new HttpGet(url.trim()));
			HttpEntity entity = response.getEntity();
			if(entity != null) {
				return entity.getContent();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String getAsString(String url) {
		try {
			HttpResponse response = client.execute(new HttpGet(url.trim()));
			HttpEntity entity = response.getEntity();
			if(entity != null) {
				return EntityUtils.toString(entity, "UTF-8");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setBasicAuth(String username, String password) {
		CredentialsProvider credProvider = new BasicCredentialsProvider();
	    credProvider.setCredentials(
	    		new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
	    		new UsernamePasswordCredentials(username, password));

	    client.setCredentialsProvider(credProvider);
	}
}
