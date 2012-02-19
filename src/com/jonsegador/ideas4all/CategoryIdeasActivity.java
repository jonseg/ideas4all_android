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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CategoryIdeasActivity extends ListActivity {
	
    private Thread t;
    private ProgressDialog dialog;	
	
    private Vector<ParsedIdeaDataSet> ideas;
	
    private int last_id = 0;

    private ArrayList<Idea> ideaList = new ArrayList<Idea>();
    private IdeaAdapter adaptador;
	
    private String ideas_url = "http://es.ideas4all.com/api/categories/category_id.xml?range=30&sort=";
    
    private String category_id;
    private String order_by;
    
    private static final int BY_DATE = Menu.FIRST;
    private static final int BY_TITLE = Menu.FIRST + 1;
    private static final int BY_RATING = Menu.FIRST + 2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_ideas);

        
        if(getIntent().hasExtra("order_by")){
        	order_by = getIntent().getStringExtra("order_by");
        }
        else{
        	order_by = "date";	
        }
        
        category_id = getIntent().getStringExtra("category_id");
        ideas_url = ideas_url.replaceAll("category_id", category_id);
        
      	ideas_url = ideas_url + order_by;
        
        adaptador = new IdeaAdapter(this);
        
        showDialog(0);
        t=new Thread() {
              public void run() {
            	  loadIdeas();
              }
        };        
        t.start();         
        
    }
    
    
    public class IdeaAdapter extends ArrayAdapter<Idea> {
    	
    	Activity context;

    	IdeaAdapter(Activity context) {
    		super(context, R.layout.category_row, ideaList);
    		this.context = context;
    	}

    	public View getView(int position, View convertView, ViewGroup parent) {
    		LayoutInflater inflater = context.getLayoutInflater();
    		View item = inflater.inflate(R.layout.idea_row, null);
    	
    		TextView lblTitle = (TextView)item.findViewById(R.id.title);
    		lblTitle.setText(ideaList.get(position).getTitle());
    	
    		TextView lblUserLogin = (TextView)item.findViewById(R.id.user_login);
    		lblUserLogin.setText("Usuario: " + ideaList.get(position).getUserLogin());

    		TextView lblVotesCount = (TextView)item.findViewById(R.id.votes_count);
    		lblVotesCount.setText(ideaList.get(position).getVotesCount());

    		TextView lblCommentsCount = (TextView)item.findViewById(R.id.comments_count);
    		lblCommentsCount.setText(ideaList.get(position).getCommentsCount());
    		
    		
    		return(item);
    	}

    }       
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
        Intent intent = new Intent(getApplicationContext(), IdeaActivity.class);
        intent.putExtra("idea_id", ideaList.get(position).getId());
        startActivity(intent);
	}        
    
	public void loadIdeas() {
		
        try {
        	
          URL url = new URL(ideas_url);

          SAXParserFactory spf = SAXParserFactory.newInstance();
          SAXParser sp = spf.newSAXParser();

          XMLReader xr = sp.getXMLReader();
          IdeaHandler myExampleHandler = new IdeaHandler();
          xr.setContentHandler(myExampleHandler);
          
          InputSource is = new InputSource(url.openStream());
          is.setEncoding("UTF-8");
          xr.parse(is);
          
          ideas = myExampleHandler.getParsedData();
          
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
        	String successmsg=(String)msg.obj;
        	if(successmsg.equals("SUCCESS")) {
        		
                int i=0;
                
                for(ParsedIdeaDataSet idea:ideas)
                {
                	if(Integer.parseInt(idea.getId()) == last_id){
                		continue;
                	}
                	
                	last_id = Integer.parseInt(idea.getId());
                	
                	ideaList.add(new Idea(idea.getId(), idea.getTitle(), idea.getUserLogin(), idea.getVotesCount(), idea.getCommentsCount()));
        		
            		i++;
                	
                }
                
                if(i==0){
                	Toast.makeText(getApplicationContext(), "La web de Ideas4All no responde", Toast.LENGTH_SHORT).show();
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
        menu.add(0, BY_DATE,0, R.string.order_by_date);
        menu.add(0, BY_TITLE,0, R.string.order_by_title);
        menu.add(0, BY_RATING,0, R.string.order_by_votes);
        return true;
    }	

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	
    	Intent intent;
    	
        switch(item.getItemId()) {
	        case BY_DATE:
	            intent=new Intent(getApplicationContext(),CategoryIdeasActivity.class);
	            intent.putExtra("order_by", "date");
	            intent.putExtra("category_id", category_id);
	            startActivity(intent);
	            finish();            
	            return true;            
	        case BY_TITLE:
	            intent=new Intent(getApplicationContext(),CategoryIdeasActivity.class);
	            intent.putExtra("order_by", "title");
	            intent.putExtra("category_id", category_id);
	            startActivity(intent);
	            finish();            
	            return true;            
	        case BY_RATING:
	            intent=new Intent(getApplicationContext(),CategoryIdeasActivity.class);
	            intent.putExtra("order_by", "rating");
	            intent.putExtra("category_id", category_id);
	            startActivity(intent);
	            finish();            
	            return true;            
        }
        
        
        return super.onMenuItemSelected(featureId, item);
    }     
    
    
}