package com.cdotti.bibleone;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class VerseActivity extends Activity implements OnItemClickListener {
	private ListView listVerse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left);
		setContentView(R.layout.activity_verse);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Bundle extras = getIntent().getExtras();
		Integer bookID = extras.getInt("bookID");
		Integer chapterNum = extras.getInt("chapterNum");
		//ArrayList<BibleVerse> verseList = extras.getParcelableArrayList("com.cdotti.bibleone.BibleVerse");
		
		listVerse = (ListView) findViewById(R.id.listVerse);
		listVerse.setAdapter(new BibleVerseAdapter(getApplicationContext(), bookID, chapterNum));
		listVerse.setOnItemClickListener(this);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.verse, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.activity_slide_in_from_left, R.anim.activity_slide_out_to_right);
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		overridePendingTransition(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left);
		super.onResume();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

}
