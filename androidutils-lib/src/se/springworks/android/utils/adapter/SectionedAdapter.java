package se.springworks.android.utils.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import se.springworks.android.utils.logging.Logger;
import se.springworks.android.utils.logging.LoggerFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * An adapter with support for sections/segments. You can add single items and
 * collections of items to a section. The order in which sections are created is
 * preserved
 * @author bjornritzl
 *
 * @param <S> Type of sections
 * @param <I> Type of items in sections
 */
public abstract class SectionedAdapter<S, I> extends BaseAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(SectionedAdapter.class);

	private LinkedHashMap<S, List<I>> itemMap = new LinkedHashMap<S, List<I>>();
	
	private List<Object> allItems = new ArrayList<Object>();
	
	public SectionedAdapter() {
		super();
	}
	
	/**
	 * Adds a new empty section. If the section already exist nothing happens
	 * @param section
	 */
	public void addSection(S section) {
		if(!itemMap.containsKey(section)) {
			itemMap.put(section, new ArrayList<I>());
		}
		rebuildAllItems();
	}
	
	/**
	 * Adds an item to a section. If the section doesn't exist it is created
	 * @param section
	 * @param item
	 */
	public void addItem(S section, I item) {
		if(!itemMap.containsKey(section)) {
			itemMap.put(section, new ArrayList<I>());
		}
		List<I> items = itemMap.get(section);
		items.add(item);
		rebuildAllItems();
	}
	
	/**
	 * Adds a collection of items to a section. If the section doesn't exist it is created
	 * @param section
	 * @param items
	 */
	public void addItems(S section, Collection<I> items) {
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
	
	@Override
	public boolean isEnabled(int position) {
		return !isSection(position);
	}
	
	public boolean isSection(int position) {
		return getItemViewType(position) == 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Object o = getItem(position);
		logger.debug("getView() " + o);
		if(isSection(position)) {
			convertView = getSectionView((S)o, convertView, parent);
		}
		else {
			convertView = getItemView((I)o, convertView, parent);
		}
		return convertView;
	}
	
	/**
	 * Creates a view to represent a section
	 * @param section The section to create a view for
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public abstract View getSectionView(S section, View convertView, ViewGroup parent);

	/**
	 * Creates a view to represent an item
	 * @param item The item to create a view for
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public abstract View getItemView(I item, View convertView, ViewGroup parent);
}
