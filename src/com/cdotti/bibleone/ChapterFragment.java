package com.cdotti.bibleone;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ChapterFragment extends Fragment implements OnItemClickListener {
	private ListView listChapter;
	private TextView textView;
	OnChapterSelectedListener mChapterSelectedCallback;
	
	// Container Activity must implement this interface
	public interface OnChapterSelectedListener {
		public void onChapterSelected(Integer bookid, Integer chapterid);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
        	mChapterSelectedCallback = (OnChapterSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnChapterSelectedListener");
        }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.activity_chapter, container, false);
		
		Integer nBookID = 1;
		
		Bundle extras = getArguments();
		if (extras != null) {
			nBookID = extras.getInt("bookID");
			String bookName = extras.getString("bookName");
			/*
			textView = (TextView) view.findViewById(R.id.lblBookName);
			textView.setText(bookName);
			*/
		}
		
		// Binding utilizado nas paradas
		listChapter = (ListView) view.findViewById(R.id.listBookVerse);
		listChapter.setAdapter(new BibleChapterAdapter(getActivity().getApplicationContext(), nBookID));
		listChapter.setOnItemClickListener(this);
		return view;
	}

	 // Listener para os cliques nos capitulos da biblia
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		BibleChapterAdapter chapterAdapter = ((BibleChapterAdapter) listChapter.getAdapter());
		BibleChapter bookChapter = chapterAdapter.getItem(position);
		
		// Send the event to the host activity
        mChapterSelectedCallback.onChapterSelected(bookChapter.getBook_id(), bookChapter.getChapter());
	}
	
	public void updateContent(Integer bookID) {
		
		listChapter.setAdapter(new BibleChapterAdapter(getActivity().getApplicationContext(), bookID));
		listChapter.invalidate();
	}
}
