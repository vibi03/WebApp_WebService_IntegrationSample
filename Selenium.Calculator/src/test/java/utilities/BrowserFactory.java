package utilities;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BrowserFactory {

	public static WebDriver driver;
	
	public static void setWebDriver() 
	{
		WebDriverManager.chromedriver().setup(); 
		ChromeOptions options= new ChromeOptions();
		options.addArguments("disable-infobars");
		options.addArguments("--start-maximized");
		driver=new ChromeDriver(options);
		Capabilities caps = ((RemoteWebDriver)driver).getCapabilities();
		System.out.println(caps.getBrowserName());
		System.out.println(caps.getVersion());
		System.out.println(caps.getPlatform());
		
	}
	public static WebDriver getDriver()
	{
		return BrowserFactory.driver;
		
	}
	
}
