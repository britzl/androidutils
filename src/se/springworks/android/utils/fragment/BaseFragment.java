package se.springworks.android.utils.fragment;

import se.springworks.android.utils.inject.GrapeGuice;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	@InjectLogger
	private Logger logger;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		GrapeGuice.getInjector(this).injectMembers(this);
		logger.debug("onCreateView()");
		return createView(inflater, container, savedInstanceState);
	}
	
	
	protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		logger.debug("onActivityCreated()");
		GrapeGuice.getInjector(this).injectViews(this);
		fragmentReadyToUse(savedInstanceState);
	}
	
	protected abstract void fragmentReadyToUse(Bundle savedInstanceState);

	@Override
	public void onStart() {
		super.onStart();
		logger.debug("onStart()");
		try {
			startFragment();
		}
		catch(Exception e) {
			logger.error("onStart() start fragment threw exception", e);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		logger.debug("onStop()");
		try {
			stopFragment();
		}
		catch(Exception e) {
			logger.error("onStart() start fragment threw exception", e);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		logger.debug("onResume()");
		try {
			resumeFragment();
		}
		catch(Exception e) {
			logger.error("onStart() start fragment threw exception", e);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		logger.debug("onPause()");
		try {
			pauseFragment();
		}
		catch(Exception e) {
			logger.error("onStart() start fragment threw exception", e);
		}
	}

	
	protected abstract void startFragment();
	protected abstract void stopFragment();
	protected abstract void resumeFragment();
	protected abstract void pauseFragment();
	
}
