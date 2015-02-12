package com.app.mymeal.data;

public class MealSuggestion {

	private String text1;
	private String text2;

	public String getText1() {
		// TODO Auto-generated method stub
		return text1;
	}

	public String getText2() {
		// TODO Auto-generated method stub
		return text2;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	@Override
	public boolean equals(Object obj) {
		MealSuggestion suggestion = (MealSuggestion) obj;
		if (this.text1.equals(suggestion.getText1()))
			return true;

		return false;
	}

	public int hashCode() {
		return 100;
	}

}
