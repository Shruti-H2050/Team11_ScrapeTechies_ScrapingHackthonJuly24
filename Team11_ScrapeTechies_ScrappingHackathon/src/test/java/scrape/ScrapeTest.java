package scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import config.DriverFactory;

@Test
public class ScrapeTest {

	  static WebDriver driver;
	  static String url = "https://www.tarladalal.com/RecipeAtoZ.aspx#";
	  
//	 public static void launchDriver() {
//		 driver = new ChromeDriver();
//		 driver.get("https://www.tarladalal.com/RecipeAtoZ.aspx#");
//	 }
	 
	 public ScrapeTest() {
		 
		 driver = DriverFactory.getInstance();
		 driver.get(url);
	 }
	 
	 
	 
	 public static void parseDataOnPage() throws IOException {

		  // list of WebElements that store all the links
		 Document document = Jsoup.connect(url).get();
		 Elements recipes = document.select(".rcc_recipecard");
		  //System.out.println(element.getFirst());
		 for(Element recipe: recipes) {
			 String recipeId = recipe.select("span").text();
			 String recipeTitle = recipe.select(".rcc_recipename ").text();
			 String recipeDesc = recipe.select(".rcc_desc").text();
			 System.out.println(recipeId);
			 System.out.println(recipeTitle);
			 System.out.println(recipeDesc);
			 System.out.println("*************************");
	
		/*** Navigate to next page using navigate method and again repeat above steps ie 
		 * call parseDataOnPage to retrieve recipeId, recipeTitl, recipeDesc*/
			 
			 /**To fetch other fields like preparation time,cooking time,ingredients create a separate class ***/
			 
		 }
		 
		//  List<WebElement> raw_recipes = driver.findElements(By.className("rcc_recipecard"));
		 // System.out.println(raw_recipes.get(1));

		  // arraylist to store all the links in string form (can be optimized)
		//  ArrayList<String> links = new ArrayList<>(14);
		  //System.out.println(links);

		  // loop through raw_recipes to fill the links arraylist
//		  for (WebElement e : raw_recipes)
//		  {
//		   // .findElement -----> finds the tag <a> inside the current WebElement
//		   // .getAttribute ----> returns the href attribute of the <a> tag in the current WebElement
//		   links.add(e.findElement(By.tagName("a")).getAttribute("href"));
//		  }
//
//		  List<WebElement> raw_ids = driver.findElements((By.className("rcc_rcpno")));
//		  System.out.println(raw_ids);
	 }
		 
//	 public static void main(String[] args) throws IOException {
//	//launchDriver();
//	parseDataOnPage();
//}

}
