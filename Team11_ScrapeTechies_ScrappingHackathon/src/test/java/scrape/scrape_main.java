package Scrape;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import Config.Tarfileextract_main;
import pojo.FilterCriteria;
import pojo.RecipeData;
import Report_generate.HTMLReportGenerator;
import Report_generate.JSONReportGenerator;
import utils.ExcelReader;


public class scrape_main {
	public static Map<String, RecipeData> ERROR_MAP = new HashMap<>();

	public static void main(String args[]) {
		
		boolean loadDataRequired = false;
		
		if(loadDataRequired) {
			loadDataFromWebsite();
		}

		generateAllReports();
		Get_tarfiles() ;
	}

	private static void loadDataFromWebsite() {

		String baseUrl = "https://www.tarladalal.com/RecipeAtoZ.aspx";
		Scrape_jsoup sj = new Scrape_jsoup();

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
	private static void Get_tarfiles() 
	{
		String srcPath_json = System.getProperty("user.dir")+"/reports/json";
		String srcPath_Html = System.getProperty("user.dir")+"/reports/html";
		Tarfileextract_main.Archivetotar(srcPath_json);
		System.out.println("Consolidated json reports are stored as Tar files");
		Tarfileextract_main.Archivetotar(srcPath_Html);
		System.out.println("Consolidated HTML reports are stored as Tar files");
		
	}
}