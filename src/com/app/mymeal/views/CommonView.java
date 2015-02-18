package com.app.mymeal.views;

import java.util.StringTokenizer;

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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class CommonView {

	private MealView breakfast;
	private MealView lunch;
	private MealView snacks;
	private MealView dinner;
	private SearchView selectedSearchView;
	private String mealType;
	private Context context;
	private MyCustomAdapter dataAdapter;

	public CommonView(Context context, MyCustomAdapter dataAdapter) {
		this.context = context;
		this.dataAdapter = dataAdapter;
	}
		
	
	public Context getContext() {
		return context;
	}



	public void setContext(Context context) {
		this.context = context;
	}



	public MyCustomAdapter getDataAdapter() {
		return dataAdapter;
	}



	public void setDataAdapter(MyCustomAdapter dataAdapter) {
		this.dataAdapter = dataAdapter;
	}



	public String getMealType() {
		return mealType;
	}

	public void setMealType(String mealType) {
		this.mealType = mealType;
	}

	public MealView getBreakfast() {
		return breakfast;
	}

	public void setBreakfast(MealView breakfast) {
		this.breakfast = breakfast;
	}

	public MealView getLunch() {
		return lunch;
	}

	public void setLunch(MealView lunch) {
		this.lunch = lunch;
	}

	public MealView getSnacks() {
		return snacks;
	}

	public void setSnacks(MealView snacks) {
		this.snacks = snacks;
	}

	public MealView getDinner() {
		return dinner;
	}

	public void setDinner(MealView dinner) {
		this.dinner = dinner;
	}

	public SearchView getSelectedSearchView() {
		return selectedSearchView;
	}

	public void setSelectedSearchView(SearchView selectedSearchView) {
		this.selectedSearchView = selectedSearchView;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void setupSearchView() {
		SearchManager searchManager = (SearchManager) ((Activity) context)
				.getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = null;
		if ("B".equals(mealType)) {
			searchView = breakfast.getSearchView();
		}
		if ("L".equals(mealType)) {
			searchView = lunch.getSearchView();
		}
		if ("S".equals(mealType)) {
			searchView = snacks.getSearchView();
		}
		if ("D".equals(mealType)) {
			searchView = dinner.getSearchView();
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

	public void clearListView() {

		Log.e("error", "Clear listview");
		
		ListView listView = null;

		if ("B".equals(mealType)) {
			listView = breakfast.getListView();
		}

		if ("L".equals(mealType)) {
			listView = lunch.getListView();
		}

		if ("S".equals(mealType)) {
			listView = snacks.getListView();
		}

		if ("D".equals(mealType)) {
			listView = dinner.getListView();
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
