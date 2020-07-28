package pageObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import io.cucumber.datatable.DataTable;

import utilities.BrowserFactory;
import utilities.Waits;


public class Manipulations {
	
	public static WebDriver driver;
	
	@FindBy(how=How.XPATH,using="//span[@class='sciop' and text()='×']")
	public static WebElement multiply;
	
	@FindBy(how=How.XPATH,using="//span[@class='sciop' and text()='–']")
	public static WebElement subtraction;
	
	@FindBy(how=How.XPATH,using="//span[@class='sciop' and text()='+']")
	public static WebElement addition;
	
	@FindBy(how=How.XPATH,using="//span[@class='sciop' and text()='/']")
	public static WebElement division;
	
	@FindBy(how=How.XPATH,using="//span[@class='scinm' and text()='0']")
	public static WebElement num0;
	
	@FindBy(how=How.XPATH,using="//span[@class='scinm' and text()='1']")
	public static WebElement num1;
	
	@FindBy(how=How.XPATH,using="//span[@class='scinm' and text()='2']")
	public static WebElement num2;
	
	@FindBy(how=How.XPATH,using="//span[@class='scinm' and text()='3']")
	public static WebElement num3;
	
	@FindBy(how=How.XPATH,using="//span[@class='scinm' and text()='4']")
	public static WebElement num4;
	
	@FindBy(how=How.XPATH,using="//span[@class='scinm' and text()='5']")
	public static WebElement num5;
	
	@FindBy(how=How.XPATH,using="//span[@class='scinm' and text()='6']")
	public static WebElement num6;
	
	@FindBy(how=How.XPATH,using="//span[@class='scinm' and text()='7']")
	public static WebElement num7;
	
	@FindBy(how=How.XPATH,using="//span[@class='scinm' and text()='8']")
	public static WebElement num8;
	
	@FindBy(how=How.XPATH,using="//span[@class='scinm' and text()='9']")
	public static WebElement num9;
	
	@FindBy(how=How.XPATH,using="//div[@id='sciOutPut']")
	public static WebElement output;
	
	public static void addition(DataTable values) {
		
		driver=BrowserFactory.getDriver();
		PageFactory.initElements(driver, Manipulations.class);
		
		String operand1="", operand2="";
		
		List<Map<String, String>> data = values.asMaps(String.class, String.class);
		for(Map<String, String>map:data)
		{
			operand1=map.get("value1").toString();
			operand2=map.get("value2").toString();
		}
		int operand1Size=operand1.length();
		int operand2Size=operand2.length();
		for(int i=0;i<operand1Size;i++)
		{
			switch(operand1.charAt(i))
			{
				case '0':
					num0.click();
					break;
				case '1':
					num1.click();
					break;
				case '2':
					num2.click();
					break;
				case '3':
					num3.click();
					break;
				case '4':
					num4.click();
					break;
				case '5':
					num5.click();
					break;
				case '6':
					num6.click();
					break;
				case '7':
					num7.click();
					break;
				case '8':
					num8.click();
					break;
				case '9':
					num9.click();
					break;
				default:
					System.out.println("Wrong input");
					break;
			}
			
		}
		
		System.out.println("Entered first operand");
		addition.click();
		System.out.println("Entered operator");
		
		for(int j=0;j<operand2Size;j++)
		{
			switch(operand2.charAt(j))
			{
				case '0':
					num0.click();
					break;
				case '1':
					num1.click();
					break;
				case '2':
					num2.click();
					break;
				case '3':
					num3.click();
					break;
				case '4':
					num4.click();
					break;
				case '5':
					num5.click();
					break;
				case '6':
					num6.click();
					break;
				case '7':
					num7.click();
					break;
				case '8':
					num8.click();
					break;
				case '9':
					num9.click();
					break;
				default:
					System.out.println("Wrong input");
					break;
			}
		}
		System.out.println("Entered second operand");
	}

	public static void saveOutputValue() {
		
		String outputValue;
		driver=BrowserFactory.getDriver();
		PageFactory.initElements(driver, Manipulations.class);
		outputValue=output.getText();	
		System.out.println("Output value is : " +outputValue);
		outputValue=outputValue.trim();
		try{
			FileWriter fw=new FileWriter("E:\\Selenium projects\\WistaProject\\result.txt");
			fw.write(outputValue);
			fw.close();
		}
		catch(IOException e)
		{
			System.out.println("Result value couldn't save to file");
		}
	}

	public static void checkOutputValue() {
		
		driver=BrowserFactory.getDriver();
		PageFactory.initElements(driver, Manipulations.class);
		
		Assert.assertEquals(true, Waits.fluentWaitForElementVisibility(output));
		
	}
	
}
