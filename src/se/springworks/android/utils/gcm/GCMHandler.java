package se.springworks.android.utils.gcm;

import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.resource.ParameterLoader;
import android.content.Context;

import com.google.android.gcm.GCMRegistrar;
import com.google.inject.Inject;

public abstract class GCMHandler implements IPushHandler {

	public static final String KEY_SENDERID = "gcm_senderid";

	@InjectLogger
	private Logger logger;
	
	@Inject
	private Context context;
	
	@Inject
	private ParameterLoader parameterLoader;

	
	@Override
	public void unregister() {
		logger.debug("unregister()");
		GCMRegistrar.unregister(context);
	}

	@Override
	public void register() {
//		GCMRegistrar.checkDevice(context);
//		GCMRegistrar.checkManifest(context);
		if (!GCMRegistrar.isRegisteredOnServer(context)) {
			final String senderId = getSenderId();
			logger.debug("register() registering gcm using sender id = %s", senderId);
			GCMRegistrar.register(context, senderId);
		}
		else {
			logger.debug("register() GCM already registered using id = %s", GCMRegistrar.getRegistrationId(context));
		}		
	}

	@Override
	public String getSenderId() {
		return parameterLoader.getString(KEY_SENDERID);
	}
}
