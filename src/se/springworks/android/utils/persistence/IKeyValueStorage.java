package se.springworks.android.utils.persistence;

public interface IKeyValueStorage {

	boolean contains(String key);
	
	void put(String key, String value);
	
	void put(String key, int value);
	
	void put(String key, long value);
	
	void put(String key, boolean value);
	
	/**
	 * Get stored string for a specific key
	 * @param key
	 * @return String or null of it doesn't exist
	 */
	String getString(String key);
	
	String getString(String key, String defaultValue);
	
	/**
	 * Get stored long for a specific key
	 * @param key
	 * @return
	 */
	long getLong(String key);

	long getLong(String key, long defaultValue);
	
	int getInt(String key);
	
	int getInt(String key, int defaultValue);
	
	boolean getBoolean(String key);
	
	boolean getBoolean(String key, boolean defaultValue);
	
	void remove(String key);
}
