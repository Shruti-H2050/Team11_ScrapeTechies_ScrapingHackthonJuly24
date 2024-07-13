package Scrape;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel_reader_E {

	
		public  void readElimationsheet( ) throws IOException {
    	    List<String> Elimination_list= new ArrayList<>();;
    	    
    		String path = System.getProperty("user.dir")+"/Testdata/Eliminationlist.xlsx";
    		File Excelfile = new File(path);
    		
    		FileInputStream Fis = new FileInputStream(Excelfile);
    		XSSFWorkbook workbook = new XSSFWorkbook(Fis);
    		XSSFSheet sheet = workbook.getSheet("Elimination");
    		for(int i=1;i<=sheet.getLastRowNum();i++)
    		{   XSSFRow E_row=sheet.getRow(i);
    			String E_value=E_row.getCell(0).toString();
			    Elimination_list.add(E_value);
    			
    		}
    		
        	System.out.println(Elimination_list);
       
		workbook.close();
	
	 }
		
	public static void main(String args[]) throws IOException
		{
		Excel_reader_E re=new Excel_reader_E();
			re.readElimationsheet( );
			
			
		}
	
	

}
