package com.appsmarttech.ultitrack92;



import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
 
public class AppPreferences extends PreferenceActivity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        
        Preference somePreference = findPreference("firstrun");
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removePreference(somePreference);
    }
 
}
