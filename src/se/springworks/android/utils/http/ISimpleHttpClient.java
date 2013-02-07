package se.springworks.android.utils.http;

import java.io.InputStream;

public interface ISimpleHttpClient {

	InputStream get(String url);
	
	String getAsString(String url);
}
