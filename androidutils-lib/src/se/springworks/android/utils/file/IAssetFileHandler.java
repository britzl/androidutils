package se.springworks.android.utils.file;

import java.io.IOException;
import java.io.InputStream;

public interface IAssetFileHandler {

	boolean exists(String name);

	String[] getFileList();

	String[] getFileList(String path);

	long getSize(String name);

	byte[] load(String name) throws Exception;

	InputStream getReadableFile(String name) throws IOException;
}
