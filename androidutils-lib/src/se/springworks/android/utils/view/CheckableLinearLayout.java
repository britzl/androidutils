package se.springworks.android.utils.view;

import java.util.ArrayList;
import java.util.List;

import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.logging.LoggerFactory;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

	private static Logger logger = LoggerFactory.getLogger();
	
	private boolean checked = false;
	private List<Checkable> checkableViews;
	
	public CheckableLinearLayout(Context context) {
		super(context);
		this.checkableViews = new ArrayList<Checkable>(5);
	}
	
	public CheckableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.checkableViews = new ArrayList<Checkable>(5);
	}

	/**
	 * Add to our checkable list all the children of the view that implement the
	 * interface Checkable
	 */
	private void findCheckableChildren(View v) {
		if (v instanceof Checkable) {
			this.checkableViews.add((Checkable) v);
		}

		if (v instanceof ViewGroup) {
			final ViewGroup vg = (ViewGroup) v;
			final int childCount = vg.getChildCount();
			for (int i = 0; i < childCount; ++i) {
				findCheckableChildren(vg.getChildAt(i));
			}
		}
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		final int childCount = this.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			findCheckableChildren(this.getChildAt(i));
		}
	}
	
	@Override
	public void setChecked(boolean checked) {
		logger.debug("setChecked %b", checked);
		this.checked = checked;
		for (Checkable c : checkableViews) {
			c.setChecked(checked);
		}
	}

	@Override
	public boolean isChecked() {
		logger.debug("isChecked %b", checked);
		return checked;
	}

	@Override
	public void toggle() {
		logger.debug("toggle %b", checked);
		checked = !checked;
		for (Checkable c : checkableViews) {
			c.toggle();
		}
	}
}
