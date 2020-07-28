package utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.junit.Assert;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlValidationUtil {

    // To validate the xml structure using the nodes present in the xml data
    public static void validateXmlStructure(String xmldata, String fileName) throws IOException {
    	 
        List<String> XmlDataNodeList = null;
        String nodeListFile = null;
        String baseFolder = "src/test/resources/Nodelist";
        Element responseXml = null;
        List<String> textNodeList = null;
        String nodename;
        Boolean elementPresent;

        System.out.println(xmldata);
        nodeListFile = baseFolder + "/"+fileName+".txt";
//        SOAPMessage response = null;
        try {
            textNodeList = TextReader.getStringList(nodeListFile);

//            response = getSoapMessageFromString(xmldata);

            XmlDataNodeList = getSoapXmlNodeList(xmldata);

            for (int i = 0; i < XmlDataNodeList.size(); i++) {
                elementPresent = false;

                for (String node : textNodeList) {
                    if (elementPresent = node.equalsIgnoreCase(XmlDataNodeList.get(i))) {
                        break;
                    }

                }
                Assert.assertTrue(elementPresent);

            }

            System.out.println("***********************************************************");
        } catch (IOException | SOAPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // Return the soap xml nodes as list object

    private static List<String> getSoapXmlNodeList(String xmldata) throws SOAPException {
        SOAPMessage response = null;

        List<String> nodelist = new ArrayList<String>();

        try {
            response = getSoapMessageFromString(xmldata);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        SOAPBody soapBody = response.getSOAPBody();
        Vector<Element> nodes = new Vector<Element>();
        String name;

        // put the reply into a Vector of Elements
        for (Iterator<?> it = soapBody.getChildElements(); it.hasNext();) {
            Node next = (Node) it.next();
            if (next instanceof SOAPBodyElement)
                nodes.add((SOAPBodyElement) next);

        }

        System.out.println(nodes.size());

        for (Element ele : nodes) {
            if (ele.hasChildNodes()) {
                NodeList list = ele.getElementsByTagName("*");
                int size = list.getLength();
                for (int i = 0; i < size; i++) {
                    nodelist.add(list.item(i).getNodeName());
                    System.out.println(list.item(i).getNodeName());

                }

            }

            nodelist.add(ele.getNodeName());
            System.out.println(ele.getNodeName());

        }

        return nodelist;

    }

    // Convert the soap xml to Soap Message

    private static SOAPMessage getSoapMessageFromString(String xml) throws SOAPException, IOException {
        MessageFactory factory = MessageFactory.newInstance();
        SOAPMessage message = factory.createMessage(new MimeHeaders(),
                new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8"))));
        return message;
    }

    
    
    public static void main(String[] args) {
        String xmldata;
        try {
            xmldata = TextReader.ReadFile("src/test/resources/");
            XmlValidationUtil.getSoapXmlNodeList(xmldata);
        } catch (SOAPException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

}
