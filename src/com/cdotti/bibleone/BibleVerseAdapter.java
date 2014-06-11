package com.cdotti.bibleone;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.ViewFlipper;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BibleVerseAdapter extends BaseAdapter implements SectionIndexer, StickyListHeadersAdapter {
	private Context mContext;
	private ArrayList<BibleVerse> mVerseArrList;
	private HashMap<Integer, Integer> indexer;
	private Integer[] sections;
	private AtomicBoolean isLoading = new AtomicBoolean(true);
	private Integer currentChapNum;
	private Integer numBookID;
	private Integer numChapterID;
	
	private static final Integer DIV_PAGE = 3;
	private static final int PENDING_VIEW_TYPE = 0;
	private static final int HEADER_VIEW_TYPE = 1;
	private static final int ITEM_VIEW_TYPE = 1;
	
	private static final int NUM_TOTAL_VIEWS = 2;
	
	private OnTouchListener mTouchListenerFallBack;
	
	// Para os itens	
	private static class ViewHolderHeader {
		TextView chapterLabel;
		TextView chapterNumber;
	}
		
	// Para os itens	
	protected static class ViewHolderItem {
		ViewFlipper viewF;
		TextView verseNum;
		TextView verseText;
		// Botoes de opcao
		Button btnOptFav;
		Button btnOptMarkRead;
	}
	
	// Construtor quando se passam os IDs do livro e capítulo
	public BibleVerseAdapter(Context c, Integer nBookID, Integer nChapterID) {		
		if (mContext == null)
			mContext = c;
		
		numBookID = nBookID;
		numChapterID = nChapterID;
		currentChapNum = numChapterID;
		
		BibleVerseDAO verseDAO = new BibleVerseDAO(mContext);
		mVerseArrList = verseDAO.getAllBookChapter(nBookID, nChapterID);
		indexer = new HashMap<Integer, Integer>();
		
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
			Integer item = mVerseArrList.get(i).getNumVerse();
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
			id = mVerseArrList.get(position).getNumVerse();
		return id;
	}	
	
	@Override
	// create a new TextView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolderItem vHolder;
		View pendingView;
		Typeface tfRobotoThin;
		Typeface tfRobotoThick;
		
		// Se estiver na última posição e deve carregar, executa carga e retorna pendingView
		if (position == (getCount() - 1) && isLoading.get()) {
			pendingView = inflater.inflate(R.layout.verse_list_pending_row, null);
			// Carrega mais versiculos
			new LoadMore(numBookID, ++currentChapNum).execute();
			return pendingView;
		}
		else {
			BibleVerse verse = mVerseArrList.get(position);
			
			// if it's not recycled, initialize some attributes
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.verse_list_row, null);
				tfRobotoThin = Typefaces.get(mContext, MainActivity.FONT_ROBOTO_100_PATH);
				tfRobotoThick = Typefaces.get(mContext, MainActivity.FONT_MAIN_FONT);
				
				vHolder = new ViewHolderItem();
				vHolder.viewF = (ViewFlipper) convertView.findViewById(R.id.viewFlipperVerseRow);
				
				vHolder.verseNum = (TextView) convertView.findViewById(R.id.lblVerseListRowVerseNum);
				vHolder.verseNum.setTypeface(tfRobotoThick);
				vHolder.verseText = (TextView) convertView.findViewById(R.id.lblVerseListRowVerseDesc);
				vHolder.verseText.setTypeface(tfRobotoThin);
				
				vHolder.btnOptFav = (Button) convertView.findViewById(R.id.btnOptFav);
				vHolder.btnOptFav.setOnClickListener(new OptButtonsListener(mContext));
				
				vHolder.btnOptMarkRead = (Button) convertView.findViewById(R.id.btnOptMarkRead);
				
				// guarda o viewHolder dentro da view
				convertView.setTag(vHolder);
			} else
				vHolder = (ViewHolderItem) convertView.getTag();
			
			vHolder.verseNum.setText(verse.getNumVerse().toString());
			vHolder.verseText.setText(verse.getText());
			// Seta o objeto versiculo como tag do botao
			vHolder.btnOptFav.setTag(verse);
			vHolder.btnOptMarkRead.setTag(verse);	
		}
		
		return convertView;
	}
	
	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		ViewHolderHeader vHolder;
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.verse_list_header, null);
			vHolder = new ViewHolderHeader();
			vHolder.chapterLabel = (TextView) convertView.findViewById(R.id.lblVerseListHeaderChapterLabel);
			vHolder.chapterNumber = (TextView) convertView.findViewById(R.id.lblVerseListHeaderChapterNum);
			
			vHolder.chapterLabel.setTypeface(Typefaces.get(mContext, MainActivity.FONT_MAIN_FONT_TITLE));
			vHolder.chapterNumber.setTypeface(Typefaces.get(mContext, MainActivity.FONT_MAIN_FONT));
			
			convertView.setTag(vHolder);
		} else 
			vHolder = (ViewHolderHeader) convertView.getTag();
		
		vHolder.chapterNumber.setText(mVerseArrList.get(position).getNumChapter().toString());
		
		return convertView;
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
	
	// Inner class para execução em background
	private class LoadMore extends AsyncTask<Void, Integer, Exception> {
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
			ArrayList<BibleVerse> mNewVerseList;
			
			// Busca todos os versiculos do proximo capitulo
			BibleVerseDAO verseDAO = new BibleVerseDAO(mContext);
			mNewVerseList = verseDAO.getAllBookChapter(this.bookID, this.chapNum);
			
			if (mNewVerseList != null)
				mVerseArrList.addAll(mNewVerseList);
			else {
				Log.e(BibleVerseAdapter.class.getName(), "None result was brought!");
				return new Exception("None result was brought!");
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
