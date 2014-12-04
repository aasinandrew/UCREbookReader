package com.ucr.ebookreader;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SearchActivityAnon extends Activity {
	
	Button search_button, browse_button;
    EditText search;
	String search_text;
	String title;
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    setContentView(R.layout.searchanon);
	    
	    
	    
	    
	    search_button = (Button) findViewById(R.id.search);
	    
	    
	    search_button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0) 
			{
				search = (EditText) findViewById(R.id.searchtext);
				search_text = search.getText().toString();
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Books");
				query.whereContains("title", search_text);
				query.findInBackground(new FindCallback<ParseObject>() {
					@Override
					public void done(List<ParseObject> books, ParseException e) {
						
						ParseObject book;
						int lastButtonId = 0;
						
						if (e == null) {
							//iterate through list of books retrieved from server
							for(int i = 0; i < books.size(); i++) {
								//Grab out the title
								book = books.get(i);
								title = book.getString("title");
								
								//Create Button
								Button bookButton = new Button(SearchActivityAnon.this);
								bookButton.setText(title);
								
								//Set OnClick for the button
								
								
								//Set Layout Parameters
								RelativeLayout rltemp = (RelativeLayout) findViewById(R.id.searchanonlayout);
								RelativeLayout.LayoutParams lptemp = new RelativeLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT);
								
								if(i == 0) lptemp.addRule(RelativeLayout.BELOW, R.id.browse);
								else lptemp.addRule(RelativeLayout.BELOW, lastButtonId);
								
								//Add Parameters to button
								bookButton.setLayoutParams(lptemp);
								
								//Set the Id (add 1000 to make it unique)
								lastButtonId = 1000 + i;
								bookButton.setId(lastButtonId);
								
								//Add Button to View
								rltemp.addView(bookButton);
							}
						} else {
							// something went wrong
						}
					}
				});
				
				/*
				
				search_text = search.getText().toString();
				if(search_text == "potter")
					Toast.makeText(SearchActivityAnon.this, "found book", Toast.LENGTH_SHORT).show();
				List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
				ParseQuery<ParseObject> titlequery = ParseQuery.getQuery("Books");
				titlequery.whereContains("title", search_text);
				queries.add(titlequery);
				ParseQuery<ParseObject> authorquery = ParseQuery.getQuery("Books");
				authorquery.whereContains("author", search_text);
				queries.add(authorquery);
				ParseQuery<ParseObject> superquery = ParseQuery.or(queries);
				titlequery.findInBackground(new FindCallback<ParseObject>() {
					@Override
					public void done(List<ParseObject> results, ParseException e) {
						// TODO Auto-generated method stub
						
						
						ParseObject book;
						int lastButtonId = 0;
						
						if (e == null) {
							
							if(results.size()> 0)
							Toast.makeText(SearchActivityAnon.this, "found book", Toast.LENGTH_SHORT).show();
							
							
							for(int i = 0; i < results.size(); i++) {
								
								
								
								book = results.get(i);
								title = book.getString("title");
								
								Button bookButton = new Button(SearchActivityAnon.this);
								bookButton.setText(title);
								
								//Set Layout Parameters
								RelativeLayout rltemp = (RelativeLayout) findViewById(R.id.searchanonlayout);
								RelativeLayout.LayoutParams lptemp = new RelativeLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT);
								
								if(i == 0) lptemp.addRule(RelativeLayout.BELOW, R.id.browse);
								else lptemp.addRule(RelativeLayout.BELOW, lastButtonId);
								
								//Add Parameters to button
								bookButton.setLayoutParams(lptemp);
								
								//Set the Id (add 1000 to make it unique)
								lastButtonId = 1000 + i;
								bookButton.setId(lastButtonId);
								
								//Add Button to View
								rltemp.addView(bookButton);
							}
						}
						else {
							
						}
					}
					
				});
				
				
				
				
				
				*/
				
				
			}
			
	   });
	    
	    browse_button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0)
			{
				Intent intent = new Intent(SearchActivityAnon.this, BrowseActivityAnon.class);
				startActivity(intent);
			}
		});	

 }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.searchmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_shop:
	            openShop();
	            return true;
	        case R.id.action_logout:
	        	Logout();
	        	return true;
	        case R.id.action_scan:
	    		Intent intent = new Intent(SearchActivityAnon.this, ScanActivity.class);
	    		//intent.putExtra("lastintent", "welcome");
	    		startActivity(intent);
	            //Scan();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void openShop() {
		Intent intent = new Intent(SearchActivityAnon.this, WelcomeAnon.class);
		startActivity(intent);
	}
	
	
	public void Logout() {
		Toast.makeText(SearchActivityAnon.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
		ParseUser.logOut();
		Intent intent = new Intent(SearchActivityAnon.this, WelcomeAnon.class);
		startActivity(intent);
	}
	
}
