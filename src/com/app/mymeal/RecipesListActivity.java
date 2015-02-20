package com.app.mymeal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.app.mymeal.adapters.RecipeHolder;
import com.app.mymeal.adapters.RecipeListAdapter;
import com.app.mymeal.data.Meal;
import com.app.mymeal.persistence.DataBaseHelper;
import com.app.mymeal.views.NavMenuView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
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

	RecipeListAdapter dataAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipes_list);

		ArrayList<Meal> recipesList = doMySearch();

		// create an ArrayAdaptar from the String Array
		dataAdapter = new RecipeListAdapter(getApplicationContext(),
				R.layout.recipes_item, recipesList);

		displayListView();

		NavMenuView navMenu = new NavMenuView(this);
		navMenu.displayNavMenu();

	}

	private ArrayList<Meal> doMySearch() {
		// TODO Auto-generated method stub
		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		myDbHelper = new DataBaseHelper(this);
		myDbHelper.openDataBase();

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
				intent.putExtra("recipeName",
						((RecipeHolder) view.getTag()).name.getText()
								.toString());
				intent.putExtra("action", "View");

				startActivity(intent);

			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		ArrayList<Meal> recipesList = doMySearch();

		// create an ArrayAdaptar from the String Array
		dataAdapter = new RecipeListAdapter(this, R.layout.recipes_item,
				recipesList);

		displayListView();

		NavMenuView navMenu = new NavMenuView(this);
		navMenu.displayNavMenu();

	}

	private void displayView(int position) {

		switch (position) {
		case 0:
			// Toast.makeText(getApplicationContext(),
			// "You selected : " + "[Home] ", Toast.LENGTH_SHORT).show();

			Intent intentHome = new Intent(getApplicationContext(),
					MyMealActivity.class);
			startActivity(intentHome);

			break;
		case 1:
			// Toast.makeText(getApplicationContext(),
			// "You selected : " + "[Recipes] ", Toast.LENGTH_SHORT)
			// .show();
			Intent intentRecipe = new Intent(getApplicationContext(),
					RecipesListActivity.class);
			startActivity(intentRecipe);

			break;

		}
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
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("error", "onOptionsItemSelected");
		if (NavMenuView.onOptionsItemSelected(item))
			return true;
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setTitle(CharSequence title) {
		Log.e("error", "setTitle");
		NavMenuView.setTitle(title);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		Log.e("error", "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
		NavMenuView.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		Log.e("error", "onPostCreate");
		super.onPostCreate(savedInstanceState);
		NavMenuView.onPostCreateTasks(savedInstanceState);
	}

}
