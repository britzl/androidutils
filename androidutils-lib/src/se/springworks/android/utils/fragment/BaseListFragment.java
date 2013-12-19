package se.springworks.android.utils.fragment;

import se.springworks.android.utils.inject.GrapeGuice;
import se.springworks.android.utils.inject.annotation.InjectLogger;
import se.springworks.android.utils.logging.Logger;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Removes a bit of boilerplate code when working with list fragments
 * @author bjornritzl
 *
 */
public abstract class BaseListFragment extends ListFragment {

	@InjectLogger
	private Logger logger;
	
	protected BaseAdapter adapter;
	
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
		
		addHeadersAndFooters(getListView());
		
		GrapeGuice.getInjector(this).injectViews(this);

		adapter = createAdapter();
		if(adapter != null) {
			setListAdapter(adapter);
		}
		
		fragmentReadyToUse(savedInstanceState);
	}

	/**
	 * Add headers and footers to the listview
	 * Note that, prior to API level 19, it isn't possible to add headers and footers
	 * after the adapter has been set. This method provides a point before the adapter
	 * has been set where the headers and footers can be set.
	 * @param listView
	 */
	protected void addHeadersAndFooters(ListView listView) {
		// override if you want to add headers and/or footers
	}
	
	/**
	 * Creates the list adapter used by this fragment
	 * @return
	 */
	protected abstract BaseAdapter createAdapter();
	
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
