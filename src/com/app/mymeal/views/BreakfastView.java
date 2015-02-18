package com.app.mymeal.views;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.app.mymeal.R;
import com.app.mymeal.util.Utils;

public class BreakfastView implements MealViewInterface, OnClickListener {

	CommonView commonView;

	public BreakfastView(CommonView commonView) {

		this.commonView = commonView;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void onAddButtonClick() {

		EditText breakfast = commonView.getBreakfast().getMeal();

		String tempBreakfastText = null;

		Log.e("error", "Concatenated meal items: "
				+ commonView.getDataAdapter().getConcatAddedMealItems().trim()
				+ ":");

		if (commonView.getDataAdapter().getConcatAddedMealItems() != null
				&& !"".equals(commonView.getDataAdapter()
						.getConcatAddedMealItems().trim())) {

			tempBreakfastText = (breakfast.getText().toString().trim() + " " + commonView
					.getDataAdapter().getConcatAddedMealItems()).trim();

		} else {

			if (commonView.getSelectedSearchView().getQuery() != null
					&& !"".equals(commonView.getSelectedSearchView().getQuery()
							.toString().trim())) {

				tempBreakfastText = (breakfast.getText().toString().trim()
						+ " " + commonView.convertIntoValidQuery(
						commonView.getSelectedSearchView().getQuery()
								.toString()).trim()).trim()
						+ " ";
			}
		}

		final SpannableStringBuilder ss;

		if (tempBreakfastText != null) {
			ArrayList<String> mealItemList = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(tempBreakfastText,
					",");
			while (tokenizer.hasMoreTokens()) {
				mealItemList.add(tokenizer.nextToken());
			}

			ss = Utils.makeTagLinks(commonView.getContext(), tempBreakfastText,
					mealItemList);
		} else {
			ss = new SpannableStringBuilder("");
		}

		breakfast.setText(ss);
		breakfast.setMovementMethod(LinkMovementMethod.getInstance());
		breakfast.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error",
						"onTouch is getting called on add breakfast..event.getAction()"
								+ event.getAction());

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				return true; // consume touch event
			}
		});

		SearchView searchView = commonView.getBreakfast().getSearchView();
		searchView.setVisibility(View.GONE);

		ListView listView = commonView.getBreakfast().getListView();
		listView.setVisibility(View.GONE);

		Button button = commonView.getBreakfast().getAddButton();
		button.setVisibility(View.GONE);

		Button clearButton = commonView.getBreakfast().getClearButton();
		clearButton.setVisibility(View.GONE);

		commonView.getBreakfast().getSearch_meal_icon().setEnabled(true);
		commonView.getBreakfast().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2);
		commonView.getLunch().getSearch_meal_icon().setEnabled(true);
		commonView.getLunch().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2);
		commonView.getSnacks().getSearch_meal_icon().setEnabled(true);
		commonView.getSnacks().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2);
		commonView.getDinner().getSearch_meal_icon().setEnabled(true);
		commonView.getDinner().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2);

		commonView.getBreakfast().setBackgroundColor(Color.TRANSPARENT);

	}

	public void onClearButtonClick() {

		EditText breakfast = commonView.getBreakfast().getMeal();

		final SpannableString ss;

		if (breakfast.getText() != null) {
			ss = new SpannableString(breakfast.getText());
		} else {
			ss = new SpannableString("");
		}

		breakfast.setText(ss);
		breakfast.setMovementMethod(LinkMovementMethod.getInstance());
		breakfast.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				return true; // consume touch event
			}
		});

		SearchView searchView = commonView.getBreakfast().getSearchView();
		searchView.setVisibility(View.GONE);

		ListView listView = commonView.getBreakfast().getListView();
		listView.setVisibility(View.GONE);

		Button button = commonView.getBreakfast().getAddButton();
		button.setVisibility(View.GONE);

		Button clearButton = commonView.getBreakfast().getClearButton();
		clearButton.setVisibility(View.GONE);

		commonView.getBreakfast().getSearch_meal_icon().setEnabled(true);
		commonView.getBreakfast().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2);
		commonView.getLunch().getSearch_meal_icon().setEnabled(true);
		commonView.getLunch().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2);
		commonView.getSnacks().getSearch_meal_icon().setEnabled(true);
		commonView.getSnacks().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2);
		commonView.getDinner().getSearch_meal_icon().setEnabled(true);
		commonView.getDinner().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2);

		commonView.getBreakfast().setBackgroundColor(Color.TRANSPARENT);

	}

	@Override
	public void onClick(View v) {

		SearchView searchView = commonView.getBreakfast().getSearchView();
		searchView.setVisibility(View.VISIBLE);

		Button button = commonView.getBreakfast().getAddButton();
		button.setVisibility(View.VISIBLE);

		Button clearButton = commonView.getBreakfast().getClearButton();
		clearButton.setVisibility(View.VISIBLE);

		commonView.setMealType("B");
		commonView.setupSearchView();
		commonView.clearListView();

		Log.e("error", "ConcatenatedMealItem onClick: "
				+ commonView.getDataAdapter().getConcatAddedMealItems());

		ListView listView = commonView.getBreakfast().getListView();
		listView.setVisibility(View.VISIBLE);

		commonView.getLunch().getSearch_meal_icon().setEnabled(false);
		commonView.getLunch().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2_disabled);
		commonView.getSnacks().getSearch_meal_icon().setEnabled(false);
		commonView.getSnacks().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2_disabled);
		commonView.getDinner().getSearch_meal_icon().setEnabled(false);
		commonView.getDinner().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2_disabled);

		commonView.getBreakfast().setBackgroundColor(Color.rgb(255, 239, 213));

	}

}
