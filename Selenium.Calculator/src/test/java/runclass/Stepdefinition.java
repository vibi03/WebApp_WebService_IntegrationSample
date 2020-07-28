package runclass;

import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;
import pageObject.LaunchCalculator;
import pageObject.Manipulations;
import utilities.BrowserFactory;

public class Stepdefinition {

	public static WebDriver driver;
	@Before
	public void beforeMethod(Scenario scenario)
	{
		System.out.println("Scenario name is - "+scenario.getName());
	}
	
	@Given("^User able to launch the calculator page$")
	public void launchCalculator()
	{
		LaunchCalculator.launch();
	}
	
	@Then("^Provide values to perform Addition$")
	public void doAddition(DataTable values)
	{
		Manipulations.addition(values);
	}
	
	@And("^Check resultant value is displayed")
	public void checkOutputValue()
	{
		Manipulations.checkOutputValue();
	}
	
	@Then("^Save output in a file")
	public void saveOutput()
	{
		Manipulations.saveOutputValue();
	}
	
	@After
	public void afterMethod(Scenario scenario) throws IOException
	{
		driver = BrowserFactory.getDriver();
		if(scenario.isFailed())
		{
			byte[] screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
			scenario.embed(screenshot, "image/png");
		}
		driver.close();
		Runtime.getRuntime().exec("Taskkill /IM chromedriver.exe /F");
		System.out.println("-------------------------------------------------------------------------------");
		System.out.println(scenario.getName()+" :-----" + scenario.getStatus());
		System.out.println("-------------------------------------------------------------------------------");
		
	}
}
