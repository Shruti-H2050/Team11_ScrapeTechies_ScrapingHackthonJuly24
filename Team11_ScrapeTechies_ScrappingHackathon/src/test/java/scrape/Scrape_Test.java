package Scrape;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import pojo.RecipeData;

@Test
public class Scrape_Test {
	
	 public static WebDriver driver;
	  
	 public static void launchDriver() {
		 driver = new ChromeDriver();
		 driver.get("https://www.tarladalal.com/RecipeAtoZ.aspx#");
		
	 }
	 

	 public static List<RecipeData> parseDataOnPage() throws IOException {
         
		 List<RecipeData> recipeDataList = new ArrayList<>();
		 
		  
		//  List<WebElement> raw_recipes = driver.findElements(By.className("rcc_recipename"));
		  
		 List<WebElement> recipe = driver.findElements(By.className("rcc_recipecard"));
		 
			  
		  for (WebElement e : recipe)
		  {
			  Document recipeDoc = Jsoup.parse(driver.);//get the funion
			  System.out.println(recipeDoc.data());
			  RecipeData data  = new RecipeData();
			  //data.setRecipeName(e.getText());
		   	  data.setRecipeUrl(e.findElement(By.tagName("a")).getAttribute("href"));
			  recipeDataList.add(data);
		  }

		  List<WebElement> raw_ids = driver.findElements((By.className("rcc_rcpno")));
		  System.out.println(raw_ids);
		  
		  return recipeDataList;
	 }

	 public static void main(String[] args) throws IOException {
			launchDriver();
			List<RecipeData> list = parseDataOnPage();
			System.out.println("List " + list);
}

}
