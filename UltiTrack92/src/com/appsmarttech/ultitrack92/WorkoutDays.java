package com.appsmarttech.ultitrack92;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WorkoutDays extends Activity {

	SQLiteDatabase db;
	Cursor cursor;
	ListAdapter adapter;
	int phaseID, Type;
	ListView lstDays;
	TextView lblHeader;
	Typeface font;
	
	@Override
	// on open 
    public void onCreate(Bundle savedInstanceState) {
		
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.days);
        //declare widgets
        lstDays = (ListView)findViewById(R.id.lstWorkouts);
        lblHeader = (TextView)findViewById(R.id.lblHeader);
        //set font
        font = Typeface.createFromAsset(getAssets(), "font.otf");
        lblHeader.setTypeface(font);
    	//open db
        db = (new DBHelper(this)).getWritableDatabase();
        //grab phaseID from previous selection
        phaseID = getIntent().getIntExtra("PHASE_ID", 1);
    	// query db based on phaseID
    	cursor = db.rawQuery("SELECT _id, DayNum, DayName, DayID, PhaseID, StrengthID, Type, HasRipper, LastDate, Completions " +
    			"FROM Days WHERE PhaseID ="  + phaseID , null);
		lstDays.setAdapter(new adapter(this,cursor));

		
		
		
			
 lstDays.setOnItemClickListener(
	new OnItemClickListener()
			{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Type = cursor.getInt(6);
			if(Type == 1)
				{
					//this is the action if it is a timed based workout
					Intent intent = new Intent(WorkoutDays.this, TimedWorkout.class);
					intent.putExtra("DAY_ID", cursor.getInt(0));
					intent.putExtra("DAY_NAME", cursor.getString(2));
					intent.putExtra("DAY_NUM", cursor.getInt(1));
					intent.putExtra("PHASE_ID", cursor.getInt(4));
					db.close();
					startActivity(intent);
				}
				else { 
					//this is the action if it is an exercise based workout
					Intent intent = new Intent(WorkoutDays.this, Exercises.class);
					intent.putExtra("PHASE_ID", cursor.getInt(4));
					intent.putExtra("HAS_RIPPER", cursor.getInt(7));
					intent.putExtra("DAY_NAME", cursor.getString(2));
					intent.putExtra("DAY_ID", cursor.getInt(3));
					intent.putExtra("DAY_ID2", cursor.getInt(0));
					intent.putExtra("DAY_NUM", cursor.getInt(1));
					db.close();
					startActivity(intent);
				 	}
																								}

			});
	
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
	      TextView t = (TextView)view.findViewById(R.id.txtDayName);  
	      t.setText(cursor.getString(cursor.getColumnIndex("DayName")));
	      
	      TextView t1 = (TextView)view.findViewById(R.id.txtCompletions);  
	      t1.setText(cursor.getString(cursor.getColumnIndex("Completions")));
	  	      
	      t.setTypeface(font);
	      t1.setTypeface(font);
	      
	      }  
	  
	    @Override  
	    public View newView(Context context, Cursor cursor, ViewGroup parent) {  
	      final View view = mInflater.inflate(R.layout.daysrow, parent, false);  
	      return view;  
	    }  
	  }  
	
	
}
	
