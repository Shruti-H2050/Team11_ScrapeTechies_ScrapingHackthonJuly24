package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class DriverFactory {

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>() ;
	
	public static WebDriver getInstance() {
		
		if(driver.get() == null) {
			
			driver.set(new ChromeDriver());
			
		}
		return driver.get();
		
	}
}
