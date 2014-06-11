package com.cdotti.bibleone;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BibleVerseDAO {
	
	protected static final String TABLE = "verse";
	protected static final String F_ID = "id";
	protected static final String F_TYPE = "type";
	protected static final String F_BOOKID = "book_id";
	protected static final String F_CHAPTER = "chapter";
	protected static final String F_VERSE = "verse";
	protected static final String F_VERSETEXT = "text";
	
	private Context mContext;
	
	public BibleVerseDAO(Context c) {
		mContext = c;
	}
	
	/**
	 * @author cledersondotti
	 * @since 2014-04-09
	 * @return Retorna um arraylist contendo os versiculos de determinado livro/capitulo
	 */
	public ArrayList<BibleVerse> getAllBookChapter(Integer _bookID, Integer _chapterNum) {
		SQLiteDatabase db = BibleDBHelper.getInstance(mContext).openDatabase();
		Cursor cursor = db.query(TABLE, 
				null,	// columns 
				F_BOOKID + "=? AND " + F_CHAPTER + "=? " ,	// where 
				new String[] {_bookID.toString(), _chapterNum.toString()},	// where_args
				null, 	// groupBy 
				null, 	// having
				F_BOOKID + "," + F_CHAPTER + "," + F_VERSE);	// orderBy
		
		ArrayList<BibleVerse> mVerseList = new ArrayList<BibleVerse>();
		BibleVerse verse;
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				verse = new BibleVerse( 
						cursor.getInt(cursor.getColumnIndex(F_ID)),
						cursor.getInt(cursor.getColumnIndex(F_BOOKID)),  
						cursor.getInt(cursor.getColumnIndex(F_CHAPTER)), 
						cursor.getInt(cursor.getColumnIndex(F_VERSE)), 
						cursor.getString(cursor.getColumnIndex(F_VERSETEXT)));
				
				mVerseList.add(verse);
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		
		if (mVerseList.size() > 0)
			return mVerseList;
		
		return null;
	}
	
	/**
	 * @author cledersondotti
	 * @since 2014-04-09
	 * @return Retorna um arraylist contendo os versiculos de determinado livro/capitulo
	 */
	public ArrayList<BibleChapter> getBookChapter(Integer _bookID, Integer maxNumVerse) {
		String cWhere = F_BOOKID + "=? " 
				+ (maxNumVerse != null ? " AND " + F_VERSE + "<=?" : "");
		SQLiteDatabase db = BibleDBHelper.getInstance(mContext).openDatabase();
		Cursor cursor = db.query(TABLE, 
				null,	// columns 
				cWhere,	// where 
				new String[] {_bookID.toString(), String.valueOf(maxNumVerse)},	// where_args
				null, 	// groupBy 
				null, 	// having
				F_BOOKID + "," + F_CHAPTER + "," + F_VERSE);	// orderBy
		
		ArrayList<BibleChapter> mChapterList = new ArrayList<BibleChapter>();
		ArrayList<BibleVerse> mVerseList;
		BibleVerse verse;
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				int id = cursor.getInt(cursor.getColumnIndex(F_ID));
				int bookID = cursor.getInt(cursor.getColumnIndex(F_BOOKID));  
				int chapterNum = cursor.getInt(cursor.getColumnIndex(F_CHAPTER));
				int verseNum = cursor.getInt(cursor.getColumnIndex(F_VERSE));
				String verseText = cursor.getString(cursor.getColumnIndex(F_VERSETEXT));
				mVerseList = new ArrayList<BibleVerse>();
				
				while (!cursor.isAfterLast() && cursor.getInt(cursor.getColumnIndex(F_CHAPTER)) == chapterNum) {
					verse = new BibleVerse(
							id,
							bookID,  
							chapterNum, 
							verseNum, 
							verseText);
					mVerseList.add(verse);
					cursor.moveToNext();
				}
				
				mChapterList.add(new BibleChapter(id, bookID, chapterNum, mVerseList));
				// Se houveram versiculos para este capitulo, move ao proximo
				if (mVerseList.size() <= 0)
					cursor.moveToNext();
			}
		}
		
		cursor.close();
		
		if (mChapterList.size() > 0)
			return mChapterList;
		
		return null;
	}
	
	public ArrayList<BibleVerse> getAllVerse() {
		String cSQL = "SELECT * FROM VERSE ";
		SQLiteDatabase db = BibleDBHelper.getInstance(mContext).openDatabase();
		Cursor cursor = db.rawQuery(cSQL, null);
		
		BibleVerse verse;		
		ArrayList<BibleVerse> mVerseList = new ArrayList<BibleVerse>();
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				int id = cursor.getInt(cursor.getColumnIndex(F_ID));
				int bookID = cursor.getInt(cursor.getColumnIndex(F_BOOKID));  
				int chapterNum = cursor.getInt(cursor.getColumnIndex(F_CHAPTER));
				int verseNum = cursor.getInt(cursor.getColumnIndex(F_VERSE));
				String verseText = cursor.getString(cursor.getColumnIndex(F_VERSETEXT));
				verse = new BibleVerse(
						id,
						bookID,  
						chapterNum, 
						verseNum, 
						verseText);
				
				mVerseList.add(verse);
				
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		
		if (mVerseList.size() > 0)
			return mVerseList;
		
		return null;
	}	
	
	// Nao faz sentido uma funcao de insercao de versiculos
	/*
	public boolean insertF(VerseFav newVerse) {
		SQLiteDatabase db = new BibleDBHelper(mContext).openDatabase();
		ContentValues values = new ContentValues();
		long result = -1;
		
		values.put(F_TYPE, cRegType);
		values.put(F_BOOKID, newVerse.getBookID());
		values.put(F_CHAPTER, newVerse.getNumChapter());
		values.put(F_VERSE, newVerse.getVerseNum());
		values.put(F_VERSETEXT, newVerse.getText());
		
		result = db.insert(TABLE, null, values);		
		
		if (result != -1)
			return true;
		
		return false;
	}
	*/
	

}
