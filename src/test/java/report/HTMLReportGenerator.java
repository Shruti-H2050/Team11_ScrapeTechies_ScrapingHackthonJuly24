package report;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import pojo.RecipeData;

public class HTMLReportGenerator {

	 public static void generateReport(List<RecipeData> data, String fileName, String title) {
	        StringBuilder htmlContent = new StringBuilder();

	        htmlContent.append("<html>");
	        htmlContent.append("<head><title>" + title + "</title></head>");
	        htmlContent.append("<body>");
	        htmlContent.append("<h1>" + title + "</h1>");
	        htmlContent.append("<table border='1'>");
	        htmlContent.append("<tr>");
	        htmlContent.append("<th>Recipe ID</th>");
	        htmlContent.append("<th>Recipe Name</th>");
	        htmlContent.append("<th>Recipe Category</th>");
	        htmlContent.append("<th>Food Category</th>");
	        htmlContent.append("<th>Ingredients</th>");
	        htmlContent.append("<th>Prep Time</th>");
	        htmlContent.append("<th>Cook Time</th>");
	        htmlContent.append("<th>Tags</th>");
	        htmlContent.append("<th>Servings</th>");
	        htmlContent.append("<th>Cuisine Category</th>");
	        htmlContent.append("<th>Description</th>");
	        htmlContent.append("<th>Preparation Method</th>");
	        htmlContent.append("<th>Nutrient Values</th>");
	        htmlContent.append("<th>Recipe URL</th>");
	        htmlContent.append("</tr>");

	        for (RecipeData item : data) {
	            htmlContent.append("<tr>");
	            htmlContent.append("<td>").append(item.getRecipeId()).append("</td>");
	            htmlContent.append("<td>").append(item.getRecipeName()).append("</td>");
	            htmlContent.append("<td>").append(item.getRecipeCategory()).append("</td>");
	            htmlContent.append("<td>").append(item.getFoodCategory()).append("</td>");
	            htmlContent.append("<td>").append(item.getIngredients()).append("</td>");
	            htmlContent.append("<td>").append(item.getPrepTime()).append("</td>");
	            htmlContent.append("<td>").append(item.getCookTime()).append("</td>");
	            htmlContent.append("<td>").append(item.getTag()).append("</td>");
	            htmlContent.append("<td>").append(item.getServings()).append("</td>");
	            htmlContent.append("<td>").append(item.getCuisineCategory()).append("</td>");
	            htmlContent.append("<td>").append(item.getRecipeDescription()).append("</td>");
	            htmlContent.append("<td>").append(item.getPreparationMethod()).append("</td>");
	            htmlContent.append("<td>").append(item.getNutrientValues()).append("</td>");
	            htmlContent.append("<td>").append(item.getRecipeUrl()).append("</td>");
	            htmlContent.append("</tr>");
	        }

	        htmlContent.append("</table>");
	        htmlContent.append("</body>");
	        htmlContent.append("</html>");

	        try (FileWriter writer = new FileWriter(fileName)) {
	            writer.write(htmlContent.toString());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        System.out.println("HTML report generated successfully.");
	    }
	}

