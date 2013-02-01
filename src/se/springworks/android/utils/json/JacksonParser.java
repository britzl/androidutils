package se.springworks.android.utils.json;

import java.util.Collection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JacksonParser extends ObjectMapper {

	private static final long serialVersionUID = 1L;
	
	private static JacksonParser instance;

	private JacksonParser() {
		super();
		configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	private static JacksonParser getInstance() {
		if(instance == null) {
			instance = new JacksonParser();
		}
		return instance;
	}
	
	public static <T> T parse(String json, Class<T> type) {
		
		try {
			return getInstance().readValue(json, type);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T> T parse(String json, TypeReference<T> type) {
		
		try {
			return getInstance().readValue(json, type);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static <T extends Collection, U> T parse(String json, Class<T> collectionClass, Class<U> elementClass) {
		
		try {
			return getInstance().readValue(json, TypeFactory.defaultInstance().constructCollectionType(collectionClass, elementClass));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
