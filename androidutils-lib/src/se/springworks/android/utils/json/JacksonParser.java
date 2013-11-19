package se.springworks.android.utils.json;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JacksonParser implements IJsonParser {
	
	private ObjectMapper mapper;

	public JacksonParser() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@Override
	public String toJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		}
		catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public <T> T fromJson(InputStream json, Class<T> type) {		
		try {
			return mapper.readValue(json, type);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public <T> T fromJson(String json, Class<T> type) {		
		try {
			return mapper.readValue(json, type);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public <T> T fromJson(InputStream json, TypeReference<T> type) {
		try {
			return mapper.readValue(json, type);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public <T> T fromJson(String json, TypeReference<T> type) {		
		try {
			return mapper.readValue(json, type);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public <T extends Collection, U> T fromJson(String json, Class<T> collectionClass, Class<U> elementClass) {
		
		try {
			return mapper.readValue(json, TypeFactory.defaultInstance().constructCollectionType(collectionClass, elementClass));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void toJson(File file, Object object) {
		try {
			mapper.writeValue(file, object);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void toJson(OutputStream out, Object object) {
		try {
			mapper.writeValue(out, object);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
