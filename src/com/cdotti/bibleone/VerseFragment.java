package com.cdotti.bibleone;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VerseFragment extends Fragment implements OnItemClickListener, OnTouchListener {
	private ImageView imgRibbon;
	private TextView textView;
	private ListView listVerse;
	private Integer bookID;
	private Integer chapterNum;
	
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
		
		//this.setTitle(bookName);
		
		textView = (TextView) view.findViewById(R.id.lblVerseListHeaderText);
		textView.setText(getResources().getString(R.string.chapterLabel) + " " + chapterNum.toString());
		
		listVerse = (ListView) view.findViewById(R.id.listVerse);
		listVerse.setAdapter(new BibleVerseAdapter(getActivity().getApplicationContext(), bookID, chapterNum));
		listVerse.setOnItemClickListener(this);

		imgRibbon = (ImageView) view.findViewById(R.id.imgRibbon);
		imgRibbon.setOnTouchListener(this);
		
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parentView, View view, int arg2, long arg3) {
		TextView txtNumView = (TextView) view.findViewById(R.id.lblVerseNum);
		
		//if (txtNumView != null)
		//	Toast.makeText(getActivity().getApplicationContext(), txtNumView.getText(), Toast.LENGTH_SHORT).show();
		
		mOnSelectedVerseCallback.onSelectedVerse(Integer.valueOf((String) txtNumView.getText()));
	}

	float downX, downY;
	int totalX, totalY;
	int scrollByX, scrollByY;
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		float currentX, currentY;
		
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:
			downX = event.getX();
			downY = event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			currentX = event.getX();
			currentY = event.getY();
			scrollByX = (int) (downX - currentX);
			scrollByY = (int) (downY + currentY);
			
			((ImageView) v).setMinimumHeight(scrollByY);
			v.invalidate();
			
			//Toast.makeText(getActivity().getApplicationContext(), "Moveu", Toast.LENGTH_SHORT).show();
			break;
			
		default:
			break;
		}
		
		return true;
	}
}
