package se.springworks.android.utils.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.inject.Inject;

public class SharedPreferencesStorage implements IKeyValueStorage {

	@Inject
	private Context context;
	
	private SharedPreferences sp;
	
	private void init() {
		if(sp == null) {
			sp = context.getSharedPreferences(context.getPackageName(), 0);
		}
	}
	
	@Override
	public void put(String key, String value) {
		init();
		sp.edit().putString(key, value).commit();
	}

	@Override
	public String getString(String key) {
		init();
		return sp.getString(key, key);
	}

	@Override
	public void remove(String key) {
		init();
		sp.edit().remove(key).commit();
	}

	@Override
	public void put(String key, long value) {
		init();
		sp.edit().putLong(key, value).commit();
	}

	@Override
	public void put(String key, boolean value) {
		init();
		sp.edit().putBoolean(key, value).commit();
	}

	@Override
	public long getLong(String key) {
		init();
		return sp.getLong(key, 0);
	}

	@Override
	public boolean getBoolean(String key) {
		init();
		return sp.getBoolean(key, false);
	}

	@Override
	public boolean contains(String key) {
		init();
		return sp.contains(key);
	}

}
