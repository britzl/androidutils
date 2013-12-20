package se.springworks.android.utils.image;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class BitmapUtils {

	/**
	 * Get a bitmap from a file in the asserts folder
	 * @param assets
	 * @param fileName
	 * @return
	 */
	public static Bitmap getFromAssets(AssetManager assets, String fileName) {
		return getFromAssets(assets, fileName, 1, null);
	}
	
	public static Bitmap getFromAssets(AssetManager assets, String fileName, int maxDownsampling, Config config) {
		Bitmap bitmap = null;
		try {
			bitmap = getAsBitmap(assets.open(fileName), maxDownsampling, config);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	/**
	 * Load a bitmap from a stream using a specific pixel configuration. If the image is too
	 * large (ie causes an OutOfMemoryError situation) the method will iteratively try to
	 * increase sample size up to a defined maximum sample size. The sample size will be doubled
	 * each try since this it is recommended that the sample size should be a factor of two
	 * @param in Stream to load bitmap from
	 * @param maxDownsampling Maximum downsampling of the bitmap before giving up
	 * @param config The {@link Config} to use when loading the bitmap
	 * @return The bitmap or null if it wasn't possible to load the bitmap
	 */
	public static Bitmap getAsBitmap(InputStream in, int maxDownsampling, Config config) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;	
		options.inPreferredConfig = config;
		Bitmap bitmap = null;
		while(bitmap == null && options.inSampleSize <= maxDownsampling) {
			try {
				bitmap = BitmapFactory.decodeStream(in, null, options);
				if(bitmap == null) {
					options.inSampleSize *= 2;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				break;
			}
			catch(OutOfMemoryError error) {
				options.inSampleSize *= 2;
			}
		}
		return bitmap;
	}

	/**
	 * Compares two bitmaps, pixel for pixel
	 * @param b1
	 * @param b2
	 * @return true if the bitmaps have the same dimensions, configuration and pixeldata
	 */
	public static boolean isSame(Bitmap b1, Bitmap b2) {
		if(b1 == null || b2 == null) {
			return false;
		}
		
		if(b1.getWidth() != b2.getWidth() || b1.getHeight() != b2.getHeight()) {
			return false;
		}
		
		if(b1.getConfig() != b2.getConfig()) {
			return false;
		}
		
		final int width = b1.getWidth();
		final int height = b2.getHeight();
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(b1.getPixel(x, y) != b2.getPixel(x, y)) {
					return false;
				}
			}
		}
		return true;
	}
}
