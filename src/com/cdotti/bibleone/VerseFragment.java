package com.cdotti.bibleone;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
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
public class VerseFragment extends Fragment implements OnItemClickListener {
	private ImageView imgRibbon;
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
		
		BibleVerseAdapter verseAdapter = new BibleVerseAdapter(getActivity().getApplicationContext(), bookID, chapterNum);
		
		listVerse = (ListView) view.findViewById(R.id.listVerse);
		listVerse.setAdapter(verseAdapter);
		listVerse.setOnItemClickListener(this);

		imgRibbon = (ImageView) view.findViewById(R.id.imgRibbon);
		imgRibbon.setOnTouchListener(new MyDragClassListener());
		
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

	private static class MyDragClassListener implements OnTouchListener {

		private Matrix matrix = new Matrix();
		
		private PointF start = new PointF();
		private PointF lastPosition = new PointF();
		
		private int mActivePointer = MotionEvent.INVALID_POINTER_ID;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			ImageView img = (ImageView) v;
			
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			
				case MotionEvent.ACTION_DOWN:
					start.x = event.getX();
					start.y = event.getY();
					lastPosition.set(start);
					break;
				case MotionEvent.ACTION_MOVE:
					matrix.postTranslate(0, event.getY() - lastPosition.y);
					lastPosition.set(event.getX(), event.getY());
					break;
					
				default:
					break;
			}
			img.setImageMatrix(matrix);
			
			return true;
		}
		
	}
	
}
