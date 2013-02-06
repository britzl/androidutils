package se.springworks.android.utils.image;

import java.io.IOException;
import java.io.InputStream;

import se.springworks.android.utils.file.IAssetFileHandler;
import se.springworks.android.utils.http.ISimpleHttpClient;
import se.springworks.android.utils.stream.StreamUtils.FlushedInputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.inject.Inject;

public class ImageLoader implements IImageLoader {
	
	@Inject
	private ISimpleHttpClient client;
	
	@Inject
	private IAssetFileHandler assetFileHandler;

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
		return BitmapFactory.decodeStream(in);
	}
	
	@Override
	public Bitmap getAsBitmap(InputStream in) {
		return BitmapFactory.decodeStream(in);
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
}
