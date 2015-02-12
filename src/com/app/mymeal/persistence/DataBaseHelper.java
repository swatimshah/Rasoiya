package com.app.mymeal.persistence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.app.mymeal.data.Meal;
import com.app.mymeal.data.MealSuggestion;
import com.app.mymeal.data.Recipe;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.SyncStateContract.Columns;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String MEAL_TABLE = "MEAL";

	private static final String DATE = "DATE";

	private static final String MEAL_TYPE = "MEAL_TYPE";

	private static final String MEAL = "MEAL";

	private static final String MEAL_ITEM_TABLE = "MEAL_ITEM";

	private static final String ITEM = "ITEM";

	private static final String ETHNICITY = "ETHNICITY";

	private static final String PREP_TIME = "PREP_TIME";

	private static final String INGREDIENTS = "INGREDIENTS";

	private static final String INGREDIENTS_LIST = "INGREDIENTS_LIST";

	private static final String COMBINED_MEAL_TYPE = "COMBINED_MEAL_TYPE";

	private static final String INGREDIENT = "INGREDIENT";

	private static final String METHOD = "METHOD";

	private static final String QUANTITY = "QUANTITY";

	private static final String UNIT = "UNIT";

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/com.app.mymeal/databases/";

	private static String DB_NAME = "Meal";

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelper(Context context) {

		super(context, DB_NAME, null, 1);
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			this.getReadableDatabase();

			try {

				copyDataBase();

			} catch (IOException e) {

				Log.e("error", "Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void createTables() {

		String sql = "create table " + MEAL_TABLE + "( " + BaseColumns._ID
				+ " integer primary key autoincrement, " + DATE
				+ " date not null, " + MEAL_TYPE + " text not null, " + MEAL
				+ " text not null)";
		Log.e("error", sql);
		myDataBase.execSQL(sql);

		String sql1 = "create table " + MEAL_ITEM_TABLE + "( "
				+ BaseColumns._ID + " integer primary key autoincrement, "
				+ ITEM + " text not null, " + ETHNICITY + " text null, "
				+ PREP_TIME + " text null, " + INGREDIENTS_LIST
				+ " text null, " + COMBINED_MEAL_TYPE + " text null, " + DATE
				+ " date null, " + METHOD + " text null " + ")";
		Log.e("error", sql1);
		myDataBase.execSQL(sql1);

		String sql2 = "create table " + INGREDIENTS + "( " + BaseColumns._ID
				+ " integer primary key autoincrement, " + INGREDIENT
				+ " text not null, " + QUANTITY + " float null, " + UNIT
				+ " text null" + ")";
		Log.e("error", sql2);
		myDataBase.execSQL(sql2);

	}

	public void saveMeal(String date, String mealType, String meal) {
		// TODO Auto-generated method stub

		StringTokenizer tokenizer = new StringTokenizer(meal.trim(), ",");
		String sql = "";
		String sql1 = "";

		while (tokenizer.hasMoreTokens()) {

			String token = tokenizer.nextToken().trim();

			// sql2 = "SELECT " + ITEM + " from " + MEAL_ITEM_TABLE;

			Cursor cursor = myDataBase.query(MEAL_ITEM_TABLE,
					new String[] { ITEM }, null, null, null, null, null);

			Set<String> mealItemsSet = new HashSet<String>();

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						String mealItem = cursor.getString(cursor
								.getColumnIndex(ITEM));
						mealItemsSet.add(mealItem);
					} while (cursor.moveToNext());
				}
			}
			cursor.close();

			if (mealItemsSet.contains(token)) {

				Cursor cursor2 = myDataBase.query(MEAL_ITEM_TABLE,
						new String[] { COMBINED_MEAL_TYPE }, ITEM + "='"
								+ token + "'", null, null, null, null);

				String combinedMealType = null;

				if (cursor2 != null) {
					if (cursor2.moveToFirst()) {
						do {
							combinedMealType = cursor2.getString(cursor2
									.getColumnIndex(COMBINED_MEAL_TYPE));
						} while (cursor2.moveToNext());
					}
				}

				cursor2.close();

				String totalMealType = "";
				String myMealType = "";

				if ("B".equals(mealType))
					myMealType = "Breakfast";
				if ("L".equals(mealType))
					myMealType = "Lunch";
				if ("S".equals(mealType))
					myMealType = "Snacks";
				if ("D".equals(mealType))
					myMealType = "Dinner";

				if (combinedMealType != null
						&& combinedMealType.toLowerCase().indexOf(
								myMealType.toLowerCase()) != -1)
					totalMealType = combinedMealType;
				else {
					StringTokenizer tokenizer2 = new StringTokenizer(
							combinedMealType, ",");
					ArrayList<String> mealTypeList = new ArrayList<String>();
					while (tokenizer2.hasMoreTokens()) {
						mealTypeList.add(tokenizer2.nextToken());
					}

					if ("B".equals(mealType))
						mealTypeList.add("Breakfast");
					if ("L".equals(mealType))
						mealTypeList.add("Lunch");
					if ("S".equals(mealType))
						mealTypeList.add("Snacks");
					if ("D".equals(mealType))
						mealTypeList.add("Dinner");

					for (String aMealType : mealTypeList) {
						totalMealType = totalMealType + "," + aMealType;
					}
				}

				Log.e("error", "totalMealType: " + totalMealType);

				sql1 = "UPDATE " + MEAL_ITEM_TABLE + " SET " + DATE + "='"
						+ date + "', " + COMBINED_MEAL_TYPE + "='"
						+ totalMealType + "'" + " where " + ITEM + "='" + token
						+ "';";

				Log.e("error", sql1);
				myDataBase.execSQL(sql1);
			} else {

				String expandedMealType = "";

				if ("B".equals(mealType))
					expandedMealType = "Breakfast";
				if ("L".equals(mealType))
					expandedMealType = "Lunch";
				if ("S".equals(mealType))
					expandedMealType = "Snacks";
				if ("D".equals(mealType))
					expandedMealType = "Dinner";

				Log.e("error", "expandedMealType: " + expandedMealType);

				sql1 = "INSERT INTO " + MEAL_ITEM_TABLE + " ( " + ITEM + ","
						+ DATE + "," + COMBINED_MEAL_TYPE + ") VALUES ( '"
						+ token + "','" + date + "', '" + expandedMealType
						+ "');";

				Log.e("error", sql1);
				myDataBase.execSQL(sql1);
			}

			Cursor cursor1 = myDataBase.query(MEAL_ITEM_TABLE,
					new String[] { BaseColumns._ID },
					ITEM + "='" + token + "'", null, null, null, null);

			String mealId = "";

			if (cursor1 != null) {
				if (cursor1.moveToFirst()) {
					mealId = cursor1.getString(cursor1
							.getColumnIndex(BaseColumns._ID));
				}
			}

			cursor1.close();

			sql = "INSERT INTO " + MEAL_TABLE + " ( " + DATE + " ," + MEAL_TYPE
					+ " ," + MEAL + " )" + " VALUES ( '" + date + "' , '"
					+ mealType + "' , '" + mealId + "');";

			Log.e("error", sql);
			myDataBase.execSQL(sql);

		}

	}

	public String searchMeal(String date, String mealType) {
		// TODO Auto-generated method stub

		Log.e("error", "SearchMeal");
		Log.e("error", date);
		Log.e("error", mealType);

		String meal = "";
		String mealId = "";

		Cursor cursor = myDataBase.query(MEAL_TABLE, new String[] { MEAL },
				DATE + "='" + date + "' AND " + MEAL_TYPE + "= '" + mealType
						+ "'", null, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					mealId = cursor.getString(cursor.getColumnIndex(MEAL));

					Log.e("error", mealId);

					Cursor cursor1 = myDataBase.query(MEAL_ITEM_TABLE,
							new String[] { ITEM }, BaseColumns._ID + "='"
									+ mealId + "'", null, null, null, null);

					String mealItem = "";

					if (cursor1 != null) {
						if (cursor1.moveToFirst()) {
							mealItem = cursor1.getString(cursor1
									.getColumnIndex(ITEM));
						}
					}

					cursor1.close();

					Log.e("error", mealItem);

					meal = meal + mealItem + ", ";

				} while (cursor.moveToNext());
			}
		}
		cursor.close();

		return meal;
	}

	public ArrayList<String> getCalendarItemsWithMealEntry(String firstDay,
			String lastDay) {
		// TODO Auto-generated method stub
		ArrayList<String> items = new ArrayList<String>();

		Cursor cursor = myDataBase.query(MEAL_TABLE, new String[] { DATE },
				DATE + "  BETWEEN '" + firstDay + "' AND '" + lastDay + "'",
				null, null, null, null);

		Set<String> expandedDateItemsSet = new HashSet<String>();

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					String dateItem = cursor.getString(cursor
							.getColumnIndex(DATE));
					expandedDateItemsSet.add(dateItem);
				} while (cursor.moveToNext());
			}
		}
		cursor.close();

		for (String date : expandedDateItemsSet) {
			Log.e("error",
					"date substring: "
							+ date.substring(date.lastIndexOf("-") + 1));
			items.add(date.substring(date.lastIndexOf("-") + 1));
		}

		return items;
	}

	public ArrayList<Meal> search(String query, String planningDate) {
		// TODO Auto-generated method stub

		ArrayList<Meal> mealItemList = new ArrayList<Meal>();

		Cursor cursor = myDataBase.query(MEAL_ITEM_TABLE, new String[] { ITEM,
				DATE, ETHNICITY, INGREDIENTS_LIST, COMBINED_MEAL_TYPE,
				BaseColumns._ID }, ITEM + " like '%" + query + "%' OR "
				+ ETHNICITY + " like '%" + query + "%' OR " + INGREDIENTS_LIST
				+ " like '%" + query + "%' OR " + COMBINED_MEAL_TYPE
				+ " like '%" + query + "%'", null, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {

					String extraSearchData = "";
					String mealItem = cursor.getString(cursor
							.getColumnIndex(ITEM));

					Integer id = cursor.getInt(cursor
							.getColumnIndex(BaseColumns._ID));

					String date = cursor.getString(cursor.getColumnIndex(DATE));

					String ethnicity = cursor.getString(cursor
							.getColumnIndex(ETHNICITY));

					if (ethnicity != null && ethnicity.contains(query))
						extraSearchData += "Ethnicity: " + ethnicity + ";";

					String ingredientsList = cursor.getString(cursor
							.getColumnIndex(INGREDIENTS_LIST));

					if (ingredientsList != null) {

						int forwardSubStringLength = (ingredientsList.length() - 1)
								- ingredientsList.indexOf(query);
						int backwardSubStringLength = ingredientsList
								.indexOf(query);

						if (ingredientsList.contains(query)) {
							extraSearchData += "Ingredients List: ..."
									+ ingredientsList
											.substring(
													ingredientsList
															.indexOf(query)
															- Math.min(30,
																	backwardSubStringLength),
													ingredientsList
															.indexOf(query)
															+ Math.min(
																	forwardSubStringLength,
																	30))
									+ "...;";
						}

					}
					String combinedMealType = cursor.getString(cursor
							.getColumnIndex(COMBINED_MEAL_TYPE));
					
					if (combinedMealType != null && combinedMealType.contains(query))
						extraSearchData += "Meal Type: ..." + combinedMealType
								+ "...;";

					// Given the meal ID, Get the mealPrepHistory from the Meal
					// table, Given today's date, Calculate the lastPrepDate
					Cursor cursorA = myDataBase.query(MEAL_TABLE,
							new String[] { DATE }, MEAL + "='" + id + "'",
							null, null, null, DATE + " DESC");

					String lastPrepDate = "";
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date today = new Date();
					Date unformattedlastPrepDate = null;
					Date unformattedPlanningDate = null;
					boolean flag = true;
					boolean dataExists = false;
					if (cursorA != null) {
						if (cursorA.moveToFirst()) {
							do {
								if (flag)
									lastPrepDate = cursorA.getString(cursorA
											.getColumnIndex(DATE));

								Log.e("error", "meal date: " + lastPrepDate);

								try {
									unformattedlastPrepDate = sdf
											.parse(lastPrepDate);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								Log.e("error", "planning date: " + planningDate);

								try {
									unformattedPlanningDate = sdf
											.parse(planningDate);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								flag = cursorA.moveToNext();

								dataExists = true;

								Log.e("error", "unformattedlastPrepDate: "
										+ unformattedlastPrepDate);
								Log.e("error", "unformattedPlanningDate: "
										+ unformattedPlanningDate);

							} while (unformattedlastPrepDate
									.after(unformattedPlanningDate)
									&& flag == true);
						}
					}

					if (dataExists
							&& unformattedlastPrepDate
									.after(unformattedPlanningDate))
						lastPrepDate = "";

					Log.e("error", "lastPrepDate: " + lastPrepDate);

					cursorA.close();
					// end of lastPrepDate calculation

					Meal meal = new Meal(mealItem, false, date,
							extraSearchData, lastPrepDate);

					mealItemList.add(meal);

				} while (cursor.moveToNext());
			}
		}

		cursor.close();

		return mealItemList;

	}

	public ArrayList<Meal> search() {
		// TODO Auto-generated method stub

		ArrayList<Meal> mealItemList = new ArrayList<Meal>();

		Cursor cursor = myDataBase.query(MEAL_ITEM_TABLE, new String[] { ITEM,
				DATE, BaseColumns._ID }, null, null, null, null, ITEM);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {

					String mealItem = cursor.getString(cursor
							.getColumnIndex(ITEM));

					int id = cursor.getInt(cursor
							.getColumnIndex(BaseColumns._ID));

					String date = cursor.getString(cursor.getColumnIndex(DATE));

					// Given the meal ID, Get the mealPrepHistory from the Meal
					// table, Given today's date, Calculate the lastPrepDate
					Cursor cursorA = myDataBase.query(MEAL_TABLE,
							new String[] { DATE }, MEAL + "='" + id + "'",
							null, null, null, DATE + " DESC");

					String lastPrepDate = "";
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date today = new Date();
					Date unformattedlastPrepDate = null;
					boolean flag = true;
					boolean dataExists = false;

					if (cursorA != null) {
						if (cursorA.moveToFirst()) {
							do {

								if (flag)
									lastPrepDate = cursorA.getString(cursorA
											.getColumnIndex(DATE));

								try {
									unformattedlastPrepDate = sdf
											.parse(lastPrepDate);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								flag = cursorA.moveToNext();

								dataExists = true;

							} while (unformattedlastPrepDate.after(today)
									&& flag == true);
						}
					}

					if (dataExists && unformattedlastPrepDate.after(today))
						lastPrepDate = "";

					Log.e("error", "lastPrepDate: " + lastPrepDate);
					cursorA.close();

					// lastPrepDate

					// end of lastPrepDate calculation

					Meal meal = new Meal(mealItem, false, date, null,
							lastPrepDate);

					mealItemList.add(meal);

				} while (cursor.moveToNext());
			}
		}

		cursor.close();

		return mealItemList;

	}

	public List<MealSuggestion> getSearchSuggestions(String query,
			String lastPlannedDate) {
		// TODO Auto-generated method stub
		List<MealSuggestion> suggestions = new ArrayList<MealSuggestion>();
		Set<MealSuggestion> suggestionsSet = new HashSet<MealSuggestion>();
		Set<MealSuggestion> mealTypeSuggestions = new HashSet<MealSuggestion>();

		Cursor cursor = myDataBase.query(MEAL_ITEM_TABLE, new String[] { ITEM,
				DATE, ETHNICITY, COMBINED_MEAL_TYPE, BaseColumns._ID }, ITEM
				+ " like '%" + query + "%' OR " + ETHNICITY + " like '%"
				+ query + "%' OR " + COMBINED_MEAL_TYPE + " like '%" + query
				+ "%'", null, null, null, null);

		Cursor cursor1 = myDataBase.query(INGREDIENTS,
				new String[] { INGREDIENT }, INGREDIENT + " like '%" + query
						+ "%'", null, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {

					String mealItem = cursor.getString(cursor
							.getColumnIndex(ITEM));
					String date = cursor.getString(cursor.getColumnIndex(DATE));
					String ethnicity = cursor.getString(cursor
							.getColumnIndex(ETHNICITY));
					String combinedMealType = cursor.getString(cursor
							.getColumnIndex(COMBINED_MEAL_TYPE));
					Integer id = cursor.getInt(cursor
							.getColumnIndex(BaseColumns._ID));

					// Given the meal ID, Get the mealPrepHistory from the Meal
					// table, Given today's date, Calculate the lastPrepDate
					Cursor cursorA = myDataBase.query(MEAL_TABLE,
							new String[] { DATE }, MEAL + "='" + id + "'",
							null, null, null, DATE + " DESC");

					String lastPrepDate = "";
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date today = new Date();
					Date unformattedlastPrepDate = null;
					Date unformattedPlanningDate = null;
					boolean dataExists = false;
					boolean flag = true;

					if (cursorA != null) {
						if (cursorA.moveToFirst()) {
							do {
								if (flag)
									lastPrepDate = cursorA.getString(cursorA
											.getColumnIndex(DATE));

								Log.e("error", "meal date: " + lastPrepDate);

								try {
									unformattedlastPrepDate = sdf
											.parse(lastPrepDate);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								try {
									unformattedPlanningDate = sdf
											.parse(lastPlannedDate);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								dataExists = true;

								flag = cursorA.moveToNext();

							} while (unformattedlastPrepDate
									.after(unformattedPlanningDate)
									&& flag == true);
						}
					}

					if (dataExists
							&& unformattedlastPrepDate
									.after(unformattedPlanningDate))
						lastPrepDate = "";

					Log.e("error", "lastPrepDate: " + lastPrepDate);
					cursorA.close();
					// end of lastPrepDate calculation

					MealSuggestion mealItemSuggestion = null;
					Log.e("error: ", "mealItem:" + mealItem);
					if (mealItem.toLowerCase().indexOf(query.toLowerCase()) != -1) {
						mealItemSuggestion = new MealSuggestion();
						mealItemSuggestion.setText1(mealItem);
						mealItemSuggestion.setText2(lastPrepDate);
					}

					MealSuggestion ethnicitySuggestion = null;
					Log.e("error: ", "ethnicity:" + ethnicity);

					if (ethnicity != null
							&& ethnicity.toLowerCase().indexOf(
									query.toLowerCase()) != -1) {
						ethnicitySuggestion = new MealSuggestion();
						Log.e("error: ", "ethnicitySuggestion"
								+ ethnicitySuggestion);
						ethnicitySuggestion.setText1(ethnicity);
					}

					Log.e("error: ", "combinedMealType:" + combinedMealType);

					if (combinedMealType != null) {

						StringTokenizer tokenizer = new StringTokenizer(
								combinedMealType, ",");
						while (tokenizer.hasMoreTokens()) {

							String token = tokenizer.nextToken();
							if (token.toLowerCase()
									.indexOf(query.toLowerCase()) != -1) {

								MealSuggestion mealTypeSuggestion = new MealSuggestion();
								mealTypeSuggestion.setText1(token.trim());

								mealTypeSuggestions.add(mealTypeSuggestion);
							}

						}
					}

					if (mealItemSuggestion != null)
						suggestionsSet.add(mealItemSuggestion);
					if (ethnicitySuggestion != null)
						suggestionsSet.add(ethnicitySuggestion);

				} while (cursor.moveToNext());
			}
		}

		suggestionsSet.addAll(mealTypeSuggestions);

		cursor.close();

		if (cursor1 != null) {
			if (cursor1.moveToFirst()) {
				do {

					String ingredient = cursor1.getString(cursor1
							.getColumnIndex(INGREDIENT));

					MealSuggestion ingredientSuggestion = new MealSuggestion();
					ingredientSuggestion.setText1(ingredient);

					suggestionsSet.add(ingredientSuggestion);

				} while (cursor1.moveToNext());
			}
		}

		cursor1.close();

		suggestions.addAll(suggestionsSet);

		return suggestions;
	}

	public void saveRecipe(String recipeName, String ethnicity,
			String prepTime, String ingredients, String mealType, String method) {
		// TODO Auto-generated method stub
		// Check if recipeName already exists in MealNames
		// If not, write a insert query
		// save recipeName, ethnicity, prepTime, ingredients and mealType
		// tokenize ingredients and save them in ingredients table

		// If yes, write a update query
		// save recipeName, ethnicity, prepTime, ingredients and mealType
		// tokenize ingredients and save them in ingredients table

		Log.e("error", "recipeName:" + recipeName);

		ArrayList<String> mealItemNameList = new ArrayList<String>();

		Cursor cursor = myDataBase.query(MEAL_ITEM_TABLE,
				new String[] { ITEM }, null, null, null, null, null);

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {

					String mealItem = cursor.getString(cursor
							.getColumnIndex(ITEM));

					Log.e("error", "mealItem to be compared: " + mealItem);

					mealItemNameList.add(mealItem.trim());

				} while (cursor.moveToNext());
			}
		}

		cursor.close();

		Log.e("error", "method in db helper: " + method);

		if (!mealItemNameList.contains(recipeName.trim())) {
			// insert
			String sql = "INSERT INTO " + MEAL_ITEM_TABLE + " ( " + ITEM + ", "
					+ ETHNICITY + ", " + PREP_TIME + ", " + COMBINED_MEAL_TYPE
					+ " ," + INGREDIENTS_LIST + ", " + METHOD + ")"
					+ " VALUES ( '" + recipeName + "' , '" + ethnicity
					+ "' , '" + prepTime + "' , '" + mealType + "' , '"
					+ ingredients + "', '" + method + "');";

			Log.e("error", sql);
			myDataBase.execSQL(sql);

		} else {
			// update

			String sql = "UPDATE " + MEAL_ITEM_TABLE + " SET " + ETHNICITY
					+ "='" + ethnicity + "', " + PREP_TIME + "='" + prepTime
					+ "', " + COMBINED_MEAL_TYPE + "='" + mealType + "',"
					+ INGREDIENTS_LIST + "='" + ingredients + "'," + METHOD
					+ "='" + method + "' WHERE " + ITEM + "='" + recipeName
					+ "'" + ";";

			Log.e("error", sql);
			myDataBase.execSQL(sql);

		}

		StringTokenizer tokenizer = new StringTokenizer(ingredients, ";");
		while (tokenizer.hasMoreTokens()) {

			String integratedToken = tokenizer.nextToken().trim();

			Log.e("error", "integratedToken" + integratedToken);

			// StringTokenizer tokenizer2 = new StringTokenizer(integratedToken,
			// ",");

			String ingredientUnit = integratedToken.substring(
					integratedToken.lastIndexOf(" ")).trim();
			String integratedTokenMinusUnit = integratedToken.substring(0,
					integratedToken.lastIndexOf(" ")).trim();
			String ingredientQuantity = integratedTokenMinusUnit.substring(
					integratedTokenMinusUnit.lastIndexOf(" ")).trim();
			String ingredientName = integratedTokenMinusUnit.substring(0,
					integratedTokenMinusUnit.lastIndexOf(" ")).trim();

			Cursor cursor1 = myDataBase.query(INGREDIENTS,
					new String[] { INGREDIENT }, null, null, null, null, null);

			Set<String> ingredientSet = new HashSet<String>();

			if (cursor1 != null) {
				if (cursor1.moveToFirst()) {
					do {
						String ingredient = cursor1.getString(cursor1
								.getColumnIndex(INGREDIENT));
						ingredientSet.add(ingredient);
					} while (cursor1.moveToNext());
				}
			}
			cursor1.close();

			if (ingredientSet.contains(ingredientName))
				;
			else {
				String sql1 = "INSERT INTO " + INGREDIENTS + " ( " + INGREDIENT
						+ ", " + QUANTITY + ", " + UNIT + ")" + " VALUES ( '"
						+ ingredientName + "', " + ingredientQuantity + ", '"
						+ ingredientUnit + "');";

				Log.e("error", sql1);
				myDataBase.execSQL(sql1);
			}
		}
	}

	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.

	public Recipe searchRecipe(String recipeName) {
		// TODO Auto-generated method stub

		Cursor cursor = myDataBase.query(MEAL_ITEM_TABLE, new String[] { ITEM,
				ETHNICITY, PREP_TIME, INGREDIENTS_LIST, COMBINED_MEAL_TYPE,
				METHOD }, ITEM + "='" + recipeName + "'", null, null, null,
				null);

		Recipe recipe = new Recipe();

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					recipe.setRecipeName(cursor.getString(cursor
							.getColumnIndex(ITEM)));
					recipe.setEthnicity(cursor.getString(cursor
							.getColumnIndex(ETHNICITY)));
					recipe.setPrepTime(cursor.getString(cursor
							.getColumnIndex(PREP_TIME)));
					recipe.setIngredients(cursor.getString(cursor
							.getColumnIndex(INGREDIENTS_LIST)));
					recipe.setCombinedMealType(cursor.getString(cursor
							.getColumnIndex(COMBINED_MEAL_TYPE)));
					recipe.setMethod(cursor.getString(cursor
							.getColumnIndex(METHOD)));

				} while (cursor.moveToNext());
			}
		}
		cursor.close();

		return recipe;
	}

	public void dropTables() throws IOException {

		String sql = "DROP TABLE IF EXISTS" + "'" + MEAL_TABLE + "'";
		String sql1 = "DROP TABLE IF EXISTS " + "'" + MEAL_ITEM_TABLE + "'";
		String sql2 = "DROP TABLE IF EXISTS " + "'" + INGREDIENTS + "'";
		Log.e("error", sql);
		Log.e("error", sql1);
		Log.e("error", sql2);
		myDataBase.execSQL(sql);
		myDataBase.execSQL(sql1);
		myDataBase.execSQL(sql2);
	}

	public void deleteMeal(String date) {
		// TODO Auto-generated method stub
		String sqlDelete = "";
		sqlDelete = "DELETE FROM " + MEAL_TABLE + " where " + DATE + "='"
				+ date + "';";

		Log.e("error", sqlDelete);
		myDataBase.execSQL(sqlDelete);

	}

}
