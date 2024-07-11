package scrape;

import java.util.List;

import pojo.RecipeData;

public class ScraperMain {
	
	public static void main(String args[]) {
	
		ScraperJsoup sj = new ScraperJsoup();
		List<RecipeData> recipeData = sj.extractRecipeData();
		System.out.println("Data:\n" + recipeData);
	}
}
