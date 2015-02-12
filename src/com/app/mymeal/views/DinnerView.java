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

	Context context;
	CommonView commonView;
	MyCustomAdapter dataAdapter;

	public DinnerView(Context context, CommonView commonView,
			MyCustomAdapter dataAdapter) {

		this.context = context;
		this.commonView = commonView;
		this.dataAdapter = dataAdapter;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public void onAddButtonClick() {

		EditText dinner = (EditText) ((Activity) context)
				.findViewById(R.id.Dinner);

		String tempDinnerText = null;

		Log.e("error",
				"ConcatenatedMealItem: "
						+ dataAdapter.getConcatAddedMealItems());

		if (dataAdapter.getConcatAddedMealItems() != null
				&& !"".equals(dataAdapter.getConcatAddedMealItems().trim())) {

			tempDinnerText = (dinner.getText().toString().trim() + " " + dataAdapter
					.getConcatAddedMealItems()).trim();

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

			ss = Utils.makeTagLinks(context, tempDinnerText, mealItemList);
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

		SearchView searchView = (SearchView) ((Activity) context)
				.findViewById(R.id.searchViewDinner);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) ((Activity) context)
				.findViewById(R.id.listView1Dinner);
		listView.setVisibility(View.GONE);

		Button button = (Button) ((Activity) context)
				.findViewById(R.id.lblAddDinner);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) ((Activity) context)
				.findViewById(R.id.lblClearDinner);
		clearButton.setVisibility(View.GONE);

		commonView.getBreakfastSearch().setEnabled(true);
		commonView.getBreakfastSearch().setImageResource(
				R.drawable.search_icon_small2);
		commonView.getLunchSearch().setEnabled(true);
		commonView.getLunchSearch().setImageResource(
				R.drawable.search_icon_small2);
		commonView.getSnacksSearch().setEnabled(true);
		commonView.getSnacksSearch().setImageResource(
				R.drawable.search_icon_small2);
		commonView.getDinnerSearch().setEnabled(true);
		commonView.getDinnerSearch().setImageResource(
				R.drawable.search_icon_small2);

		LinearLayout dinnerLayout = (LinearLayout) ((Activity) context)
				.findViewById(R.id.dinnerLayout);
		dinnerLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	public void onClearButtonClick() {

		EditText dinner = (EditText) ((Activity) context)
				.findViewById(R.id.Dinner);

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

		SearchView searchView = (SearchView) ((Activity) context)
				.findViewById(R.id.searchViewDinner);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) ((Activity) context)
				.findViewById(R.id.listView1Dinner);
		listView.setVisibility(View.GONE);

		Button button = (Button) ((Activity) context)
				.findViewById(R.id.lblAddDinner);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) ((Activity) context)
				.findViewById(R.id.lblClearDinner);
		clearButton.setVisibility(View.GONE);

		commonView.getBreakfastSearch().setEnabled(true);
		commonView.getBreakfastSearch().setImageResource(
				R.drawable.search_icon_small2);
		commonView.getLunchSearch().setEnabled(true);
		commonView.getLunchSearch().setImageResource(
				R.drawable.search_icon_small2);
		commonView.getSnacksSearch().setEnabled(true);
		commonView.getSnacksSearch().setImageResource(
				R.drawable.search_icon_small2);
		commonView.getDinnerSearch().setEnabled(true);
		commonView.getDinnerSearch().setImageResource(
				R.drawable.search_icon_small2);

		LinearLayout dinnerLayout = (LinearLayout) ((Activity) context)
				.findViewById(R.id.dinnerLayout);
		dinnerLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	@Override
	public void onClick(View v) {

		SearchView searchView = (SearchView) ((Activity) context)
				.findViewById(R.id.searchViewDinner);
		searchView.setVisibility(View.VISIBLE);

		ListView listView = (ListView) ((Activity) context)
				.findViewById(R.id.listView1Dinner);
		listView.setVisibility(View.VISIBLE);

		Button button = (Button) ((Activity) context)
				.findViewById(R.id.lblAddDinner);
		button.setVisibility(View.VISIBLE);

		Button clearButton = (Button) ((Activity) context)
				.findViewById(R.id.lblClearDinner);
		clearButton.setVisibility(View.VISIBLE);

		commonView.setMealType("D");
		commonView.setupSearchView("D", context);

		commonView.clearListView("D", context, dataAdapter);
		Log.e("error",
				"ConcatenatedMealItem onClick: "
						+ dataAdapter.getConcatAddedMealItems());

		commonView.getBreakfastSearch().setEnabled(false);
		commonView.getBreakfastSearch().setImageResource(
				R.drawable.search_icon_small2_disabled);
		commonView.getLunchSearch().setEnabled(false);
		commonView.getLunchSearch().setImageResource(
				R.drawable.search_icon_small2_disabled);
		commonView.getSnacksSearch().setEnabled(false);
		commonView.getSnacksSearch().setImageResource(
				R.drawable.search_icon_small2_disabled);

		LinearLayout dinnerLayout = (LinearLayout) ((Activity) context)
				.findViewById(R.id.dinnerLayout);
		dinnerLayout.setBackgroundColor(Color.rgb(255, 239, 213));

	}

}
