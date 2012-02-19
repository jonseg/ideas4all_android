/** 
 * Ideas4All Android application
 * 
 * @author Jon Segador <jonseg@gmail.com> || http://jonsegador.com
 * Released under MIT license, https://raw.github.com/jonseg/ideas4all_android/master/LICENSE.txt
 * 
 */

package com.jonsegador.ideas4all;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class IdeaDetailHandler extends DefaultHandler{
	
    public IdeaDetailHandler() {
		super();
		this.DataSet = new ParsedIdeaDetailDataSet();
	}	
	
    @SuppressWarnings("unused")
    private boolean in_idea = false;
    private boolean in_id = false;
	private boolean in_title = false;
	private boolean in_body = false;
	private boolean in_user_login = false;
	private boolean in_user_avatar = false;
	
	StringBuilder builder;

	private ParsedIdeaDetailDataSet DataSet;
    
    public ParsedIdeaDetailDataSet getParsedIdeaDataSets() {
        return this.DataSet;
    }      
	
    public ParsedIdeaDetailDataSet getParsedData() {
         return this.DataSet;
    }

    @Override
    public void startDocument() throws SAXException {
         this.DataSet = new ParsedIdeaDetailDataSet();
    }

    @Override
    public void endDocument() throws SAXException {
         // No hacemos nada
    }     

    @Override
    public void startElement(String namespaceURI, String localName,
              String qName, Attributes atts) throws SAXException {
         if (localName.equals("idea")) {
             this.in_idea = true;
             DataSet = new ParsedIdeaDetailDataSet();
         }else if (localName.equals("id")) {
             this.in_id = true;
             builder = new StringBuilder();
         }else if (localName.equals("title")) {
             this.in_title = true;
             builder = new StringBuilder();
         }else if (localName.equals("body")) {
             this.in_body = true;
             builder = new StringBuilder();             
	     }else if (localName.equals("user_login")) {
	        this.in_user_login = true;
	        builder = new StringBuilder();
	     }else if (localName.equals("user_attachment_url")) {
	        this.in_user_avatar = true;
	        builder = new StringBuilder();
	     }
    } 
    
    @Override
    public void endElement(String namespaceURI, String localName, String qName)
              throws SAXException {
        if (localName.equals("idea")) {
            this.in_idea = false;
        }else if (localName.equals("id")) {
            this.in_id = false;
        }else if (localName.equals("title")) {
            this.in_title = false;
        }else if (localName.equals("body")) {
            this.in_body = false;            
        }else if (localName.equals("user_login")) {
            this.in_user_login = false;
        }else if (localName.equals("user_attachment_url")) {
            this.in_user_avatar = false;
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
        else if(this.in_body){
      	 	 
      	 	 String content = builder.toString(); 
        	
             // Remove style tags & inclusive content
             Pattern style = Pattern.compile("<style.*?>.*?</style>");
             Matcher mstyle = style.matcher(content);
             while (mstyle.find()) content = mstyle.replaceAll("");

             // Remove script tags & inclusive content
             Pattern script = Pattern.compile("<script.*?>.*?</script>");
             Matcher mscript = script.matcher(content);
             while (mscript.find()) content = mscript.replaceAll("");

             // Remove primary HTML tags
             Pattern tag = Pattern.compile("<.*?>");
             Matcher mtag = tag.matcher(content);
             while (mtag.find()) content = mtag.replaceAll("");

             // Remove comment tags & inclusive content
             Pattern comment = Pattern.compile("<!--.*?-->");
             Matcher mcomment = comment.matcher(content);
             while (mcomment.find()) content = mcomment.replaceAll("");

             // Remove special characters, such as &nbsp;
             Pattern sChar = Pattern.compile("&.*?;");
             Matcher msChar = sChar.matcher(content);
             while (msChar.find()) content = msChar.replaceAll("");

             // Remove the tab characters. Replace with new line characters.
             Pattern nLineChar = Pattern.compile("\t+");
             Matcher mnLine = nLineChar.matcher(content);
             while (mnLine.find()) content = mnLine.replaceAll("\n");  
             
             // Remove style tags & inclusive content
             Pattern vars = Pattern.compile("var.*?;");
             Matcher mvars = vars.matcher(content);
             while (mvars.find()) content = mvars.replaceAll("");       
             
             // Remove style tags & inclusive content
             Pattern comments = Pattern.compile("/.*?/");
             Matcher mcomments = comments.matcher(content);
             while (mcomments.find()) content = mcomments.replaceAll("");     
             
             // Remove style tags & inclusive content
             Pattern spaces = Pattern.compile("  ");
             Matcher mspaces = spaces.matcher(content);
             while (mspaces.find()) content = mspaces.replaceAll("");               
      	 	 
             DataSet.setBody(content); 
      	 	 
        }
        else if(this.in_user_login){
        	 DataSet.setUserLogin(builder.toString()); 
        }   

        else if(this.in_user_avatar){
       	 	 DataSet.setUserAvatar(builder.toString()); 
        }          
   } 
    
	
}
