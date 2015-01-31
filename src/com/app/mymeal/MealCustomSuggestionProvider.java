package com.app.mymeal;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

public class MealCustomSuggestionProvider extends ContentProvider {

	
	private static String lastPlannedDate;
	
	public static String getLastPlannedDate() {
		return lastPlannedDate;
	}


	public static void setLastPlannedDate(String lastPlannedDate) {
		MealCustomSuggestionProvider.lastPlannedDate = lastPlannedDate;
	}


	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return true;
	}

	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		String[] COLUMNS = {
				"_id", // must include this column
				SearchManager.SUGGEST_COLUMN_TEXT_1,
				SearchManager.SUGGEST_COLUMN_TEXT_2,
				SearchManager.SUGGEST_COLUMN_INTENT_DATA,
				SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
				SearchManager.SUGGEST_COLUMN_SHORTCUT_ID };

		// TODO Auto-generated method stub
		String query = uri.getLastPathSegment();
		Log.e("error: query: ", query);
		if (SearchManager.SUGGEST_URI_PATH_QUERY.equals(query)) {

			// user hasn't entered anything
			// thus return a default cursor
			MatrixCursor cursor = new MatrixCursor(COLUMNS);
			return cursor;
		} else {
			MatrixCursor cursor = new MatrixCursor(COLUMNS);

			try {
				List<MealSuggestion> list = callmyservice(query);
				int n = 0;
				for (MealSuggestion obj : list) {
					cursor.addRow(createRow(new Integer(n), 
							obj.getText1(), obj.getText2(), query));
					n++;
				}
			} catch (Exception e) {
				Log.e("error", "Failed to lookup " + query, e);
			}
			return cursor;
		}

	}

	private List<MealSuggestion> callmyservice(String query) {
		// TODO Auto-generated method stub
		List<MealSuggestion> suggestions = new ArrayList<MealSuggestion>();
		MealSuggestion suggestion1 = new MealSuggestion();
		suggestion1.setText1("MyText1");
		suggestion1.setText2("MyText2");
		suggestions.add(suggestion1);
		
		MealSuggestion suggestion2 = new MealSuggestion();
		suggestion2.setText1("MyText3");
		suggestion2.setText2("MyText4");
		suggestions.add(suggestion2);
		
		
		DataBaseHelper myDbHelper = new DataBaseHelper(getContext());
		myDbHelper = new DataBaseHelper(getContext());
		myDbHelper.openDataBase();
		suggestions = myDbHelper.getSearchSuggestions(query, lastPlannedDate);
		
		return suggestions;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	private Object[] createRow(Integer id, String text1, String text2,
			String name) {
		
		Log.e("error", "createRow text1: " + text1);
		
		return new Object[] { id, // _id
				text1, // text1
				text2, // text2
				text1, 
				"android.intent.action.VIEW", // action
				SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT };
	}

}
