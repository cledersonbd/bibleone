package com.cdotti.bibleone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
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
	private AtomicBoolean isLoading = new AtomicBoolean(true);
	private Integer currentChapNum;
	private Integer numBookID;
	private Integer numChapterID;
	
	// Construtor quando se passam os IDs do livro e cap�tulo
	public BibleVerseAdapter(Context c, Integer nBookID, Integer nChapterID) {		
		Cursor cursor;
		
		if (mContext == null)
			mContext = c;
		
		numBookID = nBookID;
		numChapterID = nChapterID;
		currentChapNum = numChapterID;
		
		bibleDBHelper = new BibleDBHelper(mContext);
		bibleDB = bibleDBHelper.openDatabase();
		mVerseArrList = new ArrayList<BibleVerse>();
		indexer = new HashMap<Integer, Integer>();
		
		cursor = bibleDB.rawQuery("SELECT id,book_id,chapter,verse,text FROM verse WHERE book_id = ? AND chapter = ? ORDER BY book_id, chapter, verse ", new String[] {numBookID.toString(), numChapterID.toString()});
		try {
			if (cursor.moveToFirst()) {
				while (!cursor.isAfterLast()) {
					mVerseArrList.add(new BibleVerse(cursor.getInt(2), cursor.getInt(3), cursor.getString(4)));					
					cursor.moveToNext();
				}
			}	
		} finally {
			cursor.close();
		}
		
	}
	// Construtor quando se passa os vers�culos j� prontos
	public BibleVerseAdapter(Context c, ArrayList<BibleVerse> aVerseList) {		
		if (mContext == null)
			mContext = c;
		
		mVerseArrList = aVerseList;
	}
	
	private void buildSections() {
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
	
	public void addFromBibleAdapter(BibleVerseAdapter bAdapt) {
		int i;
		
		for (i = 0; i < bAdapt.getCount(); i++)
			mVerseArrList.add(bAdapt.getItem(i));
		
		buildSections();
	}
	
	@Override
	public int getItemViewType(int position) {

		// Se a posicao for maior que o tamanho do array (pendingView)
		// ou se o versiculo estiver vazio (View do capitulo)
		if (position >= mVerseArrList.size() || 
				mVerseArrList.get(position).getText().isEmpty())
			return IGNORE_ITEM_VIEW_TYPE;

		return super.getItemViewType(position);
	}
	
	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount() + 2;
	}
	
	@Override
	public int getCount() {
		if (isLoading.get())
			return mVerseArrList.size() + 1; // One more for "pending"
		
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
		View pendingView;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Typeface tf = Typefaces.get(mContext, "fonts/exo200.ttf");
		
		if (position == getCount()-1 && isLoading.get()) {
			pendingView = inflater.inflate(R.layout.pending_verse_row, null);
			// Carrega mais versiculos
			new LoadMore().execute();
			return pendingView;
		}
		else {
			int resourceID;
			// Se o versiculo estiver vazio
			if (mVerseArrList.get(position).getText().isEmpty()) {
				resourceID = R.layout.verse_list_header_row;
				if (convertView != null && convertView.findViewById(R.id.lblVerseListHeaderText) == null)
						convertView = null;
			}
			else
				resourceID = R.layout.verse_list_row;
			
			// if it's not recycled, initialize some attributes
			if (convertView == null) {
				customView = inflater.inflate(resourceID, null);
			} else {
				customView = convertView;
			}			
				
			TextView verseNum = (TextView) customView.findViewById(R.id.lblVerseNum);
			TextView verseText = (TextView) customView.findViewById(R.id.lblVerseTextView);
			TextView verseChapterText = (TextView) customView.findViewById(R.id.lblVerseListHeaderText);
			
			if (verseNum != null) {
				verseNum.setText(mVerseArrList.get(position).getId().toString());
				verseNum.setTypeface(tf);
			}
			if (verseText != null) {
				verseText.setText(mVerseArrList.get(position).getText());
				verseText.setTypeface(tf);
			}
			if (verseChapterText != null) {
				verseChapterText.setText(mContext.getResources().getString(R.string.chapterLabel) + " " +
						mVerseArrList.get(position).getNumChapter().toString());
				verseChapterText.setTextColor(Color.BLACK);
			}
		}
					
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
	
	// Inner class para execu��o em background
	public class LoadMore extends AsyncTask<Void, Integer, Exception> {
		ProgressDialog mDialog;
		
		@Override
		protected void onPreExecute() {
			//super.onPreExecute();
			isLoading.set(true);
		}

		@Override
		protected Exception doInBackground(Void... params) {
			Cursor cursor;
			
			cursor = bibleDB.rawQuery("SELECT id,book_id,chapter,verse,text FROM verse WHERE book_id = ? AND chapter = ? ORDER BY book_id, chapter, verse ", new String[] {numBookID.toString(), (++currentChapNum).toString()});
			if (cursor.moveToFirst()) {
				// Adiciona o "stub" de vers�culo (apenas o n�mero do cap�tulo)
				mVerseArrList.add(new BibleVerse(currentChapNum, -1, ""));
				// Adiciona efetivamente os vers�culos
				while (!cursor.isAfterLast()) {
					mVerseArrList.add(new BibleVerse(cursor.getInt(2), cursor.getInt(3), cursor.getString(4)));
					cursor.moveToNext();
				}
			}
			else {
				Log.e(BibleVerseAdapter.class.getName(), "None result was brought!");
				return new Exception("Excecao");
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(Exception result) {
			//super.onPostExecute(result);
			
			if (result != null)
				isLoading.set(false);
			
			BibleVerseAdapter.this.notifyDataSetChanged();
			
		}
	}
}
