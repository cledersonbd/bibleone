package com.cdotti.bibleone;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class ChapterActivity extends Activity implements OnItemClickListener {
	private ListView listChapter;
	private TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.activity_slide_in_from_right, R.anim.activity_slide_out_to_left);
		setContentView(R.layout.activity_chapter);
		
		// Show the Up button in the action bar.
		setupActionBar();
		
		Integer chapter_id = 1;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			chapter_id = extras.getInt("bookID");
			String bookName = extras.getString("bookName");
			
			// Seta o titulo
			this.setTitle(bookName + " - " + getString(R.string.chapterLabel));
			
			textView = (TextView) findViewById(R.id.lblBookName);
			textView.setText(bookName);
		}
		
		// Binding utilizado nas paradas
		listChapter = (ListView) findViewById(R.id.listBookVerse);
		listChapter.setAdapter(new BibleChapterAdapter(getApplicationContext(), chapter_id.toString()));
		listChapter.setOnItemClickListener(this);
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

	 // Listener para os cliques nos capitulos da biblia
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent(getApplicationContext(), VerseActivity.class);
		
		intent.putExtra("bookID", ((BibleChapterAdapter) listChapter.getAdapter()).getItem(position).getBook_id());
		intent.putExtra("chapterNum", ((BibleChapterAdapter) listChapter.getAdapter()).getItem(position).getChapter());
		intent.putExtra("titleName", this.getTitle());
		//intent.putParcelableArrayListExtra("com.cdotti.bibleone.BibleVerse", ((BibleChapterAdapter) listChapter.getAdapter()).getItem(position).getArrListText());
		
		startActivity(intent);
	}
}
