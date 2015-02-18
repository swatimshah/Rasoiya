package com.app.mymeal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.app.mymeal.adapters.NavDrawerListAdapter;
import com.app.mymeal.data.Meal;
import com.app.mymeal.persistence.DataBaseHelper;
import com.app.mymeal.views.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RecipesListActivity extends Activity {

	MyCustomAdapter dataAdapter = null;

	// slide menu items
	private String[] navMenuTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter navAdapter;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipes_list);

		ArrayList<Meal> recipesList = doMySearch();

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(getApplicationContext(),
				R.layout.recipes_item, recipesList);

		displayListView();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		Log.e("error", "menu item: " + navMenuTitles[0]);
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		// Recipes
		Log.e("error", "menu item: " + navMenuTitles[1]);
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));

		// setting the nav drawer list adapter
		navAdapter = new NavDrawerListAdapter(this, R.layout.drawer_list_item,
				navDrawerItems);
		mDrawerList.setAdapter(navAdapter);

		mDrawerList.bringToFront();

		mDrawerLayout.requestLayout();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		mTitle = mDrawerTitle = getTitle();

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
				// accessibility
				R.string.app_name // nav drawer close - description for
		// accessibility
		) {
			public void onDrawerClosed(View view) {
				Log.e("error", "onDrawerClosed");
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				Log.e("error", "onDrawerOpened");
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}

	private ArrayList<Meal> doMySearch() {
		// TODO Auto-generated method stub
		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		myDbHelper = new DataBaseHelper(this);
		myDbHelper.openDataBase();

		// SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//
		// dd/MM/yyyy
		// Date today = new Date();
		// String strDate = sdfDate.format(today);

		return myDbHelper.search();
	}

	private void displayListView() {

		ListView listView = (ListView) findViewById(R.id.listView);

		listView.setAdapter(dataAdapter);

		// listView.setOnClickListener(recipeListener);

		listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(view.getContext(),
						RecipeEditActivity.class);
				Log.e("error", "Recipe in list");
				intent.putExtra("recipeName", ((ViewHolder) view.getTag()).name
						.getText().toString());
				intent.putExtra("action", "View");

				startActivity(intent);

			}
		});

		// setListViewHeightBasedOnChildren(listView);

		// listView.setOnTouchListener(new OnTouchListener() {
		// // Setting on Touch Listener for handling the touch inside
		// // ScrollView
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // Disallow the touch request for parent scroll on touch of
		// // child view
		// v.getParent().requestDisallowInterceptTouchEvent(false);
		// return false;
		// }
		// });

	}

	class ViewHolder {
		TextView name;
		TextView date;
	}

	public class MyCustomAdapter extends ArrayAdapter<Meal> {

		private ArrayList<Meal> recipesList;

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<Meal> aRecipesList) {
			super(context, textViewResourceId, aRecipesList);
			this.recipesList = new ArrayList<Meal>();
			this.recipesList.addAll(aRecipesList);

			Log.e("error", "recipesList size in custom adapter: "
					+ this.recipesList.size());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			Log.e("error", "ConvertView" + String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.recipes_item, null);

				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.date = (TextView) convertView.findViewById(R.id.date);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			final Meal meal = recipesList.get(position);
			SpannableString mealNameSpannable = new SpannableString(
					meal.getName());
			mealNameSpannable.setSpan(new UnderlineSpan(), 0,
					mealNameSpannable.length(), 0);

			holder.name.setText(mealNameSpannable);

			// Start - Set the difference between dates
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date unformattedlastPrepDate = null;
			Date today = new Date();
			try {
				unformattedlastPrepDate = sdf.parse(meal.getLastPrepDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (unformattedlastPrepDate != null) {
				
				long difference = today.getTime()
						- unformattedlastPrepDate.getTime();

				long days = difference / (1000 * 60 * 60 * 24);

				holder.date.setText(days + " days ago");
				
			} else {
				
				holder.date.setText("never");
			}
			// End - set the difference between dates

			Log.e("error: holder: name", holder.name.getText().toString());
			Log.e("error: holder: date", holder.date.getText().toString());

			holder.name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(v.getContext(),
							RecipeEditActivity.class);
					intent.putExtra("recipeName", meal.getName());
					intent.putExtra("action", "View");

					startActivity(intent);
				}
			});

			return convertView;
		}

		public int getCount() {

			return recipesList.size();
		}

	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;

		int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(),
				MeasureSpec.UNSPECIFIED);
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

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		ArrayList<Meal> recipesList = doMySearch();

		// create an ArrayAdaptar from the String Array
		dataAdapter = new MyCustomAdapter(getApplicationContext(),
				R.layout.recipes_item, recipesList);

		displayListView();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		Log.e("error", "menu item: " + navMenuTitles[0]);
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		// Recipes
		Log.e("error", "menu item: " + navMenuTitles[1]);
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));

		// setting the nav drawer list adapter
		navAdapter = new NavDrawerListAdapter(this, R.layout.drawer_list_item,
				navDrawerItems);
		mDrawerList.setAdapter(navAdapter);

		mDrawerList.bringToFront();

		mDrawerLayout.requestLayout();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		mTitle = mDrawerTitle = getTitle();

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
				// accessibility
				R.string.app_name // nav drawer close - description for
		// accessibility
		) {
			public void onDrawerClosed(View view) {
				Log.e("error", "onDrawerClosed");
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				Log.e("error", "onDrawerOpened");
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.e("error", "SlideMenuClickListener.onItemClick");
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	private void displayView(int position) {

		switch (position) {
		case 0:
//			Toast.makeText(getApplicationContext(),
//					"You selected : " + "[Home] ", Toast.LENGTH_SHORT).show();

			Intent intentHome = new Intent(getApplicationContext(),
					MyMealActivity.class);
			startActivity(intentHome);

			break;
		case 1:
//			Toast.makeText(getApplicationContext(),
//					"You selected : " + "[Recipes] ", Toast.LENGTH_SHORT)
//					.show();
			Intent intentRecipe = new Intent(getApplicationContext(),
					RecipesListActivity.class);
			startActivity(intentRecipe);

			break;

		}
	}

	@Override
	public void setTitle(CharSequence title) {
		Log.e("error", "setTitle");
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		Log.e("error", "onPrepareOptionsMenu");
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		Log.e("error", "onPostCreate");
		super.onPostCreate(savedInstanceState);

		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.e("error", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("error", "onOptionsItemSelected");
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

}
