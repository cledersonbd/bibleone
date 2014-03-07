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
	
	// Construtor quando se passam os IDs do livro e capítulo
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
				// Adiciona o "stub" de versículo (apenas o número do capítulo)
				mVerseArrList.add(new BibleVerse(numChapterID, -1, ""));
				while (!cursor.isAfterLast()) {
					mVerseArrList.add(new BibleVerse(cursor.getInt(2), cursor.getInt(3), cursor.getString(4)));					
					cursor.moveToNext();
				}
			}	
		} finally {
			cursor.close();
		}
		
	}
	
	// Construtor quando se passa os versículos já prontos
	public BibleVerseAdapter(Context c, ArrayList<BibleVerse> aVerseList) {		
		if (mContext == null)
			mContext = c;
		
		mVerseArrList = aVerseList;
	}
	
	/**
	 * Função utilizada para construir as seções (capítulos) da listView
	 */
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
		BibleVerse oItem = null;
		if (position < mVerseArrList.size())
			oItem = mVerseArrList.get(position); 
		return oItem;
	}

	@Override
	public long getItemId(int position) {
		Integer id = null;
		if (position < mVerseArrList.size())
			id = mVerseArrList.get(position).getId();
		return id;
	}	
	
	@Override
	// create a new TextView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		View customView;
		View pendingView;
		//int previousChapter = -1;
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Typeface tf = Typefaces.get(mContext, "fonts/exo200.ttf");
		
		/*
		if (position <= getCount() - 1)
			previousChapter = getItem(position).getNumChapter() - 1;
		*/
		
		if (position == (getCount() - 1) && isLoading.get()) {
			pendingView = inflater.inflate(R.layout.verse_list_pending_row, null);
			// Carrega mais versiculos
			new LoadMore(numBookID, ++currentChapNum).execute();
			return pendingView;
		}
		/*
		else if (position == 0 && isLoading.get() && previousChapter >= 1) {
			pendingView = inflater.inflate(R.layout.pending_verse_row, null);
			// Carrega mais versiculos
			new LoadMore(numBookID, previousChapter).execute();
			return pendingView;
		}
		*/
		else {
			int resourceID;
			// Se o versiculo estiver vazio
			if (getItem(position).getText().isEmpty()) {
				resourceID = R.layout.verse_list_header;
				if (convertView != null && convertView.findViewById(R.id.lblVerseListHeaderChapterNum) == null)
					convertView = null;
				
				// Se o proximo item da lista nao for um item
				if ((position + 1) <= mVerseArrList.size() && !(mVerseArrList.get(position + 1).getText().isEmpty()) ) {
					
				}
			}
			else
				resourceID = R.layout.verse_list_row;
			
			// if it's not recycled, initialize some attributes
			if (convertView == null) {
				customView = inflater.inflate(resourceID, null);
			} else {
				customView = convertView;
			}			
				
			TextView verseNum = (TextView) customView.findViewById(R.id.lblVerseListRowVerseNum);
			TextView verseText = (TextView) customView.findViewById(R.id.lblVerseListRowVerseDesc);
			TextView verseChapterNumber = (TextView) customView.findViewById(R.id.lblVerseListHeaderChapterNum);
			
			if (verseNum != null) {
				verseNum.setText(mVerseArrList.get(position).getId().toString());
				verseNum.setTypeface(tf);
			}
			if (verseText != null) {
				verseText.setText(mVerseArrList.get(position).getText());
				verseText.setTypeface(tf);
			}
			if (verseChapterNumber != null) {
				verseChapterNumber.setText(mVerseArrList.get(position).getNumChapter().toString());
				verseChapterNumber.setTextColor(Color.BLACK);
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
	
	// Inner class para execução em background
	public class LoadMore extends AsyncTask<Void, Integer, Exception> {
		ProgressDialog mDialog;
		private Integer bookID;
		private Integer chapNum;
		
		public LoadMore(Integer bID, Integer cNum) {
			//if (cNum != null && cNum > 0)
				bookID = bID;
				chapNum = cNum;
		}
		
		@Override
		protected void onPreExecute() {
			//super.onPreExecute();
			isLoading.set(true);
		}

		@Override
		protected Exception doInBackground(Void... params) {
			Cursor cursor;
			
			cursor = bibleDB.rawQuery("SELECT id,book_id,chapter,verse,text FROM verse WHERE book_id = ? AND chapter = ? ORDER BY book_id, chapter, verse ",
					new String[] {bookID.toString(),chapNum.toString()} );
			if (cursor.moveToFirst()) {
				// Adiciona o "stub" de versículo (apenas o número do capítulo)
				mVerseArrList.add(new BibleVerse(chapNum, -1, ""));
				// Adiciona efetivamente os versículos
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
