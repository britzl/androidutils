package se.springworks.android.utils.image;

import java.io.InputStream;

import android.graphics.Bitmap;

public interface IImageLoader {

	InputStream getAsStream(String url);
	
	Bitmap getAsBitmap(String url);
	
	Bitmap getAsBitmap(InputStream in);

	Bitmap getFromAssets(String filename);
}
