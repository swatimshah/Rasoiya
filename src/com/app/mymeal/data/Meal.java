package com.app.mymeal.data;

import java.util.ArrayList;

public class Meal {

	private String name = null;
	private boolean selected = false;
	private String date = null;
	private String extraSearchData = null;
	private String lastPrepDate = null;

	public Meal(String name, boolean selected, String date,
			String extraSearchData, String lastPrepDate) {
		this.name = name;
		this.selected = selected;
		this.date = date;
		this.extraSearchData = extraSearchData;
		this.lastPrepDate = lastPrepDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getExtraSearchData() {
		return extraSearchData;
	}

	public void setExtraSearchData(String extraSearchData) {
		this.extraSearchData = extraSearchData;
	}

	public String getLastPrepDate() {
		return lastPrepDate;
	}

	public void setLastPrepDate(String lastPrepDate) {
		this.lastPrepDate = lastPrepDate;
	}


}
