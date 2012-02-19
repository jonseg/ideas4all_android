/** 
 * Ideas4All Android application
 * 
 * @author Jon Segador <jonseg@gmail.com> || http://jonsegador.com
 * Released under MIT license, https://raw.github.com/jonseg/ideas4all_android/master/LICENSE.txt
 * 
 */

package com.jonsegador.ideas4all;

import java.util.Vector;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CategoryHandler extends DefaultHandler{
	
    public CategoryHandler() {
		super();
		this.myParsedCategoryDataSet = new Vector<ParsedCategoryDataSet>();
	}	
	
    @SuppressWarnings("unused")
    private boolean in_category = false;
    private boolean in_id = false;
	private boolean in_name = false;
	
	StringBuilder builder;

	private ParsedCategoryDataSet DataSet;
    private Vector<ParsedCategoryDataSet> myParsedCategoryDataSet; 
    
    public Vector<ParsedCategoryDataSet> getParsedCategoryDataSets() {
        return this.myParsedCategoryDataSet;
    }      
	
    public Vector<ParsedCategoryDataSet> getParsedData() {
         return this.myParsedCategoryDataSet;
    }

    @Override
    public void startDocument() throws SAXException {
         this.myParsedCategoryDataSet = new Vector<ParsedCategoryDataSet>();
    }

    @Override
    public void endDocument() throws SAXException {
         // No hacemos nada
    }     

    @Override
    public void startElement(String namespaceURI, String localName,
              String qName, Attributes atts) throws SAXException {
         if (localName.equals("category")) {
             this.in_category = true;
             DataSet = new ParsedCategoryDataSet();
         }else if (localName.equals("id")) {
             this.in_id = true;
             builder = new StringBuilder();
         }else if (localName.equals("name")) {
             this.in_name = true;
             builder = new StringBuilder();
         }
    } 
    
    @Override
    public void endElement(String namespaceURI, String localName, String qName)
              throws SAXException {
        if (localName.equals("category")) {
            this.in_category = false;
            myParsedCategoryDataSet.add(DataSet);
        }else if (localName.equals("id")) {
            this.in_id = false;
        }else if (localName.equals("name")) {
            this.in_name = false;
        }
    } 
    
    @Override
    public void characters(char ch[], int start, int length) {
    	
         if(this.in_id){
        	 
        	 if (builder!=null) {
        	        for (int i=start; i<start+length; i++) {
        	        	builder.append(ch[i]);
        	        }
        	 }        	 
        	 
        	 DataSet.setId(builder.toString()); 
         }
         if(this.in_name){
        	 
     	     if (builder!=null) {
    	        for (int i=start; i<start+length; i++) {
    	        	builder.append(ch[i]);
    	        }
     	     }            	 
        	 
        	 DataSet.setName(builder.toString()); 
         }   
   } 
    
	
}
