package se.springworks.android.utils.eventbus;

public interface IEventBus {

	void register(Object o);
	
	void post(Object data);
	
	void unregister(Object o);
	
	void unregisterAll();
}
