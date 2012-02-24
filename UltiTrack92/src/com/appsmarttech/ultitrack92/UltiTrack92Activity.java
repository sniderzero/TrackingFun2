package com.appsmarttech.ultitrack92;




import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;


public class UltiTrack92Activity extends Activity {
	
	//declarations
	SQLiteDatabase db;
	SharedPreferences preferences;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //open DB, create it if necessary
        db = (new DBHelper(this)).getWritableDatabase();
        //open preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }
    
    public void clickBringIt(View v){
    //	boolean firstrun = preferences.getBoolean("firstrun", false);
    //	if(firstrun == true){
    		db.close();
        	Intent in = new Intent(this, Phases.class);
        	startActivity(in);
    //	}
    	//else {
    	//	preferenceSelect();
    //	}
    }
}