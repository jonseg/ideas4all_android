/** 
 * Ideas4All Android application
 * 
 * @author Jon Segador <jonseg@gmail.com> || http://jonsegador.com
 * Released under MIT license, https://raw.github.com/jonseg/ideas4all_android/master/LICENSE.txt
 * 
 */

package com.jonsegador.ideas4all;

public class ParsedCategoryDataSet {

	private String id = null;
    private String name = null;

    public String getId() {
      return id;
    }
    public void setId(String extractedString) {
     this.id = extractedString;
    }    
    
    public String getName() {
     return name;
    }
    public void setName(String extractedString) {
     this.name = extractedString;
    }
    
    public String toString(){
         return "name = " + this.name;
                   
    }	
	
}
