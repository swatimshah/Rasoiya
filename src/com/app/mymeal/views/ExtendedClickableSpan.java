package com.app.mymeal.views;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.app.mymeal.R;
import com.app.mymeal.RecipeEditActivity;
import com.app.mymeal.R.menu;
import com.app.mymeal.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;

public class ExtendedClickableSpan extends ClickableSpan {

	Context myContext;

	public ExtendedClickableSpan(Context context) {
		this.myContext = context;
	}

	@Override
	public void onClick(final View textView) {
		Log.e("error", "clickablespan onclick is called ..");
		final EditText tv = (EditText) textView;
		final Spanned s = (SpannableStringBuilder) tv.getText();
		final int start = s.getSpanStart(this);
		final int end = s.getSpanEnd(this);
		Log.e("error", "SpannableString: start: end" + s.toString() + ":"
				+ start + ":" + end);

		Log.e("error", "onClick [" + s.subSequence(start, end) + "]");

		ClickableSpan[] clickableSpans = s.getSpans(0, s.length() - 1,
				ClickableSpan.class);

		for (ClickableSpan cs : clickableSpans) {
			((SpannableStringBuilder) s).setSpan(new BackgroundColorSpan(
					Color.TRANSPARENT), s.getSpanStart(cs), s.getSpanEnd(cs),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		((SpannableStringBuilder) s).setSpan(new BackgroundColorSpan(
				Color.YELLOW), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		tv.setText(s);

		// Creating the instance of PopupMenu
		PopupMenu popup = new PopupMenu(myContext, textView);
		// Inflating the Popup using xml file
		popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());

		// registering popup with OnMenuItemClickListener
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {

				Log.e("error", "Executing if .." + ":"
						+ item.getTitle().toString() + ":");

				if ("Edit".equals(item.getTitle().toString())) {
					Log.e("error", "inside edit");

					Intent intent = new Intent(textView.getContext(),
							RecipeEditActivity.class);
					intent.putExtra("recipeName", s.subSequence(start, end)
							.toString());
					intent.putExtra("action", "Edit");

					myContext.startActivity(intent);

				} else if ("View".equals(item.getTitle().toString())) {

					Log.e("error", "inside view");

					Intent intent = new Intent(textView.getContext(),
							RecipeEditActivity.class);
					intent.putExtra("recipeName", s.subSequence(start, end)
							.toString());
					intent.putExtra("action", "View");

					myContext.startActivity(intent);

				} else if ("Delete".equals(item.getTitle().toString())) {
					Log.e("error", "s: " + s.toString());
					int startOfSpanRemoval = start;
					Log.e("error", "startOfSpanRemoval:" + startOfSpanRemoval);
					int endOfSpanRemoval = end + 2;
					Log.e("error", "endOfSpanRemoval:" + endOfSpanRemoval);
					((SpannableStringBuilder) s).removeSpan(this);
					Log.e("error", "removedSpan" + s.toString());

					String tempS = ((SpannableStringBuilder) (s)).toString()
							+ " ";

					String toBeRemoved = tempS.substring(startOfSpanRemoval,
							endOfSpanRemoval);

					Log.e("error", "toBeRemoved: " + toBeRemoved);

					String afterRemoval = tempS.replace(toBeRemoved, "") + " ";

					Log.e("error", "afterRemoval: " + afterRemoval);

					final SpannableStringBuilder ssNew;
					// if (afterRemoval != null) {
					ArrayList<String> mealItemList = new ArrayList<String>();
					StringTokenizer tokenizer = new StringTokenizer(
							afterRemoval, ",");
					while (tokenizer.hasMoreTokens()) {
						mealItemList.add(tokenizer.nextToken());
					}

					ssNew = Utils.makeTagLinks(myContext, afterRemoval,
							mealItemList);
					// } else {
					// ssNew = new SpannableStringBuilder("");
					// }

					tv.setText(ssNew.subSequence(0, ssNew.length()));
					tv.setMovementMethod(LinkMovementMethod.getInstance());
					tv.setOnTouchListener(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {

							Log.e("error", "onTouch is getting called..");
							((TextView) v).getMovementMethod().onTouchEvent(
									(TextView) v, ssNew, event);
							return true;
						}

					});

				}

				return true;
			}
		});

		popup.show();// showing popup menu

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
  