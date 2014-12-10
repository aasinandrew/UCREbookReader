package com.ucr.ebookreader;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class SearchActivityAnon extends Activity {
	
	String search_text;
	EditText search;
	Button search_button;
	Button browse_button;
	
	String title;
	String author;
	String genre;
	int price;
	
	LinearLayout list_books;
	
	ParseObject book;
	String lastintent;
	TextView results;
	int y = 0;
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		//Get the layout from the search.xml
		setContentView(R.layout.search);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			lastintent = extras.getString("lastintent");
		}
		
		search = (EditText) findViewById(R.id.searchtext);
		search_button = (Button) findViewById(R.id.search);
		browse_button = (Button) findViewById(R.id.browse);
		list_books = (LinearLayout) findViewById(R.id.linlayout);

		results = (TextView) findViewById(R.id.yee);
		results.setVisibility(View.GONE);
		
		search_button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0) 
			{
				results.setVisibility(View.VISIBLE);
				
				//Clear list of books to reorder
				LinearLayout linlay = (LinearLayout) findViewById(R.id.linlayout);
				linlay.removeAllViews();
			 
				// Retrieve the text entered from the EditText
				search_text = search.getText().toString();
				
				//Access Parse for list of books online
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Books");
				query.findInBackground(new FindCallback<ParseObject>() {
				   @Override
				   public void done(List<ParseObject> books, ParseException e) {
				         if (e == null) {
	 
				        	 for (int i = 0; i < books.size(); i++) {
				        		 title = books.get(i).getString("title");
				        		 author = books.get(i).getString("author");
				        		 genre = books.get(i).getString("genre");
				        		 price = books.get(i).getInt("price");
				        		 ParseFile cover = null;
				 				 ParseImageView coverImage;
				        		 
				        		 
				        		 if ( title.toLowerCase().contains(search_text.toLowerCase())
				     					|| author.toLowerCase().contains(search_text.toLowerCase())
				     					|| genre.toLowerCase().contains(search_text.toLowerCase()) ) {
				     				
				        			 
						        		book = books.get(i);
						        		cover = book.getParseFile("cover");
					     				final String bookObjId = book.getObjectId();
					     					
					     				TextView book_details = new TextView(SearchActivityAnon.this);
										book_details.setText("Title: " + title + "\n" +
				        				 		 			"Author: " + author + "\n" +
				        				 		 			"Genre: " + genre + "\n" +
				        				 		 			"Price: $" + price);
										
										
											
											
										//Create ImageView
										coverImage = new ParseImageView(SearchActivityAnon.this);
										
										//Set Cover Image
										coverImage.setParseFile(cover);
										coverImage.loadInBackground(new GetDataCallback() {
										     public void done(byte[] data, ParseException e) {
										     // The image is loaded and displayed     
										     }
										});
										
										//Make image clickable
										coverImage.setOnClickListener(new OnClickListener() {
											public void onClick(View v) {
												Intent intent = new Intent(SearchActivityAnon.this, PickedBookAnon.class);
												intent.putExtra("passedId", bookObjId);
												startActivity(intent);
												finish();
										    }
										});
											
										//Set Layout Parameters
										LinearLayout rltemp = (LinearLayout) findViewById(R.id.linlayout);
										LinearLayout.LayoutParams lptemp = new LinearLayout.LayoutParams(
												LayoutParams.MATCH_PARENT,
												LayoutParams.WRAP_CONTENT);
										LinearLayout.LayoutParams covertemp = new LinearLayout.LayoutParams(200,200);
									
										//Add book to List of Results
										coverImage.setLayoutParams(covertemp);
										coverImage.setId(1000 + i);
										rltemp.addView(coverImage);
										
										//Add Books details
										book_details.setLayoutParams(lptemp);
										book_details.setId(1000 + i);
										rltemp.addView(book_details);
				                 }
				        		 
				        	 }
				        	 
				        	 LinearLayout linlay = (LinearLayout) findViewById(R.id.linlayout);
				        	 if (linlay.getChildCount() == 0) 
				        	 {
		        				 Toast.makeText(
					     		 getApplicationContext(),
					   			 "No Results Found.",
					     		 Toast.LENGTH_LONG).show();
		        			 }
				        	 
				         }
				     }
				 });
				
			}
		});	
		
		
		browse_button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0)
			{
				Intent intent = new Intent(SearchActivityAnon.this, BrowseActivityAnon.class);
				startActivity(intent);
				finish();
			}
		});		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.searchanon, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_shop:
	            openShop();
	            return true;
	        case R.id.action_login:
	        	Login();
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
	
	
	public void Login() {
		Intent intent = new Intent(SearchActivityAnon.this, LoginSignupActivity.class);
		startActivity(intent);
	}
	
}
