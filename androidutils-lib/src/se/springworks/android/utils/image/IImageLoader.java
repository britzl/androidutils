package se.springworks.android.utils.image;

import java.io.InputStream;

import android.graphics.Bitmap;

/**
 * Interface for classes that provide functionality for loading images, optionally
 * using a specific pixel bit depth.
 * 
 * The loader should provide functionality to downsample a loaded image
 * in case of memory problems (OutOfMemoryError).
 * 
 * @author bjornritzl
 *
 */
public interface IImageLoader {
	
	/**
	 * Max amount of downsampling to apply in case of out of memory problems.
	 * @param max
	 */
	void setMaxDownsampling(int max);
	
	/**
	 * Bit depth to use when loading images
	 * @param config
	 */
	void setBitmapConfig(Bitmap.Config config);

	InputStream getAsStream(String url);
	
	Bitmap getAsBitmap(String url);
	
	Bitmap getAsBitmap(InputStream in);
	
	Bitmap getFromAssets(String filename);
}
