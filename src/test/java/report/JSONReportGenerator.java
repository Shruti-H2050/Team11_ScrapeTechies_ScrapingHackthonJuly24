package report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import pojo.RecipeData;

public class JSONReportGenerator {

	public static void generateReport(List<RecipeData> data, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JSON report generated successfully.");
    }
    
    public static void generateErrorReport(Map<String,RecipeData> data, String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JSON ERROR report generated successfully.");
    }
    
    public static List<RecipeData> getRecipeDataList(String fileName){
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	try {
			return Arrays.asList(gson.fromJson(new InputStreamReader(JSONReportGenerator.class.getResourceAsStream("/" + fileName)), RecipeData[].class));
		} catch (JsonSyntaxException | JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    	
    }
    
}


