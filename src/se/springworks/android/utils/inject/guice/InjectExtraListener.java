package se.springworks.android.utils.inject.guice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import se.springworks.android.utils.inject.annotation.InjectExtra;
import android.app.Activity;
import android.os.Bundle;

public class InjectExtraListener extends CustomInjectionListener {

	
	
	public InjectExtraListener() {
		super(InjectExtra.class);
	}

	@Override
	protected void inject(Object o, Field field, Annotation annotation) throws IllegalArgumentException,
			IllegalAccessException {
		if(!(o instanceof Activity)) {
			throw new IllegalArgumentException("Object must be an activity to inject extras");
		}
		
		Activity a = (Activity)o;
		Bundle extras = a.getIntent().getExtras();
		if(extras != null) {
			final String key = ((InjectExtra)annotation).key();
			Object value = extras.get(key);
			try {
				if(value != null) {
					field.set(o, value);
				}
			}
			catch(IllegalArgumentException e) {
				throw new IllegalArgumentException(e.getMessage() + " field = " + field + " key = " + key + " value = " + value);
			}
		}
	}

}
