package scrape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pojo.FilterCriteria;
import pojo.RecipeData;
import report.HTMLReportGenerator;
import report.JSONReportGenerator;
import utils.ExcelReader;

public class ScraperMain {

	public static Map<String, RecipeData> ERROR_MAP = new HashMap<>();

	public static void main(String args[]) {
		
		boolean loadDataRequired = false;
		
		if(loadDataRequired) {
			loadDataFromWebsite();
		}

		generateAllReports();
	}

	private static void loadDataFromWebsite() {

		String baseUrl = "https://www.tarladalal.com/RecipeAtoZ.aspx";
		ScraperJsoup sj = new ScraperJsoup();

		List<RecipeData> recipeData = sj.extractRecipeData(baseUrl);

		System.out.println("Data:" + recipeData);
		System.out.println("RecipeSize: " + recipeData.size());
		System.out.println("ERRORS " + ERROR_MAP);

		HTMLReportGenerator.generateReport(recipeData, "scrapedFullData.html", "Scraped Full Data Report");
		JSONReportGenerator.generateReport(recipeData, "scrapedFullData.json");
		JSONReportGenerator.generateErrorReport(ERROR_MAP, "ERRORData.json");

	}

	private static void generateAllReports() {
		
		//scrapedRecipeFullDataList from Tarladalal.com
		List<RecipeData> scrapedRecipeDataList = JSONReportGenerator.getRecipeDataList("scrapedFullData.json");
		
		//Fetch Filter Criteria from Excel
		FilterCriteria fc = new ExcelReader().readCriteriaSheet();
		
		//Filter LFV Elimination List 
		List<RecipeData> lfvEliminationList = scrapedRecipeDataList.stream().filter(
				rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getIngredients().toString().toLowerCase().contains(e)))
				.collect(Collectors.toList());
		
		JSONReportGenerator.generateReport(lfvEliminationList, "lfvEliminate.json");
		HTMLReportGenerator.generateReport(lfvEliminationList, "lfvEliminate.html", "LFV Elimination Report");

		System.out.println("FILTERED " + lfvEliminationList.size());
	}
}
