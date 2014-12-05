package com.ucr.ebookreader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@SuppressLint("NewApi")
public class Welcome extends Activity {
	
	String title = "gibberish";
	
	private static final String MEDIA_MOUNTED = "mounted";
	public final static String EXTRA_FILE = "com.ucr.ebookreader.MESSAGE";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from singleitemview.xml
		setContentView(R.layout.welcome);
		
		// Retrieve current user from Parse.com
		ParseUser currentUser = ParseUser.getCurrentUser();
		
		// Convert currentUser into String
		String struser = currentUser.getUsername().toString();
		
		// Locate TextView in welcome.xml
		TextView txtuser = (TextView) findViewById(R.id.txtuser);

		// Set the currentUser String into TextView
		txtuser.setText("You are logged in as " + struser);
		
		createButtons();
		Button subscription = (Button) findViewById(R.id.button1);
		subscription.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ParseUser.getCurrentUser().getBoolean("monthlySubscription")) {
			    	Toast.makeText(Welcome.this, "Subscription already purchased!", Toast.LENGTH_SHORT).show();
			    }
				else {
					Intent intent = new Intent(Welcome.this,PurchaseSubscription.class);
					startActivity(intent);
				}
			}
		});
		
		
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.welcome, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            openSearch();
	            return true;
	        case R.id.action_logout:
	        	Logout();
	        	return true;
	        case R.id.action_scan:
	    		Intent intent = new Intent(Welcome.this, ScanActivity.class);
	    		startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void openSearch() {
		Intent intent = new Intent(Welcome.this, SearchActivity.class);
		intent.putExtra("lastintent", "welcome");
		startActivity(intent);
	}
	
	public void Logout() {
		Toast.makeText(Welcome.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
		ParseUser.logOut();
		Intent intent = new Intent(Welcome.this, WelcomeAnon.class);
		startActivity(intent);
		//finish();
	}
	

	
	//dynamically create buttons for books
	public void createButtons() {
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Books");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> books, ParseException e) {
				
				ParseObject book;
				ParseFile cover = null;
				ParseImageView coverImage;
				int lastButtonId = 0;
				int rowId = 0;
				int[] posXY = new int[2];
				
				DisplayMetrics displaymetrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				int screenHight = displaymetrics.heightPixels;
				int screenWidth = displaymetrics.widthPixels;
				
				if (e == null) {
					int horizoncnt = 0;
					
					//iterate through list of books retrieved from server
					for(int i = 0; i < books.size(); i++) {
						//Get Image to load
						book = books.get(i);
						cover = book.getParseFile("cover");
						final String bookObjId = book.getObjectId();
						
						//Create ImageView
						coverImage = new ParseImageView(Welcome.this);
						
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
								Intent intent = new Intent(Welcome.this, PickedBook.class);
								intent.putExtra("passedId", bookObjId);
								startActivity(intent);
								finish();
						    }
						});
						
						//Set Layout Parameters
						RelativeLayout rltemp = (RelativeLayout) findViewById(R.id.welcomeRLayout);
						RelativeLayout.LayoutParams lptemp = new RelativeLayout.LayoutParams(200, 200);
						
						if(i == 0) {
							lptemp.addRule(RelativeLayout.BELOW, R.id.txtuser);
							rowId = 1000 + i;
						}
						else if(horizoncnt == 0) {
							lptemp.addRule(RelativeLayout.BELOW, rowId);
							lptemp.addRule(RelativeLayout.ALIGN_LEFT, rowId);
							rowId = lastButtonId + 1;
						}
						else {
							lptemp.addRule(RelativeLayout.RIGHT_OF, lastButtonId);
							lptemp.addRule(RelativeLayout.ALIGN_TOP, lastButtonId);
						}
						lptemp.setMargins(0, 0, 20, 20);
						horizoncnt++;
						
						//Add Parameters to button
						coverImage.setLayoutParams(lptemp);
						
						//Set the Id (add 1000 to make it unique)
						lastButtonId = 1000 + i;
						coverImage.setId(lastButtonId);
						
						//check if our next image will be off screen and adjust
						if((200 + 20)*(horizoncnt + 1) > screenWidth) horizoncnt = 0;
						
						//Add ImageView to Screen
						rltemp.addView(coverImage);
					}
				} else {
					// something went wrong
				}
			}
		});
		
	}

}