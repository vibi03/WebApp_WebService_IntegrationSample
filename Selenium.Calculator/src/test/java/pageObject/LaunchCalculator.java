package pageObject;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import utilities.BrowserFactory;
import utilities.Waits;

public class LaunchCalculator {

	public static WebDriver driver;

	@FindBy(how=How.XPATH,using="//div[@id='logo']")
	public static WebElement pagecheck;
	
	public static void launch() {
		
		BrowserFactory.setWebDriver();
		driver=BrowserFactory.getDriver();
		PageFactory.initElements(driver, LaunchCalculator.class);
		driver.get("https://www.calculator.net/");
		Assert.assertTrue("Calculator page launched successfully",Waits.fluentWaitForElementVisibility(pagecheck));
	}
	
}
