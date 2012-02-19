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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Ideas4AllActivity extends ListActivity {
	
    private Thread t;
    private ProgressDialog dialog;	
	
    private Vector<ParsedCategoryDataSet> categories;

    private ArrayList<Category> categoryList = new ArrayList<Category>();
    private CategoryAdapter adaptador;
	
    private String categories_url = "http://es.ideas4all.com/api/categories.xml";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        adaptador = new CategoryAdapter(this);
        
        showDialog(0);
        t=new Thread() {
              public void run() {
            	  loadCategories();
              }
        };        
        t.start();         
        
    }
    
    
    public class CategoryAdapter extends ArrayAdapter<Category> {
    	
    	Activity context;

    	CategoryAdapter(Activity context) {
    		super(context, R.layout.category_row, categoryList);
    		this.context = context;
    	}

    	public View getView(int position, View convertView, ViewGroup parent) {
    		LayoutInflater inflater = context.getLayoutInflater();
    		View item = inflater.inflate(R.layout.category_row, null);
    	
    		TextView lblTitulo = (TextView)item.findViewById(R.id.nombre);
    		lblTitulo.setText(categoryList.get(position).getName());
    	
    		return(item);
    	}

    }       
    
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
        Intent intent = new Intent(getApplicationContext(), CategoryIdeasActivity.class);
        intent.putExtra("category_id", categoryList.get(position).getId());
        startActivity(intent);
	}     
	    
    
	public void loadCategories() {
		
        try {
        	
          URL url = new URL(categories_url);

          SAXParserFactory spf = SAXParserFactory.newInstance();
          SAXParser sp = spf.newSAXParser();

          XMLReader xr = sp.getXMLReader();
          CategoryHandler myExampleHandler = new CategoryHandler();
          xr.setContentHandler(myExampleHandler);
          
          InputSource is = new InputSource(url.openStream());
          is.setEncoding("UTF-8");
          xr.parse(is);
          
          categories = myExampleHandler.getParsedData();
          
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
                
                for(ParsedCategoryDataSet category:categories){
                	categoryList.add(new Category(category.getId(), category.getName()));
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
    
}