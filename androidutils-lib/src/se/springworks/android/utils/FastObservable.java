package se.springworks.android.utils;

import java.util.Observable;

/**
 * Observable which implicitly calls setChanged() when notifyObservers() is called
 * @author bjornritzl
 *
 */
public class FastObservable extends Observable {
	
	
	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

	@Override
	public void notifyObservers(Object data) {
		setChanged();
		super.notifyObservers(data);
	}

}
