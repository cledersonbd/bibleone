package com.cdotti.bibleone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.content.Intent;

public class ChapterFragment extends Fragment implements OnItemClickListener {
	private ListView listChapter;
	private TextView textView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.activity_chapter, container);
		
		Integer chapter_id = 1;
		
		Bundle extras = getArguments();
		if (extras != null) {
			chapter_id = extras.getInt("bookID");
			String bookName = extras.getString("bookName");
			
			textView = (TextView) view.findViewById(R.id.lblBookName);
			textView.setText(bookName);
		}
		
		// Binding utilizado nas paradas
		listChapter = (ListView) view.findViewById(R.id.listBookVerse);
		listChapter.setAdapter(new BibleChapterAdapter(getActivity().getApplicationContext(), chapter_id.toString()));
		listChapter.setOnItemClickListener(this);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	 // Listener para os cliques nos capitulos da biblia
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		Intent intent = new Intent(getActivity().getApplicationContext(), VerseActivity.class);
		
		intent.putExtra("bookID", ((BibleChapterAdapter) listChapter.getAdapter()).getItem(position).getBook_id());
		intent.putExtra("chapterNum", ((BibleChapterAdapter) listChapter.getAdapter()).getItem(position).getChapter());
		intent.putExtra("titleName", getActivity().getTitle());
		//intent.putParcelableArrayListExtra("com.cdotti.bibleone.BibleVerse", ((BibleChapterAdapter) listChapter.getAdapter()).getItem(position).getArrListText());
		
		startActivity(intent);
	}
}
