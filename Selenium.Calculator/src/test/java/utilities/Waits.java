package utilities;

import java.time.Duration;
import java.util.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

public class Waits {
	
	public static WebDriver driver;

	
	public static boolean fluentWaitForElementVisibility(WebElement element)
	{
		driver=BrowserFactory.getDriver();
		FluentWait<WebDriver> fwait=new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(5))
				.ignoring(NoSuchElementException.class,TimeoutException.class)
				.ignoring(StaleElementReferenceException.class);
		try
		{
			fwait.until(ExpectedConditions.visibilityOf(element));
		}
		catch(Exception e)
		{
			System.out.println("Element not found");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void fluentWaitForElementToBeClickable(WebElement element)
	{
		driver=BrowserFactory.getDriver();
		FluentWait<WebDriver> fwait=new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(5))
				.ignoring(NoSuchElementException.class,TimeoutException.class)
				.ignoring(StaleElementReferenceException.class);
		try
		{
			fwait.until(ExpectedConditions.visibilityOf(element));
			fwait.until(ExpectedConditions.elementToBeClickable(element));
		}
		catch(Exception e)
		{
			System.out.println("Element not found");
			e.printStackTrace();
		}
	}
	
	public static void fluentWaitForiFrame(WebElement element)
	{
		driver=BrowserFactory.getDriver();
		FluentWait<WebDriver> fwait=new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(5))
				.ignoring(NoSuchElementException.class,TimeoutException.class)
				.ignoring(StaleElementReferenceException.class);
		try
		{
			fwait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(element));
		}
		catch(Exception e)
		{
			System.out.println("Element not found");
			e.printStackTrace();
		}
	}
}
