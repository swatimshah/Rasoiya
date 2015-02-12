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

public class BreakfastView implements MealViewInterface, OnClickListener {

	Context context;
	CommonView commonView;
	MyCustomAdapter dataAdapter;

	public BreakfastView(Context context, CommonView commonView,
			MyCustomAdapter dataAdapter) {

		// super(context);
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

		EditText breakfast = (EditText) ((Activity) context)
				.findViewById(R.id.Breakfast);

		String tempBreakfastText = null;

		Log.e("error", "Concatenated meal items: "
				+ dataAdapter.getConcatAddedMealItems().trim() + ":");

		if (dataAdapter.getConcatAddedMealItems() != null
				&& !"".equals(dataAdapter.getConcatAddedMealItems().trim())) {

			tempBreakfastText = (breakfast.getText().toString().trim() + " " + dataAdapter
					.getConcatAddedMealItems()).trim();

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

			ss = Utils.makeTagLinks(context, tempBreakfastText, mealItemList);
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

		SearchView searchView = (SearchView) ((Activity) context)
				.findViewById(R.id.searchView);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) ((Activity) context)
				.findViewById(R.id.listView1);
		listView.setVisibility(View.GONE);

		Button button = (Button) ((Activity) context).findViewById(R.id.lblAdd);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) ((Activity) context)
				.findViewById(R.id.lblClear);
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

		LinearLayout breakfastLayout = (LinearLayout) ((Activity) context)
				.findViewById(R.id.breakfastLayout);
		breakfastLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	public void onClearButtonClick() {

		EditText breakfast = (EditText) ((Activity) context)
				.findViewById(R.id.Breakfast);

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

		SearchView searchView = (SearchView) ((Activity) context)
				.findViewById(R.id.searchView);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) ((Activity) context)
				.findViewById(R.id.listView1);
		listView.setVisibility(View.GONE);

		Button button = (Button) ((Activity) context).findViewById(R.id.lblAdd);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) ((Activity) context)
				.findViewById(R.id.lblClear);
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

		LinearLayout breakfastLayout = (LinearLayout) ((Activity) context)
				.findViewById(R.id.breakfastLayout);
		breakfastLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	@Override
	public void onClick(View v) {

		SearchView searchView = (SearchView) ((Activity) context)
				.findViewById(R.id.searchView);
		searchView.setVisibility(View.VISIBLE);

		Button button = (Button) ((Activity) context).findViewById(R.id.lblAdd);
		button.setVisibility(View.VISIBLE);

		Button clearButton = (Button) ((Activity) context)
				.findViewById(R.id.lblClear);
		clearButton.setVisibility(View.VISIBLE);

		commonView.setMealType("B");
		commonView.setupSearchView("B", context);

		commonView.clearListView("B", context, dataAdapter);
		Log.e("error",
				"ConcatenatedMealItem onClick: "
						+ dataAdapter.getConcatAddedMealItems());

		ListView listView = (ListView) ((Activity) context)
				.findViewById(R.id.listView1);
		listView.setVisibility(View.VISIBLE);

		commonView.getLunchSearch().setEnabled(false);
		commonView.getLunchSearch().setImageResource(
				R.drawable.search_icon_small2_disabled);
		commonView.getSnacksSearch().setEnabled(false);
		commonView.getSnacksSearch().setImageResource(
				R.drawable.search_icon_small2_disabled);
		commonView.getDinnerSearch().setEnabled(false);
		commonView.getDinnerSearch().setImageResource(
				R.drawable.search_icon_small2_disabled);

		LinearLayout breakfastLayout = (LinearLayout) ((Activity) context)
				.findViewById(R.id.breakfastLayout);
		breakfastLayout.setBackgroundColor(Color.rgb(255, 239, 213));

	}

}
