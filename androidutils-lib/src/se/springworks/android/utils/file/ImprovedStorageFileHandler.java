package se.springworks.android.utils.file;

import java.io.File;
import java.util.Date;

import com.google.inject.Inject;

import se.springworks.android.utils.persistence.IKeyValueStorage;

import android.content.Context;

/**
 * This implementation improves the {@link StorageFileHandler} by wrapping calls to
 * {@link StorageFileHandler#setFileModifiedTime(String, long)} and
 * {@link StorageFileHandler#getFileModifiedDate(String)} in order to work around limitations
 * with these methods on some systems where permissions aren't sufficient (refer to
 * https://code.google.com/p/android/issues/detail?id=25460 for details)
 * 
 * @author bjornritzl
 *
 */
public class ImprovedStorageFileHandler extends StorageFileHandler {

	private static final String KEYPREFIX = "ISFH";
	@Inject
	private IKeyValueStorage fileModificationDateStorage;
	
	
	@Inject
	public ImprovedStorageFileHandler(Context context) {
		super(context);
	}

	
	@Override
	public Date getFileModifiedDate(String name) {
		File file = getFile(name);
		if(!file.exists()) {
			return null;
		}
		long lastModified;
		if(fileModificationDateStorage.contains(KEYPREFIX + name)) {
			lastModified = fileModificationDateStorage.getLong(KEYPREFIX + name);
		}
		else {
			lastModified = file.lastModified();
		}
		return new Date(lastModified);
	}
	
	@Override
	public boolean setFileModifiedTime(String name, long time) {
		File file = getFile(name);
		if(!file.exists()) {
			return false;
		}
		boolean success = file.setLastModified(time);
		if(!success) {
			fileModificationDateStorage.put(KEYPREFIX + name, time);
		}
		return true;
	}

}
