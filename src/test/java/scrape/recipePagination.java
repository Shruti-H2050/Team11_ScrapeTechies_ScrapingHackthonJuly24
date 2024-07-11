package scrape;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import javax.lang.model.element.Element;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

@Test
public class recipePagination {
	
		 public static WebDriver driver;
		  
		 public static void launchDriver() throws InterruptedException {
			 
			 //intialize chrome driver
			 driver = new ChromeDriver();
			 driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			driver.manage().window().maximize();
			
			// Navigate to the website
	        driver.get("https://www.tarladalal.com/RecipeAtoZ.aspx");
	        Thread.sleep(5000);
			
			 //driver.get("https://www.tarladalal.com/RecipeAtoZ.aspx#");
		 }
		 public static void recipeLink() throws InterruptedException
		 {
			 String strXpath;
			 for(int i=2;i<=5;i++)
			 {
				 
			strXpath= "//a[@class='respglink' and text()='+i+']";
			driver.findElement(By.xpath(strXpath)).click();
			Thread.sleep(2000);
		        }

			 }
		 
		 public static void main(String[] args) throws InterruptedException {
				launchDriver();
				recipeLink();
			}
}

		 


		 
