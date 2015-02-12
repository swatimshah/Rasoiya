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

public class SnacksView implements MealViewInterface, OnClickListener {

	Context context;
	CommonView commonView;
	MyCustomAdapter dataAdapter;

	public SnacksView(Context context, CommonView commonView,
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

		EditText snacks = (EditText) ((Activity) context)
				.findViewById(R.id.Snacks);

		String tempSnacksText = null;

		Log.e("error",
				"ConcatenatedMealItem: "
						+ dataAdapter.getConcatAddedMealItems());

		if (dataAdapter.getConcatAddedMealItems() != null
				&& !"".equals(dataAdapter.getConcatAddedMealItems().trim())) {

			tempSnacksText = (snacks.getText().toString().trim() + " " + dataAdapter
					.getConcatAddedMealItems()).trim();

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

			ss = Utils.makeTagLinks(context, tempSnacksText, mealItemList);
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

		SearchView searchView = (SearchView) ((Activity) context)
				.findViewById(R.id.searchViewSnacks);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) ((Activity) context)
				.findViewById(R.id.listView1Snacks);
		listView.setVisibility(View.GONE);

		Button button = (Button) ((Activity) context)
				.findViewById(R.id.lblAddSnacks);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) ((Activity) context)
				.findViewById(R.id.lblClearSnacks);
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

		LinearLayout snacksLayout = (LinearLayout) ((Activity) context)
				.findViewById(R.id.snacksLayout);
		snacksLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	public void onClearButtonClick() {

		EditText snacks = (EditText) ((Activity) context)
				.findViewById(R.id.Snacks);

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

		SearchView searchView = (SearchView) ((Activity) context)
				.findViewById(R.id.searchViewSnacks);
		searchView.setVisibility(View.GONE);

		ListView listView = (ListView) ((Activity) context)
				.findViewById(R.id.listView1Snacks);
		listView.setVisibility(View.GONE);

		Button button = (Button) ((Activity) context)
				.findViewById(R.id.lblAddSnacks);
		button.setVisibility(View.GONE);

		Button clearButton = (Button) ((Activity) context)
				.findViewById(R.id.lblClearSnacks);
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

		LinearLayout snacksLayout = (LinearLayout) ((Activity) context)
				.findViewById(R.id.snacksLayout);
		snacksLayout.setBackgroundColor(Color.TRANSPARENT);

	}

	@Override
	public void onClick(View v) {

		SearchView searchView = (SearchView) ((Activity) context)
				.findViewById(R.id.searchViewSnacks);
		searchView.setVisibility(View.VISIBLE);

		ListView listView = (ListView) ((Activity) context)
				.findViewById(R.id.listView1Snacks);
		listView.setVisibility(View.VISIBLE);

		Button button = (Button) ((Activity) context)
				.findViewById(R.id.lblAddSnacks);
		button.setVisibility(View.VISIBLE);

		Button clearButton = (Button) ((Activity) context)
				.findViewById(R.id.lblClearSnacks);
		clearButton.setVisibility(View.VISIBLE);

		commonView.setMealType("S");
		commonView.setupSearchView("S", context);

		commonView.clearListView("S", context, dataAdapter);
		Log.e("error",
				"ConcatenatedMealItem onClick: "
						+ dataAdapter.getConcatAddedMealItems());

		commonView.getBreakfastSearch().setEnabled(false);
		commonView.getBreakfastSearch().setImageResource(
				R.drawable.search_icon_small2_disabled);
		commonView.getLunchSearch().setEnabled(false);
		commonView.getLunchSearch().setImageResource(
				R.drawable.search_icon_small2_disabled);
		commonView.getDinnerSearch().setEnabled(false);
		commonView.getDinnerSearch().setImageResource(
				R.drawable.search_icon_small2_disabled);

		LinearLayout snacksLayout = (LinearLayout) ((Activity) context)
				.findViewById(R.id.snacksLayout);
		snacksLayout.setBackgroundColor(Color.rgb(255, 239, 213));

	}

}
