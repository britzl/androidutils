package se.springworks.android.utils.persistence;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.springworks.android.utils.json.IJsonParser;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Inject;

public class SharedPreferencesStorage implements IKeyValueStorage {

	@Inject
	private Context context;
	
	@Inject
	private IJsonParser jsonParser;
	
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

	@Override
	public void put(String key, Set<String> value) {
		init();
		List<String> list = new ArrayList<String>();
		for(String v : value) {
			list.add(String.valueOf(Base64.encode(v.getBytes(), Base64.NO_WRAP)));
		}
		
		String json = jsonParser.toJson(list);
		put(key, json);
	}

	@Override
	public Set<String> getStrings(String key) {
		String json = getString(key, "");
		
		Set<String> strings = new HashSet<String>();
		List<String> list = jsonParser.fromJson(json, new TypeReference<ArrayList<String>>() {});
		for(String s : list) {
			strings.add(String.valueOf(Base64.decode(s, Base64.NO_WRAP)));
		}
		return strings;
	}

	@Override
	public <T> void put(String key, T o) {
		String json = jsonParser.toJson(o);
		put(key, json);
	}

	@Override
	public <T> T getObject(String key, Class<T> cls) {
		String json = getString(key);
		return jsonParser.fromJson(json, cls);
	}
}
