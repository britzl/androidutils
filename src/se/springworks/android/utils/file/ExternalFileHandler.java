package se.springworks.android.utils.file;

import com.google.inject.Inject;

import android.content.Context;
import android.os.Environment;

/**
 * FileHandler for working with files on the application's external storage
 * folder (SD card or similar)
 * 
 * @author bjorn.ritzl
 * 
 */
public class ExternalFileHandler extends AbstractFileHandler {

	private Context context;

	@Inject
	public ExternalFileHandler(Context context) {
		super();
		this.context = context;
	}

	public static boolean isAvailable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	@Override
	public String getBaseFolder() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return this.context.getExternalFilesDir(null).getAbsolutePath();
		}
		return null;
	}
}
