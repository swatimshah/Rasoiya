package com.app.mymeal.adapters;

import java.util.ArrayList;

import com.app.mymeal.R;
import com.app.mymeal.views.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerItem> {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	public NavDrawerListAdapter(Context context, int textViewResourceId,
			ArrayList<NavDrawerItem> navDrawerItems) {
		super(context, textViewResourceId, navDrawerItems);
		Log.e("error", "constructor");
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		Log.e("error", "getCount" + navDrawerItems.size());
		return navDrawerItems.size();
	}

	@Override
	public NavDrawerItem getItem(int position) {
		Log.e("error", "getItem" + navDrawerItems.get(position));
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		Log.e("error", "getItemId" + position);
		return position;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.e("error", "Calling getView");

		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
		}

		TextView txtTitle = (TextView) convertView
				.findViewById(R.id.nav_item_title);
		Log.e("error", "Nav Item: " + navDrawerItems.get(position).getTitle());
		txtTitle.setText(navDrawerItems.get(position).getTitle());

		return convertView;
	}

}