package com.app.mymeal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.widget.LinearLayout.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeEditActivity extends Activity implements
		OnItemSelectedListener {

	String recipeName = "";
	String action = "";
	String mPrepTime = "";
	View focusedView = null;
	View previousFocusedView = null;
	LinearLayout replacedll = null;
	LinearLayout replacedml = null;
	View methodFocusedView = null;
	View previousMethodFocusedView = null;
	int ingredientIndexI = 0;
	int methodIndexK = 0;
	ViewGroup llIngredients = null;
	ViewGroup llLblIngredients = null;
	ViewGroup llMethod = null;
	ViewGroup llLblMethod = null;

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

		Log.e("error", "OnCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_edit);

		llIngredients = (ViewGroup) findViewById(R.id.ingredientsLayout);
		llLblIngredients = (ViewGroup) findViewById(R.id.lblIngredientsLayout);
		llMethod = (ViewGroup) findViewById(R.id.methodLayout);
		llLblMethod = (ViewGroup) findViewById(R.id.lblMethodLayout);

		onNewIntent(getIntent());

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

	// Expands ingredients
	private void expand() {
		// set Visible
		llIngredients.setVisibility(View.VISIBLE);

		final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		llIngredients.measure(widthSpec, heightSpec);

		ValueAnimator mAnimator = slideAnimator(0,
				llIngredients.getMeasuredHeight());
		mAnimator.start();
	}

	private void collapse() {
		int finalHeight = llIngredients.getHeight();

		ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

		mAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationEnd(Animator animator) {
				// Height=0, but it set visibility to GONE
				llIngredients.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}
		});
		mAnimator.start();
	}

	private ValueAnimator slideAnimator(int start, int end) {

		ValueAnimator animator = ValueAnimator.ofInt(start, end);

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// Update Height
				int value = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams layoutParams = llIngredients
						.getLayoutParams();
				layoutParams.height = value;
				llIngredients.setLayoutParams(layoutParams);
			}
		});
		return animator;
	}

	// Expands method during View
	private void expandMethod() {
		// set Visible
		llMethod.setVisibility(View.VISIBLE);

		int stepsLinesCount = llMethod.getChildCount() / 2;

		int finalHeight = 0;

		for (int i = 0; i < stepsLinesCount; ++i) {

			int count = (int) Math.ceil(((TextView) llMethod.getChildAt(i * 2))
					.getPaint().measureText(
							((TextView) llMethod.getChildAt(i * 2)).getText()
									.toString())
					/ getDeviceSize());

			if (stepsLinesCount < 5)
				finalHeight += (count + 1)
						* ((TextView) llMethod.getChildAt(i * 2))
								.getLineHeight() * 0.9;
			else
				finalHeight += (count + 1)
						* ((TextView) llMethod.getChildAt(i * 2))
								.getLineHeight() * 0.9;

			Log.e("error", "1");

			finalHeight += 1;

		}

		ValueAnimator mAnimator = slideAnimatorForMethod(0, finalHeight);

		mAnimator.start();
	}

	private void expandMethodEdit() {
		// set Visible
		llMethod.setVisibility(View.VISIBLE);

		int stepsLinesCount = llMethod.getChildCount();

		Log.e("error", "steps count: " + stepsLinesCount);

		int finalHeight = 0;

		for (int i = 0; i < stepsLinesCount; ++i) {

			int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);
			int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
					View.MeasureSpec.UNSPECIFIED);

			llMethod.getChildAt(i).measure(widthSpec, heightSpec);

			Log.e("error",
					"getMeasuredHeight: "
							+ ((EditText) llMethod.getChildAt(i))
									.getMeasuredHeight());

			int count = (int) Math.ceil(((EditText) llMethod.getChildAt(i))
					.getPaint().measureText(
							((EditText) llMethod.getChildAt(i)).getText()
									.toString())
					/ getDeviceSize());

			Log.e("error", "lines count for each edittext: " + count);

			if (((EditText) llMethod.getChildAt(i)).getText() == null
					|| "".equals(((EditText) llMethod.getChildAt(i)).getText()
							.toString()))
				count = 1;

			if (stepsLinesCount < 5)
				finalHeight += ((EditText) llMethod.getChildAt(i))
						.getMeasuredHeight() * 0.9 * (count + 1);
			else
				finalHeight += ((EditText) llMethod.getChildAt(i))
						.getMeasuredHeight() * 0.9 * (count);

		}

		ValueAnimator mAnimator = slideAnimatorForMethod(0, finalHeight);

		mAnimator.start();
	}

	private void collapseMethod() {
		int finalHeight = llMethod.getHeight();

		ValueAnimator mAnimator = slideAnimatorForMethod(finalHeight, 0);

		mAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationEnd(Animator animator) {
				// Height=0, but it set visibility to GONE
				llMethod.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}
		});
		mAnimator.start();
	}

	private ValueAnimator slideAnimatorForMethod(int start, int end) {

		ValueAnimator animator = ValueAnimator.ofInt(start, end);

		Log.e("error", "ValueAnimator - start, end: " + start + ", " + end);

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// Update Height
				int value = (Integer) valueAnimator.getAnimatedValue();
				ViewGroup.LayoutParams layoutParams = llMethod
						.getLayoutParams();
				layoutParams.height = value;
				llMethod.setLayoutParams(layoutParams);
			}
		});
		return animator;
	}

	private void handleIntent(Intent intent) {

		action = intent.getStringExtra("action");

		if ("View".equals(action) || "Edit".equals(action)) {
			llIngredients.setVisibility(View.GONE);
			llMethod.setVisibility(View.GONE);

			llLblIngredients.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (focusedView != null) {
						View etComplete = reConcatenate(focusedView, replacedll);
						llIngredients.addView(etComplete,
								focusedView.getId() - 1000);
						llIngredients.removeView(replacedll);
						previousFocusedView = null;
						focusedView = null;
						replacedll = null;
					}

					if (llIngredients.getVisibility() == View.GONE) {
						expand();
					} else {
						collapse();
					}
				}
			});

			llLblMethod.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					if (methodFocusedView != null) {
						View mETComplete = reConcatenateMethod(
								methodFocusedView, replacedml);
						llMethod.addView(mETComplete,
								methodFocusedView.getId() - 2000);
						llMethod.removeView(replacedml);
						previousMethodFocusedView = null;
						methodFocusedView = null;
						replacedml = null;
					}

					if (llMethod.getVisibility() == View.GONE) {
						if ("Edit".equals(action)) {
							expandMethodEdit();
						} else {
							expandMethod();
						}
					} else {
						collapseMethod();
					}
				}
			});

			ViewGroup recipeLayout = (ViewGroup) findViewById(R.id.recipeLayout);
			Log.e("error", "RecipeLayout: " + recipeLayout.getChildCount());

		}

		if ("Edit".equals(action)) {

			setTitle("Edit Recipe");

			recipeName = intent.getStringExtra("recipeName");
			TextView recipeNameView = (TextView) findViewById(R.id.recipeName);
			recipeNameView.setText(recipeName);

			DataBaseHelper myDbHelper = new DataBaseHelper(this);
			myDbHelper = new DataBaseHelper(this);
			myDbHelper.openDataBase();

			TextView ethnicity = (TextView) findViewById(R.id.Ethnicity);
			ethnicity.setText(myDbHelper.searchRecipe(recipeName)
					.getEthnicity());
			ethnicity.setOnFocusChangeListener(ingredientLayoutFocusListener);

			Spinner spinner = (Spinner) findViewById(R.id.spinner1);
			spinner.setOnItemSelectedListener(this);
			spinner.setSelection(getIndex(spinner,
					myDbHelper.searchRecipe(recipeName).getPrepTime()));

			String tempIngredientsText = myDbHelper.searchRecipe(recipeName)
					.getIngredients();

			Log.e("error", "handling intent: ingredients: "
					+ tempIngredientsText);

			if (tempIngredientsText != null && !"".equals(tempIngredientsText)) {

				String ingredientsText = tempIngredientsText.replace(",", " ");

				StringTokenizer tokenizerX = new StringTokenizer(
						ingredientsText, ";");

				int i = 0;

				while (tokenizerX.hasMoreTokens()) {

					EditText et = new EditText(this);
					et.setText(tokenizerX.nextToken().trim());
					et.setBackgroundResource(android.R.drawable.editbox_background);
					et.setOnFocusChangeListener(focusListener);
					et.setId(1000 + i);
					llIngredients.addView(et);
					++i;

				}

			} else {
				EditText et = new EditText(this);
				et.setOnFocusChangeListener(focusListener);
				et.setBackgroundResource(android.R.drawable.editbox_background);
				et.setId(1000);
				llIngredients.addView(et);
			}

			String combinedMealType = myDbHelper.searchRecipe(recipeName)
					.getCombinedMealType();
			if (combinedMealType != null) {
				StringTokenizer tokenizer = new StringTokenizer(
						combinedMealType, ",");

				CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
				CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
				CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
				CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);

				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					if ("Breakfast".equalsIgnoreCase(token.trim()))
						checkBox1.setChecked(true);
					if ("Lunch".equalsIgnoreCase(token.trim()))
						checkBox2.setChecked(true);
					if ("Snacks".equalsIgnoreCase(token.trim()))
						checkBox3.setChecked(true);
					if ("Dinner".equalsIgnoreCase(token.trim()))
						checkBox4.setChecked(true);

				}

			}

			String method = myDbHelper.searchRecipe(recipeName).getMethod();

			Log.e("error", "handling intent: method: " + method);

			int terminalHeight = 0;
			if (method != null && !"".equals(method)) {

				Log.e("error", "method is not null not empty");

				StringTokenizer tokenizerY = new StringTokenizer(method, "|");

				int k = 0;

				while (tokenizerY.hasMoreTokens()) {

					EditText et = new EditText(this);
					et.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					et.setText(tokenizerY.nextToken().trim());
					et.setOnFocusChangeListener(methodFocusChangeListener);
					et.setBackgroundResource(android.R.drawable.editbox_background);
					et.setOnKeyListener(methodKeyListener);
					et.setId(2000 + k);
					et.setSingleLine(false);
					et.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_FLAG_MULTI_LINE);

					// int count = (int) Math.ceil(et.getPaint().measureText(
					// et.getText().toString())
					// / getDeviceSize());

					// et.measure(MeasureSpec.makeMeasureSpec(0,
					// MeasureSpec.UNSPECIFIED), MeasureSpec
					// .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

					int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
							View.MeasureSpec.UNSPECIFIED);
					int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
							View.MeasureSpec.UNSPECIFIED);

					et.measure(widthSpec, heightSpec);

					terminalHeight += et.getMeasuredHeight();

					llMethod.addView(et);
					++k;
				}

			} else {

				Log.e("error", "method is null and empty");

				EditText et = new EditText(this);
				et.setBackgroundResource(android.R.drawable.editbox_background);
				et.setOnFocusChangeListener(methodFocusChangeListener);
				et.setOnKeyListener(methodKeyListener);
				et.setId(2000);

				int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);

				et.measure(widthSpec, heightSpec);

				terminalHeight += et.getMeasuredHeight();

				llMethod.addView(et);
			}

			LayoutParams paramsX = new LayoutParams(LayoutParams.MATCH_PARENT,
					(int) (terminalHeight));
			llMethod.setLayoutParams(paramsX);
			llMethod.requestLayout();

		} else if ("View".equals(action)) {

			setTitle("View Recipe");

			LinearLayout recipeLayout = (LinearLayout) findViewById(R.id.recipeLayout);

			recipeName = intent.getStringExtra("recipeName");
			TextView recipeNameView = (TextView) findViewById(R.id.recipeName);
			recipeNameView.setText(recipeName);

			DataBaseHelper myDbHelper = new DataBaseHelper(this);
			myDbHelper = new DataBaseHelper(this);
			myDbHelper.openDataBase();

			EditText ethnicity = (EditText) findViewById(R.id.Ethnicity);
			ethnicity.setVisibility(View.GONE);

			TextView ethnicityText = new TextView(this);
			ethnicityText.setText(myDbHelper.searchRecipe(recipeName)
					.getEthnicity());
			recipeLayout.addView(ethnicityText, 3);

			Spinner spinner = (Spinner) findViewById(R.id.spinner1);
			spinner.setVisibility(View.GONE);

			TextView prepTimeText = new TextView(this);
			prepTimeText.setText(myDbHelper.searchRecipe(recipeName)
					.getPrepTime());
			recipeLayout.addView(prepTimeText, 6);

			String tempIngredientsText = myDbHelper.searchRecipe(recipeName)
					.getIngredients();

			Log.e("error", "handling intent: ingredients: "
					+ tempIngredientsText);

			int i = 0;

			if (tempIngredientsText != null && !"".equals(tempIngredientsText)) {

				String ingredientsText = tempIngredientsText.replace(",", " ");

				StringTokenizer tokenizerX = new StringTokenizer(
						ingredientsText, ";");

				while (tokenizerX.hasMoreTokens()) {

					TextView et = new TextView(this);
					et.setText(tokenizerX.nextToken().trim());
					et.setId(1000 + i);
					llIngredients.addView(et);

					++i;

					View line = new View(this);
					line.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT, 1));
					line.setBackgroundColor(Color.rgb(51, 51, 51));
					llIngredients.addView(line);

					++i;

				}

			}

			String combinedMealType = myDbHelper.searchRecipe(recipeName)
					.getCombinedMealType();

			if (combinedMealType != null) {
				StringTokenizer tokenizer = new StringTokenizer(
						combinedMealType, ",");

				CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
				checkBox1.setVisibility(View.GONE);
				CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
				checkBox2.setVisibility(View.GONE);
				CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
				checkBox3.setVisibility(View.GONE);
				CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
				checkBox4.setVisibility(View.GONE);

				String sumUpMealTypes = "";

				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();

					if ("Breakfast".equalsIgnoreCase(token.trim())) {
						sumUpMealTypes = sumUpMealTypes + "Breakfast" + ", ";
					}
					if ("Lunch".equalsIgnoreCase(token.trim())) {
						sumUpMealTypes = sumUpMealTypes + "Lunch" + ", ";
					}
					if ("Snacks".equalsIgnoreCase(token.trim())) {
						sumUpMealTypes = sumUpMealTypes + "Snacks" + ", ";
					}
					if ("Dinner".equalsIgnoreCase(token.trim())) {
						sumUpMealTypes = sumUpMealTypes + "Dinner" + ", ";
					}
				}

				TextView sumUpMealTypesText = new TextView(this);
				sumUpMealTypesText.setText(sumUpMealTypes);
				recipeLayout.addView(sumUpMealTypesText, 11);

			} else {

				CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
				checkBox1.setVisibility(View.GONE);
				CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
				checkBox2.setVisibility(View.GONE);
				CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
				checkBox3.setVisibility(View.GONE);
				CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
				checkBox4.setVisibility(View.GONE);

			}

			String method = myDbHelper.searchRecipe(recipeName).getMethod();

			Log.e("error", "handling intent: method: " + method);

			int terminalHeight = 0;
			if (method != null && !"".equals(method)) {

				Log.e("error", "method is not null not empty");

				StringTokenizer tokenizerY = new StringTokenizer(method, "|");

				int k = 0;
				while (tokenizerY.hasMoreTokens()) {

					final TextView et = new TextView(this);
					et.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					et.setText(tokenizerY.nextToken().trim());
					et.setId(2000 + k);
					et.setSingleLine(false);
					et.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_FLAG_MULTI_LINE);

					int count = (int) Math.ceil(et.getPaint().measureText(
							et.getText().toString())
							/ getDeviceSize());

					et.measure(MeasureSpec.makeMeasureSpec(0,
							MeasureSpec.UNSPECIFIED), MeasureSpec
							.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

					terminalHeight += (count + 1) * et.getLineHeight() * 0.9;
					Log.e("error", "terminalHeight: " + terminalHeight);
					llMethod.addView(et);
					++k;

					View line = new View(this);
					line.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT, 1));
					line.setBackgroundColor(Color.rgb(51, 51, 51));
					terminalHeight += 1;
					llMethod.addView(line);

					++k;

				}

			}

			LayoutParams paramsX = new LayoutParams(LayoutParams.MATCH_PARENT,
					terminalHeight);
			llMethod.setLayoutParams(paramsX);
			llMethod.requestLayout();

			// Start - Add some space between "method" and the "Edit" button

			View spaceView = new View(this);
			spaceView.setPadding(0, 20, 0, 20);

			recipeLayout.addView(spaceView);

			// End - Add some space between "method" and the "Edit" button

			Button saveButton = (Button) findViewById(R.id.lblSaveMeal);
			saveButton.setVisibility(View.GONE);

			Button saveUpperButton = (Button) findViewById(R.id.lblSaveMealUpper);
			saveUpperButton.setVisibility(View.GONE);

			LinearLayout buttonLayout = new LinearLayout(this);

			Button editButton = new Button(this);
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			editButton.setLayoutParams(params);
			editButton.setText("Edit");
			editButton.setOnClickListener(editButtonListener);
			buttonLayout.addView(editButton);

			// Start - Comment "Send" button
			// Button sendButton = new Button(this);
			// sendButton.setLayoutParams(params);
			// sendButton.setText("Send");
			// sendButton.setOnClickListener(sendButtonListener);
			// buttonLayout.addView(sendButton);
			// end - Comment "Send" button

			recipeLayout.addView(buttonLayout);

			LinearLayout buttonLayout1 = (LinearLayout) findViewById(R.id.buttonLayout1);

			Button editButton1 = new Button(this);
			LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			editButton1.setLayoutParams(params1);
			editButton1.setText("Edit");
			editButton1.setOnClickListener(editButtonListener);
			buttonLayout1.addView(editButton1);

			// Start - Comment "Send" button
			// Button sendButton1 = new Button(this);
			// sendButton1.setLayoutParams(params1);
			// sendButton1.setText("Send");
			// sendButton1.setOnClickListener(sendButtonListener);
			// buttonLayout1.addView(sendButton1);
			// End - Comment "Send" button

		}

		Log.e("error", "llMethod.getChildCount: " + llMethod.getChildCount());
	}

	OnClickListener sendButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("error", "inside send");

			Intent intent = new Intent(getApplicationContext(),
					RecipeSendActivity.class);
			intent.putExtra("recipeName", recipeName);
			startActivityForResult(intent, 0);

		}
	};

	OnClickListener editButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Log.e("error", "inside edit");

			Intent intent = new Intent(getApplicationContext(),
					RecipeEditActivity.class);
			intent.putExtra("recipeName", recipeName);
			intent.putExtra("action", "Edit");
			startActivityForResult(intent, 0);

		}
	};

	private int getIndex(Spinner spinner, String myString) {

		int index = 0;

		for (int i = 0; i < spinner.getCount(); i++) {
			if (spinner.getItemAtPosition(i).equals(myString)) {
				index = i;
			}
		}
		return index;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		handleIntent(intent);
	}

	public void onSaveRecipeButtonClick(View view) {

		if (focusedView != null) {
			View etComplete = reConcatenate(focusedView, replacedll);
			llIngredients.addView(etComplete, focusedView.getId() - 1000);
			llIngredients.removeView(replacedll);
			previousFocusedView = null;
			focusedView = null;
			replacedll = null;
		}

		if (methodFocusedView != null) {
			View mETComplete = reConcatenateMethod(methodFocusedView,
					replacedml);
			llMethod.addView(mETComplete, methodFocusedView.getId() - 2000);
			llMethod.removeView(replacedml);
			previousMethodFocusedView = null;
			methodFocusedView = null;
			replacedml = null;
		}

		TextView recipeNameView = (TextView) findViewById(R.id.recipeName);
		String recipeName = recipeNameView.getText().toString().trim();

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		myDbHelper = new DataBaseHelper(this);
		myDbHelper.openDataBase();

		TextView ethnicityView = (TextView) findViewById(R.id.Ethnicity);

		// start - Replace apostrophe with \'

		String aTempStr = ethnicityView.getText().toString().trim();
		String anUpdatedStr = "";

		if (aTempStr.contains("'"))
			anUpdatedStr = aTempStr.replace("'", "''");
		else
			anUpdatedStr = aTempStr;

		// end - Replace apostrophe with \'

		String ethnicity = anUpdatedStr;

		String prepTime = mPrepTime;

		int count = llIngredients.getChildCount();
		EditText v = null;
		String ingredients = "";
		for (int i = 0; i < count; i++) {
			v = (EditText) llIngredients.getChildAt(i);

			// start - Replace apostrophe with \'

			String tempStr = v.getText().toString().trim();
			String updatedStr = "";

			if (tempStr.contains("'"))
				updatedStr = tempStr.replace("'", "''");
			else
				updatedStr = tempStr;
			// end - Replace apostrophe with \'

			ingredients = ingredients + updatedStr + ";";
		}

		CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
		CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
		CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);

		String mealType = "";

		if (checkBox1.isChecked())
			mealType = mealType + checkBox1.getText();
		if (checkBox2.isChecked())
			mealType = mealType + "," + checkBox2.getText();
		if (checkBox3.isChecked())
			mealType = mealType + "," + checkBox3.getText();
		if (checkBox4.isChecked())
			mealType = mealType + "," + checkBox4.getText();

		int mlCount = llMethod.getChildCount();
		EditText mlv = null;
		String method = "";
		for (int i = 0; i < mlCount; i++) {
			mlv = (EditText) llMethod.getChildAt(i);

			// start - Replace apostrophe with \'

			String tempStr = mlv.getText().toString().trim();
			String updatedStr = "";

			if (tempStr.contains("'"))
				updatedStr = tempStr.replace("'", "''");
			else
				updatedStr = tempStr;
			// end - Replace apostrophe with \'

			method = method + updatedStr + "|";
		}

		myDbHelper.saveRecipe(recipeName, ethnicity, prepTime, ingredients,
				mealType, method);

		Toast.makeText(this, "The recipe saved successfully.",
				Toast.LENGTH_SHORT).show();

		finish();
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// On selecting a spinner item
		String item = parent.getItemAtPosition(position).toString();

		mPrepTime = item;

	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	private OnFocusChangeListener ingredientLayoutFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			Log.e("error", "ingredientLayoutFocusListener");

			// TODO Auto-generated method stub
			if (focusedView != null) {
				View etComplete = reConcatenate(focusedView, replacedll);
				llIngredients.addView(etComplete, focusedView.getId() - 1000);
				llIngredients.removeView(replacedll);
				previousFocusedView = null;
				focusedView = null;
				replacedll = null;
			}

			if (methodFocusedView != null) {
				View mETComplete = reConcatenateMethod(methodFocusedView,
						replacedml);
				llMethod.addView(mETComplete, methodFocusedView.getId() - 2000);
				llMethod.removeView(replacedml);
				previousMethodFocusedView = null;
				methodFocusedView = null;
				replacedml = null;
			}

		}
	};

	private OnFocusChangeListener focusListener = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			Log.e("error", "focusListener is called" + v.getId() + "hasFocus: "
					+ hasFocus);

			if (methodFocusedView != null) {

				Log.e("error",
						"Completing previousMethodFocusedView action on Focus change");
				View mETComplete = reConcatenateMethod(methodFocusedView,
						replacedml);
				llMethod.addView(mETComplete, methodFocusedView.getId() - 2000);
				llMethod.removeView(replacedml);
				replacedml = null;
				previousMethodFocusedView = null;
				methodFocusedView = null;
			}

			int id = v.getId();

			if (hasFocus) {

				Log.e("error", "checking if this statement is called");

				previousFocusedView = focusedView;
				focusedView = v;

				if (previousFocusedView != null) {

					View etComplete = reConcatenate(previousFocusedView,
							replacedll);
					llIngredients.addView(etComplete,
							previousFocusedView.getId() - 1000);
					llIngredients.removeView(replacedll);

				}

				String ingredientText = ((EditText) v).getText().toString();
				String ingredientUnit = "";
				String integratedTokenMinusUnit = "";
				String ingredientName = "";
				String ingredientQuantity = "";

				if (ingredientText != null && !"".equals(ingredientText.trim())) {
					ingredientUnit = ingredientText.substring(
							ingredientText.lastIndexOf(" ")).trim();
					integratedTokenMinusUnit = ingredientText.substring(0,
							ingredientText.lastIndexOf(" ")).trim();
					ingredientQuantity = integratedTokenMinusUnit.substring(
							integratedTokenMinusUnit.lastIndexOf(" ")).trim();
					ingredientName = integratedTokenMinusUnit.substring(0,
							integratedTokenMinusUnit.lastIndexOf(" ")).trim();
				}

				replacedll = new LinearLayout(getApplicationContext());
				replacedll.setOrientation(LinearLayout.HORIZONTAL);

				EditText et1 = new EditText(getApplicationContext());
				et1.setText(ingredientName);
				et1.setBackgroundResource(android.R.drawable.editbox_background);
				et1.setTextColor(Color.BLACK);
				et1.setMaxWidth(300);
				replacedll.addView(et1);

				EditText et2 = new EditText(getApplicationContext());
				et2.setText(ingredientQuantity);
				et2.setBackgroundResource(android.R.drawable.editbox_background);
				et2.setTextColor(Color.BLACK);
				et2.setInputType(InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL);
				replacedll.addView(et2);

				String[] unit_array = getResources().getStringArray(
						R.array.unit_array);
				List<String> unitList = Arrays.asList(unit_array);
				Spinner spinner = new Spinner(getApplicationContext());
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
						getApplicationContext(),
						android.R.layout.simple_spinner_dropdown_item, unitList);
				spinner.setAdapter(spinnerArrayAdapter);
				spinner.setBackgroundResource(android.R.drawable.editbox_background);
				spinner.setLayoutParams(new LayoutParams(150, 78));
				int spinnerPosition = spinnerArrayAdapter
						.getPosition(ingredientUnit);
				spinner.setSelection(spinnerPosition);
				spinner.setOnItemSelectedListener(OnUnitSpinnerListener);
				replacedll.addView(spinner);

				ImageView plus = new ImageView(getApplicationContext());
				plus.setImageResource(R.drawable.plus_sign);
				LinearLayout.LayoutParams layoutParamsPlus = new LinearLayout.LayoutParams(
						60, 60);
				layoutParamsPlus.setMargins(20, 2, 10, 2);
				plus.setLayoutParams(layoutParamsPlus);
				plus.setOnClickListener(plusIngredientsListener);
				replacedll.addView(plus);

				ImageView minus = new ImageView(getApplicationContext());
				minus.setImageResource(R.drawable.icon_minus);
				LinearLayout.LayoutParams layoutParamsMinus = new LinearLayout.LayoutParams(
						60, 60);
				layoutParamsMinus.setMargins(20, 2, 10, 2);
				minus.setLayoutParams(layoutParamsMinus);
				minus.setOnClickListener(minusIngredientsListener);
				replacedll.addView(minus);

				replacedll.setId(id);

				llIngredients.addView(replacedll, id - 1000);

				if (focusedView != null)
					Log.e("error", "focusedViewId" + focusedView.getId());

				llIngredients.removeView(v);

			} else {

			}
		}
	};

	public View reConcatenate(View view, ViewGroup replacedll) {

		String et1Text = ((EditText) (replacedll.getChildAt(0))).getText()
				.toString();
		String et2Text = ((EditText) (replacedll.getChildAt(1))).getText()
				.toString();
		String et3Text = "";
		if (et1Text != null && !"".equals(et1Text.trim()) && et2Text != null
				&& !"".equals(et2Text.trim())) {
			et3Text = ((Spinner) (replacedll.getChildAt(2))).getSelectedItem()
					.toString();
		}

		EditText etComplete = new EditText(getApplicationContext());
		etComplete.setText(et1Text + " " + et2Text + " " + et3Text);
		etComplete.setBackgroundResource(android.R.drawable.editbox_background);
		etComplete.setId(view.getId());
		etComplete.setTextColor(Color.BLACK);
		etComplete.setOnFocusChangeListener(focusListener);

		return etComplete;

	}

	public View reConcatenateMethod(View view, ViewGroup replacedll) {

		String et1Text = ((EditText) (replacedll.getChildAt(0))).getText()
				.toString();

		EditText etComplete = new EditText(getApplicationContext());
		etComplete.setText(et1Text);
		etComplete.setBackgroundResource(android.R.drawable.editbox_background);
		etComplete.setId(view.getId());
		etComplete.setTextColor(Color.BLACK);
		etComplete.setOnFocusChangeListener(methodFocusChangeListener);

		return etComplete;

	}

	private OnFocusChangeListener methodFocusChangeListener = new OnFocusChangeListener() {
		public void onFocusChange(View v, boolean hasFocus) {
			Log.e("error", "methodFocusListener is called" + v.getId());

			if (focusedView != null) {

				Log.e("error",
						"Completing previousFocusedView action on Focus change");
				View etComplete = reConcatenate(focusedView, replacedll);
				llIngredients.addView(etComplete, focusedView.getId() - 1000);
				llIngredients.removeView(replacedll);
				replacedll = null;
				previousFocusedView = null;
				focusedView = null;
			}

			int id = v.getId();

			if (hasFocus) {
				previousMethodFocusedView = methodFocusedView;
				methodFocusedView = v;

				if (previousMethodFocusedView != null) {

					View mETComplete = reConcatenateMethod(
							previousMethodFocusedView, replacedml);
					llMethod.addView(mETComplete,
							previousMethodFocusedView.getId() - 2000);
					llMethod.removeView(replacedml);

				}

				String methodText = ((EditText) v).getText().toString();

				replacedml = new LinearLayout(getApplicationContext());
				replacedml.setOrientation(LinearLayout.HORIZONTAL);

				EditText et1 = new EditText(getApplicationContext());
				et1.setText(methodText);
				et1.setBackgroundResource(android.R.drawable.editbox_background);
				et1.setTextColor(Color.BLACK);
				et1.setMaxWidth(500);
				replacedml.addView(et1);

				ImageView plus = new ImageView(getApplicationContext());
				plus.setImageResource(R.drawable.plus_sign);
				LinearLayout.LayoutParams layoutParamsPlus = new LinearLayout.LayoutParams(
						60, 60);
				layoutParamsPlus.setMargins(20, 2, 10, 2);
				plus.setLayoutParams(layoutParamsPlus);
				plus.setOnClickListener(plusMethodListener);
				replacedml.addView(plus);

				ImageView minus = new ImageView(getApplicationContext());
				minus.setImageResource(R.drawable.icon_minus);
				LinearLayout.LayoutParams layoutParamsMinus = new LinearLayout.LayoutParams(
						60, 60);
				layoutParamsMinus.setMargins(20, 2, 10, 2);
				minus.setLayoutParams(layoutParamsMinus);
				minus.setOnClickListener(minusMethodListener);
				replacedml.addView(minus);

				replacedml.setId(id);

				llMethod.addView(replacedml, id - 2000);

				if (methodFocusedView != null)
					Log.e("error",
							"methodFocusedViewId" + methodFocusedView.getId());

				llMethod.removeView(v);

				Log.e("error", "methodFocusedView id is" + v.getId());
			} else {

			}
		}
	};

	private OnKeyListener methodKeyListener = new View.OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {

			Log.e("error", "onKey event is called.");

			// TODO Auto-generated method stub
			if ((event.getAction() == KeyEvent.ACTION_DOWN)
					&& (keyCode == KeyEvent.KEYCODE_ENTER)) {

				// Reassign IDs
				if (v != null) {
					int viewCount = ((ViewGroup) v.getParent()).getChildCount();

					for (int methodIndex = 0; methodIndex < viewCount; ++methodIndex) {
						(((ViewGroup) v.getParent()).getChildAt(methodIndex))
								.setId(2000 + methodIndex);
					}
				}
				// Reassign IDs

				EditText insertedEt = new EditText(getApplicationContext());
				insertedEt.setOnFocusChangeListener(methodFocusChangeListener);
				insertedEt.setOnKeyListener(methodKeyListener);
				insertedEt.setId(2000 + methodIndexK);

				Log.e("error", "Current Method Focussed View Id" + v.getId());

				((ViewGroup) v.getParent()).addView(insertedEt,
						v.getId() - 2000 + 1);

				// Reassign IDs
				int viewCount = ((ViewGroup) v.getParent()).getChildCount();

				for (int methodIndex = 0; methodIndex < viewCount; ++methodIndex) {
					(((ViewGroup) v.getParent()).getChildAt(methodIndex))
							.setId(2000 + methodIndex);
				}
				// Reassign IDs
				insertedEt.requestFocus();

				++methodIndexK;

				return true;
			}
			return false;

		}
	};

	protected void onResume() {

		Log.e("error", "OnResume");
		super.onResume();

		llIngredients = (ViewGroup) findViewById(R.id.ingredientsLayout);
		llIngredients.setOnFocusChangeListener(ingredientLayoutFocusListener);

		if ("View".equals(action))
			refreshView();

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
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				Log.e("error", "onDrawerOpened");
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	};

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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

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

	OnClickListener plusIngredientsListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			// Reassign Ids
			int viewCount = (llIngredients).getChildCount();

			for (int ingredientsIndex = 0; ingredientsIndex < viewCount; ++ingredientsIndex) {
				(((ViewGroup) llIngredients).getChildAt(ingredientsIndex))
						.setId(1000 + ingredientsIndex);
			}
			// Reassign Ids

			if (focusedView != null) {

				EditText newFlatIngredient = new EditText(
						getApplicationContext());
				newFlatIngredient
						.setBackgroundResource(android.R.drawable.editbox_background);
				newFlatIngredient.setTextColor(Color.BLACK);
				newFlatIngredient.setOnFocusChangeListener(focusListener);
				llIngredients.addView(newFlatIngredient, focusedView.getId()
						- (1000 - 1));

				// Reassign Ids
				int viewCountUpdated = (llIngredients).getChildCount();

				for (int ingredientsIndex = 0; ingredientsIndex < viewCountUpdated; ++ingredientsIndex) {
					((llIngredients).getChildAt(ingredientsIndex))
							.setId(1000 + ingredientsIndex);
				}
				// Reassign Ids

				int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);

				newFlatIngredient.measure(widthSpec, heightSpec);

				int terminalHeight = llIngredients.getHeight()
						+ newFlatIngredient.getMeasuredHeight();

				LayoutParams paramsX = new LayoutParams(
						LayoutParams.MATCH_PARENT, (int) (terminalHeight));
				llIngredients.setLayoutParams(paramsX);
				llIngredients.requestLayout();

			}

		}
	};

	OnClickListener plusMethodListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			// Reassign Ids
			int viewCount = (llMethod).getChildCount();

			for (int methodIndex = 0; methodIndex < viewCount; ++methodIndex) {
				(((ViewGroup) llMethod).getChildAt(methodIndex))
						.setId(2000 + methodIndex);
			}
			// Reassign Ids

			if (methodFocusedView != null) {

				EditText newFlatMethod = new EditText(getApplicationContext());
				newFlatMethod
						.setBackgroundResource(android.R.drawable.editbox_background);
				newFlatMethod.setTextColor(Color.BLACK);
				newFlatMethod
						.setOnFocusChangeListener(methodFocusChangeListener);
				newFlatMethod.setSingleLine(false);
				newFlatMethod.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_FLAG_MULTI_LINE);

				llMethod.addView(newFlatMethod, methodFocusedView.getId()
						- (2000 - 1));

				// Reassign Ids
				int viewCountUpdated = (llMethod).getChildCount();

				for (int methodIndex = 0; methodIndex < viewCountUpdated; ++methodIndex) {
					((llMethod).getChildAt(methodIndex))
							.setId(2000 + methodIndex);
				}
				// Reassign Ids

				int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);

				newFlatMethod.measure(widthSpec, heightSpec);

				int terminalHeight = llMethod.getHeight()
						+ newFlatMethod.getMeasuredHeight();

				LayoutParams paramsX = new LayoutParams(
						LayoutParams.MATCH_PARENT, (int) (terminalHeight));
				llMethod.setLayoutParams(paramsX);
				llMethod.requestLayout();

				// if (methodFocusedView != null) {
				// View mETComplete = reConcatenateMethod(
				// methodFocusedView, replacedml);
				// llMethod.addView(mETComplete,
				// methodFocusedView.getId() - 2000);
				// llMethod.removeView(replacedml);
				// previousMethodFocusedView = null;
				// methodFocusedView = null;
				// replacedml = null;
				// }
				//
				// expandMethodEdit();
			}

		}

	};

	OnClickListener minusIngredientsListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			llIngredients.removeView(replacedll);

			// Reassign Ids
			int viewCountUpdated = (llIngredients).getChildCount();

			for (int ingredientsIndex = 0; ingredientsIndex < viewCountUpdated; ++ingredientsIndex) {
				((llIngredients).getChildAt(ingredientsIndex))
						.setId(1000 + ingredientsIndex);
			}
			// Reassign Ids

			focusedView = null;
		}
	};

	OnClickListener minusMethodListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			llMethod.removeView(replacedml);

			// Reassign Ids
			int viewCountUpdated = (llMethod).getChildCount();

			for (int methodIndex = 0; methodIndex < viewCountUpdated; ++methodIndex) {
				((llMethod).getChildAt(methodIndex)).setId(2000 + methodIndex);
			}
			// Reassign Ids

			methodFocusedView = null;
		}
	};

	private OnItemSelectedListener OnUnitSpinnerListener = new AdapterView.OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView) parent.getChildAt(0)).setTextSize(15);
		}

		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	private void refreshView() {

		// start - debug for "method" label disappearance

		llIngredients.setVisibility(View.GONE);
		llMethod.setVisibility(View.GONE);

		llLblIngredients.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (focusedView != null) {
					View etComplete = reConcatenate(focusedView, replacedll);
					llIngredients.addView(etComplete,
							focusedView.getId() - 1000);
					llIngredients.removeView(replacedll);
					previousFocusedView = null;
					focusedView = null;
					replacedll = null;
				}

				if (llIngredients.getVisibility() == View.GONE) {
					expand();
				} else {
					collapse();
				}
			}
		});

		llLblMethod.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (methodFocusedView != null) {
					View mETComplete = reConcatenateMethod(methodFocusedView,
							replacedml);
					llMethod.addView(mETComplete,
							methodFocusedView.getId() - 2000);
					llMethod.removeView(replacedml);
					previousMethodFocusedView = null;
					methodFocusedView = null;
					replacedml = null;
				}

				if (llMethod.getVisibility() == View.GONE) {
					if ("Edit".equals(action)) {
						expandMethodEdit();
					} else {
						expandMethod();
					}
				} else {
					collapseMethod();
				}
			}
		});

		// ViewGroup recipeLayout = (ViewGroup) findViewById(R.id.recipeLayout);
		// Log.e("error", "RecipeLayout: " + recipeLayout.getChildCount());

		// End - debug for "method" label disappearance

		setTitle("View Recipe");
		LinearLayout recipeLayout = (LinearLayout) findViewById(R.id.recipeLayout);

		TextView recipeNameView = (TextView) findViewById(R.id.recipeName);
		recipeNameView.setText(recipeName);

		DataBaseHelper myDbHelper = new DataBaseHelper(this);
		myDbHelper = new DataBaseHelper(this);
		myDbHelper.openDataBase();

		recipeLayout.removeViewAt(3);

		TextView ethnicityText = new TextView(this);
		ethnicityText.setText(myDbHelper.searchRecipe(recipeName)
				.getEthnicity());
		recipeLayout.addView(ethnicityText, 3);

		recipeLayout.removeViewAt(6);

		TextView prepTimeText = new TextView(this);
		prepTimeText.setText(myDbHelper.searchRecipe(recipeName).getPrepTime());
		recipeLayout.addView(prepTimeText, 6);

		String tempIngredientsText = myDbHelper.searchRecipe(recipeName)
				.getIngredients();

		Log.e("error", "handling intent: ingredients: " + tempIngredientsText);

		llIngredients.removeAllViews();

		int i = 0;

		if (tempIngredientsText != null && !"".equals(tempIngredientsText)) {

			String ingredientsText = tempIngredientsText.replace(",", " ");

			StringTokenizer tokenizerX = new StringTokenizer(ingredientsText,
					";");

			while (tokenizerX.hasMoreTokens()) {

				TextView et = new TextView(this);
				et.setText(tokenizerX.nextToken().trim());
				et.setId(1000 + i);
				llIngredients.addView(et);

				++i;

				View line = new View(this);
				line.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, 1));
				line.setBackgroundColor(Color.rgb(51, 51, 51));
				llIngredients.addView(line);

				++i;

			}

		}

		String combinedMealType = myDbHelper.searchRecipe(recipeName)
				.getCombinedMealType();

		if (combinedMealType != null)
			recipeLayout.removeViewAt(11);

		if (combinedMealType != null) {
			StringTokenizer tokenizer = new StringTokenizer(combinedMealType,
					",");

			String sumUpMealTypes = "";

			while (tokenizer.hasMoreTokens()) {
				String token = tokenizer.nextToken();

				if ("Breakfast".equalsIgnoreCase(token.trim())) {
					sumUpMealTypes = sumUpMealTypes + "Breakfast" + ", ";
				}
				if ("Lunch".equalsIgnoreCase(token.trim())) {
					sumUpMealTypes = sumUpMealTypes + "Lunch" + ", ";
				}
				if ("Snacks".equalsIgnoreCase(token.trim())) {
					sumUpMealTypes = sumUpMealTypes + "Snacks" + ", ";
				}
				if ("Dinner".equalsIgnoreCase(token.trim())) {
					sumUpMealTypes = sumUpMealTypes + "Dinner" + ", ";
				}
			}

			TextView sumUpMealTypesText = new TextView(this);
			sumUpMealTypesText.setText(sumUpMealTypes);
			recipeLayout.addView(sumUpMealTypesText, 11);

		}

		llMethod.removeAllViews();

		String method = myDbHelper.searchRecipe(recipeName).getMethod();

		Log.e("error", "handling intent: method: " + method);

		if (method != null && !"".equals(method)) {

			Log.e("error", "method is not null not empty");

			StringTokenizer tokenizerY = new StringTokenizer(method, "|");

			int k = 0;

			while (tokenizerY.hasMoreTokens()) {

				TextView et = new TextView(this);
				et.setText(tokenizerY.nextToken().trim());
				et.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				et.setId(2000 + k);
				llMethod.addView(et);
				++k;

				View line = new View(this);
				line.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, 1));
				line.setBackgroundColor(Color.rgb(51, 51, 51));
				llMethod.addView(line);

				++k;

			}

			Log.e("error",
					"llMethod.getChildCount: " + llMethod.getChildCount());
		}

		// =====================================================================================

		if (llIngredients.getVisibility() == View.VISIBLE) {
			expand();
		}

		if (llMethod.getVisibility() == View.VISIBLE) {
			expandMethod();
		}

		// =====================================================================================

		Log.e("error",
				"RecipeLayout in Resume: " + recipeLayout.getChildCount());

	}

	@Override
	public void onBackPressed() {

		if ("Edit".equals(action)) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to exit?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									RecipeEditActivity.this.finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

		} else {
			super.onBackPressed();
		}
	}

	private float getDeviceSize() {
		// TODO Auto-generated method stub
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int width = size.x;
		int height = size.y;

		return width;
	}

}
