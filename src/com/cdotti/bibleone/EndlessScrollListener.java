package com.cdotti.bibleone;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.Toast;

public class EndlessScrollListener implements OnScrollListener {
	private Context mContext;
	private ListAdapter mAdapter;
	private int previousTotal = 0;
	private int visibleThreshold = 0;
	private boolean isLoading = true;
	
	public EndlessScrollListener(Context c) {
		mContext = c;
	}
	
	public EndlessScrollListener(Context c, ListAdapter b) {
		mContext = c;
		mAdapter = b;
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		if (isLoading) {
			// Se o total de itens Ž maior que o total anterior, carregou
			if (totalItemCount > previousTotal) {
				isLoading = false;
				previousTotal = totalItemCount;
			}
		}
		if (!isLoading) {
			if ( visibleItemCount > 0 && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				//Toast.makeText(mContext, "Loading more...", Toast.LENGTH_SHORT).show();
				isLoading = true;
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}
}
