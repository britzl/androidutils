package se.springworks.android.utils.file;

import java.io.File;

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
		File folder = null;
		switch(mode) {
		case INTERNALCACHE:
			folder = context.getCacheDir();
			break;
		case EXTERNALCACHE:
			folder = context.getExternalCacheDir();
			break;
		case EXTERNALFILES:
			folder = context.getExternalFilesDir(null);
			break;
		case INTERNALFILES:
			folder = context.getFilesDir();
			break;
		case PREFEREXTERNALCACHE:
			if(isExternalStorageAvailable()) {
				folder = context.getExternalCacheDir();
			}
			// folder can be null even if external storage is available if 
			// android.permission.WRITE_EXTERNAL_STORAGE isn't set
			if(folder == null) {
				folder = context.getCacheDir();				
			}
			break;
		case PREFEREXTERNALFILES:
			if(isExternalStorageAvailable()) {
				folder = context.getExternalFilesDir(null);
			}
			// folder can be null even if external storage is available if 
			// android.permission.WRITE_EXTERNAL_STORAGE isn't set
			if(folder == null) {
				folder = context.getFilesDir();
			}
			break;
		default:
			break;
		}
		
		if(folder != null) {
			return folder.getAbsolutePath();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public long getAvailableMemory() {
		StatFs statFs = new StatFs(getAbsolutePathToStorage());
		final int availableBlocks = statFs.getAvailableBlocks();
		final int blockSize = statFs.getBlockSize();
		return (long)availableBlocks * (long)blockSize;
	}

	@SuppressWarnings("deprecation")
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
