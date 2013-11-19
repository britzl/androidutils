package se.springworks.android.utils;

import se.springworks.android.utils.json.IJsonParser;
import se.springworks.android.utils.json.JacksonParser;
import se.springworks.android.utils.persistence.IKeyValueStorage;
import se.springworks.android.utils.persistence.SharedPreferencesStorage;
import android.content.Context;

import com.google.inject.AbstractModule;

public class MockModule extends AbstractModule {

	private Context context;

	public MockModule(Context context) {
		this.context = context;
	}
	
	@Override
	protected void configure() {
		bind(Context.class).toInstance(context);
		bind(IKeyValueStorage.class).to(SharedPreferencesStorage.class);
		bind(IJsonParser.class).to(JacksonParser.class);
	}

}
