package se.springworks.android.utils.otto;

import java.util.ArrayList;
import java.util.List;

import com.squareup.otto.Bus;

public class OttoBus {

	private static Bus bus = new Bus();
	
	private static List<Object> registeredObjects = new ArrayList<Object>();
	
	public static void register(Object o) {
		bus.register(o);
		registeredObjects.add(o);
	}
	
	public static void post(Object data) {
		bus.post(data);
	}
	
	public static void unregister(Object o) {
		bus.unregister(o);
		registeredObjects.remove(o);
	}
	
	public static void unregisterAll() {
		while(!registeredObjects.isEmpty()) {
			Object o = registeredObjects.remove(0);
			bus.unregister(o);
		}
	}
}
