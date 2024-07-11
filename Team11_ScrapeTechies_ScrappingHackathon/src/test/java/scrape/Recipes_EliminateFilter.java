package scrape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class Recipes_EliminateFilter {
	
	public static void main(String args[]) {

	
		WebDriver driver =new ChromeDriver();
		 //Locators
		String URL="https://www.tarladalal.com/RecipeAtoZ.aspx#";
		//List<WebElement> raw_recipes =driver.findElements(By.xpath("//div[@class='rcc_recipecard']"));//list of recipe per page
		//List<WebElement> recipes_ID =driver.findElements(By.xpath("//div[@class='rcc_recipecard']/div[2]/span/text()[1]"));//recipe id
	    List<WebElement> recipes_Name =driver.findElements(By.xpath("//span[@class='rcc_recipename'][1]/a"));//recipe name.Can navigate by this
		
		driver.get(URL);
		int row=recipes_Name.size();
		System.out.println(row);
		String S=driver.findElement(By.xpath("//div[@class='rcc_recipecard'][1]/div[2]/span")).getText();
		System.out.println(S);
	    String N =driver.findElement(By.xpath("//span[@class='rcc_recipename'][1]/a")).getText();
	    System.out.println(N);
		//div[@class='rcc_recipecard'][1]/div[2]/span/text()[1]
		/*System.out.println(rows);
	
		for(int i=0;i<=rows;i++)
		{
			String R_ID=recipes_ID.get(i).getText();
			System.out.println(R_ID);
			String R_Name=recipes_Name.get(i).getText();
			System.out.println(R_Name);
			
			
		}*/
		  
	
		
		driver.close();
			
		
		}
		
}
