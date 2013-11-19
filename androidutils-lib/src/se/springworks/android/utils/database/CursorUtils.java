package se.springworks.android.utils.database;

import android.database.Cursor;

public class CursorUtils {

	public interface CursorSeeker {
		public boolean found(Cursor c);
	}
	
	
	public static boolean moveTo(Cursor c, CursorSeeker seeker) {
		if(c == null) {
			return false;
		}
		c.moveToPosition(-1);
		while(c.moveToNext()) {
			if(seeker.found(c)) {
				return true;
			}
		}
		return false;
	}
	
	public static int findPosition(Cursor c, int columnIndex, int value) {
		if(c == null || columnIndex < 0) {
			return -1;
		}
		while(c.moveToNext()) {
			try {
				if(c.getInt(columnIndex) == value) {
					return c.getPosition();
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1;	
	}
	
	public static int findPosition(Cursor c, int columnIndex, long value) {
		if(c == null || columnIndex < 0) {
			return -1;
		}
		while(c.moveToNext()) {
			try {
				if(c.getLong(columnIndex) == value) {
					return c.getPosition();
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1;		
	}
	
	public static int findPosition(Cursor c, int columnIndex, String value) {
		if(c == null || columnIndex < 0) {
			return -1;
		}
		while(c.moveToNext()) {
			try {
				if(c.getString(columnIndex).equals(value)) {
					return c.getPosition();
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
		
	}
	
}
