package com.ucr.ebookreader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dteviot.epubviewer.Bookmark;
import com.dteviot.epubviewer.BookmarkDialog;
import com.dteviot.epubviewer.EpubWebView;
import com.dteviot.epubviewer.EpubWebView30;
import com.dteviot.epubviewer.IAction;
import com.dteviot.epubviewer.IResourceSource;
import com.dteviot.epubviewer.ResourceResponse;
import com.dteviot.epubviewer.epub.Book;
import com.dteviot.epubviewer.epub.TableOfContents;
import com.parse.ParseUser;


public class DisplayEpubWeb extends Activity implements IResourceSource{
	
    private final static int LIST_CHAPTER_ACTIVITY_ID = 1; 
    private final static int CHECK_TTS_ACTIVITY_ID = 2; 
    
    private final static int CHANGE_SIZE = 3; 
    private final static int CHANGE_FONT = 4; 
    private final static int CHANGE_BACKGROUND = 5; 
    
    private final static int BLACK = 0xff000000;
    public final static int DARKGRAY = 0xff444444;
    public final static int GREY = 0xff888888;
    public final static int WHITE = 0xffffffff;
    public final static int MAROON = 0xff800000;
    public final static int NAVY = 0xff000080;
    public final static int OLIVE = 0xff808000;
 
	private EpubWebView EpubView; 
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		ParseUser currentUser = ParseUser.getCurrentUser();
		String fileName;
		
		//if user logged in get intent from Welcome and display epub file
		if (currentUser != null) {
			Intent intent = getIntent();
			fileName = intent.getStringExtra(Welcome.EXTRA_FILE);
		}		
		//if user not logged in get intent from
		else { 
			Intent intent = getIntent();  
			fileName = intent.getStringExtra(WelcomeAnon.EXTRA_FILE);
		}
		 
	    EpubView = new EpubWebView30(this);  
	    EpubView.getSettings().setJavaScriptEnabled(true);
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN) 
		{
			EpubView.getSettings().setAllowFileAccess(true);
			EpubView.getSettings().setAllowFileAccessFromFileURLs(true);
			EpubView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		} 
	    setContentView(EpubView);
	    EpubView.setBook(fileName);
	    EpubView.loadChapter(null);
	
	    if (savedInstanceState != null) {
	        EpubView.gotoBookmark(new Bookmark(savedInstanceState));
	    } 
	    else {
            Bookmark bookmark = new Bookmark(this);
            EpubView.gotoBookmark(bookmark);
        }

	}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_epub_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // item select 
        switch (item.getItemId()) {
        case R.id.menu_bookmark:
            launchBookmarkDialog(); 
            return true;
        case R.id.menu_chapters:
            launchChaptersList();
            return true;
        case R.id.action_change_txt_size:
        	launchChangeSize();
            return true;
        case R.id.action_change_txt_font:
        	launchChangeFont();
            return true;
        case R.id.action_change_background_color:
        	launchChangeBackground();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String item;
        if (requestCode == CHECK_TTS_ACTIVITY_ID) {
            return;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case LIST_CHAPTER_ACTIVITY_ID:
        	        Uri chapterUri = data.getParcelableExtra(ListChaptersActivity.CHAPTER_EXTRA);
        	        EpubView.loadChapter(chapterUri);
                    break;
                case CHANGE_SIZE:
                	item = data.getStringExtra(ListTextSize.TEXT_SIZE);
                	EpubView.TextSize = item;
                	EpubView.loadUrl("javascript:(document.body.style.fontSize = '"+item+"px');");
                    break;
                case CHANGE_FONT:  
                	item = data.getStringExtra(ListEpubFont.TEXT_FONT); 
     		       	String font = item;   
     		       	EpubView.Font = item;
     		       	EpubView.loadUrl("javascript:var newStyle = document.createElement('style');var text = document.createTextNode(\"*{font-family: '"+font+"' !important; url('"+font+".ttf') format('truetype');}\");newStyle.appendChild(text);document.head.appendChild(newStyle);document.body.style.fontFamily = '"+font+"';");
                    break;  
                case CHANGE_BACKGROUND: 
                	item = data.getStringExtra(ListTextBackground.BACKGROUND_COLOR);  
                	if(item.equals("black"))
                	{
                		
                		EpubView.TextColor = "white";
                		EpubView.BackgroundColor = item;
                		EpubView.loadUrl("javascript:(document.body.style.color ='white');");
                		EpubView.loadUrl("javascript:(document.body.style.backgroundColor ='"+item+"');");
                		
                	} 
                	else if(item.equals("darkgray"))
                	{
                		EpubView.TextColor = "white";
                		EpubView.BackgroundColor = item;
                		EpubView.loadUrl("javascript:(document.body.style.color ='white');");
                		EpubView.loadUrl("javascript:(document.body.style.backgroundColor ='"+item+"');");

                	} 
                	else if(item.equals("grey"))
    	            {
                		EpubView.TextColor = "black";
                		EpubView.BackgroundColor = item;
                		EpubView.loadUrl("javascript:(document.body.style.color ='black');");
                		EpubView.loadUrl("javascript:(document.body.style.backgroundColor ='"+item+"');");
    	            }
                	else if(item.equals("white"))
    	            {
                		EpubView.TextColor = "black";
                		EpubView.BackgroundColor = item;
                		EpubView.loadUrl("javascript:(document.body.style.color ='black');");
                		EpubView.loadUrl("javascript:(document.body.style.backgroundColor ='"+item+"');");
    	            }
                	else if(item.equals("maroon"))
    	            {
                		EpubView.TextColor = "black";
                		EpubView.BackgroundColor = item;
                		EpubView.loadUrl("javascript:(document.body.style.color ='black');");
                		EpubView.loadUrl("javascript:(document.body.style.backgroundColor ='"+item+"');");
    	            }
                	else if(item.equals("navy"))
    	            {
                		EpubView.TextColor = "white";
                		EpubView.BackgroundColor = item;
                		EpubView.loadUrl("javascript:(document.body.style.color ='white');");
                		EpubView.loadUrl("javascript:(document.body.style.backgroundColor ='"+item+"');");
    	            }
                	else if(item.equals("olive"))
    	            {
                		EpubView.TextColor = "black";
                		EpubView.BackgroundColor = item;
                		EpubView.loadUrl("javascript:(document.body.style.color ='black');");
                		EpubView.loadUrl("javascript:(document.body.style.backgroundColor ='"+item+"');");
    	            } 
                    break;
            }
        }
    }
	
 

	//function
    private void launchChaptersList() {
        Book book = EpubView.getBook(); 
        if (book == null) {
            
        } 
        else {
            TableOfContents toc = book.getTableOfContents();
            if (toc.size() == 0) { 
            	
            } 
            else {
            	Intent listChaptersIntent = new Intent(this, ListChaptersActivity.class);
                toc.pack(listChaptersIntent, ListChaptersActivity.CHAPTERS_EXTRA);
                startActivityForResult(listChaptersIntent, 1);
            }
        }
    }


	    private void launchBookmarkDialog() {
	        BookmarkDialog dlg = new BookmarkDialog(this);
	        dlg.show();
	        dlg.setSetBookmarkAction(saveBookmark);
	        dlg.setGotoBookmarkAction(goToBookmark);
	    }

	    protected void onSaveInstanceState (Bundle outState) {
	        super.onSaveInstanceState(outState);
	        Bookmark bookmark = EpubView.getBookmark();
	        if (bookmark != null) {
	            bookmark.save(outState);
	        }
	    }

	    private IAction saveBookmark = new IAction() {
	        public void doAction() {
	            Bookmark bookmark = EpubView.getBookmark();
	            if (bookmark != null) {
	                bookmark.saveToSharedPreferences(DisplayEpubWeb.this);
	            }
	        }
	    };

	    private IAction goToBookmark = new IAction() {
	        public void doAction() {
	            EpubView.gotoBookmark(new Bookmark(DisplayEpubWeb.this));
	        }
	    };

		@Override
	    public ResourceResponse fetch(Uri resourceUri) {
	        return EpubView.getBook().fetch(resourceUri);
	    }
		
	    void launchChangeSize()
	    {
	    	Intent changeSize = new Intent(this, ListTextSize.class);
	        startActivityForResult(changeSize, CHANGE_SIZE);
	    }
	    
	    void launchChangeFont()
	    {
	    	Intent changeFont = new Intent(this, ListEpubFont.class);
	        startActivityForResult(changeFont, CHANGE_FONT);
	    }
	    
	    void launchChangeBackground()
	    {
	    	Intent changeBackground = new Intent(this, ListTextBackground.class);
	        startActivityForResult(changeBackground, CHANGE_BACKGROUND);
	    }
		
}