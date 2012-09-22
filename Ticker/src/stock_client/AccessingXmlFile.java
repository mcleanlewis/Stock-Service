package stock_client;
/*XML Manager Class
 *Parses data from XML.
 *Data is stored in a Hashtable data structure
 * Author: Alessio Failla - af128@hw.ac.uk
 */
import java.io.File;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class AccessingXmlFile {
	Hashtable<String, String> ht = new Hashtable<String, String>();

public AccessingXmlFile(){
	super();
}
 public void parseQueries(String filename) {

  try {
  File file = new File(filename);
  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
  DocumentBuilder db = dbf.newDocumentBuilder();
  Document document = db.parse(file);
  document.getDocumentElement().normalize();
  NodeList node = document.getElementsByTagName("query");
 

  for (int i = 0; i < node.getLength(); i++) {
		  Node firstNode = node.item(i);
		  Element element = (Element) firstNode;
		  NodeList idElemntList = element.getElementsByTagName("id");
		  Element idElement = (Element) idElemntList.item(0);
		  NodeList id = idElement.getChildNodes();
			
		  NodeList statamentElementList = element.getElementsByTagName("statement");
		  Element statementElement = (Element) statamentElementList.item(0);
		  NodeList statement = statementElement.getChildNodes();
	
		  ht.put(id.item(0).getNodeValue().toString(), statement.item(0).getNodeValue().toString());
  }

  } catch (Exception e) {
    e.printStackTrace();
  }
 }
 
 
}
