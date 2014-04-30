package com.cdotti.bibleone;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class EndlessScrollListener implements OnScrollListener {
	private int previousTotal = 0;
	private int visibleThreshold = 0;
	private boolean isLoading = true;
	
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
