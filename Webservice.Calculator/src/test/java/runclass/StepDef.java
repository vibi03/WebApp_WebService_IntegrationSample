package runclass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utilities.XmlValidationUtil;
import utilities.PropertyReader;
import utilities.SoapUtil;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;



public class StepDef {
	private static String url;
	String operationName = null;
	final static Logger logger = LoggerFactory.getLogger(StepDef.class);
	
	@Before
	public void initialize() throws Throwable {
		
		url = WebServiceUtil.initialize();
		logger.debug("URL is:{}", url);
		logger.info("URL is:{}", url);
		
	}
	
	
	@After
	public void after(Scenario scenario) throws InterruptedException {
		logger.info("### End Scenario: {}", scenario.getName());
	}

	
	
	
	@Given("^the consumer requests to \"(.*?)\"$")
	public void the_consumer_requests_to(String serviceOperationLabel) throws Throwable {
	    
		String serviceOperationLabelKey = serviceOperationLabel.replace(" ",
				"_");				
		
		String propertyKey = serviceOperationLabelKey;
		operationName = PropertyReader.getPropertyNotNull(propertyKey);	
		
	}


	
	
	@Given("^the Soap request contains values as$")
	public void the_Soap_request_contains_values_as(DataTable table) throws Throwable {
	   
		WebServiceUtil.createSOAPRequest(table);
		
		
	}

	@Given("^the HTTP SOAP post request is made$")
	public void the_HTTP_SOAP_post_request_is_made() throws Throwable {
	 
		SoapUtil.HttpSecuredSoapPost(url);

	}

	@Then("^the HTTP response is \"(.*?)\"$")
	public void the_HTTP_response_is(String code) throws Throwable {
	 
		
		switch (code) {
		case "200":
			assertEquals(200, WebServiceUtil.getHttpStatusCode());
			break;
		case "204":
			assertEquals(204, WebServiceUtil.getHttpStatusCode());
			break;
		case "Created":
			assertEquals(201, WebServiceUtil.getHttpStatusCode());
			break;
		case "Unauthorized":
			assertEquals(401, WebServiceUtil.getHttpStatusCode());
			break;
		case "Forbidden":
			assertEquals(403, WebServiceUtil.getHttpStatusCode());
			break;
		case "500":
			assertEquals(500, WebServiceUtil.getHttpStatusCode());
			break;
		case "400":
			assertEquals(400, WebServiceUtil.getHttpStatusCode());	
			break;
		case "404":
			assertEquals(404, WebServiceUtil.getHttpStatusCode());	
			break;	
			
		default:
			fail("unexpected http code " + code);
		}
		
	}

	@Then("^the response is validated successfully$")
	public void the_response_is_validated_successfully() throws Throwable {
	    
		SoapUtil.validateRequest();
		
		
	}

	@Then("^the following details are returned as XML:$")
	public void the_following_details_are_returned_as_XML(DataTable validationTable) throws Throwable {
	  
		WebServiceUtil.CheckResponseContentXML(operationName, validationTable);	
	}


	@Then("^the response nodes are validated successfully for \"(.*?)\" Service$")
	public void the_response_nodes_are_validated_successfully_for_Service(String fileName) throws Throwable {
	
	   
	    XmlValidationUtil.validateXmlStructure(WebServiceUtil.getResponse(), fileName);
	}

	@Then("^the following details are returned as successful XML response:$")
	public void the_following_details_are_returned_as_successful_XML_response(DataTable arg1) throws Throwable {
	   
		WebServiceUtil.CheckSuccessResponseContentXML(operationName, arg1);
		
	}
	
	
}
