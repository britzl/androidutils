package se.springworks.android.utils.view;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

public class ViewLayoutUtils {

	/**
	 * Set the margins for a view. The view must have {@link LayoutParams} that are of the
	 * type {@link MarginLayoutParams} (or a subclass)
	 * @param v 
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public static void setMargins(View v, int left, int top, int right, int bottom) {
		LayoutParams lp = v.getLayoutParams();
		if(lp == null) {
			MarginLayoutParams mlp = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
			mlp.setMargins(left, top, right, bottom);
			v.setLayoutParams(mlp);
		}
		else if(lp instanceof MarginLayoutParams) {
			MarginLayoutParams mlp = (MarginLayoutParams)lp;
			mlp.setMargins(left, top, right, bottom);
			v.setLayoutParams(mlp);
		}
	}
	
	
	public static void setSize(View v, int width, int height) {
		LayoutParams lp = v.getLayoutParams();
		if(lp == null) {
			lp = new LayoutParams(width, height);
		}
		else if(lp != null) {
			lp.width = width;
			lp.height = height;
		}
		v.setLayoutParams(lp);
	}
}
