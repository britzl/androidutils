package se.springworks.android.utils.json;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import com.fasterxml.jackson.core.type.TypeReference;

public interface IJsonParser {
	
	public String toJson(Object object);

	public void toJson(File file, Object object);
	
	public void toJson(OutputStream out, Object object);
	
	public <T> T fromJson(InputStream json, Class<T> type);
	
	public <T> T fromJson(String json, Class<T> type);
	
	public <T> T fromJson(InputStream json, TypeReference<T> type);
	
	public <T> T fromJson(String json, TypeReference<T> type);
	
	@SuppressWarnings("rawtypes")
	public <T extends Collection, U> T fromJson(String json, Class<T> collectionClass, Class<U> elementClass);

}
