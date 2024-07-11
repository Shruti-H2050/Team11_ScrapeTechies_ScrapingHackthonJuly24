package scrape;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

public class PaginationTest {
	WebDriver driver;

	@Test
	public void getPage() throws InterruptedException {
		driver = new ChromeDriver();
		driver.get("https://www.tarladalal.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		WebElement recipeAtoZ = driver.findElement(By.xpath("//div[@id='toplinks']/a[5]"));
		recipeAtoZ.click();
		for (int i = 1; i <= 27; i++) {
			WebElement num = driver.findElement(By.xpath("//div[@id='cardholder']/table/tbody/tr/td[" + i + "]"));
			num.click();

			/*WebElement div = driver.findElement(By.xpath("//*[@id=\"maincontent\"]/div[1]/div[2]"));
			
			List<WebElement> links = div.findElements(By.tagName("a"));
			System.out.println("links size = " + links.size());
			
			int lastElement = Integer.parseInt(links.get(links.size() - 1).getText());
			System.out.println("lastElement = " + lastElement);
			for (int x = 1; x <= lastElement; x++) {
				WebElement element = div.findElement(By.linkText(String.valueOf(x)));
				element.click();
			}*/
			

		}

	}
}
