package com.ucr.ebookreader;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BrowseActivityAnon extends Activity {

	
	TextView haunted;
	TextView flint;
	TextView reckless;
	TextView storm;
	TextView harry;
	TextView global;
	
	ImageButton h,f,r,s,hp,g;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    setContentView(R.layout.browseanon);
	    
	     haunted = (TextView) findViewById(R.id.hauntedway);
		 flint = (TextView) findViewById(R.id.flintlord);
		 reckless = (TextView) findViewById(R.id.reckless);
		 storm = (TextView) findViewById(R.id.storm);
		 harry = (TextView) findViewById(R.id.harrypotter);
		 global = (TextView) findViewById(R.id.global);
		 
	 haunted.append("Title: The Haunted Way\n" +
			        "Author: Neil Wesson\n" +
			 		"Price: $2\n");
	 flint.append("Title: The Flint Lord\n" +
		        "Author: Richard Herley\n" +
			 	"Price: $2\n");
	 reckless.append("Title: Reckless Viscount\n" +
		        "Author: Amy Sandas\n" +
		        "Price: $2\n");
	 storm.append("Title: Let the Storms Break\n" +
		        "Author: Shannon Messenger\n" +
		        "Price: $1\n");
	 harry.append("Title: Harry Potter and the Sorcerer's Stone\n" +
		        "Author: J.K. Rowling\n" +
		        "Price: $1\n");
	 global.append("Title: Global Forest Fragmentation\n" +
		        "Author: Chris J. Kettle\n" +
		        "Price: $5\n");	 
	 
	 h = (ImageButton) findViewById(R.id.imageButton6);
	 f = (ImageButton) findViewById(R.id.imageButton5);
	 r = (ImageButton) findViewById(R.id.imageButton3);
	 s = (ImageButton) findViewById(R.id.imageButton2);
	 hp = (ImageButton) findViewById(R.id.imageButton1);
	 g = (ImageButton) findViewById(R.id.imageButton4);
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
                openSearch();
                return true;
	        case R.id.action_shop:
	            openShop();
	            return true;
	        case R.id.action_logout:
	        	Logout();
	        	return true;
	        case R.id.action_scan:
	    		Intent intent = new Intent(BrowseActivityAnon.this, ScanActivity.class);
	    		startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void openSearch() {
		Intent intent = new Intent(BrowseActivityAnon.this, SearchActivityAnon.class);
		startActivity(intent);
	}
	
	public void openShop() {
		Intent intent = new Intent(BrowseActivityAnon.this, WelcomeAnon.class);
		startActivity(intent);
	}
	
	
	public void Logout() {
		Toast.makeText(BrowseActivityAnon.this, "Successfully logged out", Toast.LENGTH_SHORT).show();
		ParseUser.logOut();
		Intent intent = new Intent(BrowseActivityAnon.this, WelcomeAnon.class);
		startActivity(intent);
	}

}
