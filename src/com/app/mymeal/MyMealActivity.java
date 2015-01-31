package com.app.mymeal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
		gestureScanner = new GestureDetector(this, this);
		setContentView(R.layout.my_meal);

		// mDrawerLayout.setDrawerListener(mDrawerToggle);
		//
		// if (savedInstanceState == null) {
		// // on first time display view for first nav item
		// displayView(0);
		// }

		/** Create an array adapter to populate dropdownlist */
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				getActionBar().getThemedContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1, meal);

		// ArrayAdapter<String> arrayAdapter = ArrayAdapter.createFromResource(
		// getActionBar().getThemedContext(), R.array.action_list,
		// android.R.layout.simple_spinner_dropdown_item);

		/** Enabling dropdown list navigation for the action bar */
		// getActionBar().setDisplayShowTitleEnabled(false);
		// getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

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

		/**
		 * Setting dropdown items and item navigation listener for the actionbar
		 */
		// getActionBar().setListNavigationCallbacks(arrayAdapter,
		// navigationListener);

		// start - Put a help button in action bar
		// WebView webView = new WebView(this);
		// webView.getSettings().setJavaScriptEnabled(true);
		// String urlDemo = "http://www.appdemostore.com/m/5995841235976192";
		// webView.loadUrl(urlDemo);
		//
		// Button demoButton = new Button(this);
		// demoButton.setText("Demo");
		// demoButton.setBackgroundColor(Color.WHITE);
		// demoButton.setTextColor(Color.BLACK);
		//
		// demoButton.setOnClickListener(demoButtonListener);
		//
		// getActionBar().setCustomView(demoButton);
		// getActionBar().setDisplayShowCustomEnabled(true);
		// end - put a help button in action bar

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

//						Toast.makeText(
//								getApplicationContext(),
//								android.text.format.DateFormat.format(
//										"yyyy-MM", myCalendar) + "-" + day,
//								Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(v.getContext(),
								MealEditActivity.class);

						intent.putExtra(
								"date",
								android.text.format.DateFormat.format(
										"yyyy-MM", myCalendar) + "-" + day);

						intent.putExtra("activity", "MyMealActivity");

						startActivity(intent);

					} else {
//						Toast.makeText(
//								getApplicationContext(),
//								android.text.format.DateFormat.format(
//										"yyyy-MM", myCalendar) + "-" + day,
//								Toast.LENGTH_SHORT).show();

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
			// @Override
			// public boolean onTouch(View view, MotionEvent event)
			// {
			// float X = event.getX();
			// float Y = event.getY();
			//
			// switch (event.getAction()) {
			// case MotionEvent.ACTION_DOWN:
			// Log.e("error", "Down: " + X + "," + Y);
			// break;
			// case MotionEvent.ACTION_MOVE:
			// Log.e("error", "Move: " + X + "," + Y);
			// break;
			// case MotionEvent.ACTION_UP:
			// Log.e("error", "Up: " + X + "," + Y);
			// break;
			// } }
			// return true;
			// public boolean onTouch(View v, MotionEvent me) {
			// return gestureScanner.onTouchEvent(me);
			// }

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

			// for (int i = 0; i < 31; i++) {
			// Random r = new Random();
			//
			// if (r.nextInt(10) > 6) {
			// items.add(Integer.toString(i));
			// }
			// }

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

		// ArrayAdapter<String> arrayAdapter = ArrayAdapter.createFromResource(
		// getActionBar().getThemedContext(), R.array.action_list,
		// android.R.layout.simple_spinner_dropdown_item);

		/** Enabling dropdown list navigation for the action bar */
		// getActionBar().setDisplayShowTitleEnabled(false);
		// getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

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

		/**
		 * Setting dropdown items and item navigation listener for the actionbar
		 */
		// getActionBar().setListNavigationCallbacks(arrayAdapter,
		// navigationListener);

		// start - Put a help button in action bar
		// getActionBar().set

		// WebView webView = new WebView(this);
		// webView.getSettings().setJavaScriptEnabled(true);
		// String urlDemo = "http://www.appdemostore.com/m/5995841235976192";
		// webView.loadUrl(urlDemo);

		// end - put a help button in action bar

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

//						Toast.makeText(
//								getApplicationContext(),
//								android.text.format.DateFormat.format(
//										"yyyy-MM", myCalendar) + "-" + day,
//								Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(v.getContext(),
								MealEditActivity.class);

						intent.putExtra(
								"date",
								android.text.format.DateFormat.format(
										"yyyy-MM", myCalendar) + "-" + day);

						intent.putExtra("activity", "MyMealActivity");

						startActivity(intent);

					} else {
//						Toast.makeText(
//								getApplicationContext(),
//								android.text.format.DateFormat.format(
//										"yyyy-MM", myCalendar) + "-" + day,
//								Toast.LENGTH_SHORT).show();

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
			// @Override
			// public boolean onTouch(View view, MotionEvent event)
			// {
			// float X = event.getX();
			// float Y = event.getY();
			//
			// switch (event.getAction()) {
			// case MotionEvent.ACTION_DOWN:
			// Log.e("error", "Down: " + X + "," + Y);
			// break;
			// case MotionEvent.ACTION_MOVE:
			// Log.e("error", "Move: " + X + "," + Y);
			// break;
			// case MotionEvent.ACTION_UP:
			// Log.e("error", "Up: " + X + "," + Y);
			// break;
			// } }
			// return true;
			// public boolean onTouch(View v, MotionEvent me) {
			// return gestureScanner.onTouchEvent(me);
			// }

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
