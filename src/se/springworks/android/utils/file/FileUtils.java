package se.springworks.android.utils.file;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	/**
	 * Read content of a file to a byte array
	 * @param file File to read
	 * @return The bytes
	 * @throws IOException
	 */
	public static byte[] read(final File file) throws IOException {
		if(file == null) {
			return null;
		}
		final byte[] data = new byte[(int)file.length()];
		final FileInputStream fis = new FileInputStream(file);
		final BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(data);
		bis.close();
		return data;		
	}

	/**
	 * Write bytes to a file. If the path to the file doesn't exist it will be created
	 * @param file
	 * @param data
	 * @throws IOException
	 */
	public static void write(File file, byte[] data) throws IOException {
		if(file == null) {
			return;
		}
		file.getParentFile().mkdirs();
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(data);
		fos.flush();
		fos.close();
	}

	/**
	 * Write stream to a file. If the path to the file doesn't exist it will be created
	 * @param file
	 * @param stream
	 * @throws IOException
	 */
	public static void write(File file, InputStream stream) throws IOException {
		if(file == null) {
			return;
		}
		file.getParentFile().mkdirs();
		FileOutputStream fos = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[1024];
		while ((read = stream.read(bytes)) != -1) {
			fos.write(bytes, 0, read);
		}
		fos.flush();
		fos.close();
	}

	/**
	 * Write string (UTF) to a file. If the path to the file doesn't exist it will be created
	 * @param file
	 * @param data
	 * @throws IOException
	 */
	public static void write(File file, String data) throws IOException {
		if(file == null) {
			return;
		}
		file.getParentFile().mkdirs();
		FileOutputStream fos = new FileOutputStream(file);
		DataOutputStream dos = new DataOutputStream(fos);
		dos.write(data.getBytes());
//		dos.writeChars(data);
		dos.flush();
		dos.close();
	}
}
