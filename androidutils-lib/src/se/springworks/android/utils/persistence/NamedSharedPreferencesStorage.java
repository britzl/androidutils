package se.springworks.android.utils.persistence;

import se.springworks.android.utils.json.IJsonParser;
import android.content.Context;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class NamedSharedPreferencesStorage extends SharedPreferencesStorage {
	
	@Inject
	public NamedSharedPreferencesStorage(Context context, IJsonParser jsonParser, @Assisted String name) {
		super(context, jsonParser, name);
	}

}
