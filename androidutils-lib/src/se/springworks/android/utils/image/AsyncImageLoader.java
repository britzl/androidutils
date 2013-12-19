package se.springworks.android.utils.image;

import java.io.InputStream;

import se.springworks.android.utils.cache.ICache;
import se.springworks.android.utils.cache.ICache.CacheException;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class AsyncImageLoader {

	public interface OnImageLoadHandler {
		public void onImage(Bitmap bitmap);
		public void onError(String error);
	}
	
	private IImageLoader loader;
	
	
	@Inject(optional=true)
	@Named("asyncimageloader")
	private ICache<BitmapSerializer> cache;
	
	@Inject
	public AsyncImageLoader(IImageLoader loader) {
		this.loader = loader;
	}
	
	/**
	 * Loads a bitmap from a URL. If a cache is set it will be checked before
	 * loading 
	 * @param url
	 * @param handler
	 */
	public void load(final String url, final OnImageLoadHandler handler) {
		if(cache != null && cache.contains(url)) {
			handler.onImage(cache.get(url).getBitmap());
		}
		else {
			AsyncTask<String, Void, Bitmap> task = new AsyncTask<String, Void, Bitmap>() {
	
				@Override
				protected Bitmap doInBackground(String... params) {
					String url = params[0];
					return loader.getAsBitmap(url);
				}
				
				@Override
				protected void onPostExecute(Bitmap result) {
					if(result == null) {
						handler.onError("Unable to load bitmap");
						return;
					}
					try {
						if(cache != null) {
							cache.cache(url, new BitmapSerializer(result));
						}
					} catch (CacheException e) {
						e.printStackTrace();
					}
					handler.onImage(result);
				}
			};
			task.execute(url);
		}
	}
	
	public void load(InputStream in, final OnImageLoadHandler handler) {
		AsyncTask<InputStream, Void, Bitmap> task = new AsyncTask<InputStream, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(InputStream... params) {
				return loader.getAsBitmap(params[0]);
			}
			
			@Override
			protected void onPostExecute(Bitmap result) {
				if(result == null) {
					handler.onError("Unable to load bitmap");
					return;
				}
				handler.onImage(result);
			}
		};
		task.execute(in);
	}
}
