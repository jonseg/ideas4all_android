/** 
 * Ideas4All Android application
 * 
 * @author Jon Segador <jonseg@gmail.com> || http://jonsegador.com
 * Released under MIT license, https://raw.github.com/jonseg/ideas4all_android/master/LICENSE.txt
 * 
 */

package com.jonsegador.ideas4all;


public class ParsedIdeaDetailDataSet {

	private String id = null;
    private String title = null;
    private String body = null;
    private String user_login = null;
    private String user_avatar = null;

    public String getId() {
      return id;
    }
    public void setId(String extractedString) {
     this.id = extractedString;
    }    
    
    public String getTitle() {
     return title;
    }
    public void setTitle(String extractedString) {
     this.title = extractedString;
    }
    
    public String getBody() {
        return body;
    }
    public void setBody(String extractedString) {
    	this.body = extractedString;
    }    
    
    public String getUserLogin() {
     return user_login;
    }
    public void setUserLogin(String extractedString) {
     this.user_login = extractedString;
    }
       
    public String getUserAvatar() {
     return user_avatar;
    }
    public void setUserAvatar(String extractedString) {
     this.user_avatar = extractedString;
    }
    	    
    public String toString(){
         return "title = " + this.title;
                   
    }		
	
}