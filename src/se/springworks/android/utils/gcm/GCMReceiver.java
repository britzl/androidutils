package se.springworks.android.utils.gcm;

import se.springworks.android.utils.bundle.BundleUtil;
import se.springworks.android.utils.inject.GrapeGuice;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.inject.Inject;

public class GCMReceiver extends BroadcastReceiver {


	@InjectLogger
	private Logger logger;
	
	@Inject
	private IPushHandler pushHandler;
	
	@Inject
	private GoogleCloudMessaging gcm;

	@Override
	public void onReceive(Context context, Intent intent) {
		GrapeGuice.getInjector(context).injectMembers(this);
		logger.debug("onReceive() %s", BundleUtil.toString(intent.getExtras()));
        String messageType = gcm.getMessageType(intent);
        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
    		logger.debug("onReceive() error");
        	pushHandler.onError(intent.getExtras().toString());
        }
        else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
        	logger.debug("onReceive() message deleted");
        }
        else {
        	logger.debug("onReceive() message");
        	pushHandler.onMessage(intent.getExtras());
        }
        setResultCode(Activity.RESULT_OK);
	}
}
