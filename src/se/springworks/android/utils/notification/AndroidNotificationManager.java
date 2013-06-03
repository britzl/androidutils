package se.springworks.android.utils.notification;

import android.app.Notification;
import android.app.NotificationManager;

import com.google.inject.Inject;

public class AndroidNotificationManager implements INotificationManager {

	@Inject
	private NotificationManager manager;
	
	@Override
	public void notify(int id, Notification notification) {
		manager.notify(id, notification);
	}

}
