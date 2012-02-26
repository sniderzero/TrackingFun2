package com.appsmarttech.ultitrack92;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Exercises extends Activity{
	
	protected SQLiteDatabase db;
	protected Cursor cursor;
	protected ListAdapter adapter;
	int phaseID,hasRIP,dayID,dayID2, curPos, type,dayNum;
	String dayName;
	ListView lstExer;
	Typeface font, font2;
	TextView lblHeader;
	@Override
	// on open 
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.exercises);
    	//grab variables passed from previous activity
        phaseID = getIntent().getIntExtra("PHASE_ID", 0);  //phaseID to show exercise list
        hasRIP = getIntent().getIntExtra("HAS_RIPPER", 1);
        dayName = getIntent().getStringExtra("DAY_NAME");
        dayID = getIntent().getIntExtra("DAY_ID", 0);
        dayID2 = getIntent().getIntExtra("DAY_ID2", 0);  //record _id for the workoutday
        dayNum = getIntent().getIntExtra("DAY_NUM", 0);
        //build list widget
        lstExer = (ListView)findViewById(R.id.lstExercises);
        //open db
        db = (new DBHelper(this)).getWritableDatabase();
        //declare font
        font = Typeface.createFromAsset(getAssets(), "font.otf");
        //set header font
        lblHeader = (TextView)findViewById(R.id.lblHeader);
        lblHeader.setTypeface(font);
        lblHeader.setText(dayName);
    	// query db based on Ab Ripper Status
        if (hasRIP == 0){
        	nohasRipper();
        }
        else{
        	hasRipper();
        }
        
    	// set ListView to results

		lstExer.setAdapter(new adapter(this,cursor));
		db.close();
    
	//this is what we do when we select an item from our new list
	 lstExer.setOnItemClickListener(
				new OnItemClickListener()
						{
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								type = cursor.getInt(3);
								Intent intent = new Intent(Exercises.this, Details.class);
								curPos = cursor.getPosition();
								intent.putExtra("CUR_POS", curPos);
								intent.putExtra("EXER_TYPE", type);
								intent.putExtra("HAS_RIPPER", hasRIP);
								intent.putExtra("DAY_ID", dayID);
								intent.putExtra("DAY_ID2", dayID2);
								intent.putExtra("DAY_NAME", dayName);
								intent.putExtra("PHASE_ID", phaseID);
								intent.putExtra("DAY_NUM", dayNum);
								startActivity(intent);
						}
							
						});
}
public void nohasRipper(){
	cursor = db.rawQuery("SELECT _id, DayID, ExerName, type " +
			"FROM Exercises WHERE DayID =" + "'" + dayID +"'", null);
}
public void hasRipper(){
	cursor = db.rawQuery("SELECT _id, DayID, ExerName, type " +
			"FROM Exercises WHERE DayID =" + "'" + dayID +"'", null);
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
      TextView t = (TextView)view.findViewById(R.id.txtExerciseName);  
      t.setText(cursor.getString(cursor.getColumnIndex("ExerName")));
      	  
      t.setTypeface(font);
      
      }  
  
    @Override  
    public View newView(Context context, Cursor cursor, ViewGroup parent) {  
      final View view = mInflater.inflate(R.layout.exercisesrow, parent, false);  
      return view;  
    }  
  }  
//building opens menu
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options, menu);
    return true;
  }
//setting actoins for option selection
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.preferences:
        Intent in = new Intent(this, AppPreferences.class);
        startActivity(in);
          return true;
    default:
          return super.onOptionsItemSelected(item);
    }

}

}
