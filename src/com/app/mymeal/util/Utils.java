package com.app.mymeal.util;

import java.util.List;

import com.app.mymeal.views.ExtendedClickableSpan;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;

public class Utils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static SpannableStringBuilder makeTagLinks(Context context,
			final String text, final List<String> mealItemList) {
		if (text == null) {
			return null;
		}
		final SpannableStringBuilder ss = new SpannableStringBuilder(text);
		Log.e("error", "makeTagLinks: " + ss.toString() + ":");
		final List<String> items = mealItemList;
		int start = 0, end;
		for (final String item : items) {
			end = start + item.trim().length();
			if (start < end) {

				ss.setSpan(new ExtendedClickableSpan(context), start, end,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			start += item.trim().length() + 2;// comma and space in the original
												// text
			// ;)
		}
		return ss;
	}

	
}
