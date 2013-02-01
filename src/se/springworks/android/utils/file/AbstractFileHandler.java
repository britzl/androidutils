package se.springworks.android.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFileHandler implements IFileHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractFileHandler.class);
	
	public AbstractFileHandler() {
	}
	
	/**
	 * Get the base folder for all file operations
	 * @return
	 */
	public abstract String getBaseFolder();
	
	@Override
	public boolean createEmptyFile(String filename) {
		byte[] data = new byte[0];//{ (byte)1 };
		try {
			save(filename, data);
		} catch (Exception e) {
			logger.error("createEmptyFile()", e);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean delete(String name) throws Exception {
		logger.debug("delete() " + name);
		File fileToDelete = getFile(name);
		if(fileToDelete != null) {
			return fileToDelete.delete();
		}
		return false;
	}

	@Override
	public boolean exists(String name) {
		logger.debug("exists() " + name);
		String path = "";
		int index = name.lastIndexOf(File.separator);
		if(index != -1) {
			path = name.substring(0, index);
			name = name.substring(index + 1);
		}
		logger.debug("exists() name = " + name + " path = " + path);

		String files[] = getFileList(path);
		for(int i = 0; i < files.length; i++) {
			if(files[i].equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String[] getFileList() {
		return getFileList("");
	}
	
	@Override
	public String[] getFileList(String path) {
		File dir = getFile(path);
		logger.debug("getFileList() path = " + path + "base dir = " + getBaseFolder() + " full path = " + dir.getAbsolutePath());
		String files[] = dir.list();
		return files;
	}

	@Override
	public long getSize(String name) {
		File file = getFile(name);
		if(file == null) {
			return -1;
		}
		return file.length();
	}
	
	protected File getFile(String name) {
		return new File(getBaseFolder() + File.separator + name);
	}

	@Override
	public InputStream getReadableFile(String name) throws IOException {
		return new FileInputStream(getFile(name));
	}
	
	@Override
	public OutputStream getWritableFile(String name, boolean append) throws IOException {
		return new FileOutputStream(getFile(name), append);
	}
	
	@Override
	public Date getFileModifiedDate(String name) {
		File file = getFile(name);
		if(file == null) {
			return null;
		}
		return new Date(file.lastModified());
	}

	@Override
	public byte[] load(String name) throws Exception {
		File file = getFile(name);
		return FileUtils.read(file);
	}

	@Override
	public void save(String name, byte[] data) throws Exception {
		logger.debug("save() " + name);
		File file = getFile(name);
		FileUtils.write(file, data);
	}

	@Override
	public void save(String name, InputStream stream) throws Exception {
		logger.debug("save() " + name);
		File file = getFile(name);
		FileUtils.write(file, stream);
	}
}
