package com.app.mymeal.views;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.mymeal.MyMealActivity;
import com.app.mymeal.R;
import com.app.mymeal.RecipesListActivity;
import com.app.mymeal.adapters.NavDrawerListAdapter;

public class NavMenuView {

	static Context navContext;
	// slide menu items
	static String[] navMenuTitles;
	static DrawerLayout mDrawerLayout;
	static ListView mDrawerList;
	static ArrayList<NavDrawerItem> navDrawerItems;
	static NavDrawerListAdapter navAdapter;
	static ActionBarDrawerToggle mDrawerToggle;
	static CharSequence mDrawerTitle;
	static CharSequence mTitle;

	public NavMenuView(Context context) {

		navContext = context;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void displayNavMenu() {

		// load slide menu items
		navMenuTitles = navContext.getResources().getStringArray(
				R.array.nav_drawer_items);

		mDrawerLayout = (DrawerLayout) ((Activity) navContext)
				.findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) ((Activity) navContext)
				.findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		Log.e("error", "menu item: " + navMenuTitles[0]);
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		// Recipes
		Log.e("error", "menu item: " + navMenuTitles[1]);
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1]));

		// setting the nav drawer list adapter
		navAdapter = new NavDrawerListAdapter(navContext,
				R.layout.drawer_list_item, navDrawerItems);
		mDrawerList.setAdapter(navAdapter);

		mDrawerList.bringToFront();

		mDrawerLayout.requestLayout();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		mTitle = mDrawerTitle = ((Activity) navContext).getTitle();

		mDrawerToggle = new ActionBarDrawerToggle(((Activity) navContext),
				mDrawerLayout, R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
				// accessibility
				R.string.app_name // nav drawer close - description for
		// accessibility
		) {
			public void onDrawerClosed(View view) {
				Log.e("error", "onDrawerClosed");
				((Activity) navContext).getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				((Activity) navContext).invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				Log.e("error", "onDrawerOpened");
				((Activity) navContext).getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				((Activity) navContext).invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// enabling action bar app icon and behaving it as toggle button
		((Activity) navContext).getActionBar().setDisplayHomeAsUpEnabled(true);
		((Activity) navContext).getActionBar().setHomeButtonEnabled(true);

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
			// Toast.makeText(getApplicationContext(),
			// "You selected : " + "[Home] ", Toast.LENGTH_SHORT).show();

			Intent intentHome = new Intent(((Activity) navContext),
					MyMealActivity.class);
			((Activity) navContext).startActivity(intentHome);

			break;
		case 1:
			// Toast.makeText(getApplicationContext(),
			// "You selected : " + "[Recipes] ", Toast.LENGTH_SHORT)
			// .show();
			Intent intentRecipe = new Intent((Activity) navContext,
					RecipesListActivity.class);
			((Activity) navContext).startActivity(intentRecipe);

			break;

		}
	}

	public static boolean onOptionsItemSelected(MenuItem item) {
		Log.e("error", "onOptionsItemSelected");
		return mDrawerToggle.onOptionsItemSelected(item);
	}

	public static void setTitle(CharSequence title) {
		Log.e("error", "setTitle");
		mTitle = title;
		((Activity) navContext).getActionBar().setTitle(mTitle);
	}

	public static void onConfigurationChanged(Configuration newConfig) {
		Log.e("error", "onConfigurationChanged");
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public static void onPostCreateTasks(Bundle savedInstanceState) {
		Log.e("error", "onPostCreate");
		mDrawerToggle.syncState();
	}

}
