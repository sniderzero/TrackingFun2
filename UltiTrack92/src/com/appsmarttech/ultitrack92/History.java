package com.appsmarttech.ultitrack92;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;





public class History extends Activity{
	
	ListView lstHistory;
	SQLiteDatabase db;
	Cursor cursor;
	String exerName;
	ListAdapter adapter;
	TextView lblHistory;
	Integer exerType;
	SharedPreferences preferences;
	String equipPref;
	Typeface font;
	
	public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.historyview);
        //declare widgets
        lstHistory = (ListView)findViewById(R.id.lstHistory);
        lblHistory = (TextView)findViewById(R.id.lblHistory);
        
        //declaring font
        font = Typeface.createFromAsset(getAssets(), "font.otf");
        //setting widget fonts
		((TextView)findViewById(R.id.lblTime)).setTypeface(font);
		((TextView)findViewById(R.id.lblReps)).setTypeface(font);
		((TextView)findViewById(R.id.lblWeight)).setTypeface(font);
		((TextView)findViewById(R.id.lblBand)).setTypeface(font);
		((TextView)findViewById(R.id.lblAssist)).setTypeface(font);
		((TextView)findViewById(R.id.btnDone)).setTypeface(font);
		lblHistory.setTypeface(font);
        
        
        //open db connection
        db = (new DBHelper(this)).getWritableDatabase();
        //bring in variables from previous activity
        exerName = getIntent().getStringExtra("EXERCISE_NAME");
        exerType = getIntent().getIntExtra("EXERCISE_TYPE", 0);
        //set label to exercisename
        lblHistory.setText(exerName);
        //search db based on Exercise/DayName
        cursor = db.rawQuery("SELECT _id, ExerciseName, date, time, weight, reps, band, assist FROM results WHERE ExerciseName = " + "'" + exerName +"'", null);
        //grabbing the band/weight preference
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        equipPref = preferences.getString("equipmentType", "");
        //determine what to show
        whatDisplay();
        lstHistory.setAdapter(new adapter(this,cursor));
		//close db
        db.close();
		
		
}
	public void clickClose(View v){
		finish();
	}
	
	public void whatDisplay(){
		switch(exerType){
		case 1:
			showReps();
			break;
		case 2:
			weightsOrBands();
			break;
		case 3:
			showTime();
			break;
		case 4:
			showAssist();
			break;
		}
	}
	
	public class adapter extends CursorAdapter{  
	    private Cursor mCursor;  
	    private Context mContext;  
	    LayoutInflater mInflater;  
	  
	    public adapter(Context context, Cursor cursor) {  
	      super(context, cursor, true);  
	      mInflater = LayoutInflater.from(context);  
	      mContext = context;  
	    }  
	  
	    @Override  
	    public void bindView(View view, Context context, Cursor cursor) {  
	      TextView t = (TextView) view.findViewById(R.id.txtTime);  
	      t.setText(cursor.getString(cursor.getColumnIndex("time")));  
	      t.setTypeface(font);
	      t = (TextView) view.findViewById(R.id.txtDate);  
	      t.setText(cursor.getString(cursor.getColumnIndex("date")));
	      t.setTypeface(font); 
	      t = (TextView) view.findViewById(R.id.txtWeight);  
	      t.setText(cursor.getString(cursor.getColumnIndex("weight")));  
	      t.setTypeface(font);
	      t = (TextView) view.findViewById(R.id.txtAssist);  
	      t.setText(cursor.getString(cursor.getColumnIndex("assist")));
	      t.setTypeface(font);
	      t = (TextView) view.findViewById(R.id.txtReps);  
	      t.setText(cursor.getString(cursor.getColumnIndex("reps")));
	      t.setTypeface(font);
	      t = (TextView) view.findViewById(R.id.txtBand);  
	      t.setText(cursor.getString(cursor.getColumnIndex("band")));
	      t.setTypeface(font);
			
	      switch(exerType){
	      case 3:
			((TextView)view.findViewById(R.id.txtTime)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.txtReps)).setVisibility(View.INVISIBLE);
			((TextView)view.findViewById(R.id.txtWeight)).setVisibility(View.GONE); 
			((TextView)view.findViewById(R.id.txtBand)).setVisibility(View.INVISIBLE);
			((TextView)view.findViewById(R.id.txtAssist)).setVisibility(View.GONE);
			break;
			
	      case 1:
			((TextView)view.findViewById(R.id.txtTime)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.txtReps)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.txtWeight)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.txtBand)).setVisibility(View.INVISIBLE);
			((TextView)view.findViewById(R.id.txtAssist)).setVisibility(View.INVISIBLE);
			break;
			
	      case 2:
	    	  if(equipPref.equals("Bands")){
	  			((TextView)view.findViewById(R.id.txtTime)).setVisibility(View.GONE);
				((TextView)view.findViewById(R.id.txtReps)).setVisibility(View.VISIBLE);
				((TextView)view.findViewById(R.id.txtWeight)).setVisibility(View.INVISIBLE);
				((TextView)view.findViewById(R.id.txtBand)).setVisibility(View.VISIBLE);
				((TextView)view.findViewById(R.id.txtAssist)).setVisibility(View.GONE);
	  		}
	  		else {
		    	((TextView)view.findViewById(R.id.txtTime)).setVisibility(View.GONE);
				((TextView)view.findViewById(R.id.txtReps)).setVisibility(View.VISIBLE);
				((TextView)view.findViewById(R.id.txtWeight)).setVisibility(View.VISIBLE);
				((TextView)view.findViewById(R.id.txtBand)).setVisibility(View.GONE);
				((TextView)view.findViewById(R.id.txtAssist)).setVisibility(View.INVISIBLE);
	  		}    	  

			break;
	      
	      case 4:
			((TextView)view.findViewById(R.id.txtTime)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.txtReps)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.txtWeight)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.txtBand)).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(R.id.txtAssist)).setVisibility(View.VISIBLE); 
	      break;
	      }
	      }
	      

	  
	    @Override  
	    public View newView(Context context, Cursor cursor, ViewGroup parent) {  
	      final View view = mInflater.inflate(R.layout.historyrow, parent, false);  
	      return view;  
	    }  
	  }  
	
	

	
	public void showTime(){
		
		((TextView)findViewById(R.id.lblTime)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.lblReps)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.lblWeight)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.lblBand)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.lblAssist)).setVisibility(View.GONE);
				
	}
	
	public void showReps(){
		((TextView)findViewById(R.id.lblTime)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.lblReps)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.lblWeight)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.lblBand)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.lblAssist)).setVisibility(View.INVISIBLE);

	}
	
	public void showWeight(){
		((TextView)findViewById(R.id.lblTime)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.lblReps)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.lblWeight)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.lblBand)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.lblAssist)).setVisibility(View.INVISIBLE);

	}
	
	public void showBand(){
		((TextView)findViewById(R.id.lblTime)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.lblReps)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.lblWeight)).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.lblBand)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.lblAssist)).setVisibility(View.GONE);

	}
	
	public void showAssist(){
		((TextView)findViewById(R.id.lblTime)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.lblReps)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.lblWeight)).setVisibility(View.GONE);
		((TextView)findViewById(R.id.lblBand)).setVisibility(View.VISIBLE);
		((TextView)findViewById(R.id.lblAssist)).setVisibility(View.VISIBLE);

	}
	
	//checking the user's equip preferences to show weights or bands
	public void weightsOrBands(){
		if(equipPref.equals("Bands")){
			showBand();
		}
		else {
			showWeight();
		}
	}
	
}