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
		setContentView(R.layout.searchanon);
		
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
				        		 
				        		 if ( title.toLowerCase().contains(search_text.toLowerCase())
				     					|| author.toLowerCase().contains(search_text.toLowerCase())
				     					|| genre.toLowerCase().contains(search_text.toLowerCase()) ) {
				     				
				        			 
										/*//obtaining Book covers
						        		 ParseFile fileObject = (ParseFile) books.get(i).get("cover");
						        		 fileObject.getDataInBackground(new GetDataCallback() 
						        		 {
														public void done(byte[] data, ParseException e) 
														{
															if (e == null) 
															{
																// Decode the Byte[] into
																// Bitmap
																Bitmap bmp = BitmapFactory.decodeByteArray(data, 0,
																				data.length);
																ImageView cov = new ImageView(SearchActivity.this);											cov.setImageBitmap(bmp);
																cov.setMaxHeight(20);
																cov.setMaxWidth(20);
																cov.setId(1000 + i);
																rltemp.addView(cov);
																
															}
														}
														
						        		 });*/
						        		 
						        		book = books.get(i);
					     				final String bookObjId = book.getObjectId();
					     					
					     				Button buybook = new Button(SearchActivityAnon.this);
										buybook.setText("Title: " + title + "\n" +
				        				 		 		"Author: " + author + "\n" +
				        				 		 		"Genre: " + genre + "\n" +
				        				 		 		"Price: $" + price);
											
											
					     				buybook.setOnClickListener(new OnClickListener() {
											public void onClick(View v) {
												Intent intent = new Intent(SearchActivityAnon.this, PickedBookAnon.class);
												intent.putExtra("passedId", bookObjId);
												startActivity(intent);
										    }
										});
											
										//Set Layout Parameters
										LinearLayout rltemp = (LinearLayout) findViewById(R.id.linlayout);
										LinearLayout.LayoutParams lptemp = new LinearLayout.LayoutParams(
												LayoutParams.MATCH_PARENT,
												LayoutParams.WRAP_CONTENT);
									
										//Add book to List of Results
										buybook.setLayoutParams(lptemp);
										buybook.setId(1000 + i);
										rltemp.addView(buybook);
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
				//finish();
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
