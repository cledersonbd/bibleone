package com.cdotti.bibleone;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity 
		implements SharedPreferences.OnSharedPreferenceChangeListener {
	public static final String KEY_APPCOLOR_SCHEME = "prefColorSchema";
	public static final String KEY_FONTNAME = "";
	public static final String KEY_FONTFAMILY = "";
	public static final Integer KEY_FONTSIZE = 18;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		getPreferenceScreen().getSharedPreferences()
			.registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		getPreferenceScreen().getSharedPreferences()
			.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(KEY_FONTFAMILY)) {
			Preference fontFamily = findPreference(key);
			fontFamily.setSummary(sharedPreferences.getString(key, ""));
		} else if (key.equals(KEY_FONTNAME)) {
			
		}
	}
	
	

	
}
