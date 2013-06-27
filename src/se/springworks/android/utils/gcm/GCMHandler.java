package se.springworks.android.utils.gcm;

import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.resource.ParameterLoader;
import se.springworks.android.utils.threading.ICallback;
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

	
	private ICallback registrationCallback;
	
	
	@Override
	public void unregister(ICallback callback) {
		logger.debug("unregister()");
		this.registrationCallback = callback;
		GCMRegistrar.unregister(context);
	}
	
	@Override
	public void register(ICallback callback) {
		if (!GCMRegistrar.isRegisteredOnServer(context)) {
			registrationCallback = callback;
			final String senderId = getSenderId();
			logger.debug("register() registering gcm using sender id = %s", senderId);
			GCMRegistrar.register(context, senderId);
		}
		else {
			logger.debug("register() GCM already registered using id = %s", GCMRegistrar.getRegistrationId(context));
			callback.onDone();
		}		
	}

	@Override
	public final void onRegistered(String regId) {
		logger.debug("onRegistered() %s", regId);
		registerOnServer(regId, new ICallback() {
			@Override
			public void onError(Throwable t) {
				GCMRegistrar.setRegisteredOnServer(context, false);
				try {
					registrationCallback.onError(t);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				registrationCallback = null;
			}
			
			@Override
			public void onDone() {
				GCMRegistrar.setRegisteredOnServer(context, true);
				try {
					registrationCallback.onDone();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				registrationCallback = null;
			}
		});
	}

	@Override
	public final void onUnregistered(String regId) {
		logger.debug("onUnregistered() %s", regId);
		unregisterOnServer(regId, new ICallback() {
			
			@Override
			public void onError(Throwable t) {
				try {
					registrationCallback.onError(t);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				registrationCallback = null;
			}
			
			@Override
			public void onDone() {
				GCMRegistrar.setRegisteredOnServer(context, false);
				try {
					registrationCallback.onDone();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				registrationCallback = null;
			}
		});
	}

	@Override
	public void onError(String errorId) {
		logger.error("error() %s", errorId);
		GCMRegistrar.setRegisteredOnServer(context, false);
		try {
			registrationCallback.onError(new RuntimeException(errorId));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		registrationCallback = null;
	}

	@Override
	public String getSenderId() {
		return parameterLoader.getString(KEY_SENDERID);
	}
	
	@Override
	public String getRegistrationId() {
		return GCMRegistrar.getRegistrationId(context);
	}

}
