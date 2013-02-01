package se.springworks.android.utils.rest;

import java.util.Collection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.loopj.android.http.AsyncHttpResponseHandler;


public abstract class JacksonHttpResponseHandler<T> extends AsyncHttpResponseHandler {

	private TypeBase type; 
	
	public JacksonHttpResponseHandler() {
		
	}
	
	@SuppressWarnings("rawtypes")
	public JacksonHttpResponseHandler(Class<? extends Collection> collectionClass, Class<?> elementClass) {
		type = TypeFactory.defaultInstance().constructCollectionType(collectionClass, elementClass);
	}
	
	public JacksonHttpResponseHandler(TypeBase type) {
		this.type = type;
	}
	
	@Override
	public final void onSuccess(String response) {
		T t = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			if(type != null) {
				t = mapper.readValue(response, type);
			}
			else {
				t = mapper.readValue(response, new TypeReference<T>() {});
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		onSuccess(t, response);
	}
	
	public abstract void onSuccess(T response, String raw);
}
