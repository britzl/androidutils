package se.springworks.android.utils.view;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Pair;

public class SectionedFragmentPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

	private List<Pair<T, String>> fragments = new ArrayList<Pair<T, String>>();
	
	public SectionedFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public void add(T f, String title) {
		fragments.add(new Pair<T, String>(f, title));
	}
	
	@Override
	public Fragment getItem(int position) {
		return fragments.get(position).first;
	}
	
	//http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view/7287121#7287121
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}


	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return fragments.get(position).second;
	}
}