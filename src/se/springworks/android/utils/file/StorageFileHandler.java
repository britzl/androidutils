package se.springworks.android.utils.file;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.google.inject.Inject;

/**
 * Handles reading and writing to internal and external file storage
 * @author bjornritzl
 */
public class StorageFileHandler extends AbstractFileHandler {

	public enum StorageMode {
		INTERNALFILES,
		EXTERNALFILES,
		PREFEREXTERNALFILES,
		INTERNALCACHE,
		EXTERNALCACHE,
		PREFEREXTERNALCACHE
	}
	
	// set this to give all future instances the same storage mode when created
	public static StorageMode globalStorageMode = null;
	
	private StorageMode mode = StorageMode.INTERNALFILES;

	private Context context;

	@Inject
	public StorageFileHandler(Context context) {
		super();
		this.context = context;
		mode = (globalStorageMode != null) ? globalStorageMode : StorageMode.INTERNALFILES;
	}
	
	/**
	 * Set storage mode for file operations
	 * @param mode
	 */
	public void setStorageMode(StorageMode mode) {
		this.mode = mode;
	}
	
	/**
	 * Check if external storage is available
	 * @return true if external storage is available
	 */
	public boolean isExternalStorageAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	
	@Override
	public String getBaseFolder() {
		
		switch(mode) {
		case INTERNALCACHE:
			return context.getCacheDir().getAbsolutePath();
		case EXTERNALCACHE:
			return context.getExternalCacheDir().getAbsolutePath();
		case EXTERNALFILES:
			return context.getExternalFilesDir(null).getAbsolutePath();
		case INTERNALFILES:
			return context.getFilesDir().getAbsolutePath();
		case PREFEREXTERNALCACHE:
			if(isExternalStorageAvailable()) {
				return context.getExternalCacheDir().getAbsolutePath();
			}
			return context.getCacheDir().getAbsolutePath();
		case PREFEREXTERNALFILES:
			if(isExternalStorageAvailable()) {
				return context.getExternalFilesDir(null).getAbsolutePath();
			}
			return context.getFilesDir().getAbsolutePath();
		default:
			break;
		}
		return null;
	}

	@Override
	public long getAvailableMemory() {
		StatFs statFs = new StatFs(getAbsolutePathToStorage());
		final int availableBlocks = statFs.getAvailableBlocks();
		final int blockSize = statFs.getBlockSize();
		return (long)availableBlocks * (long)blockSize;
	}

	@Override
	public long getTotalMemory() {
		StatFs statFs = new StatFs(getAbsolutePathToStorage());
		final int blocks = statFs.getBlockCount();
		final int blockSize = statFs.getBlockSize();
		return (long)blocks * (long)blockSize;
	}
	
	
	private String getAbsolutePathToStorage() {
		if(mode == StorageMode.INTERNALCACHE
			|| mode == StorageMode.INTERNALFILES
			|| !isExternalStorageAvailable()) {
			return Environment.getRootDirectory().getAbsolutePath();
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

}
