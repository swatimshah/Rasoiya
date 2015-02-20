package com.app.mymeal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.app.mymeal.adapters.CalendarAdapter;
import com.app.mymeal.data.Recipe;
import com.app.mymeal.persistence.DataBaseHelper;
import com.app.mymeal.views.NavMenuView;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MyMealActivity extends Activity implements OnGestureListener {

	public Calendar myCalendar;
	public CalendarAdapter adapter;
	public ArrayList<String> items; // container to store some random calendar
									// items
	public Handler handler;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private GestureDetector gestureScanner;

	SharedPreferences mPrefs;

	/** An array of strings to populate dropdown list */
	String[] meal = new String[] { "This Month", "Coming 3 weeks",
			"Coming 2 weeks", "Coming week" };


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gestureScanner = new GestureDetector(this, this);
		setContentView(R.layout.my_meal);


		/** Create an array adapter to populate dropdownlist */
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				getActionBar().getThemedContext(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, meal);


		/** Defining Navigation listener */
		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				Toast.makeText(getApplicationContext(),
						"You selected : " + meal[itemPosition],
						Toast.LENGTH_SHORT).show();
				return false;
			}
		};


		myCalendar = Calendar.getInstance();
		adapter = new CalendarAdapter(this, myCalendar);
		items = new ArrayList<String>();
		handler = new Handler();
		handler.post(calendarUpdater);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy",
				myCalendar));

		TextView previous = (TextView) findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myCalendar.get(Calendar.MONTH) == myCalendar
						.getActualMinimum(Calendar.MONTH)) {
					myCalendar.set((myCalendar.get(Calendar.YEAR) - 1),
							myCalendar.getActualMaximum(Calendar.MONTH), 1);
				} else {
					myCalendar.set(Calendar.MONTH,
							myCalendar.get(Calendar.MONTH) - 1);
				}
				refreshCalendar();
			}
		});

		TextView next = (TextView) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myCalendar.get(Calendar.MONTH) == myCalendar
						.getActualMaximum(Calendar.MONTH)) {
					myCalendar.set((myCalendar.get(Calendar.YEAR) + 1),
							myCalendar.getActualMinimum(Calendar.MONTH), 1);
				} else {
					myCalendar.set(Calendar.MONTH,
							myCalendar.get(Calendar.MONTH) + 1);
				}
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				TextView date = (TextView) v.findViewById(R.id.date);
				ImageView image = (ImageView) v.findViewById(R.id.date_icon);

				if (date instanceof TextView && !date.getText().equals("")) {

					String day = date.getText().toString();
					if (day.length() == 1) {
						day = "0" + day;
					}

					if (image instanceof ImageView
							&& image.getVisibility() == image.VISIBLE) {


						Intent intent = new Intent(v.getContext(),
								MealEditActivity.class);

						intent.putExtra(
								"date",
								android.text.format.DateFormat.format(
										"yyyy-MM", myCalendar) + "-" + day);

						intent.putExtra("activity", "MyMealActivity");

						startActivity(intent);

					} else {

						Intent intent = new Intent(v.getContext(),
								MealEditActivity.class);

						intent.putExtra(
								"date",
								android.text.format.DateFormat.format(
										"yyyy-MM", myCalendar) + "-" + day);

						intent.putExtra("activity", "MyMealActivity");

						startActivity(intent);

					}

					// finish();
				}

			}

		});

		gridview.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent me) {
				gestureScanner.onTouchEvent(me);
				return false;
			}

		});

		Context mContext = this.getApplicationContext();
		// 0 = mode private. only this app can read these preferences
		mPrefs = mContext.getSharedPreferences("myAppPrefs", 0);

		if (getFirstRun()) {

			Log.e("error: ", "first run");
			// your code for first run goes here
			DataBaseHelper myDbHelper = new DataBaseHelper(this);
			myDbHelper = new DataBaseHelper(this);

			try {

				myDbHelper.createDataBase();
				myDbHelper.openDataBase();
				myDbHelper.dropTables();
				myDbHelper.createTables();
				Log.e("error", "Tables created..");
				populateRecipes();
				Log.e("error", "Recipes populated..");
				// This is first run
				setRunned();

			} catch (IOException ioe) {

				Log.e("error", "Unable to create database");

			}

		} else {
			// this is the case other than first run
		}

		NavMenuView navMenu = new NavMenuView(this);
		navMenu.displayNavMenu();

	}

	OnClickListener demoButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent demoIntent = new Intent(getApplicationContext(),
					DemoActivity.class);
			startActivity(demoIntent);
		}
	};

	public void refreshCalendar() {
		TextView title = (TextView) findViewById(R.id.title);

		adapter.refreshDays();
		adapter.notifyDataSetChanged();
		handler.post(calendarUpdater); // generate some random calendar items

		title.setText(android.text.format.DateFormat.format("MMMM yyyy",
				myCalendar));
	}

	public Runnable calendarUpdater = new Runnable() {

		@Override
		public void run() {
			items.clear();
			// format random values. You can implement a dedicated class to
			// provide real values

			String expandedLastDay = "";
			String expandedMonth = "";

			String lastDay = Integer.valueOf(
					myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH))
					.toString();
			if (lastDay.length() == 1)
				expandedLastDay = "0" + lastDay;
			else
				expandedLastDay = lastDay;
			String firstDay = "01";
			String month = Integer.valueOf(myCalendar.get(Calendar.MONTH) + 1)
					.toString();
			if (month.length() == 1)
				expandedMonth = "0" + month;
			else
				expandedMonth = month;
			int year = myCalendar.get(Calendar.YEAR);
			String firstDayOfMonth = year + "-" + expandedMonth + "-"
					+ firstDay;
			String lastDayOfMonth = year + "-" + expandedMonth + "-"
					+ expandedLastDay;
			Log.e("error", firstDayOfMonth);
			Log.e("error", lastDayOfMonth);

			DataBaseHelper myDbHelper = new DataBaseHelper(MyMealActivity.this);
			myDbHelper = new DataBaseHelper(MyMealActivity.this);
			myDbHelper.openDataBase();
			items = myDbHelper.getCalendarItemsWithMealEntry(firstDayOfMonth,
					lastDayOfMonth);


			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.e("error", "onTouch event occurred...");
		return gestureScanner.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.e("error", "onDown event occurred...");

		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		Log.e("error", "onFling");

		// TODO Auto-generated method stub
		try {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;
			// right to left swipe
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

				leftScroll();

			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				rightScroll();
			}
		} catch (Exception e) {
			Log.e("Exception", e.getMessage());
		}
		return true;
	}

	private void rightScroll() {
		// TODO Auto-generated method stub
		if (myCalendar.get(Calendar.MONTH) == myCalendar
				.getActualMinimum(Calendar.MONTH)) {
			myCalendar.set((myCalendar.get(Calendar.YEAR) - 1),
					myCalendar.getActualMaximum(Calendar.MONTH), 1);
		} else {
			myCalendar.set(Calendar.MONTH, myCalendar.get(Calendar.MONTH) - 1);
		}
		refreshCalendar();
	}

	private void leftScroll() {
		// TODO Auto-generated method stub
		if (myCalendar.get(Calendar.MONTH) == myCalendar
				.getActualMaximum(Calendar.MONTH)) {
			myCalendar.set((myCalendar.get(Calendar.YEAR) + 1),
					myCalendar.getActualMinimum(Calendar.MONTH), 1);
		} else {
			myCalendar.set(Calendar.MONTH, myCalendar.get(Calendar.MONTH) + 1);
		}
		refreshCalendar();
	}

	public boolean getFirstRun() {
		return mPrefs.getBoolean("recipeimporttesting36", true);
	}

	public void setRunned() {
		SharedPreferences.Editor edit = mPrefs.edit();
		edit.putBoolean("recipeimporttesting36", false);
		edit.commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		// put your code here...
		gestureScanner = new GestureDetector(this, this);
		setContentView(R.layout.my_meal);

		/** Create an array adapter to populate dropdownlist */
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				getActionBar().getThemedContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1, meal);


		/** Defining Navigation listener */
		ActionBar.OnNavigationListener navigationListener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition,
					long itemId) {
				Toast.makeText(getApplicationContext(),
						"You selected : " + meal[itemPosition],
						Toast.LENGTH_SHORT).show();
				return false;
			}
		};


		myCalendar = Calendar.getInstance();
		adapter = new CalendarAdapter(this, myCalendar);
		items = new ArrayList<String>();
		handler = new Handler();
		handler.post(calendarUpdater);

		GridView gridview = (GridView) findViewById(R.id.gridview);
		gridview.setAdapter(adapter);

		TextView title = (TextView) findViewById(R.id.title);
		title.setText(android.text.format.DateFormat.format("MMMM yyyy",
				myCalendar));

		TextView previous = (TextView) findViewById(R.id.previous);
		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myCalendar.get(Calendar.MONTH) == myCalendar
						.getActualMinimum(Calendar.MONTH)) {
					myCalendar.set((myCalendar.get(Calendar.YEAR) - 1),
							myCalendar.getActualMaximum(Calendar.MONTH), 1);
				} else {
					myCalendar.set(Calendar.MONTH,
							myCalendar.get(Calendar.MONTH) - 1);
				}
				refreshCalendar();
			}
		});

		TextView next = (TextView) findViewById(R.id.next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (myCalendar.get(Calendar.MONTH) == myCalendar
						.getActualMaximum(Calendar.MONTH)) {
					myCalendar.set((myCalendar.get(Calendar.YEAR) + 1),
							myCalendar.getActualMinimum(Calendar.MONTH), 1);
				} else {
					myCalendar.set(Calendar.MONTH,
							myCalendar.get(Calendar.MONTH) + 1);
				}
				refreshCalendar();

			}
		});

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				TextView date = (TextView) v.findViewById(R.id.date);
				ImageView image = (ImageView) v.findViewById(R.id.date_icon);

				if (date instanceof TextView && !date.getText().equals("")) {

					String day = date.getText().toString();
					if (day.length() == 1) {
						day = "0" + day;
					}

					if (image instanceof ImageView
							&& image.getVisibility() == image.VISIBLE) {


						Intent intent = new Intent(v.getContext(),
								MealEditActivity.class);

						intent.putExtra(
								"date",
								android.text.format.DateFormat.format(
										"yyyy-MM", myCalendar) + "-" + day);

						intent.putExtra("activity", "MyMealActivity");

						startActivity(intent);

					} else {

						Intent intent = new Intent(v.getContext(),
								MealEditActivity.class);

						intent.putExtra(
								"date",
								android.text.format.DateFormat.format(
										"yyyy-MM", myCalendar) + "-" + day);

						intent.putExtra("activity", "MyMealActivity");

						startActivity(intent);

					}

					// finish();
				}

			}

		});

		gridview.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent me) {
				gestureScanner.onTouchEvent(me);
				return false;
			}

		});

		Context mContext = this.getApplicationContext();
		// 0 = mode private. only this app can read these preferences
		mPrefs = mContext.getSharedPreferences("myAppPrefs", 0);

		if (getFirstRun()) {

			Log.e("error: ", "first run");
			// your code for first run goes here
			DataBaseHelper myDbHelper = new DataBaseHelper(this);
			myDbHelper = new DataBaseHelper(this);

			try {

				myDbHelper.createDataBase();
				myDbHelper.openDataBase();
				myDbHelper.dropTables();
				myDbHelper.createTables();

				// This is first run
				setRunned();

			} catch (IOException ioe) {

				Log.e("error", "Unable to create database");

			}

		} else {
			// this is the case other than first run
		}

		NavMenuView navMenu = new NavMenuView(this);
		navMenu.displayNavMenu();

	}

	private void populateRecipes() {

		Log.e("error", "Populating Recipes: ");

		XmlResourceParser xrp1 = getApplicationContext().getResources().getXml(
				R.xml.recipes_import);

		Log.e("error", "Imported recipes: ");

		try {
			xrp1.next();
			Log.e("error", "getName" + xrp1.getName());
			int eventType = xrp1.getEventType();
			Log.e("error", "getEventType" + xrp1.getEventType());
			xrp1.next();
			Log.e("error", "getName" + xrp1.getName());
			eventType = xrp1.getEventType();
			Log.e("error", "getEventType" + xrp1.getEventType());

			while (eventType != XmlPullParser.END_DOCUMENT) {
				Log.e("error", "next tag" + xrp1.getName());
				if (xrp1.getName() == null)
					break;
				if (eventType == XmlPullParser.START_TAG
						&& xrp1.getName().equalsIgnoreCase("recipe")) {
					String recipe = xrp1.nextText();
					Log.e("error", "recipe from xml: " + recipe);
					int resource = this.getResources().getIdentifier(recipe,
							"xml", this.getPackageName());

					Recipe.populateDb(getApplicationContext(), resource);
				}
				xrp1.next();
			}
		} catch (XmlPullParserException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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

			Intent intentHome = new Intent(getApplicationContext(),
					MyMealActivity.class);
			startActivity(intentHome);

			break;
		case 1:
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
