//<<<<<<< HEAD
//package scrape;
//
//import java.util.List;
//
//import pojo.RecipeData;
//import report.HTMLReportGenerator;
//import report.JSONReportGenerator;
//
//public class ScraperMain {
//	
//	public static void main(String args[]) {
//		
//		String baseUrl = "https://www.tarladalal.com/RecipeAtoZ.aspx"; 
//		ScraperJsoup sj = new ScraperJsoup();
//		List<RecipeData> recipeData = sj.extractRecipeData(baseUrl);
//		System.out.println("Data:\n" + recipeData);
//		System.out.println("RecipeSize: " + recipeData.size());
//		
//		HTMLReportGenerator.generateReport(recipeData);
//		JSONReportGenerator.generateReport(recipeData);
//	}
//}
//=======
package scrape;

import java.util.List;

import pojo.RecipeData;
import report.HTMLReportGenerator;
import report.JSONReportGenerator;

public class ScraperMain {
	
	public static void main(String args[]) {
		
		String baseUrl = "https://www.tarladalal.com/RecipeAtoZ.aspx";
		String latestrecipeUrl ="https://www.tarladalal.com/latest_recipes.aspx";
		ScraperJsoup sj = new ScraperJsoup();
		
		//RecipeExtractor re = new ScraperJsoup();
		List<RecipeData> recipeData = sj.extractRecipeData(baseUrl);
		List<RecipeData> latestrecipeData = sj.extractRecipeData(latestrecipeUrl);

		System.out.println("Data:" + recipeData);
		System.out.println("RecipeSize: " + recipeData.size());
		
		HTMLReportGenerator.generateReport(recipeData,"RecipeData.html");
		JSONReportGenerator.generateReport(recipeData);
		HTMLReportGenerator.generateReport(latestrecipeData,"LatestrecipeData.html");
		//JSONReportGenerator.generateReport(latestrecipeData);
	}
}

