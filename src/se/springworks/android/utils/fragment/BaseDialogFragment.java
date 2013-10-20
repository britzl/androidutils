package se.springworks.android.utils.fragment;

import se.springworks.android.utils.inject.GrapeGuice;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseDialogFragment extends DialogFragment {


	@InjectLogger
	private Logger logger;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		GrapeGuice.getInjector(this).injectMembers(this);
		logger.debug("onCreateView()");
		return createView(inflater, container);
	}
	
	@Override
	public final void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		logger.debug("onActivityCreated()");
		GrapeGuice.getInjector(this).injectViews(this);
		fragmentReadyToUse(savedInstanceState);
	}

	abstract protected void fragmentReadyToUse(Bundle savedInstanceState);

	public abstract View createView(LayoutInflater inflater, ViewGroup container);

	public void setTitle(String title) {
		Dialog d = getDialog();
		if(d != null) {
			d.setTitle(title);
		}
	}
	
	public void setTitle(int titleId) {
		setTitle(getString(titleId));
	}
	
	@Override
	public void dismissAllowingStateLoss() {
		if(getDialog() == null) {
			return;
		}
		super.dismissAllowingStateLoss();
	}
}
