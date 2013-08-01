package se.springworks.android.utils.fragment;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtil {

	
	
	public static void showSingle(FragmentActivity a, DialogFragment dialog, boolean addToBackStack) {
		FragmentManager fm = a.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment prev = fm.findFragmentByTag(dialog.getClass().getName());
		if(prev != null) {
			ft.remove(prev);
		}
		if(addToBackStack) {
			ft.addToBackStack(null);
		}
		ft.commitAllowingStateLoss();
		
		dialog.show(fm, dialog.getClass().getName());
	}
	
	
	public static void showSingle(FragmentActivity a, DialogFragment dialog) {
		showSingle(a, dialog, true);
	}
	
	public static void showSingleNoBack(FragmentActivity a, DialogFragment dialog) {
		showSingle(a, dialog, false);
	}
	
	
	public static void show(FragmentActivity a, DialogFragment dialog) {
		FragmentManager fm = a.getSupportFragmentManager();
		dialog.show(fm, dialog.getClass().getName());
	}
	

}
