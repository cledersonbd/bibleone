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

public class BibleChapterAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private ArrayList<BibleChapter> mChapterArr;
	private BibleDBHelper bibleDBHelper;
	private SQLiteDatabase bibleDB;
	private HashMap<Integer, Integer> indexer;
	private Integer[] sections;
	private static final Integer MAX_VERSES_PER_CHAPTER = 3;
	private static final Integer DIV_PAGE = 10;
	
	public BibleChapterAdapter(Context c, String cBookId) {
		Cursor cursor;
		int id = -1;
		Integer book_id = -1;
		int chapter = 1;
		ArrayList<BibleVerse> chapterVerse = new ArrayList<BibleVerse>();
		
		if (mContext == null)
			mContext = c;
		
		bibleDBHelper = new BibleDBHelper(mContext);
		bibleDB = bibleDBHelper.openDatabase();
		mChapterArr = new ArrayList<BibleChapter>();
		indexer = new HashMap<Integer, Integer>();
		
		cursor = bibleDB.rawQuery("SELECT id,book_id,chapter,verse,text FROM verse WHERE book_id = ? AND verse <= ? ORDER BY book_id, chapter, verse ", new String[] {cBookId, MAX_VERSES_PER_CHAPTER.toString()});
		try {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					id = cursor.getInt(0);
					book_id = cursor.getInt(1);
					chapter = cursor.getInt(2);
					chapterVerse = new ArrayList<BibleVerse>();
					
					while (!cursor.isAfterLast() && cursor.getInt(2) == chapter) {
						chapterVerse.add(new BibleVerse(cursor.getInt(2), cursor.getInt(3), cursor.getString(4)));
						cursor.moveToNext();
					}

					mChapterArr.add(new BibleChapter(id,book_id,chapter,chapterVerse));
					if (chapterVerse.size() <= 0)
						cursor.moveToNext();
				}
			}
		} finally {
			cursor.close();
		}

		for (int i = 0; i < mChapterArr.size(); i++) {
			Integer item = mChapterArr.get(i).getChapter();
			//Integer chave = ((Integer) mChapterArr.get(i).getChapter() - 1) / DIV_PAGE;
			Integer chave = (Integer) i / DIV_PAGE;
			if (!indexer.containsKey(chave))
				indexer.put(chave, item);
			
		}
		
		ArrayList<Integer> sectionList = new ArrayList<Integer>(indexer.keySet());
		Collections.sort(sectionList);
		
		sections = new Integer[sectionList.size()];
		sections = sectionList.toArray(sections);
		
	}
	
	@Override
	public int getCount() {
		return mChapterArr.size();
	}

	@Override
	public BibleChapter getItem(int position) {
		return mChapterArr.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mChapterArr.get(position).getId();
	}

	@Override
	// create a new TextView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		String cText = "";
		View customView;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Typeface tf = Typefaces.get(mContext, "fonts/exo200.ttf");
		ArrayList<BibleVerse> chapVerses = mChapterArr.get(position).getArrListText();
		
		// if it's not recycled, initialize some attributes
		if (convertView == null) {
			customView = inflater.inflate(R.layout.chapter_list_row, null);
		} else {
			customView = convertView;
		}
		
		for (int i = 0; i < chapVerses.size() ; i++)
			cText = cText.concat(chapVerses.get(i).getText());
		
		// Busca as views de texto para setar o texto
		TextView chapterNum = (TextView) customView.findViewById(R.id.lblChapterNum);
		TextView chapterText = (TextView) customView.findViewById(R.id.lblChapterTextView);
		
		chapterNum.setText(mChapterArr.get(position).getChapter().toString());
		chapterNum.setTypeface(tf);
		chapterNum.setTextColor(Color.BLACK);
		chapterText.setText(cText);
		chapterText.setTypeface(tf);
		chapterText.setTextColor(Color.DKGRAY);
		
		return customView;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		return indexer.get(sections[sectionIndex]);
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
