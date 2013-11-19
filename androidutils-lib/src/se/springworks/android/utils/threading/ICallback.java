package se.springworks.android.utils.threading;

/**
 * Generic callback interface that can be used in asynchronous method calls
 * @author bjornritzl
 *
 */
public interface ICallback {
	
	void onDone();
	
	void onError(Throwable t);
}
