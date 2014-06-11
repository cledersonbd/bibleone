package com.cdotti.bibleone;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FavVerseDAO {
	
	protected static final String TABLE = "savedHistory";
	protected static final String F_ID = "id";
	protected static final String F_TYPE = "type";
	protected static final String F_BOOKID = "book_id";
	protected static final String F_CHAPTERID = "chapter_id";
	protected static final String F_VERSEID = "verse_id";
	protected static final String F_VERSETEXT = "verse_text";
	protected static final String F_DATEADD = "dateadd";
	
	private static final String F_OUTSIDER_BOOKNAME = "book_name";
			
	private String cRegType;	// Define o tipo do favorito (Favorito ou leitura marcada)
	
	private Context mContext;
	
	public FavVerseDAO(Context c) {
		mContext = c;
		cRegType = "F";
	}
	
	public FavVerseDAO(Context c, String type) {
		mContext = c;
		cRegType = type;
	}
	
	/**
	 * @author cledersondotti
	 * @since 2014-04-09
	 * @return Retorna um arraylist contendo os versiculos favoritos gravados em banco
	 */
	public ArrayList<VerseFav> getAllF() {
		SQLiteDatabase db = BibleDBHelper.getInstance(mContext).openDatabase();
		/*
		Cursor cursor = db.query(TABLE, 
				null, TYPE+" LIKE ? " , new String[] {"%"+cRegType+"%"}, 
				null, 	// groupBy 
				null, 	// having
				DATEADD + "," + BOOKID + "," + CHAPTERID);	// orderBy
		*/
		Cursor cursor = db.rawQuery(
				"SELECT " 
				+ TABLE + "." + F_ID + "," 
				+ F_BOOKID + ","
				+ "book.name as "+F_OUTSIDER_BOOKNAME+","
				+ F_CHAPTERID + ","
				+ F_VERSEID + ","
				+ F_VERSETEXT 
				+ " FROM " + TABLE + " INNER JOIN book ON ( "+TABLE+"."+F_BOOKID+" = book.id )" 
				+ " WHERE " + F_TYPE + " LIKE ?"
				+ " ORDER BY "
				+ F_DATEADD + "," + F_BOOKID + "," + F_CHAPTERID, new String[] {"%"+cRegType+"%"});
		
		ArrayList<VerseFav> mFavList = new ArrayList<VerseFav>();
		VerseFav verse;
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				verse = new VerseFav(
						cursor.getInt(cursor.getColumnIndex(F_ID)), 
						cursor.getInt(cursor.getColumnIndex(F_BOOKID)), 
						cursor.getString(cursor.getColumnIndex(F_OUTSIDER_BOOKNAME)), 
						cursor.getInt(cursor.getColumnIndex(F_CHAPTERID)), 
						cursor.getInt(cursor.getColumnIndex(F_VERSEID)), 
						cursor.getString(cursor.getColumnIndex(F_VERSETEXT)));
				
				mFavList.add(verse);
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		
		if (mFavList.size() > 0)
			return mFavList;
		
		return null;
	}
	
	public boolean insertF(VerseFav newVerse) {
		SQLiteDatabase db = BibleDBHelper.getInstance(mContext).openDatabase();
		ContentValues values = new ContentValues();
		long result = -1;
		
		values.put(F_TYPE, cRegType);
		values.put(F_BOOKID, newVerse.getBookID());
		values.put(F_CHAPTERID, newVerse.getNumChapter());
		values.put(F_VERSEID, newVerse.getVerseNum());
		values.put(F_VERSETEXT, newVerse.getText());
		values.put(F_DATEADD, new Date().toString());
		
		result = db.insert(TABLE, null, values);		
		
		if (result != -1)
			return true;
		
		return false;
	}
	

}
