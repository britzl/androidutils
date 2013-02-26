package se.springworks.android.utils.view;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SectionedAdapter<S, I> extends BaseAdapter {

	private Hashtable<S, List<I>> itemMap = new Hashtable<S, List<I>>();
	
	private List<Object> allItems = new ArrayList<Object>();
	
	public SectionedAdapter() {
		super();
	}
	
	public void addItem(S section, I item) {
		if(!itemMap.containsKey(section)) {
			itemMap.put(section, new ArrayList<I>());
		}
		List<I> items = itemMap.get(section);
		items.add(item);
		rebuildAllItems();
	}
	
	public void addItems(S section, List<I> items) {
		if(!itemMap.containsKey(section)) {
			itemMap.put(section, new ArrayList<I>());
		}
		List<I> i = itemMap.get(section);
		i.addAll(items);
		rebuildAllItems();
	}
	
	
	synchronized private void rebuildAllItems() {
		allItems.clear();
		for(S section : itemMap.keySet()) {
			allItems.add(section);
			for(I item : itemMap.get(section)) {
				allItems.add(item);
			}
		}
		notifyDataSetChanged();		
	}
	
	
	@Override
	public int getItemViewType(int position) {
		return itemMap.containsKey(allItems.get(position)) ? 0 : 1;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getCount() {
		return allItems.size();
	}

	@Override
	public Object getItem(int position) {
		return allItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		boolean isSection = getItemViewType(position) == 0;
		Object o = getItem(position);
		if(isSection) {
			convertView = getSectionView((S)o, convertView, parent);
		}
		else {
			convertView = getItemView((I)o, convertView, parent);
		}
		return convertView;
	}
	
	public abstract View getSectionView(S section, View convertView, ViewGroup parent);

	public abstract View getItemView(I item, View convertView, ViewGroup parent);
}
