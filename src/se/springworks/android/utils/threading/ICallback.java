package se.springworks.android.utils.threading;

public interface ICallback {
	
	void onDone();
	
	void onError(Throwable t);
}
