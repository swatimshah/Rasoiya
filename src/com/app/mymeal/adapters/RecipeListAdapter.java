package com.app.mymeal.adapters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import android.widget.TextView;

public class RecipeListAdapter extends ArrayAdapter<Meal> {

	private ArrayList<Meal> recipesList;
	private Context context;	

	public RecipeListAdapter(Context context, int textViewResourceId,
			ArrayList<Meal> aRecipesList) {
		super(context, textViewResourceId, aRecipesList);
		this.recipesList = new ArrayList<Meal>();
		this.recipesList.addAll(aRecipesList);
		this.context = context;
		Log.e("error", "recipesList size in custom adapter: "
				+ this.recipesList.size());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final RecipeHolder holder;
		Log.e("error", "ConvertView" + String.valueOf(position));

		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) ((Activity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.recipes_item, null);

			holder = new RecipeHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.date = (TextView) convertView.findViewById(R.id.date);

			convertView.setTag(holder);

		} else {
			holder = (RecipeHolder) convertView.getTag();

		}

		final Meal meal = recipesList.get(position);
		SpannableString mealNameSpannable = new SpannableString(
				meal.getName());
		mealNameSpannable.setSpan(new UnderlineSpan(), 0,
				mealNameSpannable.length(), 0);

		holder.name.setText(mealNameSpannable);

		// Start - Set the difference between dates
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date unformattedlastPrepDate = null;
		Date today = new Date();
		try {
			unformattedlastPrepDate = sdf.parse(meal.getLastPrepDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (unformattedlastPrepDate != null) {
			
			long difference = today.getTime()
					- unformattedlastPrepDate.getTime();

			long days = difference / (1000 * 60 * 60 * 24);

			holder.date.setText(days + " days ago");
			
		} else {
			
			holder.date.setText("never");
		}
		// End - set the difference between dates

		Log.e("error: holder: name", holder.name.getText().toString());
		Log.e("error: holder: date", holder.date.getText().toString());

		holder.name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						RecipeEditActivity.class);
				intent.putExtra("recipeName", meal.getName());
				intent.putExtra("action", "View");

				((Activity)context).startActivity(intent);
			}
		});

		return convertView;
	}

	public int getCount() {

		return recipesList.size();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
