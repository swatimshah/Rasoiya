package com.app.mymeal.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.app.mymeal.persistence.DataBaseHelper;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

public class Recipe {

	private String recipeName;
	private String ethnicity;
	private String prepTime;
	private String ingredients;
	private String combinedMealType;
	private String method;

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getPrepTime() {
		return prepTime;
	}

	public void setPrepTime(String prepTime) {
		this.prepTime = prepTime;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getCombinedMealType() {
		return combinedMealType;
	}

	public void setCombinedMealType(String combinedMealType) {
		this.combinedMealType = combinedMealType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public static void populateDb(Context context, int resource)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		XmlResourceParser xrp = context.getResources().getXml(resource);

		String recipeName = "";
		String ethnicity = "";
		String prepTime = "";
		String mealType = "";
		String method = "";
		ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();

		xrp.next();
		int eventType = xrp.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG
					&& xrp.getName().equalsIgnoreCase("mealName")) {
				recipeName = xrp.nextText();
				Log.e("error", "recipeName: " + recipeName);
			}

			if (eventType == XmlPullParser.START_TAG
					&& xrp.getName().equalsIgnoreCase("ethnicity")) {
				ethnicity = xrp.nextText();
				Log.e("error", "ethnicity: " + ethnicity);
			}

			if (eventType == XmlPullParser.START_TAG
					&& xrp.getName().equalsIgnoreCase("prepTime")) {
				prepTime = xrp.nextText();
				Log.e("error", "prepTime: " + prepTime);
			}

			if (eventType == XmlPullParser.START_TAG
					&& xrp.getName().equalsIgnoreCase("mealType")) {
				mealType = xrp.nextText();
				Log.e("error", "mealType: " + mealType);
			}

			if (eventType == XmlPullParser.START_TAG
					&& xrp.getName().equalsIgnoreCase("ingredients")) {
				xrp.next();

				while (xrp.getName().equalsIgnoreCase("ingredient")) {

					Ingredient ingredient = new Ingredient();

					Log.e("error", "loop1");

					xrp.next();

					eventType = xrp.getEventType();
					Log.e("error", "eventType: " + xrp.getEventType()
							+ "Name: " + xrp.getName());
					if (xrp.getName().equalsIgnoreCase("name")) {

						ingredient.setName(xrp.nextText());
						Log.e("error", "name: " + ingredient.getName());
					}

					Log.e("error", "loop2");

					xrp.next();
					eventType = xrp.getEventType();
					Log.e("error", "eventType: " + xrp.getEventType()
							+ "Name: " + xrp.getName());
					if (xrp.getName().equalsIgnoreCase("quantity")) {

						ingredient.setQuantity(Float.valueOf(xrp.nextText()));
						Log.e("error", "Quantity: "
								+ ingredient.getQuantity().floatValue());
					}

					Log.e("error", "loop3");

					xrp.next();
					eventType = xrp.getEventType();
					Log.e("error", "eventType: " + xrp.getEventType()
							+ "Name: " + xrp.getName());
					if (xrp.getName().equalsIgnoreCase("unit")) {

						ingredient.setUnit(xrp.nextText());
						Log.e("error", "unit: " + ingredient.getUnit());
					}

					ingredientList.add(ingredient);

					xrp.next();
					xrp.next();
					eventType = xrp.getEventType();
					Log.e("error", "eventType: " + xrp.getEventType()
							+ "Name: " + xrp.getName());
					Log.e("error", "eventName: " + xrp.getName());
					if (xrp.getName().equalsIgnoreCase("ingredients"))
						break;

					Log.e("error", "loop4");

				}
			}

			xrp.next();
			eventType = xrp.getEventType();
			Log.e("error",
					"eventType: " + xrp.getEventType() + " Name: "
							+ xrp.getName());
			String step = "";
			if (eventType == XmlPullParser.START_TAG
					&& xrp.getName().equalsIgnoreCase("method")) {
				xrp.next();
				while (xrp.getName().equalsIgnoreCase("step")) {

					step = xrp.nextText().trim();

					//start - Replace apostrophe with ''''
					
					String tempStep = "";
					if(step.contains("'"))
						tempStep = step.replace("'", "''");
					else
						tempStep = step;
					//end - Replace apostrophe with ''''
					
					method = method + tempStep + "|";
					xrp.next();
					if (xrp.getName().equals("method"))
						break;
				}
			}
			Log.e("error", "whole method: " + method);
		}

		String concatIngredients = "";
		Log.e("error", "concatenated ingredients: " + ingredientList.size());
		for (Ingredient myIngredient : ingredientList) {
			
			//start - Replace apostrophe with ''''

			
			String tempIngredientName = "";
			
			if(myIngredient.getName().contains("'"))
				tempIngredientName = myIngredient.getName().replace("'", "''");
			else 
				tempIngredientName = myIngredient.getName();
			
			//end - replace apostrophe with ''''
			
			concatIngredients = concatIngredients + tempIngredientName
					+ " " + myIngredient.getQuantity() + " "
					+ myIngredient.getUnit() + ";";
		}

		DataBaseHelper myDbHelper = new DataBaseHelper(context);
		myDbHelper = new DataBaseHelper(context);
		myDbHelper.openDataBase();

		myDbHelper.saveRecipe(recipeName, ethnicity, prepTime,
				concatIngredients, mealType, method);

	}

	public static File getRecipeXml(String aRecipeName, Context context) {

		File recipeFile = null;

		DataBaseHelper myDbHelper = new DataBaseHelper(context);
		myDbHelper = new DataBaseHelper(context);
		myDbHelper.openDataBase();

		Recipe myRecipe = myDbHelper.searchRecipe(aRecipeName);
		String myRecipeXML = createRecipeXml(myRecipe);
		
		return new File (myRecipeXML);
	}

	private static String createRecipeXml(Recipe myRecipe) {

		String xml = "";

		StringBuffer buffer = new StringBuffer();

		buffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		buffer.append("<meal>\n");
		buffer.append("<mealName>\n");
		buffer.append(myRecipe.getRecipeName() + "\n");
		buffer.append("</mealName>\n");
		buffer.append("<ethnicity>\n");
		buffer.append(myRecipe.getEthnicity() + "\n");
		buffer.append("</ethnicity>\n");
		buffer.append("<prepTime>\n");
		buffer.append(myRecipe.getPrepTime() + "\n");
		buffer.append("</prepTime>\n");
		buffer.append("<mealType>\n");
		buffer.append(myRecipe.getCombinedMealType() + "\n");
		buffer.append("</mealType>\n");

		buffer.append("<ingredients>");
		String ingredientsText = myRecipe.getIngredients().replace(",", " ");
		StringTokenizer tokenizer = new StringTokenizer(ingredientsText, ";");
		while (tokenizer.hasMoreTokens()) {
			buffer.append("<ingredient>\n");
			buffer.append(tokenizer.nextToken() + "\n");
			buffer.append("</ingredient>\n");
		}		
		buffer.append("</ingredients>");

		buffer.append("<method>");
		StringTokenizer tokenizerX = new StringTokenizer(myRecipe.getMethod(), "|");
		while (tokenizerX.hasMoreTokens()) {
			buffer.append("<step>\n");
			buffer.append(tokenizerX.nextToken() + "\n");
			buffer.append("</step>\n");
		}		
		buffer.append("</method>");
		
		return buffer.toString();
	}
}
