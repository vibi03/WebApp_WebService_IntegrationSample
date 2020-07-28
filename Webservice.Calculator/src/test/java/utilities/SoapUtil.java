package utilities;

import static runclass.WebServiceUtil.FileName;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import runclass.WebServiceUtil;




public class SoapUtil {
	final static Logger logger = LoggerFactory.getLogger(SoapUtil.class);
	
	
	private static String HTTP_HEADER_AUTHORIZATION;
//	private static JSONObject requestJson = null;
	private static String HTTP_HEADER_CONTENT_TYPE = "text/xml";
	public static String getHTTP_HEADER_AUTHORIZATION() {
		return HTTP_HEADER_AUTHORIZATION;
	}

	public static void setHTTP_HEADER_AUTHORIZATION(
			String hTTP_HEADER_AUTHORIZATION) {
		HTTP_HEADER_AUTHORIZATION = hTTP_HEADER_AUTHORIZATION;
	}

	public static String getAuthorizationHeadder(String user, String password) {

		final String creds = user + ":" + password;
		final String authHeader = "Basic "
				+ new String(Base64.encodeBase64((creds).getBytes()));
		logger.info("Auth headder {}, {}, {}", user, password, authHeader);

		return authHeader;
	}
	
	
	
	// secured connection
	
	
	public static void HttpSecuredSoapPost(String url) throws MalformedURLException, URIException, NullPointerException, ClassNotFoundException, UnsupportedOperationException, SOAPException {
				
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory.createConnection();
		String soaprequest = WebServiceUtil.getRequest();
		String responseString = null;
		try{
			
	        final PostMethod postMethod = new PostMethod(url);
	        //postMethod.setRequestHeader("Accept",  "text/xml");
	        //postMethod.setRequestHeader("Content-Type", "");
	                  
	        RequestEntity request = new StringRequestEntity(soaprequest, HTTP_HEADER_CONTENT_TYPE, null);
	        
	        postMethod.setRequestEntity(request);
	        HttpClientParams clientParams = new HttpClientParams();
	        clientParams.setAuthenticationPreemptive(true);
	        clientParams.setConnectionManagerClass(Class.forName("org.apache.commons.httpclient.MultiThreadedHttpConnectionManager"));
	        HttpClient httpClient = new HttpClient(clientParams);
	        httpClient.setHostConfiguration(new org.apache.commons.httpclient.HostConfiguration());
	        httpClient.executeMethod(postMethod);
	     
	        logger.info("httpPost Status Line {}", postMethod.getStatusLine());
	        logger.info("httpPost Status Line {}", postMethod.getStatusCode());
	        WebServiceUtil.setStatusCode(postMethod.getStatusCode());
	        
	        
	        
	       
	    	   
	    	   responseString = postMethod.getResponseBodyAsString();
	    	   logger.info("httpPOST Response {}", responseString);
	    	   WebServiceUtil.setResponse(responseString);
	    	   
	    	   
	       
    
			
		} catch(Exception e){
			System.out.println(e);
			
		}
		
            
		
		
	}
	

	public static void HttpSoapPost(String url) {
		try {
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			String request = WebServiceUtil.getRequest();

			StringEntity stringEntity = new StringEntity(request, "UTF-8");

			// Request parameters and other properties.
			HttpPost httpPost = new HttpPost(url);

			httpPost.setEntity(stringEntity);
			httpPost.addHeader("Accept", "text/xml");
			httpPost.addHeader("SOAPAction", "");
			// Execute and get the response.

			CloseableHttpClient httpClient = HttpClients.createDefault();
					//createDefault();

			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();

			String strResponse = null;
			if (entity != null) {
				strResponse = EntityUtils.toString(entity);
			}

			System.out.println("The Soap Response is : "+strResponse);
			
			WebServiceUtil.setStatusCode(response.getStatusLine().getStatusCode());
			System.out.println("The Status Code is  "+response.getStatusLine().getStatusCode());

			
			
			WebServiceUtil.setResponse(strResponse);

			// // Process the SOAP Response
			// printSOAPResponse(soapResponse);

			soapConnection.close();
		} catch (Exception e) {
			System.err.println("Error occurred while sending SOAP Request to Server");
			e.printStackTrace();
		}

	}

	private static void printSOAPResponse(SOAPMessage soapResponse) throws Exception {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source sourceContent = soapResponse.getSOAPPart().getContent();
		System.out.print("\nResponse SOAP Message = ");
		StreamResult result = new StreamResult(System.out);
		transformer.transform(sourceContent, result);
	}

	
	public static void validateRequest() throws SAXException {

		try {

			String SOAPresponse = WebServiceUtil.getResponse();
			File RequestFile = new File("RequestFile.txt");
			
			FileWriter writer = new FileWriter(RequestFile);
			SOAPresponse = SOAPresponse.replaceAll("&#xD;", "").replaceAll("&#xd;", "");
			// System.out.println(SOAPresponse);
			logger.info("The modified response is : " + SOAPresponse);
			
			
			writer.write(SOAPresponse);
			writer.flush();
			writer.close();

			File Comparefile = null;
	
			String env = System.getenv("TESTENVIRONMENT");
			
			if ((new File("src/test/resources/"+env+"ResponseStore/"+env+"NUFF/" + FileName + ".txt")).exists()) {
				Comparefile = new File(
						"src/test/resources/"+env+"ResponseStore/"+env+"NUFF/" + WebServiceUtil.FileName + ".txt");
			} else {
				Comparefile = new File(
						"src/test/resources/"+env+"ResponseStore/"+env+"HCML/" + WebServiceUtil.FileName + ".txt");
			}

			XMLUnit.setIgnoreWhitespace(true);
			XMLUnit.setIgnoreComments(true);
			XMLUnit.setIgnoreAttributeOrder(true);
			
			Diff diff = XMLUnit.compareXML(FileUtils.readFileToString(Comparefile).replaceAll("\n|\r", ""),
					FileUtils.readFileToString(RequestFile).replaceAll("\n|\r","")); 

			DetailedDiff details = new DetailedDiff(diff);
			System.out.println("Detailed difference is " + details.getAllDifferences());
			Assert.assertTrue(diff.similar());

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	
	
	// The request should be created based on the project.
	// private static SOAPMessage createSOAPRequest(String serverURI, DataTable
	// table) throws Exception {

		// Method to convert SoapMessage to String
	public static String soapMessageToString(SOAPMessage message) {
		String result = null;

		if (message != null) {
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				message.writeTo(baos);
				result = baos.toString();
			} catch (Exception e) {
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (IOException ioe) {
					}
				}
			}
		}
		return result;
	}

}
