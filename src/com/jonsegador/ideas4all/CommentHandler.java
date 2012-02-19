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

public class CommentHandler extends DefaultHandler{
	
    public CommentHandler() {
		super();
		this.myParsedCommentDataSet = new Vector<ParsedCommentDataSet>();
	}	
	
    @SuppressWarnings("unused")
    private boolean in_comments = false;
    private boolean in_comment_item = false;
	private boolean in_comment = false;
    @SuppressWarnings("unused")
	private boolean in_user_id = false;
	private boolean in_date = false;
	
	StringBuilder builder;

	private ParsedCommentDataSet DataSet;
    private Vector<ParsedCommentDataSet> myParsedCommentDataSet; 
    
    public Vector<ParsedCommentDataSet> getParsedCommentDataSets() {
        return this.myParsedCommentDataSet;
    }      
	
    public Vector<ParsedCommentDataSet> getParsedData() {
         return this.myParsedCommentDataSet;
    }

    @Override
    public void startDocument() throws SAXException {
         this.myParsedCommentDataSet = new Vector<ParsedCommentDataSet>();
    }

    @Override
    public void endDocument() throws SAXException {
         // No hacemos nada
    }     

    @Override
    public void startElement(String namespaceURI, String localName,
              String qName, Attributes atts) throws SAXException {
    	
         if (localName.equals("comments")) {
             this.in_comments = true;
         }else if (localName.equals("comment") && !this.in_comment_item) {
             this.in_comment_item = true;
             DataSet = new ParsedCommentDataSet();
         }else if (localName.equals("comment") && this.in_comment_item) {
             this.in_comment = true;
             builder = new StringBuilder();
         }else if (localName.equals("created-at")) {
             this.in_date = true;
             builder = new StringBuilder();  
         }else if (localName.equals("user-id")) {
             this.in_user_id = true;
         }
    } 
    
    @Override
    public void endElement(String namespaceURI, String localName, String qName)
              throws SAXException {
   	
        if (localName.equals("comments")) {
            this.in_comments = false;
        }else if (localName.equals("comment") && !this.in_comment) {
            this.in_comment_item = false;
            myParsedCommentDataSet.add(DataSet);
        }else if (localName.equals("comment") && this.in_comment_item) {
            this.in_comment = false;
        }else if (localName.equals("created-at")) {
            this.in_date = false;            
        }else if (localName.equals("user-id")) {
            this.in_user_id = false;
        }
    } 
    
    @Override
    public void characters(char ch[], int start, int length) {
    	
         if(this.in_comment){
        	 
     	    if (builder!=null) {
    	        for (int i=start; i<start+length; i++) {
    	        	builder.append(ch[i]);
    	        }
    	    }            	 

        	 DataSet.setComment(builder.toString()); 
         }   
         if(this.in_date){
        	 
     	    if (builder!=null) {
    	        for (int i=start; i<start+length; i++) {
    	        	builder.append(ch[i]);
    	        }
    	    }           

        	 DataSet.setDate(builder.toString()); 
         }           
   } 
    
	
}