package utilities;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import utilities.XPathUtil;

public class XMLUtil {

	public static int IDENTICAL = 1;
	public static int SIMILAR = 2;
	private static Document underlyingServicesDoc = null;
	private static Element underlyingServicesRootXMLNode = null;
	private static DocumentBuilderFactory nonNsDbFactory = null;
	private static DocumentBuilder nonNsDBuilder = null;
	Element requestDataXML = null;

	public static Element getElementFromString(String response)
			throws ParserConfigurationException, SAXException, IOException {

//		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
//				.newInstance();
//		documentBuilderFactory.setNamespaceAware(true);
//		DocumentBuilder documentBuilder = documentBuilderFactory
//				.newDocumentBuilder();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	    factory.setNamespaceAware(true);
	    
	    DocumentBuilder builder = factory.newDocumentBuilder();
		
	    InputSource is = new InputSource();
	    is.setCharacterStream(new StringReader(response));
	    
		Document document = builder.parse(is);

		return document.getDocumentElement();
	}

	public static Document getDocumentFromString(String response)
			throws ParserConfigurationException, SAXException, IOException {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentBuilderFactory
				.newDocumentBuilder();
		@SuppressWarnings("deprecation")
		Document document = documentBuilder
				.parse(new java.io.StringBufferInputStream(response));

		return document;
	}

	public static Document addTagContentToDocument(Document document,
			String xpathToTag, String tagValue) throws Throwable {
		// Create a list of nodes in the xpath
		String xpathNodesList[] = xpathToTag.split("/");
		Node parentNode;
		// loop through child nodes from xpath -
		// 0 should be the request object so ignore it
		for (int xpathNodesListCount = 1; xpathNodesListCount < xpathNodesList.length; xpathNodesListCount++) {
			// set parent node from document
			parentNode = document.getElementsByTagName(
					xpathNodesList[(xpathNodesListCount - 1)]).item(0);
			// exception if parent node not found -
			// unless it is the request object it was created in previous loop
			if (parentNode == null) {
				throw new Exception(
						"!!! Error - xPath is not valid in request xml: "
								+ xpathToTag);
			}
			// set children of parent
			NodeList childNodesList = parentNode.getChildNodes();
			Node matchedNode = null;
			// loop through child nodes to match node from xpath
			for (int childNodesListCount = 0; childNodesListCount < childNodesList
					.getLength(); childNodesListCount++) {
				// if node from xpath is found store it and exit the loop
				if (childNodesList.item(childNodesListCount).getNodeName()
						.equals(xpathNodesList[xpathNodesListCount])) {
					matchedNode = childNodesList.item(childNodesListCount);
					break;
				}
			}

			// if node from xpath is not found create it
			if (matchedNode == null) {
				// // unless the tag is the last in the list and the tag value
				// is 'missing'
				// if ((xpathNodesListCount == xpathNodesList.length - 1)
				// && (tagValue.equalsIgnoreCase("missing"))) {
				if ((tagValue.equalsIgnoreCase("missing"))
						&& (xpathNodesListCount == xpathNodesList.length - 1)) {
				} else {
					matchedNode = document
							.createElement(xpathNodesList[xpathNodesListCount]);
					parentNode.appendChild(matchedNode);
					// }
				}
			}
			// tag value of 'no value' means create an empty tag
			if (tagValue.equalsIgnoreCase("no value")) {
				tagValue = "";
			} // if node is the last in the list (and tag value is not 'missing)
				// add the value
			if ((xpathNodesListCount == (xpathNodesList.length - 1))
					&& (!tagValue.equalsIgnoreCase("missing"))) {
				matchedNode.appendChild(document.createTextNode(tagValue));
			}

		}

		return document;
	}

	public static String getTextValue(Element element, String tagName) {

		String textVal = null;
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList != null && nodeList.getLength() > 0) {
			Element e1 = (Element) nodeList.item(0);
			if (e1.hasChildNodes())
				textVal = e1.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	public static String convertDocumentToString(Document document)
			throws Throwable {

		StringWriter stringWriter = new StringWriter();
		try {
			DOMSource domSource = new DOMSource(document);

			StreamResult streamResult = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(domSource, streamResult);
			stringWriter.flush();
		} catch (TransformerException transformerException) {
			transformerException.printStackTrace();

			return null;
		}

		return stringWriter.toString();
	}

	public static String getStringFromDocument(String documentPath, String xpath)
			throws Throwable {

		Element expectedValueElement = null;
		String responseData = null;
		String expectedValue = null;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		Document document = documentBuilderFactory.newDocumentBuilder().parse(
				new File(documentPath));
		responseData = XMLUtil.convertDocumentToString(document);
		expectedValueElement = XMLUtil.getElementFromString(responseData);
		expectedValue = (String) XPathUtil.evaluateXPath(xpath,
				expectedValueElement, XPathConstants.STRING);

		return expectedValue;
	}

	public static String getStringFromDocument(String documentPath)
			throws Throwable {

		String responseData = null;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		Document document = documentBuilderFactory.newDocumentBuilder().parse(
				new File(documentPath));
		responseData = XMLUtil.convertDocumentToString(document);

		return responseData;
	}

	public static boolean requestResponseComparator(
			String responsondingServicename, String responseServiceFilePath,
			String responseServiceTagPath, String requestingServicename,
			String requestServiceFilePath, String requestServiceTagPath)
			throws Throwable {

		Element expectedValueResponseElement = null;
		Element expectedValueRequestElement = null;
		String responseValue = null;
		String requestValue = null;
		Boolean statusFlag = false;
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		// getting Response Value
		Document document = documentBuilderFactory.newDocumentBuilder().parse(
				new File(responseServiceFilePath));
		String response = XMLUtil.convertDocumentToString(document);
		expectedValueResponseElement = XMLUtil.getElementFromString(response);
		responseValue = (String) XPathUtil.evaluateXPath(
				responseServiceTagPath, expectedValueResponseElement,
				XPathConstants.STRING);
		if (responsondingServicename.equals("getValuesUserAdmin"))
			responseValue = responseValue.substring(4, 14);
		// getting Response Value
		document = documentBuilderFactory.newDocumentBuilder().parse(
				new File(requestServiceFilePath));
		String request = XMLUtil.convertDocumentToString(document);
		expectedValueRequestElement = XMLUtil.getElementFromString(request);
		requestValue = (String) XPathUtil.evaluateXPath(requestServiceTagPath,
				expectedValueRequestElement, XPathConstants.STRING);
		// Assert
		assertEquals(requestServiceFilePath + " :", responseValue, requestValue);
		if (responseValue.equals(requestValue))
			statusFlag = true;

		return statusFlag;
	}

	public static Element addToUnderlyingServicesRoot(String elementToBeAdded) {

		underlyingServicesRootXMLNode = null;
		underlyingServicesDoc = null;
		nonNsDbFactory = null;
		nonNsDBuilder = null;
		if (null == underlyingServicesRootXMLNode) {
			if (null == underlyingServicesDoc) {
				try {
					if (null == nonNsDbFactory) {
						nonNsDbFactory = DocumentBuilderFactory.newInstance();
						nonNsDbFactory.setNamespaceAware(false);
					}

					if (null == nonNsDBuilder) {
						nonNsDBuilder = nonNsDbFactory.newDocumentBuilder();
					}
					underlyingServicesDoc = nonNsDBuilder.newDocument();
				} catch (ParserConfigurationException exception) {
					// TODO Auto-generated catch block
					exception.printStackTrace();
				}
			}
			underlyingServicesRootXMLNode = underlyingServicesDoc
					.createElement("root");
			underlyingServicesDoc.appendChild(underlyingServicesRootXMLNode);
		}

		try {
			DocumentBuilderFactory underlyingServiceNS = DocumentBuilderFactory
					.newInstance();
			underlyingServiceNS.setNamespaceAware(true);
			DocumentBuilder underlyingServiceDB = underlyingServiceNS
					.newDocumentBuilder();
			Element docElement = underlyingServiceDB.parse(
					new ByteArrayInputStream(elementToBeAdded.getBytes()))
					.getDocumentElement();
			underlyingServicesRootXMLNode
					.appendChild((Node) underlyingServicesDoc.importNode(
							docElement, true));
		} catch (DOMException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} catch (SAXException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} catch (IOException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} catch (ParserConfigurationException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}

		return underlyingServicesDoc.getDocumentElement();
	}

	public static String getXMLAsString(Object xmlNode) {

		StringWriter stringWriter = new StringWriter();
		StreamResult streamResult = new StreamResult(stringWriter);
		DOMSource domSource = new DOMSource((Node) xmlNode);
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(domSource, streamResult);
		} catch (TransformerConfigurationException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		} catch (TransformerException exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
		String xmlString = stringWriter.toString();

		return xmlString;
	}

	public static String getStringFromRequestString(String Document,
			String xpath) throws Throwable {

		Element expectedValueElement = null;
		String expectedValue = null;
		expectedValueElement = XMLUtil.getElementFromString(Document);
		expectedValue = (String) XPathUtil.evaluateXPath(xpath,
				expectedValueElement, XPathConstants.STRING);

		return expectedValue;
	}

	public static Element getUnderlyingServicesRootXMLNode() {

		return underlyingServicesDoc.getDocumentElement();
	}

	public static String getNodeValueForTag(String documentPath, int iterator,
			String tagValue) throws Throwable {

		String nodeValue = "";
		String xmlString = XMLUtil.getStringFromDocument(documentPath);
		Element elementFromXML = XMLUtil.getElementFromString(xmlString);
		NodeList nodeListFromXML = elementFromXML
				.getElementsByTagName(tagValue);
		for (int i = 0; i < nodeListFromXML.getLength(); i++) {
			if (i == iterator) {
				Element elementFromNodeList = (Element) nodeListFromXML.item(i);
				nodeValue = elementFromNodeList.getChildNodes().item(0)
						.getNodeValue();
			}
		}

		return nodeValue;
	}

	public static String getStringInBetweenTwoStrings(Document document,
			String firstString, String secondString) throws Throwable {

		String middleString = null;
		String string = XMLUtil.convertDocumentToString(document);
		Pattern pattern = Pattern.compile(firstString + "(.*?)" + secondString);
		Matcher matcher = pattern.matcher(string);
		if (matcher.find()) {
			middleString = matcher.group(1);
		} else
			middleString = "";

		return middleString;
	}
}
