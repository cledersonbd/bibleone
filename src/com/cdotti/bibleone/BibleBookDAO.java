package com.cdotti.bibleone;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BibleBookDAO {
	
	protected static final String TABLE = "book";
	protected static final String F_ID = "id";
	protected static final String F_NAME = "name";
	protected static final String F_TESTAMENTID = "testament_id";
	protected static final String F_ABBREVIATION = "abbreviation";
	
	public static final Integer OLD_TESTAMENT = 1;
	public static final Integer NEW_TESTAMENT = 2;
	
	private Context mContext;
	
	public BibleBookDAO(Context c) {
		mContext = c;
	}
	
	/**
	 * @author cledersondotti
	 * @since 2014-04-09
	 * @return Retorna um arraylist contendo os livros da biblia
	 */
	public ArrayList<BibleBook> getAll(Integer _testmentID) {
		SQLiteDatabase db = BibleDBHelper.getInstance(mContext).openDatabase();
		Cursor cursor = db.query(TABLE, 
				new String[] {F_ID, F_NAME},	// columns 
				F_TESTAMENTID + "=?" ,			// where 
				new String[] {_testmentID.toString()},	// where_args
				null, 	// groupBy 
				null, 	// having
				F_TESTAMENTID);	// orderBy
		
		ArrayList<BibleBook> mBookList = new ArrayList<BibleBook>();
		BibleBook verse;
		
		if (cursor.moveToFirst()) {
			while (!cursor.isAfterLast()) {
				verse = new BibleBook( 
						cursor.getInt(cursor.getColumnIndex(F_ID)),  
						cursor.getString(cursor.getColumnIndex(F_NAME)));
				
				mBookList.add(verse);
				cursor.moveToNext();
			}
		}
		
		cursor.close();
		
		if (mBookList.size() > 0)
			return mBookList;
		
		return null;
	}
	

}
