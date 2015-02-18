package com.app.mymeal;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.app.mymeal.adapters.MyCustomAdapter;
import com.app.mymeal.adapters.ViewHolder;
import com.app.mymeal.data.Meal;
import com.app.mymeal.persistence.DataBaseHelper;
import com.app.mymeal.util.Utils;
import com.app.mymeal.views.BreakfastView;
import com.app.mymeal.views.CommonView;
import com.app.mymeal.views.DinnerView;
import com.app.mymeal.views.LunchView;
import com.app.mymeal.views.MealView;
import com.app.mymeal.views.MealViewInterface;
import com.app.mymeal.views.NavMenuView;
import com.app.mymeal.views.SnacksView;
import com.app.mymeal.views.ViewFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MealEditActivity extends Activity {

	MyCustomAdapter dataAdapter = null;
	static String mDate = "";
	CommonView commonView = null;
	MealView breakfast = null;
	MealView lunch = null;
	MealView snacks = null;
	MealView dinner = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meal_edit);

		Log.e("error", "onCreate");

		breakfast = (MealView) findViewById(R.id.breakfastLayout);
		lunch = (MealView) findViewById(R.id.lunchLayout);
		snacks = (MealView) findViewById(R.id.snacksLayout);
		dinner = (MealView) findViewById(R.id.dinnerLayout);

		onNewIntent(getIntent());

		ArrayList<Meal> mealList = new ArrayList<Meal>();
		dataAdapter = new MyCustomAdapter(this, R.layout.meal_list, mealList);

		commonView = new CommonView(this, dataAdapter);
		commonView.setBreakfast(breakfast);
		commonView.setLunch(lunch);
		commonView.setSnacks(snacks);
		commonView.setDinner(dinner);

		commonView.getBreakfast().getSearch_meal_icon()
				.setOnClickListener(new BreakfastView(commonView));

		commonView.getLunch().getSearch_meal_icon()
				.setOnClickListener(new LunchView(commonView));

		commonView.getSnacks().getSearch_meal_icon()
				.setOnClickListener(new SnacksView(commonView));

		commonView.getDinner().getSearch_meal_icon()
				.setOnClickListener(new DinnerView(commonView));

		NavMenuView navMenu = new NavMenuView(this);
		navMenu.displayNavMenu();
	}

	public void onSaveMealButtonClick(View view) {

		TextView date = (TextView) findViewById(R.id.date);
		String mealDate = date.getText().toString();

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		myDbHelper = new DataBaseHelper(this);
		myDbHelper.openDataBase();

		myDbHelper.deleteMeal(mealDate);

		myDbHelper.saveMeal(mealDate, "B", breakfast.getMeal().getText()
				.toString());

		myDbHelper
				.saveMeal(mealDate, "L", lunch.getMeal().getText().toString());

		myDbHelper.saveMeal(mealDate, "S", snacks.getMeal().getText()
				.toString());

		myDbHelper.saveMeal(mealDate, "D", dinner.getMeal().getText()
				.toString());

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

			EditText myBreakfast = (EditText) breakfast.getMeal();

			String tempBreakfastText = myDbHelper.searchMeal(mDate, "B").trim();

			final SpannableStringBuilder ss;

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
				ss = Utils.makeTagLinks(this, tempBreakfastText, mealItemList);
			} else {
				ss = new SpannableStringBuilder("");
			}

			myBreakfast.setText(ss);
			myBreakfast.setMovementMethod(LinkMovementMethod.getInstance());
			myBreakfast.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Log.e("error",
							"onTouch is getting called..event.getAction()"
									+ event.getAction());
					((TextView) v).getMovementMethod().onTouchEvent(
							(TextView) v, ss, event);

					return true; // consume touch event
				}
			});

			EditText myLunch = (EditText) lunch.getMeal();

			String tempLunchText = myDbHelper.searchMeal(mDate, "L").trim();

			final SpannableStringBuilder ssLunch;

			if (tempLunchText != null) {
				ArrayList<String> mealItemList = new ArrayList<String>();
				StringTokenizer tokenizer = new StringTokenizer(tempLunchText,
						",");
				while (tokenizer.hasMoreTokens()) {
					mealItemList.add(tokenizer.nextToken());
				}

				if (mealItemList.size() == 0)
					tempLunchText = "";
				ssLunch = Utils.makeTagLinks(this, tempLunchText, mealItemList);

			} else {
				ssLunch = new SpannableStringBuilder("");
			}

			myLunch.setText(ssLunch);
			myLunch.setMovementMethod(LinkMovementMethod.getInstance());
			myLunch.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {

					Log.e("error", "onTouch is getting called..");

					((TextView) v).getMovementMethod().onTouchEvent(
							(TextView) v, ssLunch, event);
					return true; // consume touch event
				}
			});

			EditText mySnacks = snacks.getMeal();

			String tempSnacksText = myDbHelper.searchMeal(mDate, "S").trim();

			final SpannableStringBuilder ssSnacks;

			if (tempSnacksText != null) {
				ArrayList<String> mealItemList = new ArrayList<String>();
				StringTokenizer tokenizer = new StringTokenizer(tempSnacksText,
						",");
				while (tokenizer.hasMoreTokens()) {
					mealItemList.add(tokenizer.nextToken());
				}

				if (mealItemList.size() == 0)
					tempSnacksText = "";
				ssSnacks = Utils.makeTagLinks(this, tempSnacksText,
						mealItemList);
			} else {
				ssSnacks = new SpannableStringBuilder("");
			}

			mySnacks.setText(ssSnacks);
			mySnacks.setMovementMethod(LinkMovementMethod.getInstance());
			mySnacks.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Log.e("error", "onTouch is getting called..");

					((TextView) v).getMovementMethod().onTouchEvent(
							(TextView) v, ssSnacks, event);
					return true; // consume touch event
				}
			});

			EditText myDinner = dinner.getMeal();

			String tempDinnerText = myDbHelper.searchMeal(mDate, "D").trim();

			final SpannableStringBuilder ssDinner;

			if (tempDinnerText != null) {
				ArrayList<String> mealItemList = new ArrayList<String>();
				StringTokenizer tokenizer = new StringTokenizer(tempDinnerText,
						",");
				while (tokenizer.hasMoreTokens()) {
					mealItemList.add(tokenizer.nextToken());
				}

				if (mealItemList.size() == 0)
					tempDinnerText = "";

				ssDinner = Utils.makeTagLinks(this, tempDinnerText,
						mealItemList);
			} else {
				ssDinner = new SpannableStringBuilder("");
			}

			myDinner.setText(ssDinner);
			myDinner.setMovementMethod(LinkMovementMethod.getInstance());
			myDinner.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Log.e("error", "onTouch is getting called..");

					((TextView) v).getMovementMethod().onTouchEvent(
							(TextView) v, ssDinner, event);
					return true; // consume touch event
				}
			});

		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

			Log.e("error: ", "action : " + Intent.ACTION_SEARCH);
			Log.e("error: ", "Planned Date in ACTION_SEARCH: " + mDate);
			String query = intent.getStringExtra(SearchManager.QUERY);
			// String aMealType = intent.getStringExtra("mealType");
			if (query == null || "".equals(query.trim())) {
				ArrayList<Meal> mealItemList = new ArrayList<Meal>();
				dataAdapter.setMealList(mealItemList);
				displayListView();
			} else {
				dataAdapter.clear();
				ArrayList<Meal> mealItemList = doMySearch(query.trim());
				dataAdapter.setMealList(mealItemList);
				displayListView();
			}

		}

	}

	private ArrayList<Meal> doMySearch(String query) {
		// TODO Auto-generated method stub
		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		myDbHelper = new DataBaseHelper(this);
		myDbHelper.openDataBase();
		return myDbHelper.search(query, mDate);
	}

	private void displayListView() {

		ListView listView = null;

		if ("B".equals(commonView.getMealType())) {
			listView = breakfast.getListView();
		}

		if ("L".equals(commonView.getMealType())) {
			listView = lunch.getListView();
		}

		if ("S".equals(commonView.getMealType())) {
			listView = snacks.getListView();
		}

		if ("D".equals(commonView.getMealType())) {
			listView = dinner.getListView();
		}

		listView.setOnTouchListener(new OnTouchListener() {
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

		listView.setAdapter(dataAdapter);

		CommonView.setListViewHeightBasedOnChildren(listView);

	}

	// @Override
	// public void startActivity(Intent intent) {
	// // check if search intent
	// if (Intent.ACTION_SEARCH.equals(intent.getAction())
	// || Intent.ACTION_VIEW.equals(intent.getAction())) {
	// if ("B".equals(commonView.getMealType()))
	// intent.putExtra("mealType", "B");
	// if ("L".equals(commonView.getMealType()))
	// intent.putExtra("mealType", "L");
	// if ("S".equals(commonView.getMealType()))
	// intent.putExtra("mealType", "S");
	// if ("D".equals(commonView.getMealType()))
	// intent.putExtra("mealType", "D");
	// }
	//
	// super.startActivity(intent);
	// }

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.e("error", "onResume");
		super.onResume();

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

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState); // the UI component values are
												// saved here.
												// Toast.makeText(this,
												// "Activity state saved",
												// Toast.LENGTH_LONG).show();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// Toast.makeText(this, "Activity state restored", Toast.LENGTH_LONG)
		// .show();
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

	public void onAddButtonClick(View view) {
		if ("B".equals(commonView.getMealType())) {
			MealViewInterface mealView = ViewFactory.getViewFactory()
					.getMealView("Breakfast", commonView);
			mealView.onAddButtonClick();
		}
		if ("L".equals(commonView.getMealType())) {
			MealViewInterface mealView = ViewFactory.getViewFactory()
					.getMealView("Lunch", commonView);
			mealView.onAddButtonClick();
		}
		if ("S".equals(commonView.getMealType())) {
			MealViewInterface mealView = ViewFactory.getViewFactory()
					.getMealView("Snacks", commonView);
			mealView.onAddButtonClick();
		}
		if ("D".equals(commonView.getMealType())) {
			MealViewInterface mealView = ViewFactory.getViewFactory()
					.getMealView("Dinner", commonView);
			mealView.onAddButtonClick();
		}
	}

	public void onClearButtonClick(View view) {
		if ("B".equals(commonView.getMealType())) {
			MealViewInterface mealView = ViewFactory.getViewFactory()
					.getMealView("Breakfast", commonView);
			mealView.onClearButtonClick();
		}
		if ("L".equals(commonView.getMealType())) {
			MealViewInterface mealView = ViewFactory.getViewFactory()
					.getMealView("Lunch", commonView);
			mealView.onClearButtonClick();
		}
		if ("S".equals(commonView.getMealType())) {
			MealViewInterface mealView = ViewFactory.getViewFactory()
					.getMealView("Snacks", commonView);
			mealView.onClearButtonClick();
		}
		if ("D".equals(commonView.getMealType())) {
			MealViewInterface mealView = ViewFactory.getViewFactory()
					.getMealView("Dinner", commonView);
			mealView.onClearButtonClick();
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.e("error", "onOptionsItemSelected");
		if (NavMenuView.onOptionsItemSelected(item))
			return true;
		return super.onOptionsItemSelected(item);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		Log.e("error", "onPrepareOptionsMenu");
		return super.onPrepareOptionsMenu(menu);
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
