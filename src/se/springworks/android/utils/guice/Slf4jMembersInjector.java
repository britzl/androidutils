package se.springworks.android.utils.guice;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.MembersInjector;

public class Slf4jMembersInjector<T> implements MembersInjector<T> {
	private final Field field;
	private final Logger logger;

	Slf4jMembersInjector(Field aField) {
		this.field = aField;
		this.logger = LoggerFactory.getLogger(this.field.getDeclaringClass());
		this.field.setAccessible(true);
	}

	@Override
	public void injectMembers(T t) {
		try {
			this.field.set(t, this.logger);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}