package com.cdotti.bibleone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class MainActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Fragment bookFragment;
		bookFragment = new BookFragment();
		
		FragmentManager fm = getSupportFragmentManager(); 
		fm.beginTransaction().replace(R.id.content_frame, bookFragment).commit();		
		
	}
}
