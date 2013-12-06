package se.springworks.android.utils.cache;



public interface ICache<T> {

	@SuppressWarnings("serial")
	public class CacheException extends Exception {
		
		public CacheException(String message) {
			super(message);
		}
		
		public CacheException(String message, Throwable t) {
			super(message, t);
		}
	}
	
	/**
	 * Clears the entire cache
	 */
	public void clear();
	
	/**
	 * Cache data indefinitely
	 * @param resource
	 * @param data
	 */
	public void cache(final String resourceKey, final T data) throws CacheException;
	
	/**
	 * Cache data for a specific amount of time
	 * @param resourceKey
	 * @param data
	 * @param maxAge
	 */
	public void cache(final String resourceKey, final T data, long maxAge) throws CacheException;

	
	/**
	 * Get a cached resource. The cache will be pruned before retrieving the resource
	 * @param resourceKey The resource to get
	 * @return Cached data or null if it doesn't exist or has expired
	 */
	public T get(String resourceKey);
	
	/**
	 * Check if a resource exists in the cache. The cache will be pruned before checking
	 * @param resourceKey
	 * @return
	 */
	public boolean contains(String resourceKey);

}
