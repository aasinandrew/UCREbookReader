package com.ucr.ebookreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class WelcomeAnon extends Activity {
	
	private static final String MEDIA_MOUNTED = "mounted";
	public final static String EXTRA_FILE = "com.ucr.ebookreader.MESSAGE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from singleitemview.xml
		setContentView(R.layout.welcomeanon);
		
		// Locate TextView in welcomeanon.xml
		TextView txtuser = (TextView) findViewById(R.id.txtuser);

		// Set the currentUser String into TextView
		txtuser.setText("You are not logged in.");	
		
		createButtons();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainanon, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_search:
	            openSearch();
	            return true;
	        case R.id.action_login:
	        	Login();
	        	return true;
	        case R.id.action_scan:
	    		Intent intent = new Intent(WelcomeAnon.this, ScanActivity.class);
	    		startActivity(intent);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void openSearch() {
		Intent intent = new Intent(WelcomeAnon.this, SearchActivityAnon.class);
		intent.putExtra("lastintent", "welcomeanon");
		startActivity(intent);
		//finish();
	}
	
	public void Login() {
		Intent intent = new Intent(WelcomeAnon.this, LoginSignupActivity.class);
		startActivity(intent);
		//finish();
	}
	
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
						coverImage = new ParseImageView(WelcomeAnon.this);
						
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
								Intent intent = new Intent(WelcomeAnon.this, PickedBookAnon.class);
								intent.putExtra("passedId", bookObjId);
								startActivity(intent);
								finish();
						    }
						});
						
						//Set Layout Parameters
						RelativeLayout rltemp = (RelativeLayout) findViewById(R.id.welcomeanonRlayout);
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
