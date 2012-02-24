package com.appsmarttech.ultitrack92;


import android.app.Activity;
import android.app.ListActivity;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Phases extends Activity {
	
	
	SQLiteDatabase db;
	Cursor cursor;
	ListAdapter adapter;
	Integer phaseID;
	ListView lstPhases;
	SharedPreferences preferences;
	TextView lblHeader;
	Typeface font;
	
	@Override
	// on open 
    public void onCreate(Bundle savedInstanceState) {
		
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.phases);
        lstPhases = (ListView)findViewById(R.id.lstPhases);
        font = Typeface.createFromAsset(getAssets(), "font.otf");
        lblHeader = (TextView)findViewById(R.id.lblHeader);
        lblHeader.setTypeface(font);
    	//open db
        db = (new DBHelper(this)).getWritableDatabase();
    	// query db based on that variable
    	cursor = db.rawQuery("SELECT _id, Name, Weeks, PhaseID, Completions " +
    			"FROM Phases", null);
		lstPhases.setAdapter(new adapter(this,cursor));


			
 lstPhases.setOnItemClickListener(
	new OnItemClickListener()
			{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				phaseID = cursor.getInt(3);
			if(phaseID == 3)
				{
					Intent intent = new Intent(Phases.this, UltiTrack92Activity.class);
					//Cursor cursor2 = (Cursor) adapter.getItem(position);
					intent.putExtra("PHASE_DAY", cursor.getInt(5));
					intent.putExtra("HAS_RIPPER", cursor.getInt(6));
					intent.putExtra("DAY_ID", cursor.getInt(1));
					intent.putExtra("DAY_NAME", cursor.getString(0));
					db.close();
					startActivity(intent);
				}
				else { 
					Intent intent = new Intent(Phases.this, WorkoutDays.class);
					intent.putExtra("PHASE_ID", phaseID);
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
	      TextView t = (TextView)view.findViewById(R.id.txtPhaseName);  
	      t.setText(cursor.getString(cursor.getColumnIndex("Name")));
	  
	      TextView t1 = (TextView)view.findViewById(R.id.txtNumWeeks);  
	      t1.setText(cursor.getString(cursor.getColumnIndex("Weeks")));
	      	  
	      TextView t2 = (TextView)view.findViewById(R.id.txtCompletions);  
	      t2.setText(cursor.getString(cursor.getColumnIndex("Completions")));
	      
	      t.setTypeface(font);
	      t1.setTypeface(font);
	      t2.setTypeface(font);
	      
	      }  
	  
	    @Override  
	    public View newView(Context context, Cursor cursor, ViewGroup parent) {  
	      final View view = mInflater.inflate(R.layout.phasesrow, parent, false);  
	      return view;  
	    }  
	  }  
	
	
}




