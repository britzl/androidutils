package se.springworks.android.utils.eventbus;

import java.util.ArrayList;
import java.util.List;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class OttoBus implements IEventBus {
	
	public enum ThreadEnforcement {
		MAIN,
		NONE
	}

	private Bus bus;
	
	private List<Object> registeredObjects = new ArrayList<Object>();
	
	public OttoBus() {
		this(ThreadEnforcement.MAIN);
	}
	
	public OttoBus(ThreadEnforcement threadEnforcement) {
		switch(threadEnforcement) {
			default:
			case MAIN:
				bus = new Bus(ThreadEnforcer.MAIN);
				break;
			case NONE:				
				bus = new Bus(ThreadEnforcer.ANY);
				break;
		}
	}
	
	@Override
	public synchronized void register(Object o) {
		bus.register(o);
		registeredObjects.add(o);
	}
	
	@Override
	public synchronized void post(Object data) {
		bus.post(data);
	}
	
	@Override
	public synchronized void unregister(Object o) {
		bus.unregister(o);
		registeredObjects.remove(o);
	}
	
	@Override
	public synchronized void unregisterAll() {
		while(!registeredObjects.isEmpty()) {
			Object o = registeredObjects.remove(0);
			bus.unregister(o);
		}
	}
}
