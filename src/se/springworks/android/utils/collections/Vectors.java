package se.springworks.android.utils.collections;

import java.util.Iterator;
import java.util.Vector;

public class Vectors {

	public static <T> Vector<T> copyIterator(Iterator<T> iter) {
		Vector<T> copy = new Vector<T>();
		while (iter.hasNext()) {
			copy.add(iter.next());
		}
		return copy;
	}
}
