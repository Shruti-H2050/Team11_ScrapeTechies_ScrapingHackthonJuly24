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
import utils.Tarfileextract;

public class ScraperMain {

	public static Map<String, RecipeData> ERROR_MAP = new HashMap<>();

	public static void main(String args[]) {
		
		boolean loadDataRequired = false;//Make it true to scrape from website
		
		if(loadDataRequired) {
			
			loadDataFromWebsite();
		}

		generateAllReports();
		Get_tarfiles() ;
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
		
		System.out.println("Total Data:" + scrapedRecipeDataList.size());
		//Fetch Filter Criteria from Excel
		FilterCriteria fc = new ExcelReader().readCriteriaSheet();
		
		//Filter for LFV Elimination List 
		List<RecipeData> lfvEliminationList = scrapedRecipeDataList.stream()
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getPreparationMethod()!= null && rd.getPreparationMethod().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getRecipeDescription()!= null && rd.getRecipeDescription().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getTag()!= null && rd.getTag().toString().toLowerCase().contains(e.toLowerCase())))
				.collect(Collectors.toList());
		
		JSONReportGenerator.generateReport(lfvEliminationList, "reports/json/lfvEliminate.json");
		HTMLReportGenerator.generateReport(lfvEliminationList, "reports/html/lfvEliminate.html", "LFV Elimination Report");
			
		//Filter for LFV Add List 
		List<RecipeData> lfvAddList = lfvEliminationList.stream()
				.filter(rd -> fc.getLfvAdd().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e)))
				.collect(Collectors.toList());
					
		JSONReportGenerator.generateReport(lfvAddList, "reports/json/lfvAdd.json");
		HTMLReportGenerator.generateReport(lfvAddList, "reports/html/lfvAdd.html", "LFV Add Report");
		
		//Filter LFV Add not Vegan List
		List<RecipeData> lfvAddNotVeganList = lfvEliminationList.stream().filter(
				rd -> fc.getLfvAddNotVegan().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e)))
				.collect(Collectors.toList());
			
		JSONReportGenerator.generateReport(lfvAddNotVeganList, "reports/json/lfvAddNotVeganList.json");
		HTMLReportGenerator.generateReport(lfvAddNotVeganList, "reports/html/lfvAddNotVeganList.html", "LFV Add Not Vegan Report");

		//Filter for LFV-Allergy List
		for (String allergyName : fc.getLfvAllergyList()){
			
		List<RecipeData> lfvAllergyList = lfvEliminationList.stream().filter(
				rd -> !(rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getPreparationMethod()!= null && rd.getPreparationMethod().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getRecipeDescription() != null && rd.getRecipeCategory().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getTag() != null && rd.getTag().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.collect(Collectors.toList());
		
		JSONReportGenerator.generateReport(lfvAllergyList, "reports/json/lfvAllergy"+ allergyName+".json");
		HTMLReportGenerator.generateReport(lfvAllergyList, "reports/html/lfvAllergy"+ allergyName+".html", "LFV Allergy-"+ allergyName+" Report");
		}
			
		
		System.out.println("Elimination Filtered Count: " + lfvEliminationList.size());
		System.out.println("Add Filtered Count: " + lfvAddList.size());
//		System.out.println("lfvAllergyMilk Filtered Count: " + lfvAllergyMilkList.size());
		
	}

	private static void Get_tarfiles() 
	{
		String srcPath_json = System.getProperty("user.dir")+"/reports/json";
		String srcPath_Html = System.getProperty("user.dir")+"/reports/html";
		Tarfileextract.Archivetotar(srcPath_json);
		System.out.println("Consolidated json reports are stored as Tar files");
		Tarfileextract.Archivetotar(srcPath_Html);
		System.out.println("Consolidated HTML reports are stored as Tar files");
		
	}

}
