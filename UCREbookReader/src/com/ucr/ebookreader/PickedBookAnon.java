package com.ucr.ebookreader;

import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PickedBookAnon extends Activity {

public final static String EXTRA_FILE = "com.ucr.ebookreader.MESSAGE";
	
	String bookId;
	String title;
	String author;
	String genre;
	String currUser = ParseUser.getCurrentUser().getUsername();
	String currReview = "";
	double masterRating;
	float currRating = -1;
	int price;
	ParseImageView coverImage;
	TextView textField;
	RatingBar rb;
	ListView lv;
	Button submitReview, buy, open, sample;
	EditText rev;
	Boolean loggedIn = false;
	ParseFile cover = null;
	
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pickedbookanon);
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			bookId = extras.getString("passedId");
		}
		
		if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
			loggedIn = false;
		} else {
			loggedIn = true;
		}
		
		refreshRating();
		
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Books");
		query.getInBackground(bookId, new GetCallback<ParseObject>() {
			@Override
			public void done(ParseObject book, ParseException e) {
				if (e == null) {
					
					//update number of times book was viewed
					book.increment("timesViewed");
					book.saveInBackground();
					//Extract book info
					title = book.getString("title");
					author = book.getString("author");
					genre = book.getString("genre");
					price = book.getInt("price");
					masterRating = (float) book.getDouble("Rating");
					cover = book.getParseFile("cover");
					
					//Set Cover Image
					coverImage = (ParseImageView) findViewById(R.id.imageView1);
					coverImage.setParseFile(cover);
					coverImage.loadInBackground(new GetDataCallback() {
					     public void done(byte[] data, ParseException e) {
					     // The image is loaded and displayed     
					     }
					});
					
					//Set title
					textField = (TextView) findViewById(R.id.textView1);
					textField.setText("Title: " + title);
					
					//Set author
					textField = (TextView) findViewById(R.id.textView2);
					textField.setText("Author: " + author);
					
					//Set genre
					textField = (TextView) findViewById(R.id.textView3);
					textField.setText("Genre: " + genre);
					
					//Set Price
					textField = (TextView) findViewById(R.id.textView4);
					textField.setText("Price: $" + price);
					
					//Set Master Rating
					textField = (TextView) findViewById(R.id.textView5);
					textField.setText("Rating: " + masterRating);
					
					//Initialize Ratings Bar
					//rb = (RatingBar) findViewById(R.id.ratingBar1);
					
					//Set Ratings Bar Listener
					/*rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
						@Override
						public void onRatingChanged(RatingBar ratingBar, final float barRating,
								boolean fromUser) {
							editRatingData(barRating);
						}
					});*/
					
					//Initialize Submit button for Review section
					//submitReview = (Button) findViewById(R.id.button2);
					
					//Set Submit Button Listener
					/*submitReview.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							//check if user has purchased subscription
							if(ParseUser.getCurrentUser().getBoolean("monthlySubscription")) {
								sendReview();
							   }
							else {
							//check if user has purchased book
							ParseQuery<ParseObject> checkPurchase = ParseQuery.getQuery("PurchasedBooks");
							   checkPurchase.whereEqualTo("bookid", bookId);
							   checkPurchase.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
							   checkPurchase.findInBackground(new FindCallback<ParseObject>() {
								     public void done(List<ParseObject> objects, ParseException e) {
								         if (e == null) {
								             if (objects.isEmpty()) {
								            	 Toast.makeText(PickedBookAnon.this, "You must be logged in to do that!", Toast.LENGTH_SHORT).show();								            	   
								             }
								             else {
								            	 sendReview();
								             }
								         } else {
								             
								         }
								     }
							   });
						      }
						}
					});*/
					
					/*buy = (Button) findViewById(R.id.button1);
					
					buy.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
						   //check if user has purchased monthly subscription
						   if(ParseUser.getCurrentUser().getBoolean("monthlySubscription")) {
							   Toast.makeText(PickedBookAnon.this, "Subscription already purchased!", Toast.LENGTH_SHORT).show();
						   }
						   else {
						   //check if user has purchased book already
						   ParseQuery<ParseObject> checkPurchase = ParseQuery.getQuery("PurchasedBooks");
						   checkPurchase.whereEqualTo("bookid", bookId);
						   checkPurchase.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
						   checkPurchase.findInBackground(new FindCallback<ParseObject>() {
							     public void done(List<ParseObject> objects, ParseException e) {
							         if (e == null) {
							             if (objects.isEmpty()) {
							            	   //go to payment screen
							            	   Intent intent = new Intent(PickedBookAnon.this, PaymentActivity.class);
											   intent.putExtra("passedId", bookId);
											   startActivity(intent);
											   //finish();
							             }
							             else {
							            	  // user has already purchased book
							            	  Toast.makeText(PickedBookAnon.this, "Book already purchased!", Toast.LENGTH_SHORT).show();
							             }
							         } else {
							             
							         }
							     }
						   });
						   
			 
						  }
						}
					});
					
					open = (Button) findViewById(R.id.button3);
						
					open.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							//check if user has purchased monthly subscription
							if(ParseUser.getCurrentUser().getBoolean("monthlySubscription")) {
								   ParseQuery<ParseObject> book = ParseQuery.getQuery("Books");
					            	 book.getInBackground(bookId, new GetCallback<ParseObject>() {
					            		 public void done(ParseObject object, ParseException e) {
									         if (e == null) {
									        	 //get pdf of book
									        	 String bookUrl = object.getParseFile("text").getUrl();
									        	 //display book
									        	 Intent intent = new Intent(PickedBookAnon.this, DisplayPdf.class);
										         intent.putExtra(EXTRA_FILE, bookUrl);
										         startActivity(intent);
									         }
									         else {
									        	 
									         }
					            	 }
					                });
							   }
							else {
						   //check if user has purchased book
						   ParseQuery<ParseObject> checkPurchase = ParseQuery.getQuery("PurchasedBooks");
						   checkPurchase.whereEqualTo("bookid", bookId);
						   checkPurchase.whereEqualTo("user", ParseUser.getCurrentUser().getUsername());
						   checkPurchase.findInBackground(new FindCallback<ParseObject>() {
							     public void done(List<ParseObject> objects, ParseException e) {
							         if (e == null) {
							             if (objects.isEmpty()) {
							            	 //user has not purchased book yet
							            	 Toast.makeText(PickedBookAnon.this, "Book not purchased!", Toast.LENGTH_SHORT).show();
							            	   
							             }
							             else {		
							            	 //get pdf url of book
							            	 ParseQuery<ParseObject> book = ParseQuery.getQuery("Books");
							            	 book.getInBackground(bookId, new GetCallback<ParseObject>() {
							            		 public void done(ParseObject object, ParseException e) {
											         if (e == null) {
											        	 String bookUrl = object.getParseFile("text").getUrl();	
											        	 //display book
											        	 Intent intent = new Intent(PickedBookAnon.this, DisplayPdf.class);
												         intent.putExtra(EXTRA_FILE, bookUrl);
												         startActivity(intent);
											         }
											         else {
											        	 
											         }
							            	 }
							                });
							             }
							         } 
							             else {
							        
							             
							         }
							     }
						   });
						   
			 
						}
						}
					});*/
					
					sample = (Button) findViewById(R.id.button4);
					
					sample.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
						   			         //get pdf url of book sample   
							            	 ParseQuery<ParseObject> book = ParseQuery.getQuery("Books");
							            	 book.getInBackground(bookId, new GetCallback<ParseObject>() {
							            		 public void done(ParseObject object, ParseException e) {
											         if (e == null) {
											        	 String sampleUrl = object.getParseFile("sample").getUrl();
											        	 Intent intent = new Intent(PickedBookAnon.this, DisplayPdf.class);
												         intent.putExtra(EXTRA_FILE, sampleUrl);
												         startActivity(intent);
											         }
											         else {
											        	 
											         }
							            	 }
							                });
							             }
							         
							     });
						  
						   
			 
						
			
					
					
					//Initialize Review section
					//rev = (EditText) findViewById(R.id.editText1);
					lv = (ListView) findViewById(R.id.listView1);
					
					//Populate Review section
					populateReviews();
				} else {
					// something went wrong
				}
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.browseanon, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case R.id.action_shop:
	    		openShop();
	    		return true;
	        case R.id.action_search:
                openSearch();
                return true;
	        case R.id.action_login:
	        	Login();
	        	return true;
	        case R.id.action_scan:
	    		Intent intent = new Intent(PickedBookAnon.this, ScanActivity.class);
	    		startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	public void openShop() {
		Intent intent = new Intent(PickedBookAnon.this, WelcomeAnon.class);
		startActivity(intent);
	}
	
	public void openSearch() {
		Intent intent = new Intent(PickedBookAnon.this, SearchActivityAnon.class);
		startActivity(intent);
	}
	
	public void Login() {
		Intent intent = new Intent(PickedBookAnon.this, LoginSignupActivity.class);
		intent.putExtra("PickedBook", bookId);
		intent.putExtra("lastIntent", "PickedBookAnon");
		startActivity(intent);
		finish();
	}
	
	public void refreshRating() {
		ParseQuery<ParseObject> getrating = ParseQuery.getQuery("UserRandR");
		getrating.whereEqualTo("bookid", bookId);
		getrating.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				if(e == null)
				{
					ParseObject p;
					double total = 0;
					for(int i=0; i < objects.size(); i++)
					{
						p = objects.get(i);
						total += p.getInt("rating");
					}
					total /= objects.size();
					total = Math.round(total * 100.0);
					total = total/100.0;
					masterRating = total;
					textField = (TextView) findViewById(R.id.textView5);
					textField.setText("Rating: " + masterRating);
				}
				else {
					
				}
				

			}
			
		});
		
	}
	
	
	
	public void populateReviews() {
		
		ParseQuery<ParseObject> reviewQuery = ParseQuery.getQuery("UserRandR");
		reviewQuery.orderByDescending("updatedAt");
		reviewQuery.whereEqualTo("bookid", bookId);
		reviewQuery.whereExists("review");
		reviewQuery.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(final List<ParseObject> objects, ParseException e) {
				//Variables
				ParseObject obj = null;
				float usrRating = -1;
				String revUser = "";
				String revRev = "";
				String[] revList = new String[objects.size()];
				
				//Create Array for formatting reviews
				for(int i = 0; i < objects.size(); i++) {
					//clear values
					revUser = "";
					revRev = "";
					usrRating = -1;
					
					//set values if they are available
					obj = objects.get(i);
					revUser = obj.getString("username");
					revRev = obj.getString("review");
					usrRating = (float) obj.getDouble("rating");
					
					if(revRev != null && !(revRev.equals(""))) {
						revList[i] = revUser + ": " + revRev;
						if(usrRating != 0.0) {
							revList[i] = revList[i] + "\nMy Rating: " + 
									usrRating + "/5";
						} else {
							revList[i] = revList[i] + "\nMy Rating: User has not rated yet";
						}
					}
				}
				
				//Build ListView from array
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(PickedBookAnon.this,
						android.R.layout.simple_list_item_1, revList);
				
				lv.setAdapter(adapter);
			}
		});
	}
	
	@SuppressLint("NewApi") @Override  
	public void onBackPressed() {
	    super.onBackPressed();   
	    Intent intent = new Intent(PickedBookAnon.this, WelcomeAnon.class);
		startActivity(intent);
		finish();
	}
}
