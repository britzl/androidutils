package se.springworks.android.utils.bundle;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;

public class BundleUtil {

	public static boolean containsNonEmpty(Bundle b, String key) {
		Object o = b.get(key);
		if(o == null) {
			return false;
		}
		else if(o instanceof String) {
			String s = (String)o;
			return s != null && !s.isEmpty();
		}
		else if(o instanceof byte[]) {
			byte a[] = (byte[])o;
			return a != null && a.length != 0;
		}
		else if(o instanceof short[]) {
			short a[] = (short[])o;
			return a != null && a.length != 0;
		}
		else if(o instanceof char[]) {
			char a[] = (char[])o;
			return a != null && a.length != 0;
		}
		else if(o instanceof int[]) {
			int a[] = (int[])o;
			return a != null && a.length != 0;
		}
		else if(o instanceof float[]) {
			float a[] = (float[])o;
			return a != null && a.length != 0;
		}
		else if(o instanceof double[]) {
			double a[] = (double[])o;
			return a != null && a.length != 0;
		}
		else if(o instanceof String[]) {
			String a[] = (String[])o;
			return a != null && a.length != 0;
		}
		else if(o instanceof ArrayList) {
			ArrayList<?> a = (ArrayList<?>)o;
			return a != null && !a.isEmpty();
		}
		return o != null;
	}
	
	public static boolean getAsBoolean(Bundle b, String key) {
		if(b == null) {
			return false;
		}
		if(!b.containsKey(key)) {
			return false;
		}
		boolean value = false;
		String s = b.getString(key);
		if(s == null) {
			try {
				value = b.getBoolean(key);
			}
			catch(ClassCastException e) {

			}
		}
		else {
			try {
				value = Boolean.parseBoolean(s);
			}
			catch(NumberFormatException e) {

			}
		}
		return value;
	}

	public static int getAsInt(Bundle b, String key) {
		if(b == null) {
			return 0;
		}
		if(!b.containsKey(key)) {
			return 0;
		}
		int value = 0;
		String s = b.getString(key);
		if(s == null) {
			try {
				value = b.getInt(key);
			}
			catch(ClassCastException e) {

			}
		}
		else {
			try {
				value = Integer.parseInt(s);
			}
			catch(NumberFormatException e) {

			}
		}
		return value;
	}
	
	public static void clearExtras(Intent i) {
		i.putExtras(new Bundle());
	}

	public static Bundle copyExtras(Intent i) {
		Bundle copy = new Bundle();
		Bundle original = i.getExtras();
		if(original != null) {
			copy.putAll(original);
		}
		return copy;

	}

	public static String toString(Bundle b) {
		if(b == null) {
			return "";
		}
		
		StringBuffer buff = new StringBuffer();
		for(String key : b.keySet()) {
			final Object value = b.get(key);
			final String s = String.format("%s %s (%s)\n", key, value.toString(), value.getClass().getName());
			buff.append(s);
		}
		return buff.toString();
	}
}
