/** 
 * Ideas4All Android application
 * 
 * @author Jon Segador <jonseg@gmail.com> || http://jonsegador.com
 * Released under MIT license, https://raw.github.com/jonseg/ideas4all_android/master/LICENSE.txt
 * 
 */

package com.jonsegador.ideas4all;

import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IdeaCommentsActivity extends ListActivity {
	
	private Thread t;
	private ProgressDialog dialog;	
	
	private Vector<ParsedCommentDataSet> comments;
	
	private ArrayList<Comment> commentList = new ArrayList<Comment>();
	private CommentAdapter adaptador;
	
	private String idea_comments_url = "http://es.ideas4all.com/api/ideas/idea_id/comments.xml";
    
	private String idea_id;
    
    private static final int GOTO_IDEA = Menu.FIRST;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.idea_comments);
        
        idea_id = getIntent().getStringExtra("idea_id");
        idea_comments_url = idea_comments_url.replaceAll("idea_id", idea_id);  
        
        adaptador = new CommentAdapter(this);
        
        showDialog(0);
        t=new Thread() {
              public void run() {
            	  loadComments();
              }
        };        
        t.start();         
        
    }
    
    
    public class CommentAdapter extends ArrayAdapter<Comment> {
    	
    	Activity context;

    	CommentAdapter(Activity context) {
    		super(context, R.layout.comment_row, commentList);
    		this.context = context;
    	}

    	public View getView(int position, View convertView, ViewGroup parent) {
    		LayoutInflater inflater = context.getLayoutInflater();
    		View item = inflater.inflate(R.layout.comment_row, null);
    	
    		TextView lblComment = (TextView)item.findViewById(R.id.comment);
    		lblComment.setText(commentList.get(position).getComment());
    	
    		TextView lblDate = (TextView)item.findViewById(R.id.date);
    		lblDate.setText(commentList.get(position).getDate());

    		return(item);
    	}

    }       
    
     
    
	public void loadComments() {
		
        try {
        	
          URL url = new URL(idea_comments_url);

          SAXParserFactory spf = SAXParserFactory.newInstance();
          SAXParser sp = spf.newSAXParser();

          XMLReader xr = sp.getXMLReader();
          CommentHandler myExampleHandler = new CommentHandler();
          xr.setContentHandler(myExampleHandler);
          
          InputSource is = new InputSource(url.openStream());
          is.setEncoding("UTF-8");
          xr.parse(is);
          
          comments = myExampleHandler.getParsedData();
          
          Message myMessage=new Message();
          myMessage.obj="SUCCESS";
          handler.sendMessage(myMessage);	
          
     } catch (Exception e) {
         
    	 Vector<ParsedCommentDataSet> myParsedCommentDataSet = new Vector<ParsedCommentDataSet>();
    	 
         ParsedCommentDataSet DataSet = new ParsedCommentDataSet();
         DataSet.setComment("Esta idea no tiene comentarios");
         DataSet.setDate(" ");
         myParsedCommentDataSet.add(DataSet);
         comments = myParsedCommentDataSet;
         
         Message myMessage=new Message();
         myMessage.obj="SUCCESS";
         handler.sendMessage(myMessage);	
     }        
     
	}  
	
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	String successmsg=(String)msg.obj;
        	if(successmsg.equals("SUCCESS")) {
        		
                for(ParsedCommentDataSet comment:comments){
                	commentList.add(new Comment(comment.getComment(), comment.getDate()));
                }
                
                setListAdapter(adaptador);
                
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
        menu.add(0, GOTO_IDEA,0, "Ir a la idea");
        return true;
    }	    
    
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	
    	Intent intent;
    	
        switch(item.getItemId()) {
	        case GOTO_IDEA:
	            intent=new Intent(getApplicationContext(),IdeaActivity.class);
	            intent.putExtra("idea_id", idea_id);
	            startActivity(intent);
	            finish();            
	            return true;            
        }
        
        
        return super.onMenuItemSelected(featureId, item);
    }      
    
    
}