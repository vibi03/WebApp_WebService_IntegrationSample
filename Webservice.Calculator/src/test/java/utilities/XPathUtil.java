package utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XPathUtil {

	private static XPath xpath = XPathFactory.newInstance().newXPath();;

	public static NamespaceContext getNSContext() {

		Map<String, String> mappings = new HashMap<String, String>();
		mappings.put("ns2",
				"http://www.origostandards.com/schema/tech/v1.0/SOAPFaultDetail");
		mappings.put("ns3",
				"http://www.aviva.com/schema/UKLOrigoBondContractEnquiry/MessageTransformation");
		mappings.put(
				"de",
				"http://www.origostandards.com/schema/ce/v2.1/CEBondSingleContractDetailResponse");
		mappings.put("os", "http://www.origostandards.com/schema/soap/v1");
		mappings.put("xsi", "http://www.w3.org/2001/XMLSchema-instance");
		mappings.put(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
		NamespaceContext context = new NamespaceContextMap(mappings);

		return context;
	}

	/**
	 * This method evaluates the input XPath expression against the input xml
	 * String
	 */
	public static String evaluateXPath(String pathToEvaluate, String xmlAsString)
			throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {

		DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Node xmlNode = documentBuilder.parse(
				new ByteArrayInputStream(xmlAsString.getBytes()))
				.getDocumentElement();

		return evaluateXPath(pathToEvaluate, xmlNode);
	}

	/**
	 * This method evaluates the input XPath expression against the input xml
	 * Node structure
	 */
	public static String evaluateXPath(String pathToEvaluate, Node xmlNode)
			throws XPathExpressionException {

		if (xpath.getNamespaceContext() == null) {
			xpath.setNamespaceContext(getNSContext());
		}

		return xpath.evaluate(pathToEvaluate, xmlNode);
	}

	public static Object evaluateXPath(String path, Object elm, QName qName)
			throws Exception {

		if (xpath.getNamespaceContext() == null) {
			xpath.setNamespaceContext(getNSContext());
		}
		XPathExpression expr = xpath.compile(path);

		return expr.evaluate(elm, qName);
	}

	public static String getAttributeValue(String attributeName,
			Element currentNode) {
		
		return currentNode.getAttributes().getNamedItem(attributeName)
				.getNodeValue();
	}
}