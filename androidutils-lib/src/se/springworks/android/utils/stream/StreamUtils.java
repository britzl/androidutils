package se.springworks.android.utils.stream;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {

	public static byte[] getAsBytes(InputStream is, int bufferSize)
			throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);
		int oneByte;
		while ((oneByte = is.read()) != -1) {
			baos.write(oneByte);
		}
		return baos.toByteArray();
	}

	public static byte[] getAsBytes(InputStream is) throws IOException {
		return getAsBytes(is, 32);
	}

	/**
	 * http://stackoverflow.com/questions/2787015/skia-decoder-fails-to-decode-remote-stream
	 * 
	 * @author bjorn.ritzl
	 */
	public static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = this.in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int byteValue = read();
					if (byteValue < 0) {
						break; // we reached EOF
					}
					bytesSkipped = 1; // we read one byte
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}
	
	/**
	 * Closes a stream without throwing any exceptions
	 * @param out The stream to close. Can be null
	 */
	public static void closeSilently(OutputStream out) {
		if(out == null) {
			return;
		}
		try {
			out.close();
		} catch (IOException e) {
			// do nothing
		}
	}

}
