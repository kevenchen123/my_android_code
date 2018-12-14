package com.keven.frag;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.keven.R;

public class PrefsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
    }
}
