package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
		List<String> lfvEliminationList = getCriteriaList(workbook, "Final list for LFV Elimination ", 0, 2);
		List<String> lfvaddList = getCriteriaList(workbook, "Final list for LFV Elimination ", 1,2);
		List<String> lfvAddNotVeganList = getCriteriaList(workbook, "Final list for LFV Elimination ", 2,2);
		List<String> lfvRecipesToAvoidList = getCriteriaList(workbook, "Final list for LFV Elimination ", 3,2);
		List<String> lfvOptionalRecipeList = getCriteriaList(workbook, "Final list for LFV Elimination ", 4,2);
		
       	
    	fc.setLfvEliminate(lfvEliminationList);
    	fc.setLfvAdd(lfvaddList);
    	fc.setLfvAddNotVegan(lfvAddNotVeganList);
    	fc.setReceipeAvoid(lfvRecipesToAvoidList);
    	fc.setOptionalRecipe(lfvOptionalRecipeList);
    	
    	try {
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return fc;

	}

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
	
//	public static void main(String args[]) throws IOException
//	{
//		ExcelReader re=new ExcelReader();
//		System.out.println("Criteria:" + re.readCriteriaSheet());
//				
//	}

}
