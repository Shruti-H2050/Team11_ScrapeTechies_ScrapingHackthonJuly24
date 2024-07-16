package scrape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
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

		if (loadDataRequired) {
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
		// JSONReportGenerator.generateErrorReport(ERROR_MAP, "ERRORData.json");

	}

	private static void generateAllReports() {

		// scrapedRecipeFullDataList from Tarladalal.com
		List<RecipeData> scrapedRecipeDataList = JSONReportGenerator.getRecipeDataList("scrapedFullData.json");

		System.out.println("Total Data:" + scrapedRecipeDataList.size());
		// Fetch Filter Criteria from Excel
		FilterCriteria fc = new ExcelReader().readCriteriaSheet();

		// Filter LFV Elimination List
		List<RecipeData> lfvEliminationList = scrapedRecipeDataList.stream()
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(
						e -> rd.getIngredients() != null && rd.getIngredients().toString().toLowerCase().contains(e)))
				.collect(Collectors.toList());

		JSONReportGenerator.generateReport(lfvEliminationList, "reports/json/lfvEliminate.json");
		HTMLReportGenerator.generateReport(lfvEliminationList, "reports/html/lfvEliminate.html",
				"LFV Elimination Report");

		// Filter LFV Add List
		/*
		 * List<RecipeData> lfvAddList = lfvEliminationList.stream().filter( rd ->
		 * fc.getLfvAdd().stream().anyMatch(e -> rd.getIngredients()!= null &&
		 * rd.getIngredients().toString().toLowerCase().contains(e)))
		 * .collect(Collectors.toList());
		 */

		List<RecipeData> lfvReceipeToAvoidLst = lfvEliminationList.stream()
				.filter(rd -> !fc.getLfvReceipeToAvoid().stream()
						.anyMatch(e -> rd.getRecipeDescription() != null
								&& rd.getRecipeDescription().toString().toLowerCase().contains(e)))
				.collect(Collectors.toList());

		// JSONReportGenerator.generateReport(lfvAddList, "reports/json/lfvAdd.json");
		// HTMLReportGenerator.generateReport(lfvAddList, "reports/json/lfvAdd.html",
		// "LFV Add Report");

		JSONReportGenerator.generateReport(lfvReceipeToAvoidLst, "reports/json/lfvReceipeToAvoid.json");
		HTMLReportGenerator.generateReport(lfvReceipeToAvoidLst, "reports/html/lfvReceipeToAvoid.html",
				"LFV ReceipeToAvoid Report");

		System.out.println("Elimination Filtered Count: " + lfvEliminationList.size());
		// System.out.println("Add Filtered Count: " + lfvAddList.size());
		System.out.println("ReceipeToAvoid Filtered Count: " + lfvReceipeToAvoidLst.size());

	}

}
