package com.cdotti.bibleone;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class VerseFavFragment extends Fragment implements OnItemClickListener {
	private ListView listVerse;
	private String savedType;
	
	OnSelectedFavVerseListener mOnSelectedFavVerseCallback;

	public interface OnSelectedFavVerseListener {
		public void onSelectedFavVerse(VerseFav oVerse);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		// This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
		try {
			mOnSelectedFavVerseCallback = (OnSelectedFavVerseListener) activity;
		} catch (ClassCastException e) {
			Log.e(this.getClass().toString(), "Atividade nao implementou a interface OnSelectedFavVerseListener");
			throw new ClassCastException(activity.toString()
                    + " must implement OnSelectedFavVerseListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		TextView txtNoFavs = new TextView(getActivity().getApplicationContext());
		txtNoFavs.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		txtNoFavs.setText("Você ainda não possui versículos favoritos.");
		txtNoFavs.setGravity(Gravity.CENTER);
		txtNoFavs.setTextColor(Color.BLACK);
		txtNoFavs.setBackgroundColor(Color.WHITE);
		txtNoFavs.setVisibility(View.GONE);
		txtNoFavs.setTextSize(20);
		
		View view = inflater.inflate(R.layout.activity_favorites, container, false);
		Bundle extras = getArguments();
		savedType = extras.getString("savedType");
		
		VerseFavAdapter verseAdapter = new VerseFavAdapter(getActivity().getApplicationContext(), savedType);
		
		listVerse = (ListView) view.findViewById(R.id.listFavorites);
		listVerse.setAdapter(verseAdapter);
		( (ViewGroup) listVerse.getParent()).addView(txtNoFavs);
		listVerse.setEmptyView(txtNoFavs);
		listVerse.setOnItemClickListener(this);
		
		return view;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
		TextView txtVerseNumView = (TextView) view.findViewById(R.id.lblFavVerseRowVerseNum);
		
		if (txtVerseNumView != null) {			
			mOnSelectedFavVerseCallback.onSelectedFavVerse((VerseFav) listVerse.getAdapter().getItem(position));
		}
	}
	
}
