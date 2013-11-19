package se.springworks.android.utils.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
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
		LayoutParams params = v.getLayoutParams();
		if(params == null) {
			MarginLayoutParams mlp = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
			mlp.setMargins(left, top, right, bottom);
			v.setLayoutParams(mlp);
		}
		else if(params instanceof MarginLayoutParams) {
			MarginLayoutParams lp = (MarginLayoutParams)params;
			lp.setMargins(left, top, right, bottom);
			v.setLayoutParams(lp);
		}
	}
	
	public static void setMarginsDp(View v, int leftDp, int topDp, int rightDp, int bottomDp) {
		Context ctx = v.getContext();
		if(ctx == null) {
			return;
		}
		DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
		setMargins(v, dpToPixels(dm, leftDp), dpToPixels(dm, topDp), dpToPixels(dm, rightDp), dpToPixels(dm, bottomDp));
	}
	
	private static int dpToPixels(DisplayMetrics metrics, int valueInDp) {
		return (int)(metrics.density * valueInDp + 0.5f);
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
