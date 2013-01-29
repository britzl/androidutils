package se.springworks.android.utils.file;

import android.content.Context;

/**
 * FileHandler for working with files in the application's internal storage
 * 
 * @author bjorn.ritzl
 * 
 */
public class InternalFileHandler extends AbstractFileHandler {

	private Context context;

	public InternalFileHandler(Context context) {
		super();
		this.context = context;
	}

	@Override
	public String getBaseFolder() {
		return this.context.getFilesDir().getAbsolutePath();
	}

}
