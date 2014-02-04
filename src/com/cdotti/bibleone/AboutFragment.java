package com.cdotti.bibleone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Bundle args = getArguments();
		
		View view = inflater.inflate(R.layout.activity_about, container, false);
		TextView txtView = (TextView) view.findViewById(R.id.txtVersionNumber);
		if (args != null) {
			txtView.setText(args.getString("version_number"));
		}
		return view;
	}

}
