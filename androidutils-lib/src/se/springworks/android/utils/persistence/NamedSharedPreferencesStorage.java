package se.springworks.android.utils.persistence;

import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class NamedSharedPreferencesStorage extends SharedPreferencesStorage {
	
	@Inject
	public NamedSharedPreferencesStorage(Context context, @Assisted String name) {
		super(context, name);
	}

}
