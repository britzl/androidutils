package se.springworks.android.utils.persistence;

import java.util.Map;
import java.util.Set;

public interface IKeyValueStorage {
	
	public interface NamedKeyValueStorageFactory {
		IKeyValueStorage create(String name);
	}

	/**
	 * Check if the storage contains a specific key
	 * @param key
	 * @return
	 */
	boolean contains(String key);
	
	/**
	 * Store a single String value
	 * @param key
	 * @param value
	 */
	void put(String key, String value);
	
	/**
	 * Store a set of strings
	 * @param key
	 * @param value
	 */
	void put(String key, Set<String> value);
	
	/**
	 * Store a single integer value
	 * @param key
	 * @param value
	 */
	void put(String key, int value);

	/**
	 * Store an array of integers
	 * @param key
	 * @param value
	 */
	void put(String key, int[] value);	

	/**
	 * Store a single long value
	 * @param key
	 * @param value
	 */
	void put(String key, long value);

	/**
	 * Store an array of longs
	 * @param key
	 * @param value
	 */
	void put(String key, long[] value);
	
	/**
	 * Store a single boolean value
	 * @param key
	 * @param value
	 */
	void put(String key, boolean value);
	
	/**
	 * Store an arbitrary object
	 * @param key
	 * @param o
	 */
	<T> void put(String key, T o);
	
	/**
	 * Get all values in the storage
	 * @return
	 */
	Map<String, ?> getAll();
	
	/**
	 * Get stored string for a specific key
	 * @param key
	 * @return String or null of it doesn't exist
	 */
	String getString(String key);
	
	/**
	 * Get stored string for a specific key. If the key doesn't exist the specified default
	 * value will be returned
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	String getString(String key, String defaultValue);
	
	/**
	 * Get a stored set of strings
	 * @param key
	 * @return
	 */
	Set<String> getStrings(String key);
	
	/**
	 * Get stored long for a specific key
	 * @param key
	 * @return
	 */
	long getLong(String key);

	/**
	 * Get stored array of longs 
	 * @param key
	 * @return
	 */
	Long[] getLongs(String key);
	
	/**
	 * Get a stored long. If the key doesn't exist the specified default value will be returned
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	long getLong(String key, long defaultValue);
	
	/**
	 * Get a stored integer
	 * @param key
	 * @return
	 */
	int getInt(String key);
	
	/**
	 * Get stored integer array
	 * @param key
	 * @return
	 */
	Integer[] getInts(String key);
	
	/**
	 * Get a stored integer. If the key doesn't exits the specified default value will be returned
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	int getInt(String key, int defaultValue);
	
	/**
	 * Get a stored boolean
	 * @param key
	 * @return
	 */
	boolean getBoolean(String key);
	
	/**
	 * Get a stored boolean. If the key doesn't exist the specified default value will be returned
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	boolean getBoolean(String key, boolean defaultValue);
	
	<T> T getObject(String key, Class<T> cls);
	
	void remove(String key);
	
	void removeAll();
}
