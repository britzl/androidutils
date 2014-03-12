package se.springworks.android.utils.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SimpleListAdapter<T> extends BaseAdapter {

	private List<T> listData;
	
	private int layoutId;
	
	
	public SimpleListAdapter(List<T> listData, int layoutId) {
		super();
		this.listData = listData;
		this.layoutId = layoutId;
	}
	
	public synchronized void updateListData(List<T> listData) {
		this.listData.clear();
		this.listData.addAll(listData);
//		this.listData = listData;
		notifyDataSetChanged();
	}
	
	@Override
	public synchronized int getCount() {
		return listData.size();
	}

	@Override
	public synchronized Object getItem(int position) {
		return listData.get(position);
	}
	
	public synchronized T getItemAsType(int position) {
		return (T)listData.get(position);
	}

	@Override
	public synchronized long getItemId(int position) {
		return position;
	}

	@Override
	public synchronized View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(layoutId, null);
		}
		populateView(listData.get(position), convertView);
		return convertView;
	}
	
	protected abstract void populateView(T data, View view);
}
