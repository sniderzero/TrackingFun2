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
        //hiding first run preference from user
        Preference prefFirstrun = findPreference("firstrun");
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        preferenceScreen.removePreference(prefFirstrun);
        //hiding launch number preference from user
        Preference prefRunNum = findPreference("runnumber");
        preferenceScreen.removePreference(prefRunNum);
        //hiding provided feedback preference from user
        Preference prefFeedBack = findPreference("feedbackgiven");
        preferenceScreen.removePreference(prefFeedBack);
    }
 
}
