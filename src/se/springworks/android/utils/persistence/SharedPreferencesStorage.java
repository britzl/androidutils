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
			sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		}
	}

	@Override
	public void remove(String key) {
		init();
		sp.edit().remove(key).commit();
	}
	
	@Override
	public void put(String key, String value) {
		init();
		sp.edit().putString(key, value).commit();
	}

	@Override
	public void put(String key, int value) {
		init();
		sp.edit().putInt(key, value).commit();
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
	public String getString(String key) {
		return getString(key, null);
	}

	@Override
	public String getString(String key, String defaultValue) {
		init();
		return sp.getString(key, defaultValue);
	}

	@Override
	public long getLong(String key) {
		return getLong(key, 0);
	}

	@Override
	public long getLong(String key, long defaultValue) {
		init();
		return sp.getLong(key, defaultValue);
	}

	@Override
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		init();
		return sp.getBoolean(key, defaultValue);
	}

	@Override
	public boolean contains(String key) {
		init();
		return sp.contains(key);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		init();
		return sp.getInt(key, defaultValue);
	}

	@Override
	public int getInt(String key) {
		return getInt(key, 0);
	}
}
