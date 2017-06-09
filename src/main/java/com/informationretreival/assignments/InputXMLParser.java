package com.informationretreival.assignments;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;


public class InputXMLParser {

    private static final Logger LOG = LoggerFactory.getLogger(InputXMLParser.class);
	
	public static void main(String[] args){
		SAXParserFactory saxParserfactory = SAXParserFactory.newInstance();
		File file = new File("will_play_text.xml");
		boolean fileExists = file.exists();
		
		if (! fileExists){
		    LOG.error("Input XML File Not found : " + file.getName());
		}
		
		try {
			SAXParser saxParser = saxParserfactory.newSAXParser();
			InputXMLHandler handler = new InputXMLHandler();
			saxParser.parse(file, handler);
	        //Get DocumentStructure list
	        List<DocumentStructure> doclist = handler.getDocumentList();
	        //print Document information
	        for(DocumentStructure doc : doclist)
	            System.out.println(doc);
	        
		} catch (ParserConfigurationException e) {
		    LOG.error("Parser configuration Exception caught : " + e.getMessage());
		    e.printStackTrace();
		} catch (SAXException e) {
		    LOG.error("SAX Exception caught : " + e.getMessage());
		    e.printStackTrace();
		} catch (IOException e) {
		    LOG.error("IO Exception caught : " + e.getMessage());
		    e.printStackTrace();
		}
		
		
	}
}
