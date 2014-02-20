package com.cdotti.bibleone;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class VerseActivity extends FragmentActivity implements OnItemClickListener, VerseFragment.OnSelectedVerseListener {
	private Integer bookID;
	private Integer chapterNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left);
		setContentView(R.layout.generic_activity_frag);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		Bundle extras = getIntent().getExtras();
		bookID = extras.getInt("bookID");
		chapterNum = extras.getInt("chapterNum");
		String bookName = extras.getString("titleName");
		
		this.setTitle(bookName);
		
		Bundle args = new Bundle();
		args.putInt("bookID", bookID);
		args.putInt("chapterNum", chapterNum);
		args.putString("titleName", bookName);
		
		VerseFragment verseFragment;
		verseFragment = new VerseFragment();
		verseFragment.setArguments(args);
		
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction fragTrans = fm.beginTransaction();
		fragTrans.setCustomAnimations(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left,
				R.anim.activity_slide_in_from_left, R.anim.activity_slide_out_to_right);
		fragTrans.replace(R.id.generic_content_frame, verseFragment);
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
	public void onItemClick(AdapterView<?> parentView, View view, int arg2, long arg3) {
		TextView txtNumView = (TextView) view.findViewById(R.id.lblVerseNum);
		
		if (txtNumView != null)
			Toast.makeText(getApplicationContext(), txtNumView.getText(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onSelectedVerse(Integer verseNum) {
		
		if (verseNum != null)
			Toast.makeText(this.getApplicationContext(), verseNum.toString(), Toast.LENGTH_SHORT).show();
		
	}
}
