package com.cdotti.bibleone;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BibleDBHelper extends SQLiteOpenHelper{

	private Context mContext;
	private static final String DB_FILENAME = "NVI.db";
	public static final String DB_BOOK_TABLE_NAME = "book";
	public static final String DB_TESTAMENT_TABLE_NAME = "testment";
	public static final String DB_VERSES_TABLE_NAME = "verses";
	
	public BibleDBHelper(Context context) {
		super(context, DB_FILENAME, null, 1);
		this.mContext = context;
	}
	
	public SQLiteDatabase openDatabase() {
        File dbFile = mContext.getDatabasePath(DB_FILENAME);
        SQLiteDatabase db;
        
        if (!dbFile.exists()) {
            try {
            	this.getReadableDatabase();
            	
                copyDatabase(dbFile);
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }
        
        db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
        return db;
    }
	
    private void copyDatabase(File dbFile) throws IOException {
        InputStream is = mContext.getAssets().open(DB_FILENAME);
        OutputStream os = new FileOutputStream(dbFile);
 
        byte[] buffer = new byte[1024];
        while (is.read(buffer) > 0) {
            os.write(buffer);
        }
 
        os.flush();
        os.close();
        is.close();
    }

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
