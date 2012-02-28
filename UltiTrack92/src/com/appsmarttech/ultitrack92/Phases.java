package com.appsmarttech.ultitrack92;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	Integer phaseID, strengthID;
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
	    		final CharSequence[] items = {"Strength Week 1","Strength Week 2"};

	    		AlertDialog.Builder builder = new AlertDialog.Builder(Phases.this);
	    		builder.setTitle("Which Strength Week?");
	    		builder.setItems(items, new DialogInterface.OnClickListener() {
	    		    public void onClick(DialogInterface dialog, int item) {
	    				if(items[item] == "Strength Week 1"){
	    					strengthID = 1;
	    				}
	    				else {
	    					strengthID =2;
	    				}
	    		        Intent intent = new Intent(Phases.this, WorkoutDays.class);
	    		        intent.putExtra("STRENGTH_ID", strengthID);
	    		        intent.putExtra("PHASE_ID", phaseID);
	    		        db.close();
						startActivity(intent);
	    		    }});
	    		AlertDialog alert = builder.create();
	    		alert.show();
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
            Intent in = new Intent(Phases.this, AppPreferences.class);
            startActivity(in);
              return true;
        default:
              return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
    	   db.close();
    	   finish();
    	}
    
}




