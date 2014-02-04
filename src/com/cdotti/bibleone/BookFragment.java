package com.cdotti.bibleone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class BookFragment extends Fragment implements OnItemClickListener {
	private GridView gridNewTest;
	private GridView gridOldTest;
	
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.activity_main, container, false);
		
        // Binding utilizado nas paradas
        gridOldTest = (GridView) view.findViewById(R.id.gridOldTest);
        gridOldTest.setAdapter(new BibleBookAdapter(container.getContext(), "1"));

        // Binding utilizado nas paradas
        gridNewTest = (GridView) view.findViewById(R.id.gridNewTest);
        gridNewTest.setAdapter(new BibleBookAdapter(container.getContext(), "2"));
		
		// Configura o ItemClick function (nesta mesma classe)
		gridOldTest.setOnItemClickListener(this);
		gridNewTest.setOnItemClickListener(this);
		
		return view;
	}

	@Override
	public void onPause() {
		
		super.onPause();
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

    // Listener para os cliques nos livros da biblia (Velho/Novo Testamento)
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent(parent.getContext(), ChapterActivity.class);
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
		
		intent.putExtra("bookID", bookID);
		intent.putExtra("bookName", bookName);
		
		startActivity(intent);
	}
}
