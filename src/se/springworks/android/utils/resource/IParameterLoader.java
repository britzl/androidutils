package se.springworks.android.utils.resource;

public interface IParameterLoader {
	/**
	 * Look up the string value for the resource whose name is key.
	 * 
	 * @param key
	 *            the key for the string resource we're seeking
	 * @return the string value, or null if not found
	 */
	String getString(String key);

	/**
	 * Look up the boolean value represented by the string resource whose name
	 * is key.
	 * 
	 * @param key
	 *            the key for the string resource we're seeking
	 * @return true if the key is found and is a string 'true'. Case is ignored.
	 */
	boolean getBoolean(String key);

	/**
	 * Look up the integer value represented by the string resource whose name
	 * is key.
	 * 
	 * @param key
	 *            the key for the string resource we're seeking
	 * @param defaultValue
	 *            the value to return if the key isn't found
	 * @return the int value found, or the defaultValue if the key wasn't found
	 *         or couldn't be parsed into an int.
	 */
	int getInt(String key, int defaultValue);
	
	
	/**
	 * Check if a specific boolean resource key exists
	 * @param key
	 * @return
	 */
	boolean hasBoolean(String key);
	
	/**
	 * Check if a specific integer resource key exists
	 * @param key
	 * @return
	 */
	boolean hasInt(String key);
	
	/**
	 * Check if a specific string resource key exists
	 * @param key
	 * @return
	 */
	boolean hasString(String key);
}