package se.springworks.android.utils.cache;

import java.util.HashMap;
import java.util.Iterator;

public class MemCache<T> {

	private class CachedData {
		
		// time when the data expires (-1 = never)
		public final long expires;
		
		public final T data;
		
		public CachedData(T data) {
			this(data, (long)-1);
		}
		
		public CachedData(T data, long expires) {
			this.data = data;
			this.expires = expires;
		}
		
		/**
		 * Check if the cached data has expired
		 * @return
		 */
		public boolean hasExpired() {
			if(expires == -1) {
				return false;
			}
			return System.currentTimeMillis() >= expires;
		}
	}
	
	private HashMap<String, CachedData> cache = new HashMap<String, CachedData>();
	
	/**
	 * Remove expired data
	 */
	private void prune() {
		Iterator<CachedData> it = cache.values().iterator();
		while(it.hasNext()) {
			CachedData cacheData = it.next();
			if(cacheData.hasExpired()) {
				it.remove();
			}
		}
	}
	
	/**
	 * Clears the entire cache
	 */
	public void clear() {
		cache.clear();
	}
	
	/**
	 * Cache data indefinitely
	 * @param resource
	 * @param data
	 */
	public void cache(final String resourceKey, final T data) {
		cache(resourceKey, new CachedData(data));
	}
	
	/**
	 * Cache data for a specific amount of time
	 * @param resourceKey
	 * @param data
	 * @param maxAge
	 */
	public void cache(final String resourceKey, final T data, long maxAge) {
		cache(resourceKey, new CachedData(data, System.currentTimeMillis() + maxAge));
	}
	
	private void cache(final String resourceKey, final CachedData data) {
		cache.put(resourceKey, data);
	}
	
	/**
	 * Get a cached resource. The cache will be pruned before retrieving the resource
	 * @param resourceKey The resource to get
	 * @return Cached data or null if it doesn't exist or has expired
	 */
	public T get(String resourceKey) {
		prune();
		if(cache.containsKey(resourceKey)) {
			return cache.get(resourceKey).data;
		}
		return null;
	}
	
	/**
	 * Check if a resource exists in the cache. The cache will be pruned before checking
	 * @param resourceKey
	 * @return
	 */
	public boolean contains(String resourceKey) {
		prune();
		return cache.containsKey(resourceKey);
	}
}
