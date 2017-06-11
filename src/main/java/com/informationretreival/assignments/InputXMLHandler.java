package com.informationretreival.assignments;


import java.util.ArrayList;
import java.util.Stack;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class InputXMLHandler extends DefaultHandler {

    private static final Logger LOG = LoggerFactory.getLogger(InputXMLHandler.class);
	
    //Stores the extracted elements from XML file
    private List<DocumentStructure> documentList = new ArrayList<>();
    
    private DocumentStructure doc =  null;
    
    //As we read any XML element we will push that in this stack
    private Stack<String> elementStack = new Stack<String>();
 
    //As we complete one user block in XML, we will push the Document instance in documentList
    private Stack<Object> objectStack = new Stack<Object>();
 
    
	public List<DocumentStructure> getDocumentList() {
		return documentList;
	}

	boolean bLineId, bPlayName, bSpeechNumber, bLineNumber, bSpeaker, bTextEntry = false;
	
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

		//System.out.println("Start Element :" + qName);
    	this.elementStack.push(qName);
    	
    	if (qName.equalsIgnoreCase("column")) {
        	
        	String attributeName =  attributes.getValue("name");
        	//System.out.println(attributes.get);
        	doc = new DocumentStructure();
        	
        	if (attributeName.equalsIgnoreCase("line_id")) {
        		bLineId = true; 
        	} else if (attributeName.equalsIgnoreCase("play_name") ) {
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
        	
        	this.objectStack.push(doc);
        }
        
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
    	this.elementStack.pop();
    	
    	if (qName.equalsIgnoreCase("column")) {
            //add Employee object to list
            documentList.add(doc);
               
        }
    }
	
    //Sets the DocumentStructure Object based on the boolean values indicating existing of values for elements
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
    	
        String value = new String(ch, start, length).trim();
        
        if (value.length() == 0 && value.equalsIgnoreCase("null"))
        {
            return; // ignore white space
        }
        
        
        	if (bLineId) {
        		DocumentStructure doc = (DocumentStructure) this.objectStack.peek();
            	doc.setLineId((Integer.parseInt(value)));
            	bLineId = false;
            } else if (bPlayName) {
            	DocumentStructure doc = (DocumentStructure) this.objectStack.peek();
                doc.setPlayName(value);
                bPlayName = false;
            } else if (bSpeechNumber) {
            	DocumentStructure doc = (DocumentStructure) this.objectStack.peek();
                doc.setSpeechNumber(value);
                bSpeechNumber = false;
            } else if (bLineNumber) {
            	DocumentStructure doc = (DocumentStructure) this.objectStack.peek();
                doc.setLineNumber(value);
                bLineNumber = false;
            } else if (bSpeaker) {
            	DocumentStructure doc = (DocumentStructure) this.objectStack.peek();
                doc.setSpeaker(value);
                bSpeaker = false;
            } else if (bTextEntry) {
            	DocumentStructure doc = (DocumentStructure) this.objectStack.peek();
                doc.setTextEntry(value);
                bTextEntry = false;
            }
        }
    /**
     * Utility method for getting the current element in processing
     * */
    private String currentElement()
    {
        return this.elementStack.peek();
    }
    
    
}
