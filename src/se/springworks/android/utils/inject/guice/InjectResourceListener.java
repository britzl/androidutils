package se.springworks.android.utils.inject.guice;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import se.springworks.android.utils.inject.annotation.InjectResource;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;

public class InjectResourceListener extends CustomInjectionListener {

	private Resources resources;
	
	public InjectResourceListener(Resources resources) {
		super(InjectResource.class);
		this.resources = resources;
	}

	@Override
	protected void inject(Object o, Field field, Annotation annotation) throws IllegalArgumentException,
			IllegalAccessException {
		
        final int id = ((InjectResource)annotation).id();
        final Class<?> type = field.getType();
        if (type == String.class) {
        	field.set(o, resources.getString(id));
        }
        else if (type == Boolean.class) {
        	field.set(o, resources.getBoolean(id));
        }
        else if (Movie.class.isAssignableFrom(type)) {
        	field.set(o, resources.getMovie(id));
        }
        else if (Drawable.class.isAssignableFrom(type)) {
        	field.set(o, resources.getDrawable(id));
        }
        else if (Bitmap.class.isAssignableFrom(type)) {
        	field.set(o, BitmapFactory.decodeResource(resources, id));
        }
        else {
            throw new IllegalArgumentException("Cannot inject for type " + type + " (field " + field.getName() + ")");
        }	
	}
}
