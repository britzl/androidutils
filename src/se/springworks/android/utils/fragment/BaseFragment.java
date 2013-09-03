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
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		GrapeGuice.getInjector(this).injectMembers(this);
		logger.debug("onCreateView()");
		return createView(inflater, container, savedInstanceState);
	}
	
	
	
	@Override
	public final void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		logger.debug("onActivityCreated()");
		GrapeGuice.getInjector(this).injectViews(this);
		fragmentReadyToUse(savedInstanceState);
	}

	/**
	 * Creates the view used by this fragment
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	
	/**
	 * Called when the fragment is ready to use. This means that:
	 * 
	 * 1. The view has been created
	 * 2. All injections have taken place
	 * 3. The fragment's activity has been created
	 * 
	 * @param savedInstanceState
	 */
	protected abstract void fragmentReadyToUse(Bundle savedInstanceState);

	@Override
	public final void onStart() {
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
	public final void onStop() {
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
	public final void onResume() {
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
	public final void onPause() {
		super.onPause();
		logger.debug("onPause()");
		try {
			pauseFragment();
		}
		catch(Exception e) {
			logger.error("onStart() start fragment threw exception", e);
		}
	}

	
	protected void startFragment() {
		// override me
	}

	protected void stopFragment() {
		// override me
	}

	protected void resumeFragment() {
		// override me
	}

	protected void pauseFragment() {
		// override me
	}
	
}
