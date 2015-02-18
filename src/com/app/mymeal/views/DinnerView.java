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
import com.app.mymeal.adapters.MyCustomAdapter;
import com.app.mymeal.data.Meal;
import com.app.mymeal.util.Utils;

public class DinnerView implements MealViewInterface, OnClickListener {

	CommonView commonView;

	public DinnerView(CommonView commonView) {

		this.commonView = commonView;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void onAddButtonClick() {

		EditText dinner = commonView.getDinner().getMeal();

		String tempDinnerText = null;

		Log.e("error", "ConcatenatedMealItem: "
				+ commonView.getDataAdapter().getConcatAddedMealItems());

		if (commonView.getDataAdapter().getConcatAddedMealItems() != null
				&& !"".equals(commonView.getDataAdapter()
						.getConcatAddedMealItems().trim())) {

			tempDinnerText = (dinner.getText().toString().trim() + " " + commonView
					.getDataAdapter().getConcatAddedMealItems()).trim();

		} else {

			if (commonView.getSelectedSearchView().getQuery() != null
					&& !"".equals(commonView.getSelectedSearchView().getQuery()
							.toString().trim())) {

				tempDinnerText = (dinner.getText().toString().trim() + " " + commonView
						.convertIntoValidQuery(
								commonView.getSelectedSearchView().getQuery()
										.toString()).trim()).trim()
						+ " ";
			}
		}

		final SpannableStringBuilder ss;

		if (tempDinnerText != null) {
			ArrayList<String> mealItemList = new ArrayList<String>();
			StringTokenizer tokenizer = new StringTokenizer(tempDinnerText, ",");
			while (tokenizer.hasMoreTokens()) {
				mealItemList.add(tokenizer.nextToken());
			}

			ss = Utils.makeTagLinks(commonView.getContext(), tempDinnerText,
					mealItemList);
		} else {
			ss = new SpannableStringBuilder("");
		}

		dinner.setText(ss);
		dinner.setMovementMethod(LinkMovementMethod.getInstance());
		dinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				return true; // consume touch event
			}
		});

		SearchView searchView = commonView.getDinner().getSearchView();
		searchView.setVisibility(View.GONE);

		ListView listView = commonView.getDinner().getListView();
		listView.setVisibility(View.GONE);

		Button button = commonView.getDinner().getAddButton();
		button.setVisibility(View.GONE);

		Button clearButton = commonView.getDinner().getClearButton();
		clearButton.setVisibility(View.GONE);

		commonView.getBreakfast().setEnabled(true);
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

		commonView.getDinner().setBackgroundColor(Color.TRANSPARENT);

	}

	public void onClearButtonClick() {

		EditText dinner = commonView.getDinner().getMeal();

		final SpannableString ss;

		if (dinner.getText() != null) {
			ss = new SpannableString(dinner.getText());
		} else {
			ss = new SpannableString("");
		}

		dinner.setText(ss);
		dinner.setMovementMethod(LinkMovementMethod.getInstance());
		dinner.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.e("error", "onTouch is getting called..");

				((TextView) v).getMovementMethod().onTouchEvent((TextView) v,
						ss, event);
				return true; // consume touch event
			}
		});

		SearchView searchView = commonView.getDinner().getSearchView();
		searchView.setVisibility(View.GONE);

		ListView listView = commonView.getDinner().getListView();
		listView.setVisibility(View.GONE);

		Button button = commonView.getDinner().getAddButton();
		button.setVisibility(View.GONE);

		Button clearButton = commonView.getDinner().getClearButton();
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

		commonView.getDinner().setBackgroundColor(Color.TRANSPARENT);

	}

	@Override
	public void onClick(View v) {

		SearchView searchView = commonView.getDinner().getSearchView();
		searchView.setVisibility(View.VISIBLE);

		ListView listView = commonView.getDinner().getListView();
		listView.setVisibility(View.VISIBLE);

		Button button = commonView.getDinner().getAddButton();
		button.setVisibility(View.VISIBLE);

		Button clearButton = commonView.getDinner().getClearButton();
		clearButton.setVisibility(View.VISIBLE);

		commonView.setMealType("D");
		commonView.setupSearchView();
		commonView.clearListView();
		Log.e("error", "ConcatenatedMealItem onClick: "
				+ commonView.getDataAdapter().getConcatAddedMealItems());

		commonView.getBreakfast().getSearch_meal_icon().setEnabled(false);
		commonView.getBreakfast().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2_disabled);
		commonView.getLunch().getSearch_meal_icon().setEnabled(false);
		commonView.getLunch().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2_disabled);
		commonView.getSnacks().getSearch_meal_icon().setEnabled(false);
		commonView.getSnacks().getSearch_meal_icon()
				.setImageResource(R.drawable.search_icon_small2_disabled);

		commonView.getDinner().setBackgroundColor(Color.rgb(255, 239, 213));

	}

}
