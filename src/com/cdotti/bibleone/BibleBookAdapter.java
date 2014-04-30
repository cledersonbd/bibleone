package com.cdotti.bibleone;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BibleBookAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<BibleBook> mBookArr;
	
	public BibleBookAdapter(Context c, Integer nTestamentID) {		
		if (mContext == null)
			mContext = c;
		
		BibleBookDAO bookDAO = new BibleBookDAO(mContext);
		mBookArr = bookDAO.getAll(nTestamentID);		
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
		Typeface tfRoboto = Typefaces.get(mContext, MainActivity.FONT_MAIN_FONT);
		Float razao = 15f;
		Float textSize = 19f;
		
		// if it's not recycled, initialize some attributes
		if (convertView == null) {
			textView = new TextView(mContext);
			textView.setTypeface(tfRoboto);
			textView.setTextColor(Color.DKGRAY);
			//textView.setMinHeight(textSize.intValue());
			//textView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_textview));
			//textView.setLayoutParams(new GridView.LayoutParams(85, 85));
		} else {
			textView = (TextView) convertView;
		}
		
		if (mBookArr.get(position).getName().trim().length() > razao)
			textSize -= (Float) (mBookArr.get(position).getName().trim().length() - razao);
		
		textView.setText(mBookArr.get(position).getName());
		textView.setTextSize(textSize);
		
		return textView;
	}

}
