package se.springworks.android.utils.rest;

import java.util.HashMap;
import java.util.Iterator;

public class RestCache {

	private class CachedData {
		
		// time when the data expires (-1 = never)
		public final long expires;
		
		// time when the data was created
		public final long created;
		
		public final String data;
		
		public CachedData(String data) {
			this(data, (long)-1);
		}
		
		public CachedData(String data, long expires) {
			this.data = data;
			this.expires = expires;
			created = System.currentTimeMillis();
		}
		
		public boolean hasExpired() {
			if(expires == -1) {
				return false;
			}
			return System.currentTimeMillis() >= expires;
		}
	}
	
	private HashMap<String, CachedData> cache = new HashMap<String, CachedData>();
	
	private long maxCacheSize = -1;
	
	private long cacheSize = 0;
	
	
	private void prune() {
		cacheSize = 0;
		Iterator<CachedData> it = cache.values().iterator();
		while(it.hasNext()) {
			CachedData cacheData = it.next();
			if(cacheData.hasExpired()) {
				it.remove();
			}
			else {
				cacheSize += cacheData.data.length();
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
	 * Caches data for a resource indefinitely
	 * @param resource
	 * @param data
	 */
	public void cache(final String resource, final String data) {
		cache(resource, new CachedData(data));
	}
	
	public void cache(final String resource, final String data, long maxAge) {
		cache(resource, new CachedData(data, System.currentTimeMillis() + maxAge));
	}
	
	private void cache(final String resource, final CachedData data) {
		cache.put(resource, data);
		prune();
	}
	
	/**
	 * Get a cached resource
	 * @param resource The resource to get
	 * @return Cached data or null if it doesn't exist or has expired
	 */
	public String get(String resource) {
		prune();
		if(cache.containsKey(resource)) {
			return cache.get(resource).data;
		}
		return null;
	}
}
