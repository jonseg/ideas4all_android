/** 
 * Ideas4All Android application
 * 
 * @author Jon Segador <jonseg@gmail.com> || http://jonsegador.com
 * Released under MIT license, https://raw.github.com/jonseg/ideas4all_android/master/LICENSE.txt
 * 
 */

package com.jonsegador.ideas4all;

public class Idea {

	private String id = null;
    private String title = null;
    private String user_login = null;
    private String votes_count = null;
    private String comments_count = null;
    
	public Idea(String ident, String nom, String user_login2, String votes_count2, String comments_count2){
		id = ident;
		title = nom;
		user_login = user_login2;
		votes_count = votes_count2;
		comments_count = comments_count2;
	}    

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
    
    public String getUserLogin() {
     return user_login;
    }
    public void setUserLogin(String extractedString) {
     this.user_login = extractedString;
    }
       
    public String getVotesCount() {
     return votes_count;
    }
    public void setVotesCount(String extractedString) {
     this.votes_count = extractedString;
    }
    	    
    public String getCommentsCount() {
    	return comments_count;
    }
    public void setCommentsCount(String extractedString) {
	   this.comments_count = extractedString;
   	}    	    
    
    public String toString(){
         return "title = " + this.title;
                   
    }	
	
}
