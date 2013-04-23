package se.springworks.android.utils.resource;

import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.logging.LoggerFactory;
import android.content.Context;

/**
 * This class implements the ParameterLoader interface by loading parameters
 * from the Application's resources.
 */
public class ParameterLoader implements IParameterLoader {
	
	private static final Logger logger = LoggerFactory.getLogger(ParameterLoader.class);

	private final Context ctx;

	public ParameterLoader(Context ctx) {
		if (ctx == null) {
			throw new NullPointerException("Context cannot be null");
		}
		this.ctx = ctx;
	}

	/**
	 * Look up the resource id for the given type in the package identified by
	 * gaContext. The lookup is done by key instead of id as presence of these
	 * parameters are optional. Some or all may not be present. If gaContext is
	 * null, return 0.
	 * 
	 * @param key
	 *            the key for the string resource we're seeking
	 * @param type
	 *            the type (string, bool or integer)
	 * @return resource id of the given string resource, or 0 if not found
	 */
	private int getResourceIdForType(String key, String type) {
		if (ctx == null) {
			return 0;
		}
		return ctx.getResources().getIdentifier(key, type, ctx.getPackageName());
	}

	@Override
	public String getString(String key) {
		int id = getResourceIdForType(key, "string");
		if (id == 0) {
			return null;
		}
		else {
			return ctx.getString(id);
		}
	}

	@Override
	public boolean getBoolean(String key) {
		int id = getResourceIdForType(key, "bool");
		if (id == 0) {
			return false;
		}
		else {
			return "true".equalsIgnoreCase(ctx.getString(id));
		}
	}

	@Override
	public int getInt(String key, int defaultValue) {
		int id = getResourceIdForType(key, "integer");
		if (id == 0) {
			return defaultValue;
		}
		else {
			try {
				return Integer.parseInt(ctx.getString(id));
			}
			catch (NumberFormatException e) {
				logger.warn("NumberFormatException parsing " + ctx.getString(id));
				return defaultValue;
			}
		}
	}

	@Override
	public boolean hasBoolean(String key) {
		return getResourceIdForType(key, "bool") != 0;
	}

	@Override
	public boolean hasInt(String key) {
		return getResourceIdForType(key, "integer") != 0;
	}

	@Override
	public boolean hasString(String key) {
		return getResourceIdForType(key, "string") != 0;
	}

}