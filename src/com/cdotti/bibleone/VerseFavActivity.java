package com.cdotti.bibleone;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

public class VerseFavActivity extends BaseActivity 
		implements VerseFavFragment.OnSelectedFavVerseListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left);
		setContentView(R.layout.generic_activity_frag);
		
		// Show the Up button in the action bar.
		setupActionBar();

		Bundle args = new Bundle();
		args.putString("savedType", "F");
		
		VerseFavFragment verseFrag = new VerseFavFragment();
		verseFrag.setArguments(args);
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction fragTrans = fm.beginTransaction();
		fragTrans.setCustomAnimations(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left,
				R.anim.activity_slide_in_from_left, R.anim.activity_slide_out_to_right);
		fragTrans.replace(R.id.generic_content_frame, verseFrag);
		fragTrans.commit();
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
		getMenuInflater().inflate(R.menu.book, menu);
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
		//overridePendingTransition(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left);
		super.onResume();
	}

	@Override
	public void onSelectedFavVerse(VerseFav oVerse) {
		Intent intent = new Intent(getApplicationContext(), VerseActivity.class);
		
		intent.putExtra("bookID", oVerse.getBookID());
		intent.putExtra("chapterNum", oVerse.getNumChapter());
		intent.putExtra("verseNum", oVerse.getVerseNum());
		intent.putExtra("titleName", oVerse.getBookName());
		
		startActivity(intent);		
	}

}
