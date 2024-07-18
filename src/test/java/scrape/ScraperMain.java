package scrape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import lombok.extern.log4j.Log4j2;
import pojo.FilterCriteria;
import pojo.RecipeData;
import report.HTMLReportGenerator;
import report.JSONReportGenerator;
import utils.ConfigLoader;
import utils.ExcelReader;
import utils.Tarfileextract;

//@Log4j2
public class ScraperMain {
	
	private static final Logger log = LogManager.getLogger(ScraperMain.class);

	public static Map<String, RecipeData> ERROR_MAP = new HashMap<>();

	public static void main(String args[]) {
		
		boolean loadDataRequired = ConfigLoader.isLoadDataRequired();//Make it true in config.properties file to scrape from the website
		
		if(loadDataRequired) {
			
			loadDataFromWebsite();
		}

		generateAllReports();
		
		Get_tarfiles() ;
	}
	
	
	private static void loadDataFromWebsite() {

		String baseUrl = ConfigLoader.getProperty("baseUrl");
		String[] alphabets = ConfigLoader.getAlphabetsList();
        int threadPoolSize = ConfigLoader.getThreadPoolSize();
        int time = ConfigLoader.getTime();
        String beginsWithParam = ConfigLoader.getBeginsWithParam();
        String recipeUrlPrefix = ConfigLoader.getRecipeUrlPrefix();
		ScraperJsoup sj = new ScraperJsoup(baseUrl, alphabets, threadPoolSize, time, beginsWithParam, recipeUrlPrefix);


		List<RecipeData> recipeData = sj.extractRecipeData();
		
		log.info("Data:" + recipeData);
		log.info("RecipeSize: " + recipeData.size());
		log.info("ERRORS " + ERROR_MAP);

		HTMLReportGenerator.generateReport(recipeData, "scrapedFullData.html", "Scraped Full Data Report");
		JSONReportGenerator.generateReport(recipeData, "scrapedFullData.json");
		JSONReportGenerator.generateErrorReport(ERROR_MAP, "ERRORData.json");

	}

	private static void generateAllReports() {
		
		//scrapedRecipeFullDataList from Tarladalal.com
		List<RecipeData> scrapedRecipeDataList = JSONReportGenerator.getRecipeDataList("scrapedFullData.json");
		
		HTMLReportGenerator.generateReport(scrapedRecipeDataList, "scrapedFullData.html", "Scraped Full Data Report");
		
		log.info("Total Data:" + scrapedRecipeDataList.size());
		//Fetch Filter Criteria from Excel
		FilterCriteria fc = new ExcelReader().readCriteriaSheet();
		
		//Filter for LFV Elimination List 
		List<RecipeData> lfvEliminationListRecipe = scrapedRecipeDataList.stream()
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getPreparationMethod()!= null && rd.getPreparationMethod().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getRecipeDescription()!= null && rd.getRecipeDescription().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getTag()!= null && rd.getTag().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getRecipeName()!= null && rd.getRecipeName().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getRecipeUrl()!= null && rd.getRecipeUrl().toString().toLowerCase().contains(e.toLowerCase())))
				.collect(Collectors.toList());
		
		JSONReportGenerator.generateReport(lfvEliminationListRecipe, "recipe_reports/json/lfvEliminate.json");
		HTMLReportGenerator.generateReport(lfvEliminationListRecipe, "recipe_reports/html/lfvEliminate.html", "LFV Elimination Report");
			
		//Filter for LFV Add List 
		List<RecipeData> lfvAddListRecipe = lfvEliminationListRecipe.stream()
				.filter(rd -> fc.getLfvAdd().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e)))
				.filter(rd -> fc.getLfvAdd().stream().anyMatch(e -> rd.getPreparationMethod()!= null && rd.getPreparationMethod().toString().toLowerCase().contains(e)))
				.filter(rd -> fc.getLfvAdd().stream().anyMatch(e -> rd.getRecipeDescription()!= null && rd.getRecipeDescription().toString().toLowerCase().contains(e)))
				.collect(Collectors.toList());
					
		JSONReportGenerator.generateReport(lfvAddListRecipe, "recipe_reports/json/lfvAdd.json");
		HTMLReportGenerator.generateReport(lfvAddListRecipe, "recipe_reports/html/lfvAdd.html", "LFV Add Report");
		
		//Filter LFV Add not Vegan List
		List<RecipeData> lfvAddNotVeganList = lfvEliminationListRecipe.stream()
				.filter(rd -> fc.getLfvAddNotVegan().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e)))
				.filter(rd -> fc.getLfvAddNotVegan().stream().anyMatch(e -> rd.getPreparationMethod()!= null && rd.getPreparationMethod().toString().toLowerCase().contains(e)))
				.filter(rd -> fc.getLfvAddNotVegan().stream().anyMatch(e -> rd.getRecipeDescription()!= null && rd.getRecipeDescription().toString().toLowerCase().contains(e)))
				.collect(Collectors.toList()); 
			
		JSONReportGenerator.generateReport(lfvAddNotVeganList, "recipe_reports/json/lfvAddNotVeganList.json");
		HTMLReportGenerator.generateReport(lfvAddNotVeganList, "recipe_reports/html/lfvAddNotVeganList.html", "LFV Add Not Vegan Report");
		
		// Filter LFV Recipes to Avoid
		List<RecipeData> lfvReceipeToAvoidList = lfvEliminationListRecipe.stream()
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getIngredients() != null && rd.getIngredients().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getPreparationMethod() != null && rd.getPreparationMethod().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getRecipeDescription() != null && rd.getRecipeDescription().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getTag() != null && rd.getTag().toString().toLowerCase().contains(e.toLowerCase())))
				.collect(Collectors.toList());	

		JSONReportGenerator.generateReport(lfvReceipeToAvoidList, "recipe_reports/json/lfvReceipeToAvoid.json");
		HTMLReportGenerator.generateReport(lfvReceipeToAvoidList, "recipe_reports/html/lfvReceipeToAvoid.html","LFV ReceipeToAvoid Report");

		
		//Filter for LFV-Allergy List
		for (String allergyName : fc.getAllergyList()){
			
		List<RecipeData> lfvAllergyList = lfvEliminationListRecipe.stream()
				.filter(rd -> !(rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getPreparationMethod()!= null && rd.getPreparationMethod().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getRecipeDescription() != null && rd.getRecipeDescription().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getTag() != null && rd.getTag().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getRecipeName() != null && rd.getRecipeName().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getRecipeUrl() != null && rd.getRecipeUrl().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.collect(Collectors.toList());
		
		JSONReportGenerator.generateReport(lfvAllergyList, "recipe_reports/json/lfvAllergy"+ allergyName+".json");
		HTMLReportGenerator.generateReport(lfvAllergyList, "recipe_reports/html/lfvAllergy"+ allergyName+".html", "LFV Allergy-"+ allergyName+" Report");
		}
			
		//Filter for LCHF Elimination List
		List<RecipeData> lchfEliminationListRecipe = scrapedRecipeDataList.stream()
						.filter(rd -> !fc.getLchfEliminate().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e.toLowerCase())))
						.filter(rd -> !fc.getLchfEliminate().stream().anyMatch(e -> rd.getPreparationMethod()!= null && rd.getPreparationMethod().toString().toLowerCase().contains(e.toLowerCase())))
						.filter(rd -> !fc.getLchfEliminate().stream().anyMatch(e -> rd.getRecipeDescription()!= null && rd.getRecipeDescription().toString().toLowerCase().contains(e.toLowerCase())))
						.filter(rd -> !fc.getLchfEliminate().stream().anyMatch(e -> rd.getTag()!= null && rd.getTag().toString().toLowerCase().contains(e.toLowerCase())))
						.filter(rd -> !fc.getLchfEliminate().stream().anyMatch(e -> rd.getRecipeName()!= null &&  rd.getRecipeName().toString().toLowerCase().contains(e.toLowerCase())))
						.filter(rd -> !fc.getLchfEliminate().stream().anyMatch(e -> rd.getRecipeUrl()!= null && rd.getRecipeUrl().toString().toLowerCase().contains(e.toLowerCase())))
						.collect(Collectors.toList());
								
		JSONReportGenerator.generateReport(lchfEliminationListRecipe, "recipe_reports/json/LchfElimination.json");
		HTMLReportGenerator.generateReport(lchfEliminationListRecipe, "recipe_reports/html/LchfElimination.html", "LCHF Elimination Report");
									
		//Filter for LCHF Add List
		List<RecipeData> lchfAddListRecipe = lchfEliminationListRecipe.stream()
						.filter(rd -> fc.getLfvAdd().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e)))
						.collect(Collectors.toList());
											
		JSONReportGenerator.generateReport(lchfAddListRecipe, "recipe_reports/json/LchfAddList_Recipe.json");
		HTMLReportGenerator.generateReport(lchfAddListRecipe, "recipe_reports/html/LchfAddList_Recipe.html", "LCHF Add Report");
		
		//Filter for LCHF-Allergy List
		for (String allergyName : fc.getAllergyList()){
					
				List<RecipeData> lchfAllergyList = lchfEliminationListRecipe.stream()
						.filter(rd -> !(rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(allergyName.toLowerCase())))
						.filter(rd -> !(rd.getPreparationMethod()!= null && rd.getPreparationMethod().toString().toLowerCase().contains(allergyName.toLowerCase())))
						.filter(rd -> !(rd.getRecipeDescription() != null && rd.getRecipeDescription().toString().toLowerCase().contains(allergyName.toLowerCase())))
						.filter(rd -> !(rd.getTag() != null && rd.getTag().toString().toLowerCase().contains(allergyName.toLowerCase())))
						.filter(rd -> !(rd.getRecipeName() != null && rd.getRecipeName().toString().toLowerCase().contains(allergyName.toLowerCase())))
						.filter(rd -> !(rd.getRecipeUrl() != null && rd.getRecipeUrl().toString().toLowerCase().contains(allergyName.toLowerCase())))
						.collect(Collectors.toList());
				
				JSONReportGenerator.generateReport(lchfAllergyList, "recipe_reports/json/lchfAllergy"+ allergyName+".json");
				HTMLReportGenerator.generateReport(lchfAllergyList, "recipe_reports/html/lchfAllergy"+ allergyName+".html", "LCHF Allergy - "+ allergyName+" Report");
				}
		
		
		log.info("LFV Eliminated Recipe Count: " + lfvEliminationListRecipe.size());
		log.info("LFV Added Recipe Count: " + lfvAddListRecipe.size());
		log.info("LCHF Eliminated Recipe Count " + lchfEliminationListRecipe.size());
		log.info("LCHF Added Recipe Count: " + lchfAddListRecipe.size());
		
	}

	private static void Get_tarfiles() 
	{
		String srcPath_json = System.getProperty("user.dir")+"/recipe_reports/json";
		String srcPath_Html = System.getProperty("user.dir")+"/recipe_reports/html";
		Tarfileextract.Archivetotar(srcPath_json);
		log.info("Consolidated json reports are stored as Tar files");
		Tarfileextract.Archivetotar(srcPath_Html);
		log.info("Consolidated HTML reports are stored as Tar files");
		
	}

}
