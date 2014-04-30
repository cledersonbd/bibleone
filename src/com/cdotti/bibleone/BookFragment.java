package com.cdotti.bibleone;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class BookFragment extends Fragment implements OnItemClickListener {
	private GridView gridNewTest;
	private GridView gridOldTest;
	
	OnBookSelectedListener mBookSelectedCallback;
	
	// Container Activity must implement this interface
	public interface OnBookSelectedListener {
		public void onBookSelected(Integer bookID, String bookName);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
		try {
			mBookSelectedCallback = (OnBookSelectedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
                    + " must implement OnBookSelectedListener");
		}
	}
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.activity_book, container, false);
		
        // Binding utilizado nas paradas
        gridOldTest = (GridView) view.findViewById(R.id.gridOldTest);
        gridOldTest.setAdapter(new BibleBookAdapter(getActivity().getApplicationContext(), BibleBookDAO.OLD_TESTAMENT));

        // Binding utilizado nas paradas
        gridNewTest = (GridView) view.findViewById(R.id.gridNewTest);
        gridNewTest.setAdapter(new BibleBookAdapter(getActivity().getApplicationContext(), BibleBookDAO.NEW_TESTAMENT));
		
		// Configura o ItemClick function (nesta mesma classe)
		gridOldTest.setOnItemClickListener(this);
		gridNewTest.setOnItemClickListener(this);
		
		return view;
	}

    // Listener para os cliques nos livros da biblia (Velho/Novo Testamento)
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		//Intent intent = new Intent(parent.getContext(), ChapterActivity.class);
		Integer bookID = 0;
		String bookName = "";
		
		if (parent.getId() == R.id.gridOldTest) {
			bookID = ((BibleBookAdapter) gridOldTest.getAdapter()).getItem(position).getId();
			bookName = ((BibleBookAdapter) gridOldTest.getAdapter()).getItem(position).getName();
		}
		else if (parent.getId() == R.id.gridNewTest) {
			bookID = ((BibleBookAdapter) gridNewTest.getAdapter()).getItem(position).getId();
			bookName = ((BibleBookAdapter) gridNewTest.getAdapter()).getItem(position).getName();
		}
		/*
		
		intent.putExtra("bookID", bookID);
		intent.putExtra("bookName", bookName);
		
		startActivity(intent);
		*/
		
		// Send the event to the host activity
		mBookSelectedCallback.onBookSelected(bookID, bookName);
	}
}
