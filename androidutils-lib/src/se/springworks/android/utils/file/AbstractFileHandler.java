package se.springworks.android.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.logging.LoggerFactory;

public abstract class AbstractFileHandler implements IFileHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractFileHandler.class);
	
	public AbstractFileHandler() {
	}
	
	@Override
	public boolean createEmptyFile(String filename) {
		File file = getFile(filename);
		try {
			file.getParentFile().mkdirs();
			return file.createNewFile();
		}
		catch (IOException e) {
		}
		return false;
	}
	
	@Override
	public boolean delete(String name) throws IOException {
		logger.debug("delete() " + name);
		File fileToDelete = getFile(name);
		if(fileToDelete != null) {
			return fileToDelete.delete();
		}
		return false;
	}
	
	@Override
	public boolean deleteDir(String path) throws IOException {
		File dirToDelete = getFile(path);
		if(dirToDelete == null) {
			return false;
		}

		boolean success = true;
		if(dirToDelete.isDirectory()) {
			// recursively delete all files and folders first
			for(String file : dirToDelete.list()) {
				success = deleteDir(file) && success;
			}
		}
		return dirToDelete.delete() && success;
	}

	@Override
	public boolean exists(String name) {
//		logger.debug("exists() " + name);
		File file = getFile(name);
		return file.exists();
	}

	@Override
	public String[] getFileList() {
		return getFileList("");
	}
	
	@Override
	public String[] getFileList(String path) {
		File dir = getFile(path);
		logger.debug("getFileList() path = " + path + " base dir = " + getBaseFolder() + " full path = " + dir.getAbsolutePath());
		String files[] = dir.list();
		return files;
	}

	@Override
	public long getSize(String name) {
		File file = getFile(name);
		if(!file.exists()) {
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
		File file = getFile(name);
		file.getParentFile().mkdirs();
		return new FileOutputStream(file, append);
	}
	
	@Override
	public Date getFileModifiedDate(String name) {
		File file = getFile(name);
		if(!file.exists()) {
			return null;
		}
		return new Date(file.lastModified());
	}
	
	@Override
	public boolean setFileModifiedTime(String name, long time) {
		File file = getFile(name);
		if(!file.exists()) {
			return false;
		}
		return file.setLastModified(time);
	}

	@Override
	public byte[] load(String name) throws IOException {
		File file = getFile(name);
		return FileUtils.read(file);
	}

	@Override
	public void save(String name, byte[] data) throws IOException {
		logger.debug("save() " + name);
		File file = getFile(name);
		FileUtils.write(file, data);
	}
	
	@Override
	public void save(String name, String data) throws IOException{
		File file = getFile(name);
		logger.debug("save() " + name + " " + file);
		FileUtils.write(file, data);
	}

	@Override
	public void save(String name, InputStream stream) throws IOException {
		logger.debug("save() " + name);
		File file = getFile(name);
		FileUtils.write(file, stream);
	}
}
