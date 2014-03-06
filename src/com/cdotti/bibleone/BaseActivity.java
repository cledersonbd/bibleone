package com.cdotti.bibleone;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity 
	implements OnSharedPreferenceChangeListener {

	private SharedPreferences mSharedPrefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Carrega as configuracoes do usuario em settings
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String prefColor = mSharedPrefs.getString(SettingsActivity.APP_COLOR_SCHEME, "");
		int meuResID = getResources().getIdentifier(prefColor, "style", getPackageName()); 
		
		setTheme(meuResID);
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		mSharedPrefs
			.registerOnSharedPreferenceChangeListener(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		mSharedPrefs
		.unregisterOnSharedPreferenceChangeListener(this);
		super.onPause();
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
		if (key.equals(SettingsActivity.APP_COLOR_SCHEME)) {
			Toast.makeText(getApplicationContext(), "Atualizou o valor da cor", Toast.LENGTH_SHORT).show();
		}
		
		
	}
}
