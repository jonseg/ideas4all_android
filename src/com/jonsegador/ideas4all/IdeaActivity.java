/** 
 * Ideas4All Android application
 * 
 * @author Jon Segador <jonseg@gmail.com> || http://jonsegador.com
 * Released under MIT license, https://raw.github.com/jonseg/ideas4all_android/master/LICENSE.txt
 * 
 */

package com.jonsegador.ideas4all;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class IdeaActivity extends Activity {
	
    private Thread t;
    private ProgressDialog dialog;	
	
    private ParsedIdeaDetailDataSet idea;
	
    private String idea_url = "http://es.ideas4all.com/api/ideas/idea_id.xml";
    
    private String idea_id;
    
    private static final int GOTO_COMMENTS = Menu.FIRST;
    
    private ImageView lblAvatar;
    private Bitmap loadedImage;
    private String imageHttpAddress;    
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idea);
        
        idea_id = getIntent().getStringExtra("idea_id");
        idea_url = idea_url.replaceAll("idea_id", idea_id);
        
        showDialog(0);
        t=new Thread() {
              public void run() {
            	  loadIdea();
              }
        };        
        t.start();         
        
    }
    
	public void loadIdea() {
		
        try {
        	
          URL url = new URL(idea_url);

          SAXParserFactory spf = SAXParserFactory.newInstance();
          SAXParser sp = spf.newSAXParser();

          XMLReader xr = sp.getXMLReader();
          IdeaDetailHandler myExampleHandler = new IdeaDetailHandler();
          xr.setContentHandler(myExampleHandler);
          
          InputSource is = new InputSource(url.openStream());
          is.setEncoding("UTF-8");
          xr.parse(is);
          
          idea = myExampleHandler.getParsedData();
          
          Message myMessage=new Message();
          myMessage.obj="SUCCESS";
          handler.sendMessage(myMessage);	          
          
     } catch (Exception e) {
    	//Log.e("Ideas4All", "Error", e);
     }        
     
	}  	
	
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	String loginmsg=(String)msg.obj;
        	if(loginmsg.equals("SUCCESS")) {
        		
        		TextView lblTitle = (TextView) findViewById(R.id.title);
        		lblTitle.setText(idea.getTitle());
        		
        		TextView lblBody = (TextView) findViewById(R.id.body);
        		lblBody.setText(idea.getBody());
        		
        		TextView lblUser = (TextView) findViewById(R.id.user_login);
        		lblUser.setText(idea.getUserLogin());
        		
        		imageHttpAddress = idea.getUserAvatar();
        		lblAvatar = (ImageView) findViewById(R.id.user_avatar);       
                downloadFile(imageHttpAddress);  
                
                removeDialog(0);
        	}
        }
    };	
	
    @Override
    protected Dialog onCreateDialog(int id) {
          switch (id) {
                case 0: {
                      dialog = new ProgressDialog(this);
                      dialog.setMessage("Cargando...");
                      dialog.setIndeterminate(true);
                      dialog.setCancelable(true);
                      return dialog;
                }
          }
          return null;
    }   	
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, GOTO_COMMENTS,0, "Ver los comentarios");
        return true;
    }	    
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	
    	Intent intent;
    	
        switch(item.getItemId()) {
	        case GOTO_COMMENTS:
	            intent=new Intent(getApplicationContext(),IdeaCommentsActivity.class);
	            intent.putExtra("idea_id", idea_id);
	            startActivity(intent);
	            finish();            
	            return true;            
        }
        
        
        return super.onMenuItemSelected(featureId, item);
    }  
    
    
    void downloadFile(String imageHttpAddress) {
        URL imageUrl = null;
        try {
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
            lblAvatar.setImageBitmap(loadedImage);
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error cargando la imagen: "+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }    
    
}