package utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import pojo.FilterCriteria;

public class ExcelReader {
	
	public  FilterCriteria readCriteriaSheet()
	{	
		FilterCriteria fc = new FilterCriteria();
		
		InputStream is = ExcelReader.class.getResourceAsStream("/testdata/IngredientsAndComorbidities-ScrapperHackathon.xlsx");
		
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//LFV Elimination Excel Data
		List<String> lfvEliminationList = getCriteriaList(workbook, "Final list for LFV Elimination ", 0, 2);
		List<String> lfvAddList = getCriteriaList(workbook, "Final list for LFV Elimination ", 1,2);
		List<String> lfvAddNotVeganList = getCriteriaList(workbook, "Final list for LFV Elimination ", 2,2);
		List<String> lfvRecipesToAvoidList = getCriteriaList(workbook, "Final list for LFV Elimination ", 3,2);
		List<String> lfvOptionalRecipeList = getCriteriaList(workbook, "Final list for LFV Elimination ", 4,2);
		//List<String> lfvOptionalRecipeList = new ArrayList<String>(
				   // Arrays.asList( "tea", "without sugar"));
				
		//LCHF Elimination Excel Data 
		List<String> lchfEliminationList = getCriteriaList(workbook, "Final list for LCHFElimination ", 0, 2);
		List<String> lchfAddList = getCriteriaList(workbook, "Final list for LCHFElimination ", 1,2);
		List<String> lchfRecipesToAvoidList = getCriteriaList(workbook, "Final list for LCHFElimination ", 3,2);
		List<String> lchfFoodProcessingList = getCriteriaList(workbook, "Final list for LCHFElimination ", 4,2);
		
		//LFV Allergy-Milk Excel Data
		List<String> lfvAllergyList = getCriteriaList(workbook, "Filter -1 Allergies - Bonus Poi", 0, 1);
		
		System.out.println("Allergy: " + lfvAllergyList.get(0));
		
       	//LFV Excel Extracted Data setting into Pojo
    	fc.setLfvEliminate(lfvEliminationList);
    	fc.setLfvAdd(lfvAddList);
    	fc.setLfvAddNotVegan(lfvAddNotVeganList);
    	fc.setLfvReceipeToAvoid(lfvRecipesToAvoidList);
    	fc.setLfvOptionalRecipe(lfvOptionalRecipeList);
    	
    	//LCHF Excel Extracted Data setting into Pojo
    	fc.setLchfEliminate(lchfEliminationList);
    	fc.setLchfAdd(lchfAddList);
    	fc.setLchfReceipeToAvoid(lchfRecipesToAvoidList);
    	fc.setLchfFoodProcessing(lchfFoodProcessingList);
    	
    	//LFV Allergy List from Excel
    	fc.setLfvAllergyList(lfvAllergyList);
    	
    	
    	try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return fc;

	}
	
	//Fetch the Elimination Excel Full Data
	private List<String> getCriteriaList(XSSFWorkbook workbook, String sheetName, int colIndex, int rowIndex) 
	{	
		List<String> criteriaList= new ArrayList<>();
		XSSFSheet sheet = workbook.getSheet(sheetName);
		
		for(int i=rowIndex; i<=sheet.getLastRowNum(); i++)
		{   
			XSSFRow criteriaRow = sheet.getRow(i);
			
			String criteriaCellValue = criteriaRow.getCell(colIndex) != null ? criteriaRow.getCell(colIndex).getStringCellValue():null;
			
			if(criteriaCellValue != null && !criteriaCellValue.isEmpty())
			{	
				criteriaList.add(criteriaCellValue.toLowerCase());
			}
	
		}
		return criteriaList;
	}
	
}