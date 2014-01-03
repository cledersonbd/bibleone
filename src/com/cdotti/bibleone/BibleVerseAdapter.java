package com.cdotti.bibleone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class BibleVerseAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private ArrayList<BibleVerse> mVerseArrList;
	private BibleDBHelper bibleDBHelper;
	private SQLiteDatabase bibleDB;
	private HashMap<Integer, Integer> indexer;
	private Integer[] sections;
	private static final Integer DIV_PAGE = 3;
	
	// Construtor quando se passam os IDs do livro e cap’tulo
	public BibleVerseAdapter(Context c, Integer nBookID, Integer nChapterID) {		
		Cursor cursor;
		
		if (mContext == null)
			mContext = c;
		
		bibleDBHelper = new BibleDBHelper(mContext);
		bibleDB = bibleDBHelper.openDatabase();
		mVerseArrList = new ArrayList<BibleVerse>();
		indexer = new HashMap<Integer, Integer>();
		
		cursor = bibleDB.rawQuery("SELECT id,book_id,chapter,verse,text FROM verse WHERE book_id = ? AND chapter = ? ORDER BY book_id, chapter, verse ", new String[] {nBookID.toString(), nChapterID.toString()});
		try {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					mVerseArrList.add(new BibleVerse(cursor.getInt(3), cursor.getString(4)));
					
					cursor.moveToNext();
				}
			}	
		} finally {
			cursor.close();
		}
		
		for (int i = 0; i < mVerseArrList.size(); i++) {
			Integer item = mVerseArrList.get(i).getId();
			Integer chave = (Integer) i / DIV_PAGE;
			if (!indexer.containsKey(chave))
				indexer.put(chave, item);
			
		}
		
		ArrayList<Integer> sectionList = new ArrayList<Integer>(indexer.keySet());
		Collections.sort(sectionList);
		
		sections = new Integer[sectionList.size()];
		sections = sectionList.toArray(sections);
		
	}
	// Construtor quando se passa os vers’culos j‡ prontos
	public BibleVerseAdapter(Context c, ArrayList<BibleVerse> aVerseList) {		
		if (mContext == null)
			mContext = c;
		
		mVerseArrList = aVerseList;
	}
	
	@Override
	public int getCount() {
		return mVerseArrList.size();
	}

	@Override
	public BibleVerse getItem(int position) {
		return mVerseArrList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mVerseArrList.get(position).getId();
	}

	@Override
	// create a new TextView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View customView;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Typeface tf = Typefaces.get(mContext, "fonts/exo200.ttf");
		
		// if it's not recycled, initialize some attributes
		if (convertView == null) {
			customView = inflater.inflate(R.layout.verse_list_row, null);
		} else {
			customView = convertView;
		}
		
		
		TextView verseNum = (TextView) customView.findViewById(R.id.lblVerseNum);
		TextView verseText = (TextView) customView.findViewById(R.id.lblVerseTextView);
		
		verseNum.setText(mVerseArrList.get(position).getId().toString());
		verseNum.setTypeface(tf);
		verseNum.setTextColor(Color.BLACK);
		verseText.setText(mVerseArrList.get(position).getText());
		verseText.setTypeface(tf);
		verseText.setTextColor(Color.DKGRAY);
		
		return customView;
	}
	@Override
	public int getPositionForSection(int sectionIndexer) {
		return indexer.get(sections[sectionIndexer]);
	}
	@Override
	public int getSectionForPosition(int position) {
		return position % DIV_PAGE;
	}
	@Override
	public Object[] getSections() {
		return sections;
	}

}
