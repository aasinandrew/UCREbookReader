package com.ucr.ebookreader;


import java.util.ArrayList;
import com.parse.GetDataCallback; 
import com.parse.ParseImageView; 
import java.util.Collections;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class BrowseActivity extends Activity 
{
	
	String title;
	String author;
	String genre;
	int price;
	int times_purchased;
	int times_viewed;
	
	Button sort_tp_button;
	Button sort_tv_button;
	
	LinearLayout list_books;
	ParseObject book;
	String lastintent;
	
	TextView sv;
	TextView sp;
	TextView sc;
	
	String[] categories = {"Action", "Biology", "Fantasy", "Horror", "Romance"};
	
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//Get the layout from the browse.xml
		setContentView(R.layout.browse);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			lastintent = extras.getString("lastintent");
		}
		
		sort_tp_button = (Button) findViewById(R.id.timespurchased_button);
		sort_tv_button = (Button) findViewById(R.id.timesviewed_button);
		list_books = (LinearLayout) findViewById(R.id.ll);
		
		sv = (TextView) findViewById(R.id.nah2);
		sv.setVisibility(View.GONE);
		sp = (TextView) findViewById(R.id.nah3);
		sp.setVisibility(View.GONE);
		sc = (TextView) findViewById(R.id.nah4);

		//Access Parse for list of books online
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Books");
		query.findInBackground(new FindCallback<ParseObject>() {
			
			@Override
			public void done(List<ParseObject> books, ParseException e) {
				if (e == null) {
					for (int j = 0; j < categories.length; j++)
					{	
						//Set Layout Parameters
						LinearLayout rltemp = (LinearLayout) findViewById(R.id.ll);
						LinearLayout.LayoutParams lptemp = new LinearLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
						
						//Add Genre label
						TextView cat = new TextView(BrowseActivity.this);
						cat.setText(categories[j]);
						cat.setLayoutParams(lptemp);
						cat.setId(1000 + j);
						rltemp.addView(cat);
						
						for (int i = 0; i < books.size(); i++) 
						{
							title = books.get(i).getString("title");
			        		author = books.get(i).getString("author");
			        		genre = books.get(i).getString("genre");
			        		price = books.get(i).getInt("price");
			        		ParseFile cover = null;
			 				ParseImageView coverImage;
			 				 
							if (books.get(i).getString("genre").toLowerCase()
									.contains(categories[j].toLowerCase()))
							{
								book = books.get(i);
				        		cover = book.getParseFile("cover");
			     				final String bookObjId = book.getObjectId();
			     					
			     				TextView book_details = new TextView(BrowseActivity.this);
								book_details.setText("Title: " + title + "\n" +
		        				 		 			"Author: " + author + "\n" +
		        				 		 			"Genre: " + genre + "\n" +
		        				 		 			"Price: $" + price);
							
								//Create ImageView
								coverImage = new ParseImageView(BrowseActivity.this);
								
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
										Intent intent = new Intent(BrowseActivity.this, PickedBook.class);
										intent.putExtra("passedId", bookObjId);
										startActivity(intent);
										finish();
								    }
								});
									
								//Set Layout Parameters
								LinearLayout rinltemp = (LinearLayout) findViewById(R.id.ll);
								LinearLayout.LayoutParams linptemp = new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT);
								LinearLayout.LayoutParams covertemp = new LinearLayout.LayoutParams(200,200);
							
								//Add book to List of Results
								coverImage.setLayoutParams(covertemp);
								coverImage.setId(1000 + i);
								rinltemp.addView(coverImage);
								
								//Add Books details
								book_details.setLayoutParams(linptemp);
								book_details.setId(1000 + i);
								rinltemp.addView(book_details);
							}
						}
					}
				}
			}
		});
		
		
		sort_tp_button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0)
			{
				sp.setVisibility(View.VISIBLE);
				sv.setVisibility(View.GONE);
				sc.setVisibility(View.GONE);
				
				//Clear list of books to reorder
				LinearLayout linlay = (LinearLayout) findViewById(R.id.ll);
				linlay.removeAllViews();
				
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Books");
				
				//Sorting Books by timesPurchased
				query.orderByDescending("timesPurchased");
				query.findInBackground(new FindCallback<ParseObject>() 
				{
					@Override
					public void done(List<ParseObject> books, ParseException e) 
					{
						if (e == null) 
						{
							for (int i = 0; i < books.size(); i++) 
							{
								title = books.get(i).getString("title");
								author = books.get(i).getString("author");
								genre = books.get(i).getString("genre");
								price = books.get(i).getInt("price");
								times_purchased = books.get(i).getInt("timesPurchased");
								ParseFile cover = null;
				 				ParseImageView coverImage;
							
				 				book = books.get(i);
				        		cover = book.getParseFile("cover");
			     				final String bookObjId = book.getObjectId();
			     					
			     				TextView book_details = new TextView(BrowseActivity.this);
								book_details.setText("Title: " + title + "\n" +
		        				 		 			"Author: " + author + "\n" +
		        				 		 			"Genre: " + genre + "\n" +
		        				 		 			"Price: $" + price);
							
								//Create ImageView
								coverImage = new ParseImageView(BrowseActivity.this);
								
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
										Intent intent = new Intent(BrowseActivity.this, PickedBook.class);
										intent.putExtra("passedId", bookObjId);
										startActivity(intent);
										finish();
								    }
								});
						    
								//Set Layout Parameters
								LinearLayout rltemp = (LinearLayout) findViewById(R.id.ll);
								LinearLayout.LayoutParams lptemp = new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT);
								LinearLayout.LayoutParams covertemp = new LinearLayout.LayoutParams(200,200);
								
								//Add Times Purchased label
								TextView tp_textview = new TextView(BrowseActivity.this);
								tp_textview.setText("Times Puchased: " + times_purchased);
								tp_textview.setLayoutParams(lptemp);
								tp_textview.setId(1000 + i);
								rltemp.addView(tp_textview);
							
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
					}
				});
			}
		});
		
		sort_tv_button.setOnClickListener(new OnClickListener()
		{
			public void onClick(View arg0)
			{
				sp.setVisibility(View.GONE);
				sv.setVisibility(View.VISIBLE);
				sc.setVisibility(View.GONE);
				
				//Clear list of books to reorder
				LinearLayout linlay = (LinearLayout) findViewById(R.id.ll);
				linlay.removeAllViews();
				
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Books");
				
				//Sorting Books by timesViewed
				query.orderByDescending("timesViewed");
				query.findInBackground(new FindCallback<ParseObject>() 
				{
					@Override
					public void done(List<ParseObject> books, ParseException e) 
					{
						if (e == null) 
						{
							
							for (int i = 0; i <  books.size() ; i++) 
							{
								title = books.get(i).getString("title");
								author = books.get(i).getString("author");
								genre = books.get(i).getString("genre");
								price = books.get(i).getInt("price");
								times_viewed = books.get(i).getInt("timesViewed");
								ParseFile cover = null;
				 				ParseImageView coverImage;
							
				 				book = books.get(i);
				        		cover = book.getParseFile("cover");
			     				final String bookObjId = book.getObjectId();
			     					
			     				TextView book_details = new TextView(BrowseActivity.this);
								book_details.setText("Title: " + title + "\n" +
		        				 		 			"Author: " + author + "\n" +
		        				 		 			"Genre: " + genre + "\n" +
		        				 		 			"Price: $" + price);
							
								//Create ImageView
								coverImage = new ParseImageView(BrowseActivity.this);
								
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
										Intent intent = new Intent(BrowseActivity.this, PickedBook.class);
										intent.putExtra("passedId", bookObjId);
										startActivity(intent);
										finish();
								    }
								});
						    
								//Set Layout Parameters
								LinearLayout rltemp = (LinearLayout) findViewById(R.id.ll);
								LinearLayout.LayoutParams lptemp = new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT);
								LinearLayout.LayoutParams covertemp = new LinearLayout.LayoutParams(200,200);
								
								//Add Times Purchased label
								TextView tp_textview = new TextView(BrowseActivity.this);
								tp_textview.setText("Times Viewed: " + times_viewed);
								tp_textview.setLayoutParams(lptemp);
								tp_textview.setId(1000 + i);
								rltemp.addView(tp_textview);
							
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
					}
				});
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	        	Intent search = new Intent(BrowseActivity.this, SearchActivity.class);
	            startActivity(search);
	        	return true;
	        case R.id.action_shop:
	            Intent intent = new Intent(BrowseActivity.this, Welcome.class);
	            startActivity(intent);
	            return true;
	        case R.id.action_logout:
	        	Logout();
	        	return true;
	        case R.id.action_scan:
	    		Intent intent1 = new Intent(BrowseActivity.this, ScanActivity.class);
	    		startActivity(intent1);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void Logout() {
		Toast.makeText(BrowseActivity.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
		ParseUser.logOut();
		Intent intent = new Intent(BrowseActivity.this, WelcomeAnon.class);
		startActivity(intent);
		//finish();
	}

	
	/*@Override  
	public void onBackPressed() {
	    super.onBackPressed(); 
	    if (lastintent.equals("welcome")){
	    	Intent intent = new Intent(BrowseActivity.this, Welcome.class);
	    	startActivity(intent);
	    	finish();
	    }
	    else {
	    	Intent intent = new Intent(BrowseActivity.this, WelcomeAnon.class);
	    	startActivity(intent);
	    	finish();
	    }
	}*/
}