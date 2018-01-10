package analysisTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class SessionXMLDecoder {
	private String vendorParserClass = "org.apache.xerces.parsers.SAXParser";
	private String xmlURI;
	private HashMap<String, String> values;
	private boolean busy;
	private ArrayList<String> interactions;
	
	public SessionXMLDecoder(String filename){
		values = new HashMap<String, String>();
		this.xmlURI = filename;
		this.interactions = new ArrayList<String>();
	}
	
	public void decode()throws SAXException, IOException{
		busy = false;
		XMLReader reader = XMLReaderFactory.createXMLReader(vendorParserClass);
		MyContentHandler myContentHandler = new MyContentHandler();
		reader.setContentHandler(myContentHandler);
		InputSource inputSource = new InputSource(xmlURI);
		busy = true;
		reader.parse(inputSource);
	}
	
	public String getValue(String key){
		if(!busy)return (String)values.get(key);
		else return null;
	}
	
	public ArrayList<String> getInteractionData(){
		if(!busy)return this.interactions;
		else return null;
	}
	
	class MyContentHandler implements ContentHandler{
		private String value;
		private String current;
		public  MyContentHandler(){
			value = "";
			current = "";
		}
		@Override
		public void characters(char[] ch, int start, int length)throws SAXException {
			value = new String(ch, start, length);
			if(current.equals("INTERACTIONS")){
				interactions.add(value);
			}
		}

		@Override
		public void endDocument() throws SAXException {
//			for(Iterator i = values.keySet().iterator();i.hasNext();){
//				String uri =(String)i.next();
//				String thisPrefix = (String)values.get(uri);
////				System.out.println(uri+ " = "+thisPrefix);
//			}
			busy = false;
		}

		@Override
		public void endElement(String uri, String localName, String qName)throws SAXException {
			if(!localName.equals("SESSION")&&!localName.equals("INTERACTIONS"))	values.put(localName, value);
		}

		@Override
		public void endPrefixMapping(String prefix) throws SAXException {}

		@Override
		public void ignorableWhitespace(char[] ch, int start, int length)throws SAXException {}

		@Override
		public void processingInstruction(String target, String data)throws SAXException {}

		@Override
		public void setDocumentLocator(Locator locator) {	}

		@Override
		public void skippedEntity(String name) throws SAXException {	}

		@Override
		public void startDocument() throws SAXException {	}

		@Override
		public void startElement(String uri, String localName, String qName,Attributes atts) throws SAXException {
			current = localName;
		}

		@Override
		public void startPrefixMapping(String prefix, String uri)throws SAXException {	}
	}
}
