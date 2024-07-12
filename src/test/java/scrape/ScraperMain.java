package scrape;



import java.util.List;

import pojo.RecipeData;
import report.HTMLReportGenerator;
import report.JSONReportGenerator;

public class ScraperMain {
	
	public static void main(String args[]) {
		
		String baseUrl = "https://www.tarladalal.com/RecipeAtoZ.aspx"; 
		ScraperJsoup sj = new ScraperJsoup();
		List<RecipeData> recipeData = sj.extractRecipeData(baseUrl);
		System.out.println("Data:\n" + recipeData);
		System.out.println("RecipeSize: " + recipeData.size());
		
		HTMLReportGenerator.generateReport(recipeData);
		JSONReportGenerator.generateReport(recipeData);
	}
}

