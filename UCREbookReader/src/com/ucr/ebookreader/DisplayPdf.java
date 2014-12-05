package com.ucr.ebookreader;

import java.util.List;

import com.dteviot.epubviewer.Bookmark;
import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint({ "NewApi", "SetJavaScriptEnabled", "SdCardPath" }) 
public class DisplayPdf extends Activity {
	
	static final String STATE_SAVEPAGE = "SAVEPAGE";
	static final String STATE_FILENAME = "FILENAME";
	
	private WebView wv;
	Button GoBtn;
	EditText Num;
	String PageNum; 
	int savePage = 1;
	int bookmark = 1;
	String fileName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_pdf);
		
		/*
		ParseQuery<ParseObject> lastpage = ParseQuery.getQuery("Bookmarks");
		lastpage.whereEqualTo("title", fileName);
		lastpage.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
		lastpage.findInBackground(new FindCallback<ParseObject>() {
		     public void done(List<ParseObject> objects, ParseException e) {
		         if (e == null) {
		             if (objects.isEmpty()) {
       	   
		             }
		             else {
		            	 	ParseObject p = objects.get(0);
		            	    savePage = p.getInt("pagenumber");
		            	 
		             }
		         } else {
		             
		         }
		     }
	   });
	   */
		
		wv = (WebView) findViewById(R.id.webViewPdf);
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		//if user logged in get intent from Welcome and display pdf file
		if (currentUser != null) {
			//get name of file from intent
			Intent intent = getIntent();
			fileName = intent.getStringExtra(Welcome.EXTRA_FILE);
		}
		//if user not logged in get intent from
		else {
			//get name of file from intent
			Intent intent = getIntent();
			fileName = intent.getStringExtra(WelcomeAnon.EXTRA_FILE);
		}
		//find xml elements
		GoBtn = (Button) findViewById(R.id.GoBtn);
		Num = (EditText) findViewById(R.id.NumPage);
	
		//set WebView Settings
		WebSettings settings = wv.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.supportZoom();
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		settings.setBuiltInZoomControls(true);
		wv.setWebChromeClient(new WebChromeClient());
		
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) 
		{
			settings.setAllowFileAccessFromFileURLs(true);
			settings.setAllowUniversalAccessFromFileURLs(true);
		} 

		//set file into WebView and Load WebView 
		wv.loadUrl("javascript:var url = '"+fileName+"'");
		wv.loadUrl("file:///android_asset/pdfviewer/index.html"); 
		wv.loadUrl("javascript:onGoToPage("+String.valueOf(10)+")");

        //Go Button listener to go to desired page
		GoBtn.setOnClickListener(new OnClickListener() {
 
			public void onClick(View view) {
				PageNum = Num.getText().toString();
				((EditText) findViewById(R.id.NumPage)).setText("");
				wv.loadUrl("javascript:onGoToPage("+PageNum+")");				
				savePage = Integer.parseInt(PageNum);

			} 
		});	
		
	
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_pdf, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

    	switch (item.getItemId()) {
		case R.id.action_settings:					
			return true;	
		        
		case R.id.action_next:
			wv.loadUrl("javascript:onNextPage()");
			savePage++;
	    	return true;
		
		case R.id.action_previous:
			wv.loadUrl("javascript:onPrevPage()");
			savePage--;
	    	return true;
	    	
		case R.id.action_goToBookmark:
			wv.loadUrl("javascript:onGoToPage("+String.valueOf(bookmark)+")");
	    	return true;
	    	
		case R.id.action_saveBookmark:
			bookmark = savePage;
	    	return true;

		default:
	    	return true;
		}
    }
    
    
    @Override
	public void onBackPressed() {
    	if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
    		
    	}
    	else {
    		ParseQuery<ParseObject> checkbookmark = ParseQuery.getQuery("Bookmarks");
        	checkbookmark.whereEqualTo("title", fileName);
        	checkbookmark.whereEqualTo("format", "pdf");
        	checkbookmark.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
        	checkbookmark.findInBackground(new FindCallback<ParseObject>() {
    		     public void done(List<ParseObject> objects, ParseException e) {
    		         if (e == null) {
    		             if (objects.isEmpty()) {
    		            	ParseObject bookmark = new ParseObject("Bookmarks");
    		             	bookmark.put("title", fileName);
    		             	bookmark.put("pagenumber", savePage);
    		             	bookmark.put("format", "pdf");
    		             	bookmark.put("user", ParseUser.getCurrentUser().getUsername());
    		             	bookmark.saveInBackground();	            	   
    		             }
    		             else {
    		            	 ParseObject p = objects.get(0);		  
    		            	 p.put("pagenumber", savePage);
    		            	 p.saveInBackground();
    		            	 
    		            	 
    		             }
    		         } else {
    		             
    		         }
    		     }
    	   });
    	}
    	
    		super.onBackPressed();
     }
     
     
    
    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
        wv.saveState(outState);
        outState.putString(STATE_FILENAME, fileName);
        outState.putInt(STATE_SAVEPAGE, savePage);
     }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState); 

        fileName = savedInstanceState.getString(STATE_FILENAME);
        savePage = savedInstanceState.getInt(STATE_SAVEPAGE);
        wv.restoreState(savedInstanceState);
        wv.reload();
		//wv.loadUrl("javascript:var url = '"+fileName+"'");
		//wv.loadUrl("file:///android_asset/pdfviewer/index.html"); 
        //wv.loadUrl("javascript:onGoToPage("+String.valueOf(savePage)+")");
        
    }
    //*/
 
}

