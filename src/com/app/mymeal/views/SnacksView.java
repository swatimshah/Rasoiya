package com.app.mymeal.views;

import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.app.mymeal.R;
import com.app.mymeal.adapters.MealAdapter;
import com.app.mymeal.data.Meal;
import com.app.mymeal.util.Utils;

public class SnacksView implements MealViewInterface, OnClickListener {

	CommonView commonView;

	public SnacksView(CommonView commonView) {

		this.commonView = commonView;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void onAddButtonClick() {

		EditText snacks = commonView.getSnacks().getMeal();

		String tempSnacksText = null;

		Log.e("error", "ConcatenatedMealItem: "
				+ commonView.getDataAdapter().getConcatAddedMealItems());

		if (commonView.getDataAdapter().getConcatAddedMealItems() != null
				&& !"".equals(commonView.getDataAdapter()
						.getConcatAddedMealItems().trim())) {

			tempSnacksText = (snacks.getText().toString().trim() + " " + commonView
					.getDataAdapter().getConcatAddedMealItems()).trim();

		} else {

			if (commonView.getSelectedSearchView().getQuery() != null
					&& !"".equals(commonView.getSelectedSearchView().getQuery()
							.toString().trim())) {

				tempSnacksText = (snacks.getText().toString().trim() + " " + commonView
						.convertIntoValidQuery(
								commonView.getSelectedSearchView().getQuery()
										.toString()).trim()).trim()
						+ " ";
			}
		}

		final SpannableStringBuilder ss;

		if (tempSnacksText != null) {
			ArrayList<String> mealItemList = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(tempSnacksText, ",");
			while (tokenizer.hasMoreTokens()) {
				mealItemList.add(tokenizer.nextToken());
			}

			ss = Utils.makeTagLinks(commonView.getContext(), tempSnacksText,
					mealItemList);
		} else {
			ss = new SpannableStringBuilder("");
		}

		snacks.setText(ss);
		snacks.setMovementMethod(LinkMovementMethod.getInstance());
		snacks.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				return true; // consume touch event
			}
		});

		SearchView searchView = commonView.getSnacks().getSearchView();
		searchView.setVisibility(View.GONE);

		ListView listView = commonView.getSnacks().getListView();
		listView.setVisibility(View.GONE);

		Button button = commonView.getSnacks().getAddButton();
		button.setVisibility(View.GONE);

		Button clearButton = commonView.getSnacks().getClearButton();
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

		commonView.getSnacks().setBackgroundColor(Color.TRANSPARENT);

	}

	public void onClearButtonClick() {

		EditText snacks = commonView.getSnacks().getMeal();

		final SpannableString ss;

		if (snacks.getText() != null) {
			ss = new SpannableString(snacks.getText());
		} else {
			ss = new SpannableString("");
		}

		snacks.setText(ss);
		snacks.setMovementMethod(LinkMovementMethod.getInstance());
		snacks.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				return true; // consume touch event
			}
		});

		SearchView searchView = commonView.getSnacks().getSearchView();
		searchView.setVisibility(View.GONE);

		ListView listView = commonView.getSnacks().getListView();
		listView.setVisibility(View.GONE);

		Button button = commonView.getSnacks().getAddButton();
		button.setVisibility(View.GONE);

		Button clearButton = commonView.getSnacks().getClearButton();
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

		commonView.getSnacks().setBackgroundColor(Color.TRANSPARENT);

	}

	@Override
	public void onClick(View v) {

		SearchView searchView = commonView.getSnacks().getSearchView();
		searchView.setVisibility(View.VISIBLE);

		ListView listView = commonView.getSnacks().getListView();
		listView.setVisibility(View.VISIBLE);

		Button button = commonView.getSnacks().getAddButton();
		button.setVisibility(View.VISIBLE);

		Button clearButton = commonView.getSnacks().getClearButton();
		clearButton.setVisibility(View.VISIBLE);

		commonView.setMealType("S");
		commonView.setupSearchView();
		commonView.clearListView();
		Log.e("error",
				"ConcatenatedMealItem onClick: "
						+ commonView.getDataAdapter().getConcatAddedMealItems());

		commonView.getBreakfast().getSearch_meal_icon().setEnabled(false);
		commonView.getBreakfast().getSearch_meal_icon().setImageResource(
				R.drawable.search_icon_small2_disabled);
		commonView.getLunch().getSearch_meal_icon().setEnabled(false);
		commonView.getLunch().getSearch_meal_icon().setImageResource(
				R.drawable.search_icon_small2_disabled);
		commonView.getDinner().getSearch_meal_icon().setEnabled(false);
		commonView.getDinner().getSearch_meal_icon().setImageResource(
				R.drawable.search_icon_small2_disabled);

		commonView.getSnacks().setBackgroundColor(Color.rgb(255, 239, 213));

	}

}
