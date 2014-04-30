package com.cdotti.bibleone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class VerseFavAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private ArrayList<VerseFav> mVerseArrList;
	private HashMap<Integer, Integer> indexer;
	private Integer[] sections;
	private String cType;
	
	private static final Integer DIV_PAGE = 3;
	private static final int PENDING_VIEW_TYPE = 0;
	private static final int HEADER_VIEW_TYPE = 1;
	private static final int ITEM_VIEW_TYPE = 1;
	
	private static final int NUM_TOTAL_VIEWS = 2;
	
	private OnTouchListener mTouchListenerFallBack;
	
	private static class ViewHolderItem {
		TextView bookName;
		TextView chapterNum;
		TextView verseNum;
		TextView verseText;
	}
	
	// Construtor quando se passam os IDs do livro e capítulo
	public VerseFavAdapter(Context c, String cVerseType) {		
		
		if (mContext == null)
			mContext = c;
		
		cType = cVerseType;
		
		FavVerseDAO favDAO = new FavVerseDAO(mContext, cType);
		mVerseArrList = favDAO.getAllF();		
		
	}
	
	// Construtor quando se passa os versículos já prontos
	public VerseFavAdapter(Context c, ArrayList<VerseFav> aVerseList) {		
		if (mContext == null)
			mContext = c;
		
		mVerseArrList = aVerseList;
	}
	
	/**
	 * Função utilizada para construir as seções (capítulos) da listView
	 */
	private void buildSections() {
		for (int i = 0; i < mVerseArrList.size(); i++) {
			Integer item = mVerseArrList.get(i).getVerseNum();
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
	public int getItemViewType(int position) {
		int nViewType = IGNORE_ITEM_VIEW_TYPE;
		
		// Se a posicao for maior que o tamanho do array (pendingView)
		if (position >= mVerseArrList.size())
			nViewType = PENDING_VIEW_TYPE;
		// Se for um cabecalho
		else if (mVerseArrList.get(position).isHeader())
			nViewType = HEADER_VIEW_TYPE;
		// Senao so pode ser um versiculo
		else
			nViewType = ITEM_VIEW_TYPE;

		return nViewType;
	}
	
	@Override
	public int getViewTypeCount() {
		return NUM_TOTAL_VIEWS;
	}
	
	@Override
	public int getCount() {
		if (mVerseArrList != null)
			return mVerseArrList.size();
		return 0;
	}

	@Override
	public VerseFav getItem(int position) {
		VerseFav oItem = null;
		if (position < mVerseArrList.size())
			oItem = mVerseArrList.get(position); 
		return oItem;
	}

	@Override
	public long getItemId(int position) {
		Integer id = null;
		if (position < mVerseArrList.size())
			id = mVerseArrList.get(position).getVerseNum();
		return id;
	}	
	
	@Override
	// create a new TextView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Typeface tf = Typefaces.get(mContext, "fonts/exo200.ttf");
		ViewHolderItem vHolder;
		
		// if it's not recycled, initialize some attributes
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.fav_verse_row, null);
			
			vHolder = new ViewHolderItem();
			vHolder.bookName = (TextView) convertView.findViewById(R.id.lblFavVerseBookName);
			
			vHolder.chapterNum = (TextView) convertView.findViewById(R.id.lblFavVerseChapterNum);
			vHolder.chapterNum.setTypeface(tf);
			vHolder.verseNum = (TextView) convertView.findViewById(R.id.lblFavVerseRowVerseNum);
			vHolder.verseNum.setTypeface(tf);
			vHolder.verseText = (TextView) convertView.findViewById(R.id.lblFavVerseRowVerseDesc);
			vHolder.verseText.setTypeface(tf);
			
			convertView.setTag(vHolder);
		}
		else
			vHolder = (ViewHolderItem) convertView.getTag();
		
		vHolder.bookName.setText(mVerseArrList.get(position).getBookName());
		vHolder.chapterNum.setText(mVerseArrList.get(position).getNumChapter().toString());
		vHolder.verseNum.setText(mVerseArrList.get(position).getVerseNum().toString());
		vHolder.verseText.setText(mVerseArrList.get(position).getText());
		
		return convertView;
	}
	/*
	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		View customView;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			customView = inflater.inflate(R.layout.verse_list_header, null);
		} else
			customView = convertView;
		
		TextView verseChapterNumber = (TextView) customView.findViewById(R.id.lblVerseListHeaderChapterNum);
		
		if (verseChapterNumber != null) {
			verseChapterNumber.setText(mVerseArrList.get(position).getNumChapter().toString());
		}
		
		return customView;
	}

	@Override
	public long getHeaderId(int position) {
		int numChap;
		
		if (position >= mVerseArrList.size())
			numChap = mVerseArrList.get(position - 1).getNumChapter();
		else	
			numChap = mVerseArrList.get(position).getNumChapter();
		
		return numChap;
	}
	
	*/
	
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
	
	public boolean setOnTouchReceiver(OnTouchListener touchListener) {
		if (mTouchListenerFallBack == null) {
			mTouchListenerFallBack = touchListener;
			return true;
		}
		return false;
	}

}
