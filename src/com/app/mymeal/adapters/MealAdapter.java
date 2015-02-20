package com.app.mymeal.adapters;

import java.util.ArrayList;

import com.app.mymeal.R;
import com.app.mymeal.RecipeEditActivity;
import com.app.mymeal.data.Meal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class MealAdapter extends ArrayAdapter<Meal> {

	private ArrayList<Meal> mealList;
	private ArrayList<String> addedMealItems = new ArrayList<String>();
	private Context context;

	public ArrayList<Meal> getMealList() {
		return mealList;
	}

	public void setMealList(ArrayList<Meal> mealList) {
		this.mealList = mealList;
	}

	public ArrayList<String> getAddedMealItems() {
		return addedMealItems;
	}

	public void setAddedMealItems(ArrayList<String> addedMealItems) {
		this.addedMealItems = addedMealItems;
	}

	public MealAdapter(Context context, int textViewResourceId,
			ArrayList<Meal> mealList) {
		super(context, textViewResourceId, mealList);
		this.context = context;
		this.mealList = new ArrayList<Meal>();
		this.mealList.addAll(mealList);

		Log.e("error",
				"mealList size in custom adapter: " + this.mealList.size());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MealHolder holder;
		Log.e("error", "ConvertView" + String.valueOf(position));

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) ((Activity) context)
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.meal_list, null);

			holder = new MealHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.checkBox1);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.extraInfo = (TextView) convertView
					.findViewById(R.id.extraInfo);
			convertView.setTag(holder);

		} else {
			holder = (MealHolder) convertView.getTag();

		}

		final Meal meal = mealList.get(position);
		SpannableString mealNameSpannable = new SpannableString(meal.getName());
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

				((Activity) context).startActivity(intent);
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

	public void clear() {

		addedMealItems.clear();
		mealList.clear();

	}
}