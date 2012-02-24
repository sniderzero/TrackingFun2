package com.appsmarttech.ultitrack92;




import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class UltiTrack92Activity extends Activity {
	
	//declarations
	SQLiteDatabase db;
	SharedPreferences preferences;
	String strTrack,strEquip;
	Spinner spnTrack,spnEquip;
	
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
    	boolean firstrun = preferences.getBoolean("firstrun", false);
    	if(firstrun == true){
    		db.close();
        	Intent in = new Intent(this, Phases.class);
        	startActivity(in);
    	}
    	else {
    		preferenceSelect();
    	}
    }
    
    public void preferenceSelect ()  //First time run code
    {
        Dialog dialog = new Dialog(UltiTrack92Activity.this);
        dialog.setContentView(R.layout.firstrundialog);
        dialog.setTitle("Set Your Preferences");
        dialog.setCancelable(true);
        //set up spinners
        ArrayAdapter<CharSequence> adapter_track = ArrayAdapter.createFromResource(
        this, R.array.listTrack, android.R.layout.simple_spinner_item);
        adapter_track.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spnTrack = (Spinner) dialog.findViewById(R.id.spnTrack);
        spnTrack.setAdapter(adapter_track);
        
        ArrayAdapter<CharSequence> adapter_equip = ArrayAdapter.createFromResource(
        this, R.array.listEquip, android.R.layout.simple_spinner_item );
        adapter_equip.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spnEquip = (Spinner) dialog.findViewById(R.id.spnEquip);
        spnEquip.setAdapter(adapter_equip);
        //set up button
        Button button = (Button) dialog.findViewById(R.id.btnStart);
        button.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
        	Editor edit = preferences.edit();
        	strTrack = spnTrack.getItemAtPosition((int) spnTrack.getSelectedItemId()).toString();
        	strEquip = spnEquip.getItemAtPosition((int) spnEquip.getSelectedItemId()).toString();
        	//saving preferences to the preference file
        	edit.putString("trackType", strTrack);
        	edit.putString("equipmentType", strEquip);
        	edit.putBoolean("firstrun", true);
        	edit.commit();
        	Intent in = new Intent(UltiTrack92Activity.this, Phases.class);
        	startActivity(in);
        	db.close();
        	finish();
            }
        });
        
        
        dialog.show();
    	
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
      }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.preferences:
            Intent in = new Intent(UltiTrack92Activity.this, AppPreferences.class);
            startActivity(in);
              return true;
        default:
              return super.onOptionsItemSelected(item);
        }

    }
}