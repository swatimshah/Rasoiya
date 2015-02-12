package com.app.mymeal.views;

import java.util.StringTokenizer;

import com.app.mymeal.R;
import com.app.mymeal.adapters.MyCustomAdapter;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class CommonView {

	private ImageView breakfastSearch;
	private ImageView lunchSearch;
	private ImageView snacksSearch;
	private ImageView dinnerSearch;
	private SearchView selectedSearchView;
	private String mealType;

	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public SearchView getSelectedSearchView() {
		return selectedSearchView;
	}

	public void setSelectedSearchView(SearchView selectedSearchView) {
		this.selectedSearchView = selectedSearchView;
	}

	public ImageView getBreakfastSearch() {
		return breakfastSearch;
	}

	public void setBreakfastSearch(ImageView breakfastSearch) {
		this.breakfastSearch = breakfastSearch;
	}

	public ImageView getLunchSearch() {
		return lunchSearch;
	}

	public void setLunchSearch(ImageView lunchSearch) {
		this.lunchSearch = lunchSearch;
	}

	public ImageView getSnacksSearch() {
		return snacksSearch;
	}

	public void setSnacksSearch(ImageView snacksSearch) {
		this.snacksSearch = snacksSearch;
	}

	public ImageView getDinnerSearch() {
		return dinnerSearch;
	}

	public void setDinnerSearch(ImageView dinnerSearch) {
		this.dinnerSearch = dinnerSearch;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void setupSearchView(String aMealType, Context context) {
		SearchManager searchManager = (SearchManager) ((Activity) context)
				.getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = null;
		if ("B".equals(aMealType)) {
			searchView = (SearchView) ((Activity) context)
					.findViewById(R.id.searchView);
		}
		if ("L".equals(aMealType)) {
			searchView = (SearchView) ((Activity) context)
					.findViewById(R.id.searchViewLunch);
		}
		if ("S".equals(aMealType)) {
			searchView = (SearchView) ((Activity) context)
					.findViewById(R.id.searchViewSnacks);
		}
		if ("D".equals(aMealType)) {
			searchView = (SearchView) ((Activity) context)
					.findViewById(R.id.searchViewDinner);
		}

		SearchableInfo searchableInfo = searchManager
				.getSearchableInfo(((Activity) context).getComponentName());
		searchView.setSearchableInfo(searchableInfo);
		searchView.setIconifiedByDefault(false); // Do not iconify the widget;
													// expand it by default
		searchView.setQuery("", true);
		searchView.setFocusable(true);
		searchView.requestFocusFromTouch();
		searchView.setQueryRefinementEnabled(true);

		int id = searchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) searchView.findViewById(id);
		textView.setTextSize(15);

		setSelectedSearchView(searchView);
	}

	public String convertIntoValidQuery(String query) {
		// TODO Auto-generated method stub

		String validQuery = "";

		if (query != null) {

			StringTokenizer tokenizer = new StringTokenizer(query, ",");

			while (tokenizer.hasMoreTokens()) {
				validQuery = validQuery + tokenizer.nextToken().trim() + ", ";
			}

		} else {
			query = "";
		}

		return validQuery.trim();
	}

	public void clearListView(String aMealType, Context context,
			final MyCustomAdapter dataAdapter) {

		Log.e("error", "Clear listview");
		
		ListView listView = null;

		if ("B".equals(aMealType)) {
			listView = (ListView) ((Activity) context)
					.findViewById(R.id.listView1);
		}

		if ("L".equals(aMealType)) {
			listView = (ListView) ((Activity) context)
					.findViewById(R.id.listView1Lunch);
		}

		if ("S".equals(aMealType)) {
			listView = (ListView) ((Activity) context)
					.findViewById(R.id.listView1Snacks);
		}

		if ("D".equals(aMealType)) {
			listView = (ListView) ((Activity) context)
					.findViewById(R.id.listView1Dinner);
		}

		// Assign adapter to ListView
		//dataAdapter.close();
		
		((Activity) context).runOnUiThread(new Runnable() {
			public void run() {
				dataAdapter.clear();
				dataAdapter.notifyDataSetChanged();
			}
		});
		
		
		listView.setAdapter(dataAdapter);
		
		setListViewHeightBasedOnChildren(listView);

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.AT_MOST);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (i == 0)
				view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
						LayoutParams.WRAP_CONTENT));

			view.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		Log.e("error: ", "params height:" + params.height);
		Log.e("error: ", "count:" + listAdapter.getCount());
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

}
