package se.springworks.android.utils.notification;

import com.google.inject.Inject;

import android.app.Notification;
import android.app.NotificationManager;

public class AndroidNotificationManager implements INotificationManager {

	@Inject
	private NotificationManager manager;
	
	@Override
	public void notify(int id, Notification notification) {
		manager.notify(id, notification);
	}

}
