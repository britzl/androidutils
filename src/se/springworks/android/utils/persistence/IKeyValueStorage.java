package se.springworks.android.utils.persistence;

public interface IKeyValueStorage {

	boolean contains(String key);
	
	void put(String key, String value);
	
	void put(String key, long value);
	
	void put(String key, boolean value);
	
	String getString(String key);
	
	long getLong(String key);
	
	boolean getBoolean(String key);
	
	void remove(String key);
}
