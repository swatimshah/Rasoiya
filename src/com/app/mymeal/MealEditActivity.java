package com.app.mymeal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.app.mymeal.RecipesListActivity.ViewHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView.SearchAutoComplete;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MealEditActivity extends Activity {

	MyCustomAdapter dataAdapter = null;
	String mMealType = "";
	static String mDate = "";
	ImageView breakfastSearch;
	ImageView lunchSearch;
	ImageView snacksSearch;
	ImageView dinnerSearch;

	SearchView selectedSearchView = null;

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
		setContentView(R.layout.meal_edit);

		Log.e("error", "onCreate");

		onNewIntent(getIntent());

		breakfastSearch = (ImageView) findViewById(R.id.search_breakfast_icon);
		lunchSearch = (ImageView) findViewById(R.id.search_lunch_icon);
		snacksSearch = (ImageView) findViewById(R.id.search_snacks_icon);
		dinnerSearch = (ImageView) findViewById(R.id.search_dinner_icon);

		breakfastSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SearchView searchView = (SearchView) findViewById(R.id.searchView);
				searchView.setVisibility(View.VISIBLE);

				// ImageView addQuery = (ImageView) findViewById(R.id.addQuery);
				// addQuery.setVisibility(View.VISIBLE);

				// ListView listView = (ListView) findViewById(R.id.listView1);
				// listView.setVisibility(View.VISIBLE);

				Button button = (Button) findViewById(R.id.lblAdd);
				button.setVisibility(View.VISIBLE);

				Button clearButton = (Button) findViewById(R.id.lblClear);
				clearButton.setVisibility(View.VISIBLE);

				mMealType = "B";
				setupSearchView("B");

				ArrayList<Meal> mealList = new ArrayList<Meal>();

				// create an ArrayAdaptar from the String Array
				dataAdapter = new MyCustomAdapter(getApplicationContext(),
						R.layout.meal_list, mealList);

				clearListView("B");

				ListView listView = (ListView) findViewById(R.id.listView1);
				listView.setVisibility(View.VISIBLE);

				lunchSearch.setEnabled(false);
				lunchSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				snacksSearch.setEnabled(false);
				snacksSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				dinnerSearch.setEnabled(false);
				dinnerSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);

				LinearLayout breakfastLayout = (LinearLayout) findViewById(R.id.breakfastLayout);
				breakfastLayout.setBackgroundColor(Color.rgb(255, 239, 213));

			}
		});

		lunchSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SearchView searchView = (SearchView) findViewById(R.id.searchViewLunch);
				searchView.setVisibility(View.VISIBLE);

				ListView listView = (ListView) findViewById(R.id.listView1Lunch);
				listView.setVisibility(View.VISIBLE);

				Button button = (Button) findViewById(R.id.lblAddLunch);
				button.setVisibility(View.VISIBLE);

				Button clearButton = (Button) findViewById(R.id.lblClearLunch);
				clearButton.setVisibility(View.VISIBLE);

				mMealType = "L";
				setupSearchView("L");

				ArrayList<Meal> mealList = new ArrayList<Meal>();

				// create an ArrayAdaptar from the String Array
				dataAdapter = new MyCustomAdapter(getApplicationContext(),
						R.layout.meal_list, mealList);

				clearListView("L");

				breakfastSearch.setEnabled(false);
				breakfastSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				snacksSearch.setEnabled(false);
				snacksSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				dinnerSearch.setEnabled(false);
				dinnerSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);

				LinearLayout lunchLayout = (LinearLayout) findViewById(R.id.lunchLayout);
				lunchLayout.setBackgroundColor(Color.rgb(255, 239, 213));

			}

		});

		snacksSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SearchView searchView = (SearchView) findViewById(R.id.searchViewSnacks);
				searchView.setVisibility(View.VISIBLE);

				ListView listView = (ListView) findViewById(R.id.listView1Snacks);
				listView.setVisibility(View.VISIBLE);

				Button button = (Button) findViewById(R.id.lblAddSnacks);
				button.setVisibility(View.VISIBLE);

				Button clearButton = (Button) findViewById(R.id.lblClearSnacks);
				clearButton.setVisibility(View.VISIBLE);

				mMealType = "S";
				setupSearchView("S");

				ArrayList<Meal> mealList = new ArrayList<Meal>();

				// create an ArrayAdaptar from the String Array
				dataAdapter = new MyCustomAdapter(getApplicationContext(),
						R.layout.meal_list, mealList);

				clearListView("S");

				breakfastSearch.setEnabled(false);
				breakfastSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				lunchSearch.setEnabled(false);
				lunchSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				dinnerSearch.setEnabled(false);
				dinnerSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);

				LinearLayout snacksLayout = (LinearLayout) findViewById(R.id.snacksLayout);
				snacksLayout.setBackgroundColor(Color.rgb(255, 239, 213));

			}
		});

		dinnerSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SearchView searchView = (SearchView) findViewById(R.id.searchViewDinner);
				searchView.setVisibility(View.VISIBLE);

				ListView listView = (ListView) findViewById(R.id.listView1Dinner);
				listView.setVisibility(View.VISIBLE);

				Button button = (Button) findViewById(R.id.lblAddDinner);
				button.setVisibility(View.VISIBLE);

				Button clearButton = (Button) findViewById(R.id.lblClearDinner);
				clearButton.setVisibility(View.VISIBLE);

				mMealType = "D";
				setupSearchView("D");

				ArrayList<Meal> mealList = new ArrayList<Meal>();

				// create an ArrayAdaptar from the String Array
				dataAdapter = new MyCustomAdapter(getApplicationContext(),
						R.layout.meal_list, mealList);

				clearListView("D");

				breakfastSearch.setEnabled(false);
				breakfastSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				lunchSearch.setEnabled(false);
				lunchSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				snacksSearch.setEnabled(false);
				snacksSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);

				LinearLayout dinnerLayout = (LinearLayout) findViewById(R.id.dinnerLayout);
				dinnerLayout.setBackgroundColor(Color.rgb(255, 239, 213));

			}
		});

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

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.items, menu);
	// return super.onCreateOptionsMenu(menu);
	// }

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	//
	// super.onOptionsItemSelected(item);
	//
	// switch (item.getItemId()) {
	// case R.id.Recipes:
	// Toast.makeText(getBaseContext(), "You selected Recipes",
	// Toast.LENGTH_SHORT).show();
	// Intent intent = new Intent(this, RecipesListActivity.class);
	// startActivity(intent);
	//
	// break;
	// }
	//
	// return true;
	// }

	public void onSaveMealButtonClick(View view) {

		TextView date = (TextView) findViewById(R.id.date);
		String mealDate = date.getText().toString();

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		myDbHelper = new DataBaseHelper(this);
		myDbHelper.openDataBase();

		myDbHelper.deleteMeal(mealDate);

		TextView breakFast = (TextView) findViewById(R.id.Breakfast);
		String breakfastMeal = breakFast.getText().toString();
		myDbHelper.saveMeal(mealDate, "B", breakfastMeal);

		TextView lunch = (TextView) findViewById(R.id.Lunch);
		String lunchMeal = lunch.getText().toString();
		myDbHelper.saveMeal(mealDate, "L", lunchMeal);

		TextView snacks = (TextView) findViewById(R.id.Snacks);
		String snacksMeal = snacks.getText().toString();
		myDbHelper.saveMeal(mealDate, "S", snacksMeal);

		TextView dinner = (TextView) findViewById(R.id.Dinner);
		String dinnerMeal = dinner.getText().toString();
		myDbHelper.saveMeal(mealDate, "D", dinnerMeal);

		Toast.makeText(this, "The meal saved successfully.", Toast.LENGTH_SHORT)
				.show();

		Intent intent = new Intent(this, MyMealActivity.class);
		startActivity(intent);

	}

	public void onNewIntent(Intent intent) {

		String source = intent.getStringExtra("activity");

		Log.e("error", "source: " + source);

		
		if ("MyMealActivity".equals(source)) {

			mDate = intent.getStringExtra("date");
			TextView aDate = (TextView) findViewById(R.id.date);
			aDate.setText(mDate);

			MealCustomSuggestionProvider.setLastPlannedDate(mDate);

			DataBaseHelper myDbHelper = new DataBaseHelper(this);
			myDbHelper = new DataBaseHelper(this);
			myDbHelper.openDataBase();

			// TextView breakfast = (TextView) findViewById(R.id.Breakfast);
			// breakfast.setText(myDbHelper.searchMeal(date, "B"));

			EditText breakfast = (EditText) findViewById(R.id.Breakfast);
			// breakfast.setText(myDbHelper.searchMeal(date, "B"));

			String tempBreakfastText = myDbHelper.searchMeal(mDate, "B").trim();

			final SpannableString ss;

			if (tempBreakfastText != null) {
				ArrayList<String> mealItemList = new ArrayList<String>();
				StringTokenizer tokenizer = new StringTokenizer(
						tempBreakfastText, ",");
				while (tokenizer.hasMoreTokens()) {
					mealItemList.add(tokenizer.nextToken());
				}
				Log.e("error", "before calling makeTagLinks from breakfast."
						+ tempBreakfastText);
				if (mealItemList.size() == 0)
					tempBreakfastText = "";
				ss = makeTagLinks(tempBreakfastText, breakfast, mealItemList);
			} else {
				ss = new SpannableString("");
			}

			// breakfast.setFocusableInTouchMode(true); - Neverbefocusable
			breakfast.setText(ss);
			// breakfast.setSelection(breakfast.getText().length());-
			// Neverbefocusable
			// breakfast.requestFocus();- Neverbefocusable
			breakfast.setMovementMethod(LinkMovementMethod.getInstance());
			breakfast.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Log.e("error",
							"onTouch is getting called..event.getAction()"
									+ event.getAction());

					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						Layout layout = ((EditText) v).getLayout();
						float x = event.getX() + ((EditText) v).getScrollX();
						float y = event.getY() + ((EditText) v).getScrollY();
						int line = layout.getLineForVertical((int) y);

						// Here is what you wanted:

						int offset = layout.getOffsetForHorizontal(line, x);
						Log.e("error", "onTouch is getting called.. offset: "
								+ offset);
						if (offset > 0)
							if (x > layout.getLineMax(0))
								((EditText) v).setSelection(offset); // touch
																		// was
																		// at
																		// the
																		// end
																		// of
																		// the
																		// text
							else
								((EditText) v).setSelection(offset - 1);
						break;
					}

					((TextView) v).getMovementMethod().onTouchEvent(
							(TextView) v, ss, event);
					// ((EditText) v).setFocusable(true);- Neverbefocusable
					// int cursorPosition = ((EditText) v).getSelectionStart();
					// Log.e("error", "cursor position: " + cursorPosition);
					// ((EditText) v).setSelection(cursorPosition);
					// ((EditText) v).requestFocus();- Neverbefocusable
					// showKeyboard(v);- Neverbefocusable

					return true; // consume touch event
				}
			});

			// breakfast.addTextChangedListener(new
			// GenericTextWatcher(breakfast));

			// breakfast.setOnFocusChangeListener(listener);
			// breakfast.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Log.e("error", "onClick is getting called over breakfast..");
			// ((EditText)v).setFocusable(true);
			// ((EditText)v).setSelection(((EditText)v).getText().length());
			// ((EditText)v).requestFocus();
			// }
			// });

			EditText lunch = (EditText) findViewById(R.id.Lunch);
			// lunch.setText(myDbHelper.searchMeal(date, "L"));

			String tempLunchText = myDbHelper.searchMeal(mDate, "L").trim();

			final SpannableString ssLunch;

			if (tempLunchText != null) {
				ArrayList<String> mealItemList = new ArrayList<String>();
				StringTokenizer tokenizer = new StringTokenizer(tempLunchText,
						",");
				while (tokenizer.hasMoreTokens()) {
					mealItemList.add(tokenizer.nextToken());
				}

				if (mealItemList.size() == 0)
					tempLunchText = "";
				ssLunch = makeTagLinks(tempLunchText, lunch, mealItemList);

			} else {
				ssLunch = new SpannableString("");
			}

			// lunch.setFocusableInTouchMode(true);-- Neverbefocusable
			lunch.setText(ssLunch);
			// lunch.setSelection(lunch.getText().length());- Neverbefocusable
			// lunch.requestFocus();
			lunch.setMovementMethod(LinkMovementMethod.getInstance());
			lunch.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {

					Log.e("error", "onTouch is getting called..");

					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						Layout layout = ((EditText) v).getLayout();
						float x = event.getX() + ((EditText) v).getScrollX();
						float y = event.getY() + ((EditText) v).getScrollY();
						int line = layout.getLineForVertical((int) y);

						// Here is what you wanted:

						int offset = layout.getOffsetForHorizontal(line, x);

						if (offset > 0)
							if (x > layout.getLineMax(0))
								((EditText) v).setSelection(offset); // touch
																		// was
																		// at
																		// the
																		// end
																		// of
																		// the
																		// text
							else
								((EditText) v).setSelection(offset - 1);
						break;
					}

					((TextView) v).getMovementMethod().onTouchEvent(
							(TextView) v, ssLunch, event);
					// ((EditText) v).setFocusable(true);-- Neverbefocusable
					// ((EditText) v).setSelection(((EditText) v).getText()
					// .length());
					// ((EditText) v).requestFocus();- Neverbefocusable
					// showKeyboard(v);- Neverbefocusable
					return true; // consume touch event
				}
			});

			// lunch.setOnFocusChangeListener(listener);
			// lunch.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// ((EditText)v).setFocusable(true);
			// ((EditText)v).setSelection(((EditText)v).getText().length());
			// ((EditText)v).requestFocus();
			// }
			// });

			EditText snacks = (EditText) findViewById(R.id.Snacks);
			// snacks.setText(myDbHelper.searchMeal(date, "S"));

			String tempSnacksText = myDbHelper.searchMeal(mDate, "S").trim();

			final SpannableString ssSnacks;

			if (tempSnacksText != null) {
				ArrayList<String> mealItemList = new ArrayList<String>();
				StringTokenizer tokenizer = new StringTokenizer(tempSnacksText,
						",");
				while (tokenizer.hasMoreTokens()) {
					mealItemList.add(tokenizer.nextToken());
				}

				if (mealItemList.size() == 0)
					tempSnacksText = "";
				ssSnacks = makeTagLinks(tempSnacksText, snacks, mealItemList);
			} else {
				ssSnacks = new SpannableString("");
			}

			// snacks.setFocusableInTouchMode(true);- Neverbefocusable
			snacks.setText(ssSnacks);
			// snacks.setSelection(snacks.getText().length());- Neverbefocusable
			// snacks.requestFocus();
			snacks.setMovementMethod(LinkMovementMethod.getInstance());
			snacks.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Log.e("error", "onTouch is getting called..");

					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						Layout layout = ((EditText) v).getLayout();
						float x = event.getX() + ((EditText) v).getScrollX();
						float y = event.getY() + ((EditText) v).getScrollY();
						int line = layout.getLineForVertical((int) y);

						// Here is what you wanted:

						int offset = layout.getOffsetForHorizontal(line, x);

						if (offset > 0)
							if (x > layout.getLineMax(0))
								((EditText) v).setSelection(offset); // touch
																		// was
																		// at
																		// the
																		// end
																		// of
																		// the
																		// text
							else
								((EditText) v).setSelection(offset - 1);
						break;
					}

					((TextView) v).getMovementMethod().onTouchEvent(
							(TextView) v, ssSnacks, event);
					// ((EditText) v).setFocusable(true);- Neverbefocusable
					// ((EditText) v).setSelection(((EditText) v).getText()
					// .length());
					// ((EditText) v).requestFocus();- Neverbefocusable
					// showKeyboard(v);- Neverbefocusable
					return true; // consume touch event
				}
			});

			// snacks.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// ((EditText)v).setFocusable(true);
			// ((EditText)v).setSelection(((EditText)v).getText().length());
			// ((EditText)v).requestFocus();
			// }
			// });

			EditText dinner = (EditText) findViewById(R.id.Dinner);
			// dinner.setText(myDbHelper.searchMeal(date, "D"));

			String tempDinnerText = myDbHelper.searchMeal(mDate, "D").trim();

			final SpannableString ssDinner;

			if (tempDinnerText != null) {
				ArrayList<String> mealItemList = new ArrayList<String>();
				StringTokenizer tokenizer = new StringTokenizer(tempDinnerText,
						",");
				while (tokenizer.hasMoreTokens()) {
					mealItemList.add(tokenizer.nextToken());
				}

				if (mealItemList.size() == 0)
					tempDinnerText = "";

				ssDinner = makeTagLinks(tempDinnerText, dinner, mealItemList);
			} else {
				ssDinner = new SpannableString("");
			}

			// dinner.setFocusableInTouchMode(true);- Neverbefocusable
			dinner.setText(ssDinner);
			// dinner.setSelection(dinner.getText().length());- Neverbefocusable
			// dinner.requestFocus();
			dinner.setMovementMethod(LinkMovementMethod.getInstance());
			dinner.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Log.e("error", "onTouch is getting called..");

					switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						Layout layout = ((EditText) v).getLayout();
						float x = event.getX() + ((EditText) v).getScrollX();
						float y = event.getY() + ((EditText) v).getScrollY();
						int line = layout.getLineForVertical((int) y);

						// Here is what you wanted:

						int offset = layout.getOffsetForHorizontal(line, x);

						if (offset > 0)
							if (x > layout.getLineMax(0))
								((EditText) v).setSelection(offset); // touch
																		// was
																		// at
																		// the
																		// end
																		// of
																		// the
																		// text
							else
								((EditText) v).setSelection(offset - 1);
						break;
					}

					((TextView) v).getMovementMethod().onTouchEvent(
							(TextView) v, ssDinner, event);
					// ((EditText) v).setFocusable(true);- Neverbefocusable
					// ((EditText) v).setSelection(((EditText) v).getText()
					// .length());
					// ((EditText) v).requestFocus();- Neverbefocusable
					// showKeyboard(v);- Neverbefocusable
					return true; // consume touch event
				}
			});

			// dinner.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// ((EditText)v).setFocusable(true);
			// ((EditText)v).setSelection(((EditText)v).getText().length());
			// ((EditText)v).requestFocus();
			// }
			// });

		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

			Log.e("error: ", "action : " + Intent.ACTION_SEARCH);
			Log.e("error: ", "Planned Date in ACTION_SEARCH: " + mDate);
			String query = intent.getStringExtra(SearchManager.QUERY);
			String aMealType = intent.getStringExtra("mealType");
			if (query == null || "".equals(query.trim())) {
				ArrayList<Meal> mealItemList = new ArrayList<Meal>();
				dataAdapter = new MyCustomAdapter(this, R.layout.meal_list,
						mealItemList);
				displayListView(aMealType);
			} else {
				ArrayList<Meal> mealItemList = doMySearch(query.trim());
				dataAdapter = new MyCustomAdapter(this, R.layout.meal_list,
						mealItemList);
				displayListView(aMealType);
			}

		} else if (Intent.ACTION_VIEW.equals(intent.getAction())) {

			Log.e("error: ", "action : " + Intent.ACTION_VIEW);

			Log.e("error: getPath()", intent.getData().getPath());
			String aMealType = intent.getStringExtra("mealType");
			ArrayList<Meal> mealItemList = doMySearch(intent.getData()
					.getPath().trim());
			dataAdapter = new MyCustomAdapter(this, R.layout.meal_list,
					mealItemList);
			displayListView(aMealType);
		}
	}

	public void onAddButtonClick(View view) {

		EditText breakfast = (EditText) findViewById(R.id.Breakfast);

		String tempBreakfastText = null;

		Log.e("error", "Concatenated meal items: "
				+ dataAdapter.getConcatAddedMealItems().trim() + ":");

		Log.e("error",
				"seletced search view query: " + selectedSearchView.getQuery()
						+ ":");

		if (dataAdapter.getConcatAddedMealItems() != null
				&& !"".equals(dataAdapter.getConcatAddedMealItems().trim())) {

			tempBreakfastText = (breakfast.getText().toString().trim() + " " + dataAdapter
					.getConcatAddedMealItems()).trim();

		} else {

			if (selectedSearchView.getQuery() != null
					&& !"".equals(selectedSearchView.getQuery().toString()
							.trim())) {

				tempBreakfastText = (breakfast.getText().toString().trim()
						+ " " + convertIntoValidQuery(
							selectedSearchView.getQuery().toString()).trim())
						.trim() + " ";
			}
		}

		final SpannableString ss;

		if (tempBreakfastText != null) {
			ArrayList<String> mealItemList = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(tempBreakfastText,
					",");
			while (tokenizer.hasMoreTokens()) {
				mealItemList.add(tokenizer.nextToken());
			}

			ss = makeTagLinks(tempBreakfastText, breakfast, mealItemList);
		} else {
			ss = new SpannableString("");
		}

		// breakfast.setFocusable(true);- Neverbefocusable
		breakfast.setText(ss);
		// breakfast.setSelection(breakfast.getText().length());-
		// Neverbefocusable
		// breakfast.requestFocus();- Neverbefocusable
		// showKeyboard(breakfast);- Neverbefocusable
		breakfast.setMovementMethod(LinkMovementMethod.getInstance());
		breakfast.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error",
						"onTouch is getting called on add breakfast..event.getAction()"
								+ event.getAction());

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Layout layout = ((EditText) v).getLayout();
					float x = event.getX() + ((EditText) v).getScrollX();
					float y = event.getY() + ((EditText) v).getScrollY();
					int line = layout.getLineForVertical((int) y);

					// Here is what you wanted:

					int offset = layout.getOffsetForHorizontal(line, x);

					if (offset > 0)
						if (x > layout.getLineMax(0))
							((EditText) v).setSelection(offset); // touch
																	// was
																	// at
																	// the
																	// end
																	// of
																	// the
																	// text
						else
							((EditText) v).setSelection(offset - 1);
					break;
				}

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				// ((EditText) v).setFocusable(true);- Neverbefocusable
				// ((EditText) v).setSelection(((EditText)
				// v).getText().length());
				// ((EditText) v).requestFocus();- Neverbefocusable
				// showKeyboard(v);- Neverbefocusable
				return true; // consume touch event
			}
		});

		SearchView searchView = (SearchView) findViewById(R.id.searchView);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setVisibility(View.GONE);

		Button button = (Button) findViewById(R.id.lblAdd);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) findViewById(R.id.lblClear);
		clearButton.setVisibility(View.GONE);

		breakfastSearch.setEnabled(true);
		breakfastSearch.setImageResource(R.drawable.search_icon_small2);
		lunchSearch.setEnabled(true);
		lunchSearch.setImageResource(R.drawable.search_icon_small2);
		snacksSearch.setEnabled(true);
		snacksSearch.setImageResource(R.drawable.search_icon_small2);
		dinnerSearch.setEnabled(true);
		dinnerSearch.setImageResource(R.drawable.search_icon_small2);

		LinearLayout breakfastLayout = (LinearLayout) findViewById(R.id.breakfastLayout);
		breakfastLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	private String convertIntoValidQuery(String query) {
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

	public void onAddLunchButtonClick(View view) {

		EditText lunch = (EditText) findViewById(R.id.Lunch);

		String tempLunchText = null;

		if (dataAdapter.getConcatAddedMealItems() != null
				&& !"".equals(dataAdapter.getConcatAddedMealItems().trim())) {

			tempLunchText = (lunch.getText().toString().trim() + " " + dataAdapter
					.getConcatAddedMealItems()).trim();

		} else {
			if (selectedSearchView.getQuery() != null
					&& !"".equals(selectedSearchView.getQuery().toString()
							.trim())) {

				tempLunchText = (lunch.getText().toString().trim() + " " + convertIntoValidQuery(
						selectedSearchView.getQuery().toString()).trim())
						.trim() + " ";

			}
		}

		final SpannableString ss;

		if (tempLunchText != null) {
			ArrayList<String> mealItemList = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(tempLunchText, ",");
			while (tokenizer.hasMoreTokens()) {
				mealItemList.add(tokenizer.nextToken());
			}

			ss = makeTagLinks(tempLunchText, lunch, mealItemList);
		} else {
			ss = new SpannableString("");
		}

		// lunch.setText(lunch.getText() +
		// dataAdapter.getConcatAddedMealItems());
		// lunch.setFocusable(true);- Neverbefocusable
		lunch.setText(ss);
		// lunch.setSelection(lunch.getText().length());- Neverbefocusable
		// lunch.requestFocus();- Neverbefocusable
		// showKeyboard(lunch);- Neverbefocusable
		lunch.setMovementMethod(LinkMovementMethod.getInstance());
		lunch.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Layout layout = ((EditText) v).getLayout();
					float x = event.getX() + ((EditText) v).getScrollX();
					float y = event.getY() + ((EditText) v).getScrollY();
					int line = layout.getLineForVertical((int) y);

					// Here is what you wanted:

					int offset = layout.getOffsetForHorizontal(line, x);

					if (offset > 0)
						if (x > layout.getLineMax(0))
							((EditText) v).setSelection(offset); // touch
																	// was
																	// at
																	// the
																	// end
																	// of
																	// the
																	// text
						else
							((EditText) v).setSelection(offset - 1);
					break;
				}

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				// ((EditText) v).setFocusable(true);- Neverbefocusable
				// ((EditText) v).setSelection(((EditText)
				// v).getText().length());
				// ((EditText) v).requestFocus();- Neverbefocusable
				// showKeyboard(v);- Neverbefocusable
				return true; // consume touch event
			}
		});

		SearchView searchView = (SearchView) findViewById(R.id.searchViewLunch);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) findViewById(R.id.listView1Lunch);
		listView.setVisibility(View.GONE);

		Button button = (Button) findViewById(R.id.lblAddLunch);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) findViewById(R.id.lblClearLunch);
		clearButton.setVisibility(View.GONE);

		breakfastSearch.setEnabled(true);
		breakfastSearch.setImageResource(R.drawable.search_icon_small2);
		lunchSearch.setEnabled(true);
		lunchSearch.setImageResource(R.drawable.search_icon_small2);
		snacksSearch.setEnabled(true);
		snacksSearch.setImageResource(R.drawable.search_icon_small2);
		dinnerSearch.setEnabled(true);
		dinnerSearch.setImageResource(R.drawable.search_icon_small2);

		LinearLayout lunchLayout = (LinearLayout) findViewById(R.id.lunchLayout);
		lunchLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	public void onAddSnacksButtonClick(View view) {

		EditText snacks = (EditText) findViewById(R.id.Snacks);

		String tempSnacksText = null;

		if (dataAdapter.getConcatAddedMealItems() != null
				&& !"".equals(dataAdapter.getConcatAddedMealItems().trim())) {

			tempSnacksText = (snacks.getText().toString().trim() + " " + dataAdapter
					.getConcatAddedMealItems()).trim();

		} else {

			if (selectedSearchView.getQuery() != null
					&& !"".equals(selectedSearchView.getQuery().toString()
							.trim())) {

				tempSnacksText = (snacks.getText().toString().trim() + " " + convertIntoValidQuery(
						selectedSearchView.getQuery().toString()).trim())
						.trim() + " ";
			}
		}

		final SpannableString ss;

		if (tempSnacksText != null) {
			ArrayList<String> mealItemList = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(tempSnacksText, ",");
			while (tokenizer.hasMoreTokens()) {
				mealItemList.add(tokenizer.nextToken());
			}

			ss = makeTagLinks(tempSnacksText, snacks, mealItemList);
		} else {
			ss = new SpannableString("");
		}

		// snacks.setText(snacks.getText() +
		// dataAdapter.getConcatAddedMealItems());
		// snacks.setFocusable(true);- Neverbefocusable
		snacks.setText(ss);
		// snacks.setSelection(snacks.getText().length());- Neverbefocusable
		// snacks.requestFocus();- Neverbefocusable
		// showKeyboard(snacks);- Neverbefocusable
		snacks.setMovementMethod(LinkMovementMethod.getInstance());
		snacks.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Layout layout = ((EditText) v).getLayout();
					float x = event.getX() + ((EditText) v).getScrollX();
					float y = event.getY() + ((EditText) v).getScrollY();
					int line = layout.getLineForVertical((int) y);

					// Here is what you wanted:

					int offset = layout.getOffsetForHorizontal(line, x);

					if (offset > 0)
						if (x > layout.getLineMax(0))
							((EditText) v).setSelection(offset); // touch
																	// was
																	// at
																	// the
																	// end
																	// of
																	// the
																	// text
						else
							((EditText) v).setSelection(offset - 1);
					break;
				}

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				// ((EditText) v).setFocusable(true);- Neverbefocusable
				// ((EditText) v).setSelection(((EditText)
				// v).getText().length());
				// ((EditText) v).requestFocus();- Neverbefocusable
				// showKeyboard(v);
				return true; // consume touch event
			}
		});

		SearchView searchView = (SearchView) findViewById(R.id.searchViewSnacks);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) findViewById(R.id.listView1Snacks);
		listView.setVisibility(View.GONE);

		Button button = (Button) findViewById(R.id.lblAddSnacks);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) findViewById(R.id.lblClearSnacks);
		clearButton.setVisibility(View.GONE);

		breakfastSearch.setEnabled(true);
		breakfastSearch.setImageResource(R.drawable.search_icon_small2);
		lunchSearch.setEnabled(true);
		lunchSearch.setImageResource(R.drawable.search_icon_small2);
		snacksSearch.setEnabled(true);
		snacksSearch.setImageResource(R.drawable.search_icon_small2);
		dinnerSearch.setEnabled(true);
		dinnerSearch.setImageResource(R.drawable.search_icon_small2);

		LinearLayout snacksLayout = (LinearLayout) findViewById(R.id.snacksLayout);
		snacksLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	public void onAddDinnerButtonClick(View view) {

		EditText dinner = (EditText) findViewById(R.id.Dinner);

		String tempDinnerText = null;

		if (dataAdapter.getConcatAddedMealItems() != null
				&& !"".equals(dataAdapter.getConcatAddedMealItems().trim())) {

			tempDinnerText = (dinner.getText().toString().trim() + " " + dataAdapter
					.getConcatAddedMealItems()).trim();

		} else {

			if (selectedSearchView.getQuery() != null
					&& !"".equals(selectedSearchView.getQuery().toString()
							.trim())) {

				tempDinnerText = (dinner.getText().toString().trim() + " " + convertIntoValidQuery(
						selectedSearchView.getQuery().toString()).trim())
						.trim() + " ";
			}
		}

		final SpannableString ss;

		if (tempDinnerText != null) {
			ArrayList<String> mealItemList = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(tempDinnerText, ",");
			while (tokenizer.hasMoreTokens()) {
				mealItemList.add(tokenizer.nextToken());
			}

			ss = makeTagLinks(tempDinnerText, dinner, mealItemList);
		} else {
			ss = new SpannableString("");
		}

		// dinner.setText(dinner.getText() +
		// dataAdapter.getConcatAddedMealItems());
		// dinner.setFocusable(true);- Neverbefocusable
		dinner.setText(ss);
		// dinner.setSelection(dinner.getText().length());- Neverbefocusable
		// dinner.requestFocus();- Neverbefocusable
		// showKeyboard(dinner);- Neverbefocusable
		dinner.setMovementMethod(LinkMovementMethod.getInstance());
		dinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Layout layout = ((EditText) v).getLayout();
					float x = event.getX() + ((EditText) v).getScrollX();
					float y = event.getY() + ((EditText) v).getScrollY();
					int line = layout.getLineForVertical((int) y);

					// Here is what you wanted:

					int offset = layout.getOffsetForHorizontal(line, x);

					if (offset > 0)
						if (x > layout.getLineMax(0))
							((EditText) v).setSelection(offset); // touch
																	// was
																	// at
																	// the
																	// end
																	// of
																	// the
																	// text
						else
							((EditText) v).setSelection(offset - 1);
					break;
				}

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				// ((EditText) v).setFocusable(true);- Neverbefocusable
				// ((EditText) v).setSelection(((EditText)
				// v).getText().length());
				// ((EditText) v).requestFocus();- Neverbefocusable
				// showKeyboard(v);- Neverbefocusable
				return true; // consume touch event
			}
		});

		SearchView searchView = (SearchView) findViewById(R.id.searchViewDinner);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) findViewById(R.id.listView1Dinner);
		listView.setVisibility(View.GONE);

		Button button = (Button) findViewById(R.id.lblAddDinner);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) findViewById(R.id.lblClearDinner);
		clearButton.setVisibility(View.GONE);

		breakfastSearch.setEnabled(true);
		breakfastSearch.setImageResource(R.drawable.search_icon_small2);
		lunchSearch.setEnabled(true);
		lunchSearch.setImageResource(R.drawable.search_icon_small2);
		snacksSearch.setEnabled(true);
		snacksSearch.setImageResource(R.drawable.search_icon_small2);
		dinnerSearch.setEnabled(true);
		dinnerSearch.setImageResource(R.drawable.search_icon_small2);

		LinearLayout dinnerLayout = (LinearLayout) findViewById(R.id.dinnerLayout);
		dinnerLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	public void onClearButtonClick(View view) {

		EditText breakfast = (EditText) findViewById(R.id.Breakfast);

		final SpannableString ss;

		if (breakfast.getText() != null) {
			ss = new SpannableString(breakfast.getText());
		} else {
			ss = new SpannableString("");
		}

		// breakfast.setFocusable(true);- Neverbefocusable
		breakfast.setText(ss);
		// breakfast.setSelection(breakfast.getText().length());-
		// Neverbefocusable
		// breakfast.requestFocus();- Neverbefocusable
		// showKeyboard(breakfast);- Neverbefocusable
		breakfast.setMovementMethod(LinkMovementMethod.getInstance());
		breakfast.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Layout layout = ((EditText) v).getLayout();
					float x = event.getX() + ((EditText) v).getScrollX();
					float y = event.getY() + ((EditText) v).getScrollY();
					int line = layout.getLineForVertical((int) y);

					// Here is what you wanted:

					int offset = layout.getOffsetForHorizontal(line, x);

					if (offset > 0)
						if (x > layout.getLineMax(0))
							((EditText) v).setSelection(offset); // touch
																	// was
																	// at
																	// the
																	// end
																	// of
																	// the
																	// text
						else
							((EditText) v).setSelection(offset - 1);
					break;
				}

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				// ((EditText) v).setFocusable(true);- Neverbefocusable
				// ((EditText) v).setSelection(((EditText)
				// v).getText().length());
				// ((EditText) v).requestFocus();- Neverbefocusable
				// showKeyboard(v);- Neverbefocusable
				return true; // consume touch event
			}
		});

		SearchView searchView = (SearchView) findViewById(R.id.searchView);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) findViewById(R.id.listView1);
		listView.setVisibility(View.GONE);

		Button button = (Button) findViewById(R.id.lblAdd);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) findViewById(R.id.lblClear);
		clearButton.setVisibility(View.GONE);

		breakfastSearch.setEnabled(true);
		breakfastSearch.setImageResource(R.drawable.search_icon_small2);
		lunchSearch.setEnabled(true);
		lunchSearch.setImageResource(R.drawable.search_icon_small2);
		snacksSearch.setEnabled(true);
		snacksSearch.setImageResource(R.drawable.search_icon_small2);
		dinnerSearch.setEnabled(true);
		dinnerSearch.setImageResource(R.drawable.search_icon_small2);

		LinearLayout breakfastLayout = (LinearLayout) findViewById(R.id.breakfastLayout);
		breakfastLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	public void onClearLunchButtonClick(View view) {

		EditText lunch = (EditText) findViewById(R.id.Lunch);

		final SpannableString ss;

		if (lunch.getText() != null) {
			ss = new SpannableString(lunch.getText());
		} else {
			ss = new SpannableString("");
		}

		// lunch.setFocusable(true);- Neverbefocusable
		lunch.setText(ss);
		// lunch.setSelection(lunch.getText().length());- Neverbefocusable
		// lunch.requestFocus();- Neverbefocusable
		// showKeyboard(lunch);- Neverbefocusable
		lunch.setMovementMethod(LinkMovementMethod.getInstance());
		lunch.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Layout layout = ((EditText) v).getLayout();
					float x = event.getX() + ((EditText) v).getScrollX();
					float y = event.getY() + ((EditText) v).getScrollY();
					int line = layout.getLineForVertical((int) y);

					// Here is what you wanted:

					int offset = layout.getOffsetForHorizontal(line, x);

					if (offset > 0)
						if (x > layout.getLineMax(0))
							((EditText) v).setSelection(offset); // touch
																	// was
																	// at
																	// the
																	// end
																	// of
																	// the
																	// text
						else
							((EditText) v).setSelection(offset - 1);
					break;
				}

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				// ((EditText) v).setFocusable(true);- Neverbefocusable
				// ((EditText) v).setSelection(((EditText)
				// v).getText().length());
				// ((EditText) v).requestFocus();- Neverbefocusable
				// showKeyboard(v);- Neverbefocusable
				return true; // consume touch event
			}
		});

		SearchView searchView = (SearchView) findViewById(R.id.searchViewLunch);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) findViewById(R.id.listView1Lunch);
		listView.setVisibility(View.GONE);

		Button button = (Button) findViewById(R.id.lblAddLunch);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) findViewById(R.id.lblClearLunch);
		clearButton.setVisibility(View.GONE);

		breakfastSearch.setEnabled(true);
		breakfastSearch.setImageResource(R.drawable.search_icon_small2);
		lunchSearch.setEnabled(true);
		lunchSearch.setImageResource(R.drawable.search_icon_small2);
		snacksSearch.setEnabled(true);
		snacksSearch.setImageResource(R.drawable.search_icon_small2);
		dinnerSearch.setEnabled(true);
		dinnerSearch.setImageResource(R.drawable.search_icon_small2);

		LinearLayout lunchLayout = (LinearLayout) findViewById(R.id.lunchLayout);
		lunchLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	public void onClearSnacksButtonClick(View view) {

		EditText snacks = (EditText) findViewById(R.id.Snacks);

		final SpannableString ss;

		if (snacks.getText() != null) {
			ss = new SpannableString(snacks.getText());
		} else {
			ss = new SpannableString("");
		}

		// snacks.setFocusable(true);- Neverbefocusable
		snacks.setText(ss);
		// snacks.setSelection(snacks.getText().length());- Neverbefocusable
		// snacks.requestFocus();- Neverbefocusable
		// showKeyboard(snacks);- Neverbefocusable
		snacks.setMovementMethod(LinkMovementMethod.getInstance());
		snacks.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Layout layout = ((EditText) v).getLayout();
					float x = event.getX() + ((EditText) v).getScrollX();
					float y = event.getY() + ((EditText) v).getScrollY();
					int line = layout.getLineForVertical((int) y);

					// Here is what you wanted:

					int offset = layout.getOffsetForHorizontal(line, x);

					if (offset > 0)
						if (x > layout.getLineMax(0))
							((EditText) v).setSelection(offset); // touch
																	// was
																	// at
																	// the
																	// end
																	// of
																	// the
																	// text
						else
							((EditText) v).setSelection(offset - 1);
					break;
				}

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				// ((EditText) v).setFocusable(true);- Neverbefocusable
				// ((EditText) v).setSelection(((EditText)
				// v).getText().length());
				// ((EditText) v).requestFocus();- Neverbefocusable
				// showKeyboard(v);- Neverbefocusable
				return true; // consume touch event
			}
		});

		SearchView searchView = (SearchView) findViewById(R.id.searchViewSnacks);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) findViewById(R.id.listView1Snacks);
		listView.setVisibility(View.GONE);

		Button button = (Button) findViewById(R.id.lblAddSnacks);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) findViewById(R.id.lblClearSnacks);
		clearButton.setVisibility(View.GONE);

		breakfastSearch.setEnabled(true);
		breakfastSearch.setImageResource(R.drawable.search_icon_small2);
		lunchSearch.setEnabled(true);
		lunchSearch.setImageResource(R.drawable.search_icon_small2);
		snacksSearch.setEnabled(true);
		snacksSearch.setImageResource(R.drawable.search_icon_small2);
		dinnerSearch.setEnabled(true);
		dinnerSearch.setImageResource(R.drawable.search_icon_small2);

		LinearLayout snacksLayout = (LinearLayout) findViewById(R.id.snacksLayout);
		snacksLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	public void onClearDinnerButtonClick(View view) {

		EditText dinner = (EditText) findViewById(R.id.Dinner);

		final SpannableString ss;

		if (dinner.getText() != null) {
			ss = new SpannableString(dinner.getText());
		} else {
			ss = new SpannableString("");
		}

		// dinner.setFocusable(true);- Neverbefocusable
		dinner.setText(ss);
		// dinner.setSelection(dinner.getText().length());- Neverbefocusable
		// dinner.requestFocus();- Neverbefocusable
		// showKeyboard(dinner);- Neverbefocusable
		dinner.setMovementMethod(LinkMovementMethod.getInstance());
		dinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					Layout layout = ((EditText) v).getLayout();
					float x = event.getX() + ((EditText) v).getScrollX();
					float y = event.getY() + ((EditText) v).getScrollY();
					int line = layout.getLineForVertical((int) y);

					// Here is what you wanted:

					int offset = layout.getOffsetForHorizontal(line, x);

					if (offset > 0)
						if (x > layout.getLineMax(0))
							((EditText) v).setSelection(offset); // touch
																	// was
																	// at
																	// the
																	// end
																	// of
																	// the
																	// text
						else
							((EditText) v).setSelection(offset - 1);
					break;
				}

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				// ((EditText) v).setFocusable(true);- Neverbefocusable
				// ((EditText) v).setSelection(((EditText)
				// v).getText().length());
				// ((EditText) v).requestFocus();- Neverbefocusable
				// showKeyboard(v);- Neverbefocusable
				return true; // consume touch event
			}
		});

		SearchView searchView = (SearchView) findViewById(R.id.searchViewDinner);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) findViewById(R.id.listView1Dinner);
		listView.setVisibility(View.GONE);

		Button button = (Button) findViewById(R.id.lblAddDinner);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) findViewById(R.id.lblClearDinner);
		clearButton.setVisibility(View.GONE);

		breakfastSearch.setEnabled(true);
		breakfastSearch.setImageResource(R.drawable.search_icon_small2);
		lunchSearch.setEnabled(true);
		lunchSearch.setImageResource(R.drawable.search_icon_small2);
		snacksSearch.setEnabled(true);
		snacksSearch.setImageResource(R.drawable.search_icon_small2);
		dinnerSearch.setEnabled(true);
		dinnerSearch.setImageResource(R.drawable.search_icon_small2);

		LinearLayout dinnerLayout = (LinearLayout) findViewById(R.id.dinnerLayout);
		dinnerLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	private ArrayList<Meal> doMySearch(String query) {
		// TODO Auto-generated method stub
		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		myDbHelper = new DataBaseHelper(this);
		myDbHelper.openDataBase();
		return myDbHelper.search(query, mDate);
	}

	private void setupSearchView(String aMealType) {
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = null;
		if ("B".equals(aMealType)) {
			searchView = (SearchView) findViewById(R.id.searchView);
		}
		if ("L".equals(aMealType)) {
			searchView = (SearchView) findViewById(R.id.searchViewLunch);
		}
		if ("S".equals(aMealType)) {
			searchView = (SearchView) findViewById(R.id.searchViewSnacks);
		}
		if ("D".equals(aMealType)) {
			searchView = (SearchView) findViewById(R.id.searchViewDinner);
		}

		SearchableInfo searchableInfo = searchManager
				.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(searchableInfo);
		searchView.setIconifiedByDefault(false); // Do not iconify the widget;
													// expand it by default
		// searchView.setSubmitButtonEnabled(true);
		searchView.setQuery("", true);
		searchView.setFocusable(true);
		searchView.requestFocusFromTouch();
		searchView.setQueryRefinementEnabled(true);

		int id = searchView.getContext().getResources()
				.getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) searchView.findViewById(id);
		textView.setTextSize(15);

		selectedSearchView = searchView;
	}

	private void displayListView(String aMealType) {

		ListView listView = null;

		if ("B".equals(aMealType)) {
			listView = (ListView) findViewById(R.id.listView1);
		}

		if ("L".equals(aMealType)) {
			listView = (ListView) findViewById(R.id.listView1Lunch);
		}

		if ("S".equals(aMealType)) {
			listView = (ListView) findViewById(R.id.listView1Snacks);
		}

		if ("D".equals(aMealType)) {
			listView = (ListView) findViewById(R.id.listView1Dinner);
		}

		listView.setAdapter(dataAdapter);

		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		//
		// Intent intent = new Intent(view.getContext(),
		// RecipeEditActivity.class);
		// Log.e("error", "Recipe in list");
		// intent.putExtra("recipeName", ((ViewHolder) view.getTag()).name
		// .getText().toString());
		// intent.putExtra("action", "View");
		//
		// startActivity(intent);
		//
		// }
		// });

		setListViewHeightBasedOnChildren(listView);

		listView.setOnTouchListener(new OnTouchListener() {
			// Setting on Touch Listener for handling the touch inside
			// ScrollView
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// Disallow the touch request for parent scroll on touch of
				// child view
				v.getParent().requestDisallowInterceptTouchEvent(false);
				return false;
			}
		});

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

		// Assign adapter to ListView

		// listView.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // When clicked, show a toast with the TextView text
		// Meal meal = (Meal) parent.getItemAtPosition(position);
		// Toast.makeText(getApplicationContext(),
		// "Clicked on Row: " + meal.getName(), Toast.LENGTH_LONG)
		// .show();
		// }
		// });

	}

	static class ViewHolder {
		CheckBox checkBox;
		TextView name;
		TextView date;
		TextView extraInfo;
	}

	public class MyCustomAdapter extends ArrayAdapter<Meal> {

		private ArrayList<Meal> mealList;
		private ArrayList<String> addedMealItems = new ArrayList<String>();

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<Meal> mealList) {
			super(context, textViewResourceId, mealList);
			this.mealList = new ArrayList<Meal>();
			this.mealList.addAll(mealList);

			Log.e("error",
					"mealList size in custom adapter: " + this.mealList.size());
		}

		// private class ViewHolder {
		// CheckBox checkBox;
		// TextView name;
		// TextView date;
		// }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			Log.e("error", "ConvertView" + String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.meal_list, null);

				holder = new ViewHolder();
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.checkBox = (CheckBox) convertView
						.findViewById(R.id.checkBox1);
				holder.date = (TextView) convertView.findViewById(R.id.date);
				holder.extraInfo = (TextView) convertView.findViewById(R.id.extraInfo);
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();

			}

			final Meal meal = mealList.get(position);
			SpannableString mealNameSpannable = new SpannableString(
					meal.getName());
			mealNameSpannable.setSpan(new UnderlineSpan(), 0,
					mealNameSpannable.length(), 0);
			holder.name.setText(mealNameSpannable);
			holder.date.setText(meal.getLastPrepDate());
			holder.extraInfo.setText(meal.getExtraSearchData());

			Log.e("error: holder: name", holder.name.getText().toString());
			Log.e("error: holder: date", holder.date.getText().toString());
			Log.e("error: holder: extraInfo", holder.extraInfo.getText().toString());
			
			holder.name.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(v.getContext(),
							RecipeEditActivity.class);
					intent.putExtra("action", "View");
					intent.putExtra("recipeName", meal.getName());

					startActivity(intent);
				}
			});

			holder.checkBox.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (holder.checkBox.isChecked()) {
						Log.e("error", "checkbox is checked.");
						holder.checkBox.setChecked(true);
						addedMealItems.add(meal.getName());
					} else {
						Log.e("error", "checkbox is not checked.");
						holder.checkBox.setChecked(false);
						addedMealItems.remove(meal.getName());
					}
				}
			});

			
			return convertView;
		}

		public String getConcatAddedMealItems() {

			String concatAddedMealItems = "";

			for (String mealItem : addedMealItems) {

				concatAddedMealItems = concatAddedMealItems + mealItem + ", ";
			}

			return concatAddedMealItems;
		}

		public int getCount() {

			return mealList.size();
		}

	}

	private void clearListView(String aMealType) {

		ListView listView = null;

		if ("B".equals(aMealType)) {
			listView = (ListView) findViewById(R.id.listView1);
		}

		if ("L".equals(aMealType)) {
			listView = (ListView) findViewById(R.id.listView1Lunch);
		}

		if ("S".equals(aMealType)) {
			listView = (ListView) findViewById(R.id.listView1Snacks);
		}

		if ("D".equals(aMealType)) {
			listView = (ListView) findViewById(R.id.listView1Dinner);
		}

		// Assign adapter to ListView
		listView.setAdapter(dataAdapter);

		setListViewHeightBasedOnChildren(listView);

		// listView.setOnItemClickListener(new OnItemClickListener() {
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // When clicked, show a toast with the TextView text
		// Meal meal = (Meal) parent.getItemAtPosition(position);
		// Toast.makeText(getApplicationContext(),
		// "Clicked on Row: " + meal.getName(), Toast.LENGTH_LONG)
		// .show();
		// }
		// });

	}

	@Override
	public void startActivity(Intent intent) {
		// check if search intent
		if (Intent.ACTION_SEARCH.equals(intent.getAction())
				|| Intent.ACTION_VIEW.equals(intent.getAction())) {
			if ("B".equals(mMealType))
				intent.putExtra("mealType", "B");
			if ("L".equals(mMealType))
				intent.putExtra("mealType", "L");
			if ("S".equals(mMealType))
				intent.putExtra("mealType", "S");
			if ("D".equals(mMealType))
				intent.putExtra("mealType", "D");
		}

		super.startActivity(intent);
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

	private SpannableString makeTagLinks(final String text, final TextView tv,
			final List<String> mealItemList) {
		if (text == null || tv == null) {
			return null;
		}
		final SpannableString ss = new SpannableString(text);
		Log.e("error", "makeTagLinks: " + ss.toString() + ":");
		final List<String> items = mealItemList;
		int start = 0, end;
		for (final String item : items) {
			end = start + item.trim().length();
			if (start < end) {

				ClickableSpan clickableSpan = new ClickableSpan() {

					@Override
					public void onClick(final View textView) {
						Log.e("error", "clickablespan onclick is called ..");
						final EditText tv = (EditText) textView;
						final Spanned s = (Spanned) tv.getText();
						final int start = ss.getSpanStart(this);
						final int end = ss.getSpanEnd(this);

						final ClickableSpan removeSpan = this;

						Log.e("error", "onClick [" + ss.subSequence(start, end)
								+ "]");

//						Toast.makeText(getApplicationContext(),
//								"onClick [" + ss.subSequence(start, end) + "]",
//								Toast.LENGTH_SHORT).show();

						ClickableSpan[] clickableSpans = ss.getSpans(0,
								ss.length() - 1, ClickableSpan.class);

						for (ClickableSpan cs : clickableSpans) {
							ss.setSpan(new BackgroundColorSpan(
									Color.TRANSPARENT), ss.getSpanStart(cs), ss
									.getSpanEnd(cs),
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}

						ss.setSpan(new BackgroundColorSpan(Color.YELLOW),
								start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

						tv.setText(ss);

						// Creating the instance of PopupMenu
						PopupMenu popup = new PopupMenu(MealEditActivity.this,
								textView);
						// Inflating the Popup using xml file
						popup.getMenuInflater().inflate(R.menu.poupup_menu,
								popup.getMenu());

						// registering popup with OnMenuItemClickListener
						popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
							public boolean onMenuItemClick(MenuItem item) {
//								Toast.makeText(MealEditActivity.this,
//										"You Clicked : " + item.getTitle(),
//										Toast.LENGTH_SHORT).show();

								Log.e("error", "Executing if .." + ":"
										+ item.getTitle().toString() + ":");

								if ("Edit".equals(item.getTitle().toString())) {
									Log.e("error", "inside edit");

									Intent intent = new Intent(textView
											.getContext(),
											RecipeEditActivity.class);
									intent.putExtra("recipeName", ss
											.subSequence(start, end).toString());
									intent.putExtra("action", "Edit");

									startActivity(intent);

								} else if ("View".equals(item.getTitle()
										.toString())) {

									Log.e("error", "inside view");

									Intent intent = new Intent(textView
											.getContext(),
											RecipeEditActivity.class);
									intent.putExtra("recipeName", ss
											.subSequence(start, end).toString());
									intent.putExtra("action", "View");

									startActivity(intent);

								} else if ("Delete".equals(item.getTitle()
										.toString())) {
									SpannableString tempSS = new SpannableString(
											text + " ");
									Log.e("error", "ss: " + tempSS.toString());
									Log.e("error", "s: " + s.toString());
									tempSS.removeSpan(removeSpan);
									int startAfterRemovingSpan = s
											.getSpanStart(removeSpan);
									Log.e("error", "" + startAfterRemovingSpan);
									int endAfterRemovingSpan = s
											.getSpanEnd(removeSpan) + 2;
									Log.e("error", "" + endAfterRemovingSpan);
									String toBeRemoved = tempSS.toString()
											.substring(startAfterRemovingSpan,
													endAfterRemovingSpan);
									String afterRemoval = tempSS.toString()
											.replace(toBeRemoved, "") + " ";
									final SpannableString ssNew;
									;
									if (afterRemoval != null) {
										ArrayList<String> mealItemList = new ArrayList<String>();
										StringTokenizer tokenizer = new StringTokenizer(
												afterRemoval, ",");
										while (tokenizer.hasMoreTokens()) {
											mealItemList.add(tokenizer
													.nextToken());
										}

										ssNew = makeTagLinks(afterRemoval, tv,
												mealItemList);
									} else {
										ssNew = new SpannableString("");
									}

									// tv.setFocusableInTouchMode(true);-
									// Neverbefocusable
									tv.setText(ssNew.subSequence(0,
											ssNew.length()));
									// tv.setSelection(ssNew.length());-
									// Neverbefocusable
									// tv.requestFocus();- Neverbefocusable
									tv.setMovementMethod(LinkMovementMethod
											.getInstance());
									tv.setOnTouchListener(new OnTouchListener() {
										@Override
										public boolean onTouch(View v,
												MotionEvent event) {

											Log.e("error",
													"onTouch is getting called..");

											switch (event.getAction()) {
											case MotionEvent.ACTION_UP:
												Layout layout = ((EditText) v)
														.getLayout();
												float x = event.getX()
														+ ((EditText) v)
																.getScrollX();
												float y = event.getY()
														+ ((EditText) v)
																.getScrollY();
												int line = layout
														.getLineForVertical((int) y);

												// Here is what you wanted:

												int offset = layout
														.getOffsetForHorizontal(
																line, x);

												if (offset > 0)
													if (x > layout
															.getLineMax(0))
														((EditText) v)
																.setSelection(offset); // touch
													else
														((EditText) v)
																.setSelection(offset - 1);
												break;
											}

											((TextView) v).getMovementMethod()
													.onTouchEvent((TextView) v,
															ssNew, event);
											// ((EditText)
											// v).setFocusable(true);-
											// Neverbefocusable
											// ((EditText)
											// v).setSelection(((EditText)
											// v).getText()
											// .length());
											// ((EditText) v).requestFocus();-
											// Neverbefocusable
											// showKeyboard(v);-
											// Neverbefocusable
											return true;
										}
									});

								}

								return true;
							}
						});

						popup.show();// showing popup menu

					}
				};

				ss.setSpan(clickableSpan, start, end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			start += item.trim().length() + 2;// comma and space in the original
												// text
			// ;)
		}
		// tv.setText(ss);
		// tv.setMovementMethod(LinkMovementMethod.getInstance());
		// tv.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// Log.e("error", "onTouch is getting called..");
		// ((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
		// ss, event);
		// ((EditText)v).setFocusable(true);
		// ((EditText)v).setSelection(((EditText)v).getText().length());
		// ((EditText)v).requestFocus();
		// return true; // consume touch event
		// }
		// });
		return ss;
	}

	public void showKeyboard(View v) {
		if (v != null) {
			// activity.getWindow()
			// .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
		}
	}

	public void hideKeyboard(View v) {
		if (v != null) {
			// activity.getWindow()
			// .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(),
					InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
	}

	// OnKeyListener keyListener = new EditText.OnKeyListener() {
	// @Override
	// public boolean onKey(View v, int keyCode, KeyEvent event) {
	//
	// Log.e("error", "onKey is called ..");
	// //This is the filter
	// if (event.getAction()!=KeyEvent.ACTION_DOWN)
	// return true;
	//
	// switch (keyCode) {
	// case KeyEvent.KEYCODE_COMMA :
	// Log.e("error", "comma pressed");
	// break;
	// }
	//
	// return true;
	// }
	//
	// };

	// OnEditorActionListener editorListener = new
	// TextView.OnEditorActionListener() {
	//
	// @Override
	// public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	//
	// Log.e("error", "onEditorAction");
	//
	// if (event.getAction() == KeyEvent.ACTION_DOWN
	// && event.getKeyCode() == KeyEvent.KEYCODE_ENTER
	// || actionId == EditorInfo.IME_ACTION_DONE) {
	// Log.e("MyApp", " ------> IN EDITOR ACTION DONE");
	// }
	// return false;
	// }
	// };

	// private TextWatcher inputTextWatcher = new TextWatcher() {
	// public void afterTextChanged(Editable s) { }
	// public void beforeTextChanged(CharSequence s, int start, int count, int
	// after)
	// { }
	// public void onTextChanged(CharSequence s, int start, int before, int
	// count) {
	// Log.e("error", s.charAt(count-1) + " character to send");;
	// }
	// };

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		Log.e("error", "onResume");

		// onNewIntent(getIntent());

		breakfastSearch = (ImageView) findViewById(R.id.search_breakfast_icon);
		lunchSearch = (ImageView) findViewById(R.id.search_lunch_icon);
		snacksSearch = (ImageView) findViewById(R.id.search_snacks_icon);
		dinnerSearch = (ImageView) findViewById(R.id.search_dinner_icon);

		breakfastSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SearchView searchView = (SearchView) findViewById(R.id.searchView);
				searchView.setVisibility(View.VISIBLE);

				// ImageView addQuery = (ImageView) findViewById(R.id.addQuery);
				// addQuery.setVisibility(View.VISIBLE);

				// ListView listView = (ListView) findViewById(R.id.listView1);
				// listView.setVisibility(View.VISIBLE);

				Button button = (Button) findViewById(R.id.lblAdd);
				button.setVisibility(View.VISIBLE);

				Button clearButton = (Button) findViewById(R.id.lblClear);
				clearButton.setVisibility(View.VISIBLE);

				mMealType = "B";
				setupSearchView("B");

				ArrayList<Meal> mealList = new ArrayList<Meal>();

				// create an ArrayAdaptar from the String Array
				dataAdapter = new MyCustomAdapter(getApplicationContext(),
						R.layout.meal_list, mealList);

				clearListView("B");

				ListView listView = (ListView) findViewById(R.id.listView1);
				listView.setVisibility(View.VISIBLE);

				lunchSearch.setEnabled(false);
				lunchSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				snacksSearch.setEnabled(false);
				snacksSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				dinnerSearch.setEnabled(false);
				dinnerSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);

				LinearLayout breakfastLayout = (LinearLayout) findViewById(R.id.breakfastLayout);
				breakfastLayout.setBackgroundColor(Color.rgb(255, 239, 213));

			}
		});

		lunchSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SearchView searchView = (SearchView) findViewById(R.id.searchViewLunch);
				searchView.setVisibility(View.VISIBLE);

				ListView listView = (ListView) findViewById(R.id.listView1Lunch);
				listView.setVisibility(View.VISIBLE);

				Button button = (Button) findViewById(R.id.lblAddLunch);
				button.setVisibility(View.VISIBLE);

				Button clearButton = (Button) findViewById(R.id.lblClearLunch);
				clearButton.setVisibility(View.VISIBLE);

				mMealType = "L";
				setupSearchView("L");

				ArrayList<Meal> mealList = new ArrayList<Meal>();

				// create an ArrayAdaptar from the String Array
				dataAdapter = new MyCustomAdapter(getApplicationContext(),
						R.layout.meal_list, mealList);

				clearListView("L");

				breakfastSearch.setEnabled(false);
				breakfastSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				snacksSearch.setEnabled(false);
				snacksSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				dinnerSearch.setEnabled(false);
				dinnerSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);

				LinearLayout lunchLayout = (LinearLayout) findViewById(R.id.lunchLayout);
				lunchLayout.setBackgroundColor(Color.rgb(255, 239, 213));

			}

		});

		snacksSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SearchView searchView = (SearchView) findViewById(R.id.searchViewSnacks);
				searchView.setVisibility(View.VISIBLE);

				ListView listView = (ListView) findViewById(R.id.listView1Snacks);
				listView.setVisibility(View.VISIBLE);

				Button button = (Button) findViewById(R.id.lblAddSnacks);
				button.setVisibility(View.VISIBLE);

				Button clearButton = (Button) findViewById(R.id.lblClearSnacks);
				clearButton.setVisibility(View.VISIBLE);

				mMealType = "S";
				setupSearchView("S");

				ArrayList<Meal> mealList = new ArrayList<Meal>();

				// create an ArrayAdaptar from the String Array
				dataAdapter = new MyCustomAdapter(getApplicationContext(),
						R.layout.meal_list, mealList);

				clearListView("S");

				breakfastSearch.setEnabled(false);
				breakfastSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				lunchSearch.setEnabled(false);
				lunchSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				dinnerSearch.setEnabled(false);
				dinnerSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);

				LinearLayout snacksLayout = (LinearLayout) findViewById(R.id.snacksLayout);
				snacksLayout.setBackgroundColor(Color.rgb(255, 239, 213));

			}
		});

		dinnerSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				SearchView searchView = (SearchView) findViewById(R.id.searchViewDinner);
				searchView.setVisibility(View.VISIBLE);

				ListView listView = (ListView) findViewById(R.id.listView1Dinner);
				listView.setVisibility(View.VISIBLE);

				Button button = (Button) findViewById(R.id.lblAddDinner);
				button.setVisibility(View.VISIBLE);

				Button clearButton = (Button) findViewById(R.id.lblClearDinner);
				clearButton.setVisibility(View.VISIBLE);

				mMealType = "D";
				setupSearchView("D");

				ArrayList<Meal> mealList = new ArrayList<Meal>();

				// create an ArrayAdaptar from the String Array
				dataAdapter = new MyCustomAdapter(getApplicationContext(),
						R.layout.meal_list, mealList);

				clearListView("D");

				breakfastSearch.setEnabled(false);
				breakfastSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				lunchSearch.setEnabled(false);
				lunchSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);
				snacksSearch.setEnabled(false);
				snacksSearch
						.setImageResource(R.drawable.search_icon_small2_disabled);

				LinearLayout dinnerLayout = (LinearLayout) findViewById(R.id.dinnerLayout);
				dinnerLayout.setBackgroundColor(Color.rgb(255, 239, 213));

			}
		});

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

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.e("error", "Calling onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		Log.e("error", "Calling onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Log.e("error", "Calling onDestroy");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Log.e("error", "Calling onStart");
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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState); // the UI component values are
												// saved here.
//		Toast.makeText(this, "Activity state saved", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
//		Toast.makeText(this, "Activity state restored", Toast.LENGTH_LONG)
//				.show();
	}

	@Override
	public void onBackPressed() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								MealEditActivity.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

}
