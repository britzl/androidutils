package se.springworks.android.utils.inject.guice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public abstract class CustomInjectionListener implements TypeListener {
	
	private Class<? extends Annotation> annotationClass;
	
	public CustomInjectionListener(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}
	
	@Override
	public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
		for (Class<?> c = typeLiteral.getRawType(); c != Object.class; c = c.getSuperclass()) {
			for (final Field field : c.getDeclaredFields()) {
				if (isFieldOfCorrectType(field) && field.isAnnotationPresent(annotationClass)) {
					final Annotation annotation = field.getAnnotation(annotationClass);
					typeEncounter.register(new MembersInjector<T>() {
						@Override
						public void injectMembers(T t) {
							field.setAccessible(true);
							try {
								inject(t, field, annotation);
							}
							catch (IllegalArgumentException e) {
								throw new RuntimeException(e);
							}
							catch (IllegalAccessException e) {
								throw new RuntimeException(e);
							}
						}
					});
				}
			}
		}
	}
	
	/**
	 * Check if a field is of the correct type for the kind of injection provided by
	 * this custom injector. The default behavior is to accepts all fields. Override this
	 * method to limit injection
	 * @param field
	 * @return true if the field is of the correct type
	 */
	protected boolean isFieldOfCorrectType(Field field) {
		return true;
	}
	
	protected abstract void inject(Object object, Field field, Annotation annotation) throws IllegalArgumentException, IllegalAccessException;
}