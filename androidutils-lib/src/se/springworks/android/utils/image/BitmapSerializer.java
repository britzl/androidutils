package se.springworks.android.utils.image;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * Class to serialize a bitmap
 * @author bjornritzl
 *
 */
public class BitmapSerializer implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Bitmap bitmap;

	public BitmapSerializer() {
		
	}
	
	public BitmapSerializer(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
	private synchronized void writeObject(final ObjectOutputStream out) throws IOException {
		final int width = bitmap.getWidth();
		final int height = bitmap.getHeight();
		final int pixelCount = width * height;
		
	    out.writeInt(height);
	    out.writeInt(width);
	    out.writeInt(bitmap.getConfig().ordinal());
	    
	    int[] pixelBuffer = new int[pixelCount];

	    bitmap.getPixels(pixelBuffer, 0, width, 0, 0, width, height);
	    for(int i = 0; i < pixelCount; i++) {
	    	out.writeInt(pixelBuffer[i]);
	    }
	}


	
	private synchronized void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
	    final int height = in.readInt();
	    final int width = in.readInt();
	    final int pixelCount = width * height;
	    final Bitmap.Config config = Bitmap.Config.values()[in.readInt()];

	    bitmap = Bitmap.createBitmap(width, height, config);

	    int[] pixelBuffer = new int[pixelCount];
	    for(int i = 0; i < pixelCount; i++) {
	    	pixelBuffer[i] = in.readInt();
	    }
	    bitmap.setPixels(pixelBuffer, 0, width, 0, 0, width, height);
	}
}
