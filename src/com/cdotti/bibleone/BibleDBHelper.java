package com.cdotti.bibleone;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BibleDBHelper extends SQLiteOpenHelper{

	private Context mContext;
	
	private static BibleDBHelper mInstance = null;
	
	private static SQLiteDatabase mDB = null;
	
	private static final int DB_VERSION = 1;
	
	private static final String CLASS_TAG = "BibleDBHelper";
	
	private static final String DB_FILENAME = "NVI.db";
	public static final String DB_BOOK_TABLE_NAME = "book";
	public static final String DB_TESTAMENT_TABLE_NAME = "testment";
	public static final String DB_VERSES_TABLE_NAME = "verses";
	
	// Unico metodo que deve ser chamado para buscar instancia
	// Garante o "Singleton" da conexao
	public static BibleDBHelper getInstance(Context c) {
		if (mInstance == null)
			mInstance = new BibleDBHelper(c);
		
		return mInstance;
	}
	
	private BibleDBHelper(Context context) {
		super(context, DB_FILENAME, null, DB_VERSION);
		mContext = context;
	}
	
	public SQLiteDatabase openDatabase() {
        File dbFile = mContext.getDatabasePath(DB_FILENAME);
        
        if (mDB == null) {
	        if (!dbFile.exists()) {
	            try {
	            	// Essa funcao faz a magica de criar o path para copia :|  entender isso aqui pelamor!
	            	getWritableDatabase();
	            	
	                copyDatabase(dbFile);
	                mDB = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
	                mDB.setVersion(DB_VERSION);
	                mDB.close();
	            } catch (IOException e) {
	                throw new RuntimeException("Error creating source database", e);
	            }
	        } else {
	        	mDB = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
	        	int currVersion = mDB.getVersion();
	        	mDB.close();
	        	// Precisa de update
	            if (currVersion < DB_VERSION) {
	            	mContext.deleteDatabase(DB_FILENAME);
	            	try {
						copyDatabase(dbFile);
						mDB = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
						mDB.setVersion(DB_VERSION);
						mDB.close();
					} catch (IOException e) {
						throw new RuntimeException("Error UPDATING database", e);
					}
	            }
	            
	        }
	        mDB = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
	        Log.i(CLASS_TAG, "DB version: " + mDB.getVersion());
        }
        
        return mDB;
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
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		File dbFile = mContext.getDatabasePath(DB_FILENAME);
		
		Log.i(CLASS_TAG, "Database - oldversion: " + String.valueOf(oldVersion) + " | newVersion: " + String.valueOf(newVersion));
		
		if (newVersion > oldVersion) {
			mContext.deleteDatabase(DB_FILENAME);
			try {
				copyDatabase(dbFile);
			} catch (IOException e) {
				throw new RuntimeException("Error REcreating source database", e);
			}
		}
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}
}
