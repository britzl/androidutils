package se.springworks.android.utils.guice;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class Slf4jTypeListener implements TypeListener {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(Slf4jTypeListener.class);

	@Override
	public <T> void hear(TypeLiteral<T> typeLiteral,
			TypeEncounter<T> typeEncounter) {
		for (Class<?> c = typeLiteral.getRawType(); c != Object.class; c = c
				.getSuperclass()) {
			for (Field field : c.getDeclaredFields()) {
				if (field.getType() == Logger.class
						&& field.isAnnotationPresent(InjectLogger.class)) {
					typeEncounter.register(new Slf4jMembersInjector<T>(field));
				}
			}
		}

		// for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
		// if (field.getType() == Logger.class &&
		// field.isAnnotationPresent(InjectLogger.class)) {
		// typeEncounter.register(new Slf4jMembersInjector<T>(field));
		// }
		// }
	}

	// @Override
	// public <I> void hear(final TypeLiteral<I> type, TypeEncounter<I>
	// encounter) {
	// final Field field = getLoggerField(type.getRawType());
	// logger.debug("hear() " + field);
	// if (field != null) {
	// encounter.register(new InjectionListener<I>() {
	// @Override
	// public void afterInjection(I injectee) {
	// try {
	// field.setAccessible(true);
	// field.set(injectee, LoggerFactory.getLogger(type.getRawType()));
	// } catch (IllegalAccessException e) {
	// throw new ProvisionException("Unable to inject SLF4J logger", e);
	// }
	// }
	// });
	// }
	// }
	//
	// protected Field getLoggerField(Class<?> clazz) {
	// // search for Logger in current class and return it if found
	// for (final Field field : clazz.getDeclaredFields()) {
	// final Class<?> typeOfField = field.getType();
	// if (Logger.class.isAssignableFrom(typeOfField)) {
	// return field;
	// }
	// }
	//
	// // search for Logger in superclass if not found in this class
	// if (clazz.getSuperclass() != null) {
	// return getLoggerField(clazz.getSuperclass());
	// }
	//
	// // not in current class and not having superclass, return null
	// return null;
	// }
}
