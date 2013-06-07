package se.springworks.android.utils.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public interface IFileHandler {

	/**
	 * Creates an empty file 
	 * @param filename
	 * @return true if successful, otherwise false
	 */
	public boolean createEmptyFile(String filename);
	
	/**
	 * Deletes a file
	 * @param name Name of the file to delete
	 * @return true if successful, otherwise false
	 */
	public boolean delete(String name) throws IOException;
	
	/**
	 * Checks if a file exists
	 * @param name Name of the file to look for
	 * @return true if the file exists, otherwise false
	 * @throws Exception
	 */
	public boolean exists(String name);

	/**
	 * Get a list of files in the root folder
	 * @return
	 */
	public String[] getFileList();

	/**
	 * Get a list of files in a specific folder
	 * @return
	 */
	public String[] getFileList(String path);

	/**
	 * Get the size of a file
	 * @param name
	 * @return Size of the file in bytes or -1 if the file doesn't exist
	 */
	public long getSize(String name);
	
	/**
	 * Get an input stream (reading) for a file
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public InputStream getReadableFile(String name) throws IOException;

	/**
	 * Get an output stream (writing) for a file
	 * @param name
	 * @param append
	 * @return
	 * @throws IOException
	 */
	public OutputStream getWritableFile(String name, boolean append) throws IOException;
	
	
	/**
	 * Get the modification date of a file
	 * @param name
	 * @return The modification date or null if the file doesn't exist
	 */
	public Date getFileModifiedDate(String name);
	
	
	/**
	 * Sets the modification time of a file
	 * @param name
	 * @param time
	 * @return true if successful, otherwise false
	 */
	public boolean setFileModifiedTime(String name, long time);

	/**
	 * Loads a file
	 * @param name Name of the file to loiad
	 * @return The bytes of the file
	 * @throws Exception
	 */
	public byte[] load(String name) throws IOException;

	/**
	 * Saves a file
	 * @param name Name of the file to write to
	 * @param data Bytes to write to the file
	 * @throws Exception
	 */
	public void save(String name, byte[] data) throws IOException;
	
	/**
	 * Saves a file
	 * @param name Name of the file to write to
	 * @param data String to write to the file (using writeUTF)
	 * @throws Exception
	 */
	public void save(String name, String data) throws IOException;
	
	/**
	 * Saves a file
	 * @param name Name of the file to write to
	 * @param stream Stream of bytes to write to the file
	 * @throws Exception
	 */
	public void save(String name, InputStream stream) throws IOException;

}
