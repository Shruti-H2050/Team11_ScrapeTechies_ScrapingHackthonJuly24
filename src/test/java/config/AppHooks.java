package config;

public class AppHooks {

	package AppHooks;

	import java.util.Properties;

	import org.openqa.selenium.OutputType;
	import org.openqa.selenium.TakesScreenshot;
	import org.openqa.selenium.WebDriver;

	import com.LMS.factory.DriverFactory;
	import com.LMS.utility.ApplicationData;
	import com.LMS.utility.ConfigReader;
	import com.LMS.utility.ExcelReader;
	import com.LMS.utility.LMSConstants;

	import io.cucumber.java.After;
	import io.cucumber.java.AfterStep;
	import io.cucumber.java.Before;
	import io.cucumber.java.BeforeAll;
	import io.cucumber.java.Scenario;

	public class ApplicationHooks {
		
		ConfigReader configReader = new ConfigReader();
		static Properties prop;
		DriverFactory driverFactory = new DriverFactory();	
		public static WebDriver driver;
		//public  WebDriver driver = DriverFactory.getDriver();

		@BeforeAll
		public static void before_all() {
			prop= ConfigReader.init_prop();
			ApplicationData appData = new ApplicationData();
			appData.setApplicationData(ExcelReader.loadExcelData());
			LMSConstants.applicationData = appData;
		}
	    @Before
	    public void beforeScenario() {
	    	
	    	try {
	    		//prop = configReader.init_prop();
	    	
	    	if(driver== null) {
	    		String browserName = prop.getProperty("browser");
	    		//driver = DriverManager.launchBrowser();
	    		driver = driverFactory.init_driver(browserName);   	
	    	}
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    }
	    
	   @AfterStep
	   		
		public void quitBrowser() {
			driver.quit();
		}
	}
	
}
