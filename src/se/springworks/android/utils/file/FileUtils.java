package se.springworks.android.utils.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	
	public static byte[] read(final File file) throws IOException {
		final byte[] data = new byte[(int)file.length()];
		final FileInputStream fis = new FileInputStream(file);
		final BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(data);
		bis.close();
		return data;		
	}

	public static void write(File file, byte[] data) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.flush();
		fos.close();
	}

	public static void write(File file, InputStream stream) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = stream.read(bytes)) != -1) {
			fos.write(bytes, 0, read);
		}
		fos.flush();
		fos.close();
	}
}
