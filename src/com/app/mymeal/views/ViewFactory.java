package com.app.mymeal.views;

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
	
	
	public MealViewInterface getMealView(String viewName, CommonView commonView) {
		
		if("Breakfast".equals(viewName))
			return new BreakfastView(commonView);
		else if ("Lunch".equals(viewName))
			return new LunchView(commonView);
		else if ("Snacks".equals(viewName))
			return new SnacksView(commonView);
		else if ("Dinner".equals(viewName))
			return new DinnerView(commonView);
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
