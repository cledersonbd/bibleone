package com.cdotti.bibleone;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;

public class VerseFragment extends Fragment implements OnItemClickListener {
	private TextView textView;
	private ListView listVerse;
	private Integer bookID;
	private Integer chapterNum;
	private Integer currentChapterNum;
	private ProgressDialog mDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_verse, container);
		
		Bundle extras = getArguments();
		bookID = extras.getInt("bookID");
		chapterNum = extras.getInt("chapterNum");
		currentChapterNum = chapterNum;
		String bookName = extras.getString("titleName");
		
		//this.setTitle(bookName);
		
		textView = (TextView) view.findViewById(R.id.lblVerseListHeaderText);
		textView.setText(getResources().getString(R.string.chapterLabel) + " " + chapterNum.toString());
		
		listVerse = (ListView) view.findViewById(R.id.listVerse);
		listVerse.setAdapter(new BibleVerseAdapter(getActivity().getApplicationContext(), bookID, chapterNum));
		listVerse.setOnItemClickListener(this);

		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parentView, View view, int arg2, long arg3) {
		TextView txtNumView = (TextView) view.findViewById(R.id.lblVerseNum);
		
		if (txtNumView != null)
			Toast.makeText(getActivity().getApplicationContext(), txtNumView.getText(), Toast.LENGTH_SHORT).show();
	}
}
