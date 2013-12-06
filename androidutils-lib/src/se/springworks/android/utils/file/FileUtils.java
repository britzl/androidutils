package se.springworks.android.utils.file;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

	private static final int MAX_FILENAME_LENGTH = 127;
	
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
		if(file == null || stream == null) {
			return;
		}
		file.getParentFile().mkdirs();
		FileOutputStream fos = new FileOutputStream(file);
		int read = 0;
		byte[] bytes = new byte[10000];
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
	
    /**
     * Converts any string into a string that is safe to use as a file name.
     * The result will only include ascii characters and numbers, and the "-","_", and "." characters.
     * 
     * Modified slightly from:
     * http://activemq.apache.org/maven/5.7.0/kahadb/apidocs/org/apache/kahadb/util/IOHelper.html
     * @param name
     * @return
     */
    public static String toFileSystemSafeName(String name) {
        int size = name.length();
        StringBuffer rc = new StringBuffer(size * 2);
        for (int i = 0; i < size; i++) {
            char c = name.charAt(i);
            boolean valid = c >= 'a' && c <= 'z';
            valid = valid || (c >= 'A' && c <= 'Z');
            valid = valid || (c >= '0' && c <= '9');
            valid = valid || (c == '_') || (c == '-') || (c == '.') || (c == '#');

            if (valid) {
                rc.append(c);
            } else {
                // Encode the character using hex notation
                rc.append('#');
                rc.append(Integer.toHexString(c));
            }
        }
        String result = rc.toString();
        if (result.length() > MAX_FILENAME_LENGTH) {
            result = result.substring(result.length() - MAX_FILENAME_LENGTH, result.length());
        }
        return result;
    }
	
}
