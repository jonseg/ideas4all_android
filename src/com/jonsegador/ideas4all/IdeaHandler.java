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

public class IdeaHandler extends DefaultHandler{
	
    public IdeaHandler() {
		super();
		this.myParsedIdeaDataSet = new Vector<ParsedIdeaDataSet>();
	}	
	
    @SuppressWarnings("unused")
    private boolean in_ideas = false;
    private boolean in_id = false;
	private boolean in_title = false;
	private boolean in_user_login = false;
	private boolean in_votes_count = false;
	private boolean in_comments_count = false;
	
	StringBuilder builder;

	private ParsedIdeaDataSet DataSet;
    private Vector<ParsedIdeaDataSet> myParsedIdeaDataSet; 
    
    public Vector<ParsedIdeaDataSet> getParsedIdeaDataSets() {
        return this.myParsedIdeaDataSet;
    }      
	
    public Vector<ParsedIdeaDataSet> getParsedData() {
         return this.myParsedIdeaDataSet;
    }

    @Override
    public void startDocument() throws SAXException {
         this.myParsedIdeaDataSet = new Vector<ParsedIdeaDataSet>();
    }

    @Override
    public void endDocument() throws SAXException {
         // No hacemos nada
    }     

    @Override
    public void startElement(String namespaceURI, String localName,
              String qName, Attributes atts) throws SAXException {
         if (localName.equals("ideas")) {
             this.in_ideas = true;
             DataSet = new ParsedIdeaDataSet();
         }else if (localName.equals("id")) {
             this.in_id = true;
             builder = new StringBuilder();
         }else if (localName.equals("title")) {
             this.in_title = true;
             builder = new StringBuilder();
	     }else if (localName.equals("user_login")) {
	        this.in_user_login = true;
	        builder = new StringBuilder();
         }else if (localName.equals("votes_count")) {
             this.in_votes_count = true;
             builder = new StringBuilder();
	     }else if (localName.equals("comments_count")) {
	        this.in_comments_count = true;
	        builder = new StringBuilder();
	     }
    } 
    
    @Override
    public void endElement(String namespaceURI, String localName, String qName)
              throws SAXException {
        if (localName.equals("ideas")) {
            this.in_ideas = false;
            myParsedIdeaDataSet.add(DataSet);
        }else if (localName.equals("id")) {
            this.in_id = false;
        }else if (localName.equals("title")) {
            this.in_title = false;
        }else if (localName.equals("user_login")) {
            this.in_user_login = false;
        }else if (localName.equals("votes_count")) {
            this.in_votes_count = false;
        }else if (localName.equals("comments_count")) {
            this.in_comments_count = false;
        }
    } 
    
    @Override
    public void characters(char ch[], int start, int length) {

	    if (builder!=null) {
	        for (int i=start; i<start+length; i++) {
	        	builder.append(ch[i]);
	        }
	    }      	
    	
        if(this.in_id){
        	 DataSet.setId(builder.toString()); 
        }
        else if(this.in_title){
        	 DataSet.setTitle(builder.toString()); 
        }
        else if(this.in_user_login){
        	 DataSet.setUserLogin(builder.toString()); 
        }   
        else if(this.in_votes_count){
       	 	 DataSet.setVotesCount(builder.toString()); 
        }
        else if(this.in_comments_count){
       	 	 DataSet.setCommentsCount(builder.toString()); 
        }          
   } 
    
	
}
