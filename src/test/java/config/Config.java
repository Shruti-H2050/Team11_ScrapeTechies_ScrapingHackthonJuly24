package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Config {

	 public static WebDriver driver;
	  
	 public static void launchDriver() {
		 driver = new ChromeDriver();
		 driver.get("https://www.tarladalal.com/RecipeAtoZ.aspx#");
	 }
	 
}
