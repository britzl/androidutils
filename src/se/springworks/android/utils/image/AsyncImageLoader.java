package se.springworks.android.utils.image;

import java.io.InputStream;

import com.google.inject.Inject;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class AsyncImageLoader {

	public interface OnImageLoadHandler {
		public void onImage(Bitmap bitmap);
		public void onError(String error);
	}
	
	@Inject
	private IImageLoader loader;
	
	/**
	 * Loads a bitmap form a URL
	 * @param url
	 * @param handler
	 */
	public void load(String url, final OnImageLoadHandler handler) {
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
				handler.onImage(result);
			}
		};
		task.execute(url);
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
