package com.cdotti.bibleone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.content.Context;
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
	private HashMap<Integer, Integer> indexer;
	private Integer[] sections;
	private static final Integer MAX_VERSES_PER_CHAPTER = 3;
	private static final Integer DIV_PAGE = 10;
	
	// Classe ViewHolder para manter as views a serem trabalhadas
	private static class ViewHolder {
		TextView chapterNum;
		TextView chapterText;
	}
	
	public BibleChapterAdapter(Context c, Integer nBookID) {
		if (mContext == null)
			mContext = c;
		
		BibleVerseDAO verseDAO = new BibleVerseDAO(mContext);
		mChapterArr = verseDAO.getBookChapter(nBookID, MAX_VERSES_PER_CHAPTER);
		indexer = new HashMap<Integer, Integer>();

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
		ViewHolder vHolder;
		String cText = "";
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Typeface tfThin = Typefaces.get(mContext, MainActivity.FONT_ROBOTO_100_PATH);
		Typeface tfThick = Typefaces.get(mContext, MainActivity.FONT_MAIN_FONT);
		ArrayList<BibleVerse> chapVerses = mChapterArr.get(position).getArrListText();
		
		// if it's not recycled, initialize some attributes
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.chapter_list_row, null);
			vHolder = new ViewHolder();
			vHolder.chapterNum = (TextView) convertView.findViewById(R.id.lblChapterNum);
			vHolder.chapterNum.setTypeface(tfThick);
			vHolder.chapterNum.setTextColor(Color.BLACK);
			
			vHolder.chapterText = (TextView) convertView.findViewById(R.id.lblChapterTextView);
			vHolder.chapterText.setTypeface(tfThin);
			vHolder.chapterText.setTextColor(Color.DKGRAY);
			
			convertView.setTag(vHolder);
		} else
			vHolder = (ViewHolder) convertView.getTag();
		
		for (int i = 0; i < chapVerses.size() ; i++)
			cText = cText.concat(chapVerses.get(i).getText());
		
		vHolder.chapterNum.setText(mChapterArr.get(position).getChapter().toString());
		vHolder.chapterText.setText(cText);
		
		return convertView;
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
