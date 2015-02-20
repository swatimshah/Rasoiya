package com.app.mymeal.views;

import com.app.mymeal.R;

import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MethodView extends LinearLayout {

	private Context context;
	private EditText et1;
	private ImageView plus; 
	private ImageView minus;
	
	public void setContext(Context context) {
		this.context = context;
	}

	public EditText getEt1() {
		return et1;
	}

	public void setEt1(EditText et1) {
		this.et1 = et1;
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

	public MethodView (Context context, String methodText, int id) {
		super(context);
		this.context = context;
		
		setOrientation(LinearLayout.HORIZONTAL);

		et1 = new EditText(context);
		et1.setText(methodText);
		et1.setBackgroundResource(android.R.drawable.editbox_background);
		et1.setTextColor(Color.BLACK);
		et1.setMaxWidth(500);
		addView(et1);

		plus = new ImageView(context);
		plus.setImageResource(R.drawable.plus_sign);
		LinearLayout.LayoutParams layoutParamsPlus = new LinearLayout.LayoutParams(
				60, 60);
		layoutParamsPlus.setMargins(20, 2, 10, 2);
		plus.setLayoutParams(layoutParamsPlus);
		//plus.setOnClickListener(plusMethodListener);
		addView(plus);

		minus = new ImageView(context);
		minus.setImageResource(R.drawable.icon_minus);
		LinearLayout.LayoutParams layoutParamsMinus = new LinearLayout.LayoutParams(
				60, 60);
		layoutParamsMinus.setMargins(20, 2, 10, 2);
		minus.setLayoutParams(layoutParamsMinus);
		//minus.setOnClickListener(minusMethodListener);
		addView(minus);

		setId(id);

	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
