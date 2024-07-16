package utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    public static List<String> readIngredients(String filePath, String sheetName, int columnIndex) throws IOException {
        List<String> ingredients = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            for (Row row : sheet) {
                String ingredient = row.getCell(columnIndex).getStringCellValue();
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }
}
