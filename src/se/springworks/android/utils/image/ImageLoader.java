package se.springworks.android.utils.image;

import java.io.IOException;
import java.io.InputStream;

import se.springworks.android.utils.file.IAssetFileHandler;
import se.springworks.android.utils.http.ISimpleHttpClient;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.stream.StreamUtils.FlushedInputStream;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

import com.google.inject.Inject;

public class ImageLoader implements IImageLoader {
	@InjectLogger
	private Logger logger;
	
	@Inject
	private ISimpleHttpClient client;
	
	@Inject
	private IAssetFileHandler assetFileHandler;

	private int maxDownsampling = 1;

	private Config config;

	@Override
	public InputStream getAsStream(String url) {
		InputStream in = client.get(url);
		if(in == null) {
			return null;
		}
		return new FlushedInputStream(in);
	}
	
	@Override
	public Bitmap getAsBitmap(String url) {
		InputStream in = getAsStream(url);
		if(in == null) {
			return null;
		}
		return getAsBitmap(in);
	}
	
	/**
	 * Load a bitmap from a stream using a specific pixel configuration. If the image is too
	 * large (ie causes an OutOfMemoryError situation) the method will iteratively try to
	 * increase sample size up to a defined maximum sample size. The sample size will be doubled
	 * each try since this it is recommended that the sample size should be a factor of two
	 */
	@Override
	public Bitmap getAsBitmap(InputStream in) {
		logger.debug("getAsBitmap()");
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;	
		options.inPreferredConfig = config;
		Bitmap bitmap = null;
		while(bitmap == null && options.inSampleSize <= maxDownsampling) {
			logger.debug("trying to load using sample size %d", options.inSampleSize);
			try {
				bitmap = BitmapFactory.decodeStream(in, null, options);
				if(bitmap == null) {
					options.inSampleSize *= 2;
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				logger.debug("exception %s", e.getMessage());
				break;
			}
			catch(OutOfMemoryError error) {
				logger.debug("out of memory, doubling sample size");
				options.inSampleSize *= 2;
			}
		}
		logger.debug("load done %s", bitmap);
		return bitmap;
	}

	@Override
	public Bitmap getFromAssets(String filename) {
		try {
			return getAsBitmap(assetFileHandler.getReadableFile(filename));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@Override
	public void setMaxDownsampling(int max) {
		this.maxDownsampling = max;
	}

	@Override
	public void setBitmapConfig(Config config) {
		this.config = config;
	}
}
