package se.springworks.android.utils.file;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.springworks.android.utils.stream.StreamUtils;

import android.content.res.AssetManager;

public class AssetFileHandler implements IFileHandler {

	private static final Logger logger = LoggerFactory.getLogger(AssetFileHandler.class);

	private AssetManager assetManager;

	public AssetFileHandler(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	@Override
	public boolean createEmptyFile(String filename) {
		throw new RuntimeException("Not able to save files to assets folder");
	}

	@Override
	public boolean delete(String name) throws Exception {
		throw new RuntimeException(
				"Not able to delete files from assets folder");
	}

	@Override
	public boolean exists(String name) {
		logger.debug("exists() " + name);
		String path = "";
		int index = name.lastIndexOf(File.separator);
		if (index != -1) {
			path = name.substring(0, index);
			name = name.substring(index + 1);
		}
		logger.debug("exists() name = " + name + " path = " + path);

		String files[] = getFileList(path);
		for (int i = 0; i < files.length; i++) {
			if (files[i].equals(name)) {
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
		try {
			return this.assetManager.list(path);
		} catch (IOException e) {
		}
		return null;
	}

	@Override
	public long getSize(String name) {
		try {
			return this.assetManager.openFd(name).getLength();
		} catch (IOException e) {
		}
		return 0;
	}

	@Override
	public FileDescriptor getFileDescriptor(String name) throws IOException {
		return this.assetManager.openFd(name).getFileDescriptor();
	}

	@Override
	public Date getFileModifiedDate(String name) {
		return new Date();
	}

	@Override
	public byte[] load(String name) throws Exception {
		InputStream stream = this.assetManager.open(name);
		return StreamUtils.getAsBytes(stream);
	}

	@Override
	public void save(String name, byte[] data) throws Exception {
		throw new RuntimeException("Not able to save files to assets folder");
	}

	@Override
	public void save(String name, InputStream stream) throws Exception {
		throw new RuntimeException("Not able to save files to assets folder");
	}

	@Override
	public InputStream getFileAsStream(String name) throws IOException {
		return this.assetManager.open(name);
	}

}
