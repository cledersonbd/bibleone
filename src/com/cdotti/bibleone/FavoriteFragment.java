package com.cdotti.bibleone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FavoriteFragment extends Fragment implements OnItemClickListener {

	private ListView listFav;
	// Tipo do item == "F" para favorito, "M" para marcado
	private String cItemType;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.activity_favorites, container, false);
		
		Bundle extras = getArguments();
		cItemType = extras.getString("savedItemType");
		
		VerseFavAdapter verseAdapter = new VerseFavAdapter(getActivity().getApplicationContext(), cItemType);
		
		listFav = (ListView) view.findViewById(R.id.listFavorites);
		listFav.setAdapter(verseAdapter);
		listFav.setOnItemClickListener(this);
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
