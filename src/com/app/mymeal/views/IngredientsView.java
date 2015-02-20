package com.app.mymeal.views;

import java.util.Arrays;
import java.util.List;

import com.app.mymeal.R;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class IngredientsView extends LinearLayout {

	private Context context;
	private EditText et1;
	private EditText et2;
	private Spinner spinner;
	private ImageView plus;
	private ImageView minus;
	private int id;
	
	
	public void setContext(Context context) {
		this.context = context;
	}

	public EditText getEt1() {
		return et1;
	}

	public void setEt1(EditText et1) {
		this.et1 = et1;
	}

	public EditText getEt2() {
		return et2;
	}

	public void setEt2(EditText et2) {
		this.et2 = et2;
	}

	public Spinner getSpinner() {
		return spinner;
	}

	public void setSpinner(Spinner spinner) {
		this.spinner = spinner;
	}

	public ImageView getPlus() {
		return plus;
	}

	public void setPlus(ImageView plus) {
		this.plus = plus;
	}

	public ImageView getMinus() {
		return minus;
	}

	public void setMinus(ImageView minus) {
		this.minus = minus;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OnItemSelectedListener getOnUnitSpinnerListener() {
		return OnUnitSpinnerListener;
	}

	public void setOnUnitSpinnerListener(
			OnItemSelectedListener onUnitSpinnerListener) {
		OnUnitSpinnerListener = onUnitSpinnerListener;
	}

	public IngredientsView(Context context, String ingredientName,
			String ingredientQuantity, String ingredientUnit, int id) {
		super(context);
		this.context = context;

		setOrientation(LinearLayout.HORIZONTAL);

		et1 = new EditText(context);
		et1.setText(ingredientName);
		et1.setBackgroundResource(android.R.drawable.editbox_background);
		et1.setTextColor(Color.BLACK);
		et1.setMaxWidth(300);
		addView(et1);

		et2 = new EditText(context);
		et2.setText(ingredientQuantity);
		et2.setBackgroundResource(android.R.drawable.editbox_background);
		et2.setTextColor(Color.BLACK);
		et2.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		addView(et2);

		String[] unit_array = getResources().getStringArray(R.array.unit_array);
		List<String> unitList = Arrays.asList(unit_array);
		spinner = new Spinner(context);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
				context, android.R.layout.simple_spinner_dropdown_item,
				unitList);
		spinner.setAdapter(spinnerArrayAdapter);
		spinner.setBackgroundResource(android.R.drawable.editbox_background);
		spinner.setLayoutParams(new LayoutParams(150, 78));
		int spinnerPosition = spinnerArrayAdapter.getPosition(ingredientUnit);
		spinner.setSelection(spinnerPosition);
		spinner.setOnItemSelectedListener(OnUnitSpinnerListener);
		addView(spinner);

		plus = new ImageView(context);
		plus.setImageResource(R.drawable.plus_sign);
		LinearLayout.LayoutParams layoutParamsPlus = new LinearLayout.LayoutParams(
				60, 60);
		layoutParamsPlus.setMargins(20, 2, 10, 2);
		plus.setLayoutParams(layoutParamsPlus);
//		plus.setOnClickListener(plusIngredientsListener);
		addView(plus);

		minus = new ImageView(context);
		minus.setImageResource(R.drawable.icon_minus);
		LinearLayout.LayoutParams layoutParamsMinus = new LinearLayout.LayoutParams(
				60, 60);
		layoutParamsMinus.setMargins(20, 2, 10, 2);
		minus.setLayoutParams(layoutParamsMinus);
//		minus.setOnClickListener(minusIngredientsListener);
		addView(minus);

		setId(id);

	}

	private OnItemSelectedListener OnUnitSpinnerListener = new AdapterView.OnItemSelectedListener() {
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
			((TextView) parent.getChildAt(0)).setTextSize(15);
		}

		public void onNothingSelected(AdapterView<?> parent) {
		}
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
