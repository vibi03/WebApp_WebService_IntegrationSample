package runclass;

import java.io.BufferedReader;
import java.io.FileInputStream;


//import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.xpath.XPathConstants;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import io.cucumber.datatable.DataTable;
import utilities.PropertyReader;
import utilities.SoapUtil;
import utilities.TextReader;
import utilities.XMLUtil;
import utilities.XPathUtil;

public class WebServiceUtil {

	private static final String Calculator = "http://www.dneonline.com/calculator.asmx";
	private static final String SERVERURI = "http://tempuri.org/";
	private static int httpStatusCode;
	private static String SOAPrequest;
	private static String SOAPresponse;
	public static String FileName;

	final static Logger logger = LoggerFactory.getLogger(WebServiceUtil.class);

	public static String initialize() {

		final String applicationcontext = Calculator;
				
		return  applicationcontext;
	}

	public static void setStatusCode(int i) {
		httpStatusCode = i;
	}

	public static int getHttpStatusCode() {
		return httpStatusCode;
	}

	public static void setBasicAuthCreds(String user, String password) {
		final String authHeader = SoapUtil.getAuthorizationHeadder(user, password) ;  
		SoapUtil.setHTTP_HEADER_AUTHORIZATION(authHeader);
	}
	
	public static void createSOAPRequest(DataTable table) throws Exception {

		List<Map<String, String>> data = table.asMaps(String.class, String.class);

		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		SOAPPart soapPart = soapMessage.getSOAPPart();

		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("tem", SERVERURI);	

		// SOAP Body
		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("Add", "tem");
		soapBodyElem.addChildElement("intA","tem").addTextNode(data.get(0).get("value1"));
		soapBodyElem.addChildElement("intB","tem").addTextNode(data.get(0).get("value2"));
		
		soapMessage.getSOAPHeader();
		SOAPFactory.newInstance();
		soapMessage.saveChanges();

		/* Print the request message */
		System.out.print("Request SOAP Message = ");
		soapMessage.writeTo(System.out);

		System.out.println();

		SOAPrequest = SoapUtil.soapMessageToString(soapMessage);



	}

	public static String getResponse() {

		return SOAPresponse;
	}

	public static String getRequest() {

		return SOAPrequest;
	}

	public static void setResponse(String repsone) {

		SOAPresponse = repsone;
	}

	public static void setRequestXml(String Filename) throws IOException {
		StringWriter request = new StringWriter();
		request.write(TextReader.ReadFile(Filename));
		WebServiceUtil.SOAPrequest = request.toString();
		logger.info("The Wrong Xml request is :" + request);

	}

	public static void CheckResponseContentXML(String operationName, DataTable expectedResponseValues)
			throws Throwable {
		Element responseXml = XMLUtil.getElementFromString(SOAPresponse);
		for (Map<Object, Object> row : expectedResponseValues.asMaps(String.class, String.class)) {
			String tagLabel = (String) row.get("Response Item");

			String tag = tagLabel.replace(" ", "_");
			
			String propertyKey = operationName.concat(".").concat(tag);
			String responseXPath = PropertyReader.getPropertyNotNull(propertyKey);

			String xpathToTag = "//".concat(responseXPath);

			System.out.println("xpathToTag:" + xpathToTag);

			// get expected response value
			String expectedResponseValue = (String) row.get("Expected Value");
			// get actual response value
			String actualResponseValue = (String) XPathUtil.evaluateXPath(xpathToTag, responseXml,
					XPathConstants.STRING);
	
		
			Assert.assertEquals(expectedResponseValue, actualResponseValue);

		}
		System.out.println("Assertion Completed Successfully");
	}

	

	public static void CheckSuccessResponseContentXML(String operationName,DataTable tabel)
			throws Throwable {
		Element responseXml = XMLUtil.getElementFromString(SOAPresponse);
		 {
			 for (Map<Object, Object> row : tabel.asMaps(String.class, String.class)) {
					String tagLabel = (String) row.get("Response Item");
			
			String tag = tagLabel.replace(" ", "_");

			String propertyKey = operationName.concat(".").concat(tag);
			String responseXPath = PropertyReader.getPropertyNotNull(propertyKey);

			String xpathToTag = "//".concat(responseXPath) + "/text()";

			System.out.println("xpathToTag:" + xpathToTag);

			
			// get actual response value
			String actualResponseValue = (String) XMLUtil.getTextValue(responseXml, "AddResult");

			System.out.println("actualResponseValue---"+actualResponseValue);
			Assert.assertNotNull(actualResponseValue);
			String webAppResult="";
			/*FileReader fr=new FileReader("E:\\Selenium projects\\WistaProject\\result.txt");
			
	          int i;    
	          while((i=fr.read())!=-1)    
	          System.out.print("Result from text file -"+(char)i);  	          
	          webAppResult+=(char)i;
	          fr.close();    */
			InputStream is = new FileInputStream("E:\\Selenium projects\\WistaProject\\result.txt");
			BufferedReader buf = new BufferedReader(new InputStreamReader(is));
			        
			webAppResult = buf.readLine();
			System.out.println("Result from text file -" + webAppResult);
	          Assert.assertTrue((webAppResult).equals(actualResponseValue));
		}
		System.out.println("Assertion Completed Successfully");
	}
}

}
