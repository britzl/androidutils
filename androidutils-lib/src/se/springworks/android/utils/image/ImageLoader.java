package se.springworks.android.utils.image;

import java.io.InputStream;

import se.springworks.android.utils.http.ISimpleHttpClient;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.stream.StreamUtils.FlushedInputStream;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.google.inject.Inject;

public class ImageLoader implements IImageLoader {
	@InjectLogger
	private Logger logger;
	
	@Inject
	private ISimpleHttpClient client;
	
	@Inject
	private AssetManager assetManager;

	private int maxDownsampling = 1;

	private Config config;

	private InputStream getAsStream(String url) {
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
	
	@Override
	public Bitmap getAsBitmap(InputStream in) {
		logger.debug("getAsBitmap()");
		return BitmapUtils.getAsBitmap(in, maxDownsampling, config);
	}

	@Override
	public Bitmap getFromAssets(String fileName) {
		return BitmapUtils.getFromAssets(assetManager, fileName, maxDownsampling, config);
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
