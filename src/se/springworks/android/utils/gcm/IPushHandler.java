package se.springworks.android.utils.gcm;

public interface IPushHandler {

	void register();
	
	void unregister();
	
	String getSenderId();
}
