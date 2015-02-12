package com.app.mymeal.views;

import android.content.Context;

import com.app.mymeal.adapters.MyCustomAdapter;

public class ViewFactory {

	public static ViewFactory viewFactory = null;
	
	public ViewFactory() {
		
	}
	
	public static ViewFactory getViewFactory() {
		if(viewFactory == null) {
			viewFactory = new ViewFactory();
			return viewFactory;
		}	
		else 
			return viewFactory;
	}
	
	
	public MealViewInterface getMealView(String viewName, Context context, CommonView commonView, MyCustomAdapter dataAdapter) {
		
		if("Breakfast".equals(viewName))
			return new BreakfastView(context, commonView, dataAdapter);
		else if ("Lunch".equals(viewName))
			return new LunchView(context, commonView, dataAdapter);
		else if ("Snacks".equals(viewName))
			return new SnacksView(context, commonView, dataAdapter);
		else if ("Dinner".equals(viewName))
			return new DinnerView(context, commonView, dataAdapter);
		else 
			return null;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
