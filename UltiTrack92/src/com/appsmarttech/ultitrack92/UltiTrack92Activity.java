package com.appsmarttech.ultitrack92;




import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import android.widget.Toast;


public class UltiTrack92Activity extends Activity {
	
	//declarations
	SQLiteDatabase db;
	SharedPreferences preferences;
	String strTrack,strEquip, strRuns;
	Spinner spnEquip;
	Integer intRuns;
	Editor edit;
	Boolean bnFeedback;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //open DB, create it if necessary
        db = (new DBHelper(this)).getWritableDatabase();
        //open preferences
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        //declare preference editor
        edit = preferences.edit();
        //grabbing number or runs preference
        strRuns = preferences.getString("runnumber", "0");
        //grabbing if feedback has been given boolean
        bnFeedback = preferences.getBoolean("feedbackgiven", false);
        //converting it to Integer
        intRuns = Integer.valueOf(strRuns);
        //adding one everytime the user runs the app
        intRuns = intRuns+1;
        //converting back to a string
        strRuns = String.valueOf(intRuns);
        //storing in preferences
        edit.putString("runnumber", strRuns);
        edit.commit();
        
       //check if the user has given feedback
        if(bnFeedback == false){
        
        //check if the number of runs is 5/10/15
       
        if(intRuns ==  10 || intRuns == 15 || intRuns == 25){
        	//ask for feedback if it is
        	
            final Dialog dialog = new Dialog(UltiTrack92Activity.this);
            dialog.setContentView(R.layout.rateappdialog);
            dialog.setTitle("Will you rate UltiTrack92?");
            dialog.setCancelable(true);
            //declare dialog buttons
            Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
            Button btnLater = (Button) dialog.findViewById(R.id.btnLater);
            Button btnNothanks = (Button) dialog.findViewById(R.id.btnNothanks);
            btnYes.setOnClickListener(new OnClickListener() {
            @Override
                public void onClick(View v) {
            	edit.putBoolean("feedbackgiven", true);
            	edit.commit();
            	rateApp();
            	dialog.dismiss();
                }
            });
            btnLater.setOnClickListener(new OnClickListener() {
            @Override
                public void onClick(View v) {
            	dialog.dismiss();
                }
            });
            btnNothanks.setOnClickListener(new OnClickListener() {
            @Override
                public void onClick(View v) {
            	edit.putBoolean("feedbackgiven", true);
            	edit.commit();
            	dialog.dismiss();
                }
            });
        	dialog.show();

        }
        }
       
    }
    
    public void clickBringIt(View v){
    	boolean firstrun = preferences.getBoolean("firstrun", false);
    	if(firstrun == true){
    		db.close();
        	Intent in = new Intent(this, Phases.class);
        	startActivity(in);
    	}
    	else {
    		db.close();
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

    public void rateApp(){
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse("market://details?id=com.appsmarttech.ultitrack92"));
    	startActivity(intent);
    }
    
//create options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
      }
//what to do when options menu is clicked
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