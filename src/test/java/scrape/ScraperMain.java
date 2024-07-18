package scrape;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.extern.log4j.Log4j2;
import pojo.FilterCriteria;
import pojo.RecipeData;
import report.HTMLReportGenerator;
import report.JSONReportGenerator;
import utils.ConfigLoader;
import utils.ExcelReader;
import utils.Tarfileextract;

public class ScraperMain {

	private static final Logger log = LogManager.getLogger(ScraperMain.class);
	public static Map<String, RecipeData> ERROR_MAP = new HashMap<>();

	public static void main(String args[]) {
		
		boolean loadDataRequired = ConfigLoader.isLoadDataRequired();//Make it true in config.properties file to scrape from website
		
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
        int timeout = ConfigLoader.getTimeout();
        String beginsWithParam = ConfigLoader.getBeginsWithParam();
        String recipeUrlPrefix = ConfigLoader.getRecipeUrlPrefix();
		ScraperJsoup sj = new ScraperJsoup(baseUrl, alphabets, threadPoolSize, timeout, beginsWithParam, recipeUrlPrefix);

		List<RecipeData> recipeData = sj.extractRecipeData();
		
		log.info("Data:" + recipeData);
		log.info("RecipeSize: " + recipeData.size());
		log.info("ERRORS " + ERROR_MAP);

		HTMLReportGenerator.generateReport(recipeData, "scrapedFullData.html", "Scraped Full Data Report");
		JSONReportGenerator.generateReport(recipeData, "scrapedFullData.json");
		//JSONReportGenerator.generateErrorReport(ERROR_MAP, "ERRORData.json");

	}

	private static void generateAllReports() {
		
		//scrapedRecipeFullDataList from Tarladalal.com
		try {
		List<RecipeData> scrapedRecipeDataList = JSONReportGenerator.getRecipeDataList("scrapedFullData.json");
		if (scrapedRecipeDataList == null) {
            throw new NullPointerException("Recipe data list is null");
        }
		
		
		log.info("Total Data:" + scrapedRecipeDataList.size());
		//Fetch Filter Criteria from Excel
		FilterCriteria fc = new ExcelReader().readCriteriaSheet();
		
		//Filter for LFV Elimination List 
		List<RecipeData> lfvEliminationList = scrapedRecipeDataList.stream()
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getPreparationMethod()!= null && rd.getPreparationMethod().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getRecipeDescription()!= null && rd.getRecipeDescription().toString().toLowerCase().contains(e.toLowerCase())))
				.filter(rd -> !fc.getLfvEliminate().stream().anyMatch(e -> rd.getTag()!= null && rd.getTag().toString().toLowerCase().contains(e.toLowerCase())))
				.collect(Collectors.toList());
		
		JSONReportGenerator.generateReport(lfvEliminationList, ConfigLoader.getProperty("reportFilePath")+"json/lfvEliminate.json");
		HTMLReportGenerator.generateReport(lfvEliminationList, ConfigLoader.getProperty("reportFilePath")+"html/lfvEliminate.html", "LFV Elimination Report");
			
		//Filter for LFV Add List 
		List<RecipeData> lfvAddList = lfvEliminationList.stream()
				.filter(rd -> fc.getLfvAdd().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e)))
				.collect(Collectors.toList());
					
		JSONReportGenerator.generateReport(lfvAddList, ConfigLoader.getProperty("reportFilePath")+"json/lfvAdd.json");
		HTMLReportGenerator.generateReport(lfvAddList, ConfigLoader.getProperty("reportFilePath")+"html/lfvAdd.html", "LFV Add Report");
		
		//Filter LFV Add not Vegan List
		List<RecipeData> lfvAddNotVeganList = lfvEliminationList.stream().filter(
				rd -> fc.getLfvAddNotVegan().stream().anyMatch(e -> rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(e)))
				.collect(Collectors.toList());
			
		JSONReportGenerator.generateReport(lfvAddNotVeganList, ConfigLoader.getProperty("reportFilePath")+"json/lfvAddNotVeganList.json");
		HTMLReportGenerator.generateReport(lfvAddNotVeganList, ConfigLoader.getProperty("reportFilePath")+"html/lfvAddNotVeganList.html", "LFV Add Not Vegan Report");

		//Filter for LFV-Allergy List
		for (String allergyName : fc.getLfvAllergyList()){
			
		List<RecipeData> lfvAllergyList = lfvEliminationList.stream().filter(
				rd -> !(rd.getIngredients()!= null && rd.getIngredients().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getPreparationMethod()!= null && rd.getPreparationMethod().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getRecipeDescription() != null && rd.getRecipeCategory().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.filter(rd -> !(rd.getTag() != null && rd.getTag().toString().toLowerCase().contains(allergyName.toLowerCase())))
				.collect(Collectors.toList());
		
		JSONReportGenerator.generateReport(lfvAllergyList, ConfigLoader.getProperty("reportFilePath")+"json/lfvAllergy"+ allergyName+".json");
		HTMLReportGenerator.generateReport(lfvAllergyList, ConfigLoader.getProperty("reportFilePath")+"html/lfvAllergy"+ allergyName+".html", "LFV Allergy-"+ allergyName+" Report");
		}
			
		
		System.out.println("Elimination Filtered Count: " + lfvEliminationList.size());
		System.out.println("Add Filtered Count: " + lfvAddList.size());
//		System.out.println("lfvAllergyMilk Filtered Count: " + lfvAllergyMilkList.size());
		}catch(NullPointerException e)
		{
			log.error("Null pointer exception occured");
		}
	}

	private static void Get_tarfiles() 
	{
		String srcPath_json = System.getProperty("user.dir") + "/"+ ConfigLoader.getProperty("reportFilePath") + "json";
        String srcPath_Html = System.getProperty("user.dir") + "/"+ ConfigLoader.getProperty("reportFilePath") + "html";
		Tarfileextract.Archivetotar(srcPath_json);
		log.info("Consolidated json reports are stored as Tar files");
		Tarfileextract.Archivetotar(srcPath_Html);
		log.info("Consolidated HTML reports are stored as Tar files");
		
	}

}