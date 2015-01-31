package com.app.mymeal;

import java.util.ArrayList;
import java.util.Calendar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarAdapter extends BaseAdapter {

	static final int FIRST_DAY_OF_WEEK = 0; // Sunday = 0, Monday = 1

	private ArrayList<String> items;
	// references to our items
	public String[] days;
	private java.util.Calendar myCalendar;
	private Calendar clonedCalendar;
	private Context mContext;

	public CalendarAdapter(Context c, Calendar aCalendar) {
		// TODO Auto-generated constructor stub
		myCalendar = aCalendar;
		clonedCalendar = (Calendar) aCalendar.clone();
		mContext = c;
		myCalendar.set(Calendar.DAY_OF_MONTH, 1);
		this.items = new ArrayList<String>();
		refreshDays();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return days.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = convertView;
		TextView dayView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.calendar_item, null);

		}
		dayView = (TextView) v.findViewById(R.id.date);

		// disable empty days from the beginning
		if (days[position].equals("")) {
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else {
			// mark current day as focused
			if (myCalendar.get(Calendar.YEAR) == clonedCalendar
					.get(Calendar.YEAR)
					&& myCalendar.get(Calendar.MONTH) == clonedCalendar
							.get(Calendar.MONTH)
					&& days[position].equals(""
							+ clonedCalendar.get(Calendar.DAY_OF_MONTH))) {
				v.setBackgroundResource(R.drawable.item_background_focused);
			} else {
				v.setBackgroundResource(R.drawable.list_item_background);
			}
		}
		dayView.setText(days[position]);

		// create date string for comparison
		String date = days[position];

		if (date.length() == 1) {
			date = "0" + date;
		}
		String monthStr = "" + (myCalendar.get(Calendar.MONTH) + 1);
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}

		// show icon if date is not empty and it exists in the items array
		ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
		if (date.length() > 0 && items != null && items.contains(date)) {
			iw.setVisibility(View.VISIBLE);
		} else {
			iw.setVisibility(View.INVISIBLE);
		}
		return v;

	}

	public void setItems(ArrayList<String> items) {
		// TODO Auto-generated method stub
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				items.set(i, "0" + items.get(i));
			}
		}
		this.items = items;

	}

	public void refreshDays() {
		// clear items
		items.clear();

		int lastDay = myCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int firstDay = (int) myCalendar.get(Calendar.DAY_OF_WEEK);

		// figure size of the array
		if (firstDay == 1) {
			days = new String[lastDay + (FIRST_DAY_OF_WEEK * 6)];
		} else {
			days = new String[lastDay + firstDay - (FIRST_DAY_OF_WEEK + 1)];
		}

		int j = FIRST_DAY_OF_WEEK;

		// populate empty days before first real day
		if (firstDay > 1) {
			for (j = 0; j < firstDay - FIRST_DAY_OF_WEEK; j++) {
				days[j] = "";
			}
		} else {
			for (j = 0; j < FIRST_DAY_OF_WEEK * 6; j++) {
				days[j] = "";
			}
			j = FIRST_DAY_OF_WEEK * 6 + 1; // sunday => 1, monday => 7
		}

		// populate days
		int dayNumber = 1;
		for (int i = j - 1; i < days.length; i++) {
			days[i] = "" + dayNumber;
			dayNumber++;
		}
	}

}
