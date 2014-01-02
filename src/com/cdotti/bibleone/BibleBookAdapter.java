package com.cdotti.bibleone;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BibleBookAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BibleBook> mBookArr;
	private BibleDBHelper bibleDBHelper;
	private SQLiteDatabase bibleDB;
	
	public BibleBookAdapter(Context c, String cTipoTestamento) {
		Cursor cursor;
		
		if (mContext == null)
			mContext = c;
		
		bibleDBHelper = new BibleDBHelper(mContext);
		bibleDB = bibleDBHelper.openDatabase();
		mBookArr = new ArrayList<BibleBook>();
		
		cursor = bibleDB.rawQuery("SELECT id,name FROM book WHERE testament_id = ?", new String[] {cTipoTestamento});
		try {
			if (cursor.moveToFirst()) {
				while (!cursor.isLast()) {
					mBookArr.add(new BibleBook(cursor.getInt(0), cursor.getString(1)));
					cursor.moveToNext();
				}
			}	
		} finally {
			cursor.close();
		}
		
	}
	
	@Override
	public int getCount() {
		return mBookArr.size();
	}

	@Override
	public BibleBook getItem(int position) {
		return mBookArr.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mBookArr.get(position).getId();
	}

	@Override
	// create a new TextView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;
		Typeface tf = Typefaces.get(mContext, "fonts/exo200.ttf");
		Float razao = 15f;
		Float textSize = 19f;
		
		// if it's not recycled, initialize some attributes
		if (convertView == null) {
			textView = new TextView(mContext);
			//textView.setLayoutParams(new GridView.LayoutParams(85, 85));
		} else {
			textView = (TextView) convertView;
		}
		
		if (mBookArr.get(position).getName().trim().length() > razao)
			textSize -= (Float) (mBookArr.get(position).getName().trim().length() - razao);
		
		textView.setText(mBookArr.get(position).getName());
		textView.setTypeface(tf);
		textView.setTextColor(Color.DKGRAY);
		textView.setTextSize(textSize);
		//textView.setMinHeight(textSize.intValue());
		//textView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_textview));
		
		return textView;
	}

}
