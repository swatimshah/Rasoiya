package com.app.mymeal.views;

import com.app.mymeal.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

public class MealView extends LinearLayout {

	private TextView lblMeal;
	private ImageView search_meal_icon;
	private EditText meal;
	private SearchView searchView;
	private ListView listView;
	private Button addButton;
	private Button clearButton;
	private String mealLabel;
	
	
	public MealView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.meal, this, true);

		lblMeal = (TextView) findViewById(R.id.lblMeal);
		search_meal_icon = (ImageView) findViewById(R.id.search_meal_icon);
		meal = (EditText) findViewById(R.id.meal);
		searchView = (SearchView) findViewById(R.id.searchView);
		listView = (ListView) findViewById(R.id.listView);
		addButton = (Button) findViewById(R.id.lblAdd);
		clearButton = (Button) findViewById(R.id.lblClear);
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.MealView);
		mealLabel = array.getString(R.styleable.MealView_mealLabel);
		Log.e("error", "mealLabel: " + mealLabel);
		lblMeal.setText(mealLabel);
		array.recycle();

	}


	public TextView getLblMeal() {
		return lblMeal;
	}

	public void setLblMeal(TextView lblMeal) {
		this.lblMeal = lblMeal;
	}

	public ImageView getSearch_meal_icon() {
		return search_meal_icon;
	}

	public void setSearch_meal_icon(ImageView search_meal_icon) {
		this.search_meal_icon = search_meal_icon;
	}

	public EditText getMeal() {
		return meal;
	}

	public void setMeal(EditText meal) {
		this.meal = meal;
	}

	public SearchView getSearchView() {
		return searchView;
	}

	public void setSearchView(SearchView searchView) {
		this.searchView = searchView;
	}

	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public Button getAddButton() {
		return addButton;
	}

	public void setAddButton(Button addButton) {
		this.addButton = addButton;
	}

	public Button getClearButton() {
		return clearButton;
	}

	public void setClearButton(Button clearButton) {
		this.clearButton = clearButton;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
