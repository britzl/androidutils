package se.springworks.android.utils.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import se.springworks.android.utils.file.FileUtils;
import se.springworks.android.utils.file.IFileHandler;
import se.springworks.android.utils.persistence.IKeyValueStorage;
import se.springworks.android.utils.stream.StreamUtils;
import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;

public class DiskCache<T extends Serializable> implements ICache<T> {

	public static class CachedData {
		
		public static final long NEVER_EXPIRES = -1;
		
		// time when the data expires (-1 = never)
		@JsonProperty
		public long expires;
		
		@JsonProperty
		public String filename;
		
		public CachedData() {
			
		}
		
		public CachedData(String filename, long expires) {
			this.filename = filename;
			this.expires = expires;
		}
		
		public String getFilename() {
			return filename;
		}
		
		/**
		 * Check if the cached data has expired
		 * @return
		 */
		public boolean hasExpired() {
			if(expires == NEVER_EXPIRES) {
				return false;
			}
			return System.currentTimeMillis() >= expires;
		}
	}
	
	
	@Inject
	private IFileHandler fileHandler;
	
	private IKeyValueStorage fileIndex;
	
	
	@Inject
	public DiskCache(Context context, IKeyValueStorage.NamedKeyValueStorageFactory fileIndexStorageFactory) {
		fileIndex = fileIndexStorageFactory.create(context.getPackageName() + "diskcache");
	}
	
	/**
	 * Remove expired data
	 */
	private void prune() {
		for(String key : fileIndex.getAll().keySet()) {
			CachedData cachedData = fileIndex.getObject(key, CachedData.class);
			if(cachedData != null && cachedData.hasExpired()) {
				fileIndex.remove(key);
				try {
					fileHandler.delete(cachedData.getFilename());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void clear() {
		for(String key : fileIndex.getAll().keySet()) {
			CachedData cachedData = fileIndex.getObject(key, CachedData.class);
			if(cachedData != null) {
				String filename = cachedData.getFilename();
				try {
					fileHandler.delete(filename);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		fileIndex.removeAll();
	}

	@Override
	public void cache(String resourceKey, T data) throws CacheException {
		writeToCache(resourceKey, data, CachedData.NEVER_EXPIRES);
	}

	@Override
	public void cache(String resourceKey, T data, long maxAge) throws CacheException {
		writeToCache(resourceKey, data, System.currentTimeMillis() + maxAge);
	}
	
	
	private void writeToCache(String resourceKey, T data, long expires) throws CacheException {
		String filename = FileUtils.toFileSystemSafeName(resourceKey);
		ObjectOutputStream oos = null;
		try {
			OutputStream out = fileHandler.getWritableFile(filename, false);
			oos = new ObjectOutputStream(out);
			oos.writeObject(data);
			fileIndex.put(resourceKey, new CachedData(filename, expires));
		}
		catch(IOException e) {
			e.printStackTrace();
			throw new CacheException("Unable to cache file", e);
		}
		finally {
			StreamUtils.closeSilently(oos);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(String resourceKey) {
		prune();
		CachedData data = fileIndex.getObject(resourceKey, CachedData.class);
		if(data == null) {
			return null;
		}

		T t = null;
		try {
			InputStream in = fileHandler.getReadableFile(data.getFilename());
			ObjectInputStream ois = new ObjectInputStream(in);
			t = (T)ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
				
		return t;
	}

	@Override
	public boolean contains(String resourceKey) {
		prune();
		return fileIndex.contains(resourceKey);
	}

}
