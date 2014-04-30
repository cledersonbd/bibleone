package com.cdotti.bibleone;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VerseFragment extends Fragment implements OnItemClickListener, OnSharedPreferenceChangeListener {
	private StickyListHeadersListView listVerse;
	private Integer bookID;
	private Integer chapterNum;
	private Integer versePosToGo = -1;
	
	protected static final String CLASS_TAG = "VerseFragment";
	
	OnSelectedVerseListener mOnSelectedVerseCallback;

	public interface OnSelectedVerseListener {
		public void onSelectedVerse(Integer verseNum);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
		try {
			mOnSelectedVerseCallback = (OnSelectedVerseListener) activity;
		} catch (ClassCastException e) {
			Log.e(this.getClass().toString(), "Atividade nao implementou a interface OnSelectedVerseListener");
			throw new ClassCastException(activity.toString()
                    + " must implement OnSelectedVerseListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.activity_verse, container, false);
		
		Bundle extras = getArguments();
		bookID = extras.getInt("bookID");
		chapterNum = extras.getInt("chapterNum");
		versePosToGo = extras.getInt("verseNum") - 1;	// Posicao e o numero do versiculo menos 1
		
		BibleVerseAdapter verseAdapter = new BibleVerseAdapter(getActivity().getApplicationContext(), bookID, chapterNum);
		
		listVerse = (StickyListHeadersListView) view.findViewById(R.id.listVerse);
		listVerse.setAdapter(verseAdapter);
		listVerse.setOnTouchListener(new VerseListGestureListener(getActivity().getApplicationContext()));
	
		// Se existe um versiculo para "rolar para", 'agenda' uma a‹o 
		if (listVerse != null && versePosToGo >= 0) {
			listVerse.post(new Runnable() {
				
				@SuppressLint("NewApi")
				@Override
				public void run() {
					View view;
					listVerse.smoothScrollToPosition(versePosToGo);

					view = listVerse.getChildAt(versePosToGo);
					if (view != null) {
						ObjectAnimator oa = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f, 1.0f);
						oa.start();
					}

				}
			});
		}
		
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parentView, View view, int arg2, long arg3) {
		TextView txtNumView = (TextView) view.findViewById(R.id.lblVerseListRowVerseNum);
		Integer nVerseNum = null;
		
		if (txtNumView != null) {
			nVerseNum = Integer.valueOf((String) txtNumView.getText());
			mOnSelectedVerseCallback.onSelectedVerse(nVerseNum);
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		
	}
	
}
