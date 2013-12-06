package se.springworks.android.utils.cache;

import java.util.HashMap;
import java.util.Iterator;

public class MemCache<T> implements ICache<T> {

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
	
	@Override
	public void clear() {
		cache.clear();
	}
	
	@Override
	public void cache(final String resourceKey, final T data) {
		cache(resourceKey, new CachedData(data));
	}

	@Override
	public void cache(final String resourceKey, final T data, long maxAge) {
		cache(resourceKey, new CachedData(data, System.currentTimeMillis() + maxAge));
	}
	
	private void cache(final String resourceKey, final CachedData data) {
		cache.put(resourceKey, data);
	}
	
	@Override
	public T get(String resourceKey) {
		prune();
		if(cache.containsKey(resourceKey)) {
			return cache.get(resourceKey).data;
		}
		return null;
	}
	
	@Override
	public boolean contains(String resourceKey) {
		prune();
		return cache.containsKey(resourceKey);
	}
}
