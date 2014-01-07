package se.springworks.android.utils.json;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import se.springworks.android.utils.reflect.JavaTypeToken;

public interface IJsonParser {
	
	public String toJson(Object object);

	public void toJson(File file, Object object);
	
	public void toJson(OutputStream out, Object object);
	
	public <T> T fromJson(InputStream json, Class<T> type);
	
	public <T> T fromJson(String json, Class<T> type);
	
	public <T> T fromJson(InputStream json, JavaTypeToken<T> type);
	
	public <T> T fromJson(String json, JavaTypeToken<T> type);
}
