package com.informationretreival.assignments;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class InputXMLHandler extends DefaultHandler {

    private static final Logger LOG = LoggerFactory.getLogger(InputXMLHandler.class);
	
    private List<DocumentStructure> documentList = null;
    private DocumentStructure doc =  null;
    
	public List<DocumentStructure> getDocumentList() {
		return documentList;
	}

	boolean bLineId, bPlayName, bSpeechNumber, bLineNumber, bSpeaker, bTextEntry = false;
	
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

		//System.out.println("Start Element :" + qName);
    	
        if (qName.equalsIgnoreCase("table")) {

            //create a new Employee and put it in Map
            String name = attributes.getValue("name");
            System.out.println(name);
         	doc = new DocumentStructure();
        	doc.setName(name);
        	documentList = new ArrayList<>();
        
        } else if (qName.equalsIgnoreCase("column")) {
        	
        	String attributeName =  attributes.getValue("name");
        	
        	if (attributeName.equalsIgnoreCase("line_id")) {
        		bLineId = true;
        	
        	} else if (attributeName.equalsIgnoreCase("play_name")) {
        		bPlayName = true;
        	} else if (attributeName.equalsIgnoreCase("speech_number")) {
        		bSpeechNumber = true;
        	} else if (attributeName.equalsIgnoreCase("line_number")) {
        		bLineNumber = true;
        	} else if (attributeName.equalsIgnoreCase("speaker")) {
        		bSpeaker = true;
        	} else if (attributeName.equalsIgnoreCase("text_entry")) {
        		bTextEntry = true;
        	}
        }
        
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("table")) {
            //add Employee object to list
            documentList.add(doc);
               
        }
    }
	
    //Sets the DocumentStructure Object based on the boolean values indicating existing of values for elements
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
    	
        if (bLineId) {
        	doc.setLineId((Integer.parseInt(new String(ch, start, length))));
        	bLineId = false;
        } else if (bPlayName) {
            doc.setPlayName(new String(ch, start, length));
            bPlayName = false;
        } else if (bSpeechNumber) {
            doc.setSpeechNumber(new String(ch, start, length));
            bSpeechNumber = false;
        } else if (bLineNumber) {
            doc.setLineNumber(new String(ch, start, length));
            bLineNumber = false;
        } else if (bSpeaker) {
            doc.setSpeaker(new String(ch, start, length));
            bSpeaker = false;
        } else if (bTextEntry) {
            doc.setTextEntry(new String(ch, start, length));
            bTextEntry = false;
        }
        
        

    }
    
    
}
