package se.springworks.android.utils.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;

/**
 * This ViewPager subclass greatly simplify the way you work with a view pager and
 * actionbar tabs. Create an instance of this class, pass it an actionbar and start
 * adding fragments. Each added fragment results in a new tab and all tab and page
 * navigation is hooked up and handled automatically.
 * @author bjornritzl
 *
 */
public class FragmentViewPager extends ViewPager implements TabListener {
	
	public static interface Listener {
		void onTabSelected(String title);
	}

	private ActionBar actionBar;
	private SectionedFragmentPagerAdapter<Fragment> adapter;
	
	private Listener listener;
	
	public FragmentViewPager(Context context) {
		super(context);
	}
	
	public FragmentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setListener(Listener l) {
		this.listener = l;
	}
	
	public void init(final ActionBar actionBar, FragmentManager fragmentManager) {
		this.actionBar = actionBar;
		adapter = new SectionedFragmentPagerAdapter<Fragment>(fragmentManager);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		setAdapter(adapter);
		setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});
	}
	
	public void addFragment(Fragment fragment, String title) {
		actionBar.addTab(actionBar.newTab().setText(title).setTabListener(this));
		adapter.add(fragment, title);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		setCurrentItem(tab.getPosition());
		if(listener != null) listener.onTabSelected(tab.getText().toString());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub		
	}
}
