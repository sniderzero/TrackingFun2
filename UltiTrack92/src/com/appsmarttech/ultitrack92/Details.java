package com.appsmarttech.ultitrack92;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Details extends Activity {

//declare stuff here for non-stopwatch
protected SQLiteDatabase db;
protected Cursor cursor, cursor_user, curDay, curPhase;
protected ListAdapter adapter;
protected int curPos;
int dayID, type, hasRIP, dayID2, phaseID, intCompletes, intPhaseCompletes, dayNum;
String spnBandStr, spnAssistStr, TimerStr, strDate, eTxtWeightStr, eTxtRepsStr, dayName;
TextView txtName, txtRepsValue, txtWeightValue, txtTimeValue, txtAssistValue, txtBandValue, txtDateValue;
TextView lblReps, lblWeight, lblAssist, lblBand, lblLastRound, txtReps, txtWeight, txtTime, txtAssist, txtBand, txtDate;
Spinner spnBand, spnAssist;
ArrayAdapter<CharSequence> adapter_band, adapter_rep, adapter_weight, adapter_assist;
Button btnNext, btnPrev, actionBtn, btnSave, btnStart, btnReset, btnStop, btnHistory;
SharedPreferences preferences;
String equipPref;
EditText eTxtReps, eTxtWeight;
//declare stuff her for stopwatch
private TextView timerTextView;
private Handler mHandler = new Handler();
private long startTime;
private long elapsedTime;
private final int REFRESH_RATE = 100;
private String hours,minutes,seconds;
private long secs,mins,hrs;
private boolean stopped = false;
Intent intentHome;
Typeface font;


@Override
// on open
    public void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
        setContentView(R.layout.detailview);
        //grab variables from previous activity
        curPos = getIntent().getIntExtra("CUR_POS",-1); //current postion
        dayID = getIntent().getIntExtra("DAY_ID", 0); //day id
        hasRIP = getIntent().getIntExtra("HAS_RIPPER", 1); //has ripper or not
        dayID2 = getIntent().getIntExtra("DAY_ID2",1);  //day _id for saving completion date and completion number
        phaseID = getIntent().getIntExtra("PHASE_ID",1); //PhaseID for updating Phase completions
        dayName = getIntent().getStringExtra("DAY_NAME"); //dayName for share dialog
        dayNum = getIntent().getIntExtra("DAY_NUM",1); //for determining if the phase is finished
        //grabbing the band/weight preference
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        equipPref = preferences.getString("equipmentType", "");
        //building widgets
        txtName = (TextView) findViewById(R.id.txtName);
        txtRepsValue = (TextView) findViewById(R.id.txtRepsValue);
        txtWeightValue = (TextView) findViewById(R.id.txtWeightValue);
        txtTimeValue = (TextView) findViewById(R.id.txtTimeValue);
        txtAssistValue = (TextView) findViewById(R.id.txtAssistValue);
        txtBandValue = (TextView) findViewById(R.id.txtBandValue);
        txtDateValue = (TextView) findViewById(R.id.txtDateValue);
        lblReps = (TextView) findViewById(R.id.lblReps);
        lblWeight= (TextView) findViewById(R.id.lblWeight);
        lblAssist= (TextView) findViewById(R.id.lblAssist);
        lblBand= (TextView) findViewById(R.id.lblBand);
        lblLastRound= (TextView) findViewById(R.id.lblLastRound);
        txtReps= (TextView) findViewById(R.id.txtReps);
        txtWeight= (TextView) findViewById(R.id.txtWeight);
        txtTime= (TextView) findViewById(R.id.txtTime);
        txtAssist= (TextView) findViewById(R.id.txtAssist);
        txtBand= (TextView) findViewById(R.id.txtBand);
        txtDate= (TextView) findViewById(R.id.txtDate);
        btnNext = (Button) findViewById(R.id.imgNext);
        btnPrev = (Button) findViewById(R.id.imgPrev);
        btnSave = (Button) findViewById(R.id.imgSave);
        timerTextView = (TextView) findViewById(R.id.timer); 
        btnStart = (Button)findViewById(R.id.startButton);
        btnReset = (Button)findViewById(R.id.resetButton);
        btnStop = (Button)findViewById(R.id.stopButton);
        btnHistory = (Button)findViewById(R.id.btnHist);
        
        //declaring font
        font = Typeface.createFromAsset(getAssets(), "font.otf");
        
        //setting widgets to font
        txtName.setTypeface(font);
        txtRepsValue.setTypeface(font);
        txtWeightValue.setTypeface(font);
        txtTimeValue.setTypeface(font);
        txtAssistValue.setTypeface(font);
        txtBandValue.setTypeface(font);
        txtDateValue.setTypeface(font);
        btnNext.setTypeface(font);
        btnPrev.setTypeface(font);
        btnSave.setTypeface(font);
        timerTextView.setTypeface(font);
        btnStart.setTypeface(font);
        btnReset.setTypeface(font);
        btnStop.setTypeface(font);
        btnHistory.setTypeface(font);
        lblReps.setTypeface(font);
        lblWeight.setTypeface(font);
        lblAssist.setTypeface(font);
        lblBand.setTypeface(font);
        lblLastRound.setTypeface(font);
        txtReps.setTypeface(font);
        txtWeight.setTypeface(font);
        txtTime.setTypeface(font);
        txtAssist.setTypeface(font);
        txtBand.setTypeface(font);
        txtDate.setTypeface(font);

        
                  
        //building home intent
        intentHome = new Intent(this, UltiTrack92Activity.class);
        if(equipPref.equals("Generic Bands")) { 
        //creating some spinner adapters
        adapter_band = ArrayAdapter.createFromResource(
        		this, R.array.Generic_Bands, android.R.layout.simple_spinner_item);
        }
        else {
            adapter_band = ArrayAdapter.createFromResource(
            		this, R.array.X_Bands, android.R.layout.simple_spinner_item);
        }
        
        adapter_band.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        
        
        adapter_assist= ArrayAdapter.createFromResource(
        this, R.array.assist_values, android.R.layout.simple_spinner_item );
        adapter_assist.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        //creating spinners and assigning adapters
        eTxtReps = (EditText) this.findViewById(R.id.eTxtReps);
        eTxtWeight = (EditText) this.findViewById(R.id.eTxtWeight);
        spnBand = (Spinner) findViewById( R.id.spnBand );
        spnBand.setAdapter(adapter_band);
        spnAssist = (Spinner) findViewById( R.id.spnAssist );
        spnAssist.setAdapter(adapter_assist);

        //open db
        db = (new DBHelper(this)).getWritableDatabase();

        // query db based on that variable and move to correct position
        if (hasRIP == 0){
         nohasRipper();
        }
        else{
         hasRipper();
        }
        //move to position selected in list
        cursor.moveToPosition(curPos);
    	//grabbing the number of current completions for the day
    	curDay = db.rawQuery("SELECT _id, Completions FROM Days WHERE _id = " +  "'" + dayID2 + "'", null);
    	curDay.moveToFirst();
    	//assigning completions to an int
    	intCompletes = curDay.getInt(1);
    	//grabbing the number of current completions for the phase
    	curPhase = db.rawQuery("SELECT _id, PhaseID, Completions FROM Phases WHERE PhaseID = " +  "'" + phaseID + "'", null);
    	curPhase.moveToFirst();
    	//assigning completions to an int
    	intPhaseCompletes = curPhase.getInt(2);
     
     
     // txtName Value based on previous selection criteria
     txtName.setText(cursor.getString(2));
     
     // filling out user stats     
     fillUserStats();
          
     //calling function to set what is visible
     showWhat();
     viewRefresh();
     //checking to see if we are at the beginning or end, and showing the appropriate buttons for each
     atEnd();
     atBeginning();
     
     //setting the date string
     strDate = functions.getDate();  //grabbed from the functions class
     
     
     
    
    }
    public void nextExercise(View v)
    {
     //saving user stats
     saveRecord();
     //moving to next record
     cursor.moveToNext();
     //reseting our view
     fillUserStats();
     //reset type
     showWhat();
     txtName.setText(cursor.getString(2));
     //checking to see where we are and setting next button to invisible if we are at the end
     atEnd();
     atBeginning();
     viewRefresh();
    }
    
  public void prevExercise(View v)
        {
	  //moving to prev record
	  cursor.moveToPrevious();
	  //reseting our view
	  fillUserStats();
	  //reset type
	  showWhat();
	  txtName.setText(cursor.getString(2));
	  //checking to see where we are and setting next button to invisible if we are at the end
	  atEnd();
	  atBeginning();
	  viewRefresh();
    }
  
  public void clickSave(View v)
		  {
	  	if(dayNum ==7){
	  		intPhaseCompletes = intPhaseCompletes +1;
	  		db.execSQL("UPDATE Phases SET phaseCompletions= "+ intCompletes + " WHERE PhaseID = " + "'" + phaseID + "'");
	  	}
	  	intCompletes = intCompletes +1;
  		db.execSQL("UPDATE Days SET LastDate= "+ "'" + strDate + "'" + " WHERE _id = " + "'" + dayID2 + "'");
  		db.execSQL("UPDATE Days SET Completions= "+ intCompletes + " WHERE _id = " + "'" + dayID2 + "'");
  		saveRecord();
  		db.close();
  		dialogShare();
		  }
  
  
public void saveRecord(){
	//grabing values
	eTxtWeightStr = eTxtWeight.getText().toString();
	eTxtRepsStr = eTxtReps.getText().toString();
	spnBandStr = spnBand.getItemAtPosition((int) spnBand.getSelectedItemId()).toString();
	spnAssistStr = spnAssist.getItemAtPosition((int) spnAssist.getSelectedItemId()).toString();
	TimerStr = timerTextView.getText().toString();
	int repsLength = eTxtRepsStr.length();
	
	if(repsLength == 0 && TimerStr.equals("00:00:00")){
		Toast.makeText(this, "Exercise Not Saved, the number of reps or time is 0", 50).show();
	}
	else {
		
		//storing them in DB if they aren't all 0
		db.execSQL("INSERT INTO results (ExerciseName,weight,reps,band,date,assist,time) VALUES("+ "'" + cursor.getString(2) +"'" + ","
				+  "'" + eTxtWeightStr +"'" +","+ "'" +eTxtRepsStr+"'" +","+"'" + spnBandStr +"'"+","+ "'" + strDate +"'"+ ","+"'" + 
				spnAssistStr +"'"+ ","+"'" + TimerStr +"'"+ ")");
		Toast.makeText(this, "Exercise Saved", 50).show();
		

	}


  }


  public void fillUserStats(){
   //changing the textviews to show the users last results
   cursor_user = db.rawQuery("SELECT _id, weight, ExerciseName, reps, time, date, assist, band FROM results WHERE ExerciseName = " +
   "'" + cursor.getString(2) +"'", null);
  
   if (cursor_user.getCount()==0){
   txtRepsValue.setText("");
   txtWeightValue.setText("");
   txtTimeValue.setText("");
   txtAssistValue.setText("");
   txtBandValue.setText("");
   txtDateValue.setText("");
   }
   else{
   cursor_user.moveToLast();
   txtRepsValue.setText(cursor_user.getString(3));
   txtWeightValue.setText(cursor_user.getString(1));
   txtTimeValue.setText(cursor_user.getString(4));
   txtAssistValue.setText(cursor_user.getString(6));
   txtBandValue.setText(cursor_user.getString(7));
   txtDateValue.setText(cursor_user.getString(5));
   }
  }
  
  //function resets spinners and timer
   public void viewRefresh(){
		((TextView)findViewById(R.id.timer)).setText("00:00:00");
		eTxtWeight.setText("");
		eTxtReps.setText("");
		spnAssist.setSelection(0);
		spnBand.setSelection(0);
  
   }

   //Switch that decides what widgets are displayed on the screen
   public void showWhat(){
   type = cursor.getInt(3);
   switch(type){
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
   case 5:
   showBand();
   break;
   }

   }
   
   //checking to see if we are at the end of the cursor
public void atEnd(){
if (cursor.isLast() == false) {
     btnNext.setVisibility(View.VISIBLE);
     btnSave.setVisibility(View.GONE);
     }
else if (cursor.isLast() == true){
btnNext.setVisibility(View.GONE);
btnSave.setVisibility(View.VISIBLE);
}
}

//check to see if we are at the beginning of the cursor
public void atBeginning(){
if (cursor.isFirst() == false) {
     btnPrev.setVisibility(View.VISIBLE);
     }
else if (cursor.isFirst() == true){
btnPrev.setVisibility(View.INVISIBLE);
}
}

//cursor created when the day does not have ab ripper x
public void nohasRipper(){
	cursor = db.rawQuery("SELECT _id, DayID, ExerName, type " +
			"FROM Exercises WHERE DayID =" + "'" + dayID +"'", null);

}

//cursor created when the day does have ab ripper x
public void hasRipper(){
	cursor = db.rawQuery("SELECT _id, DayID, ExerName, type " +
			"FROM Exercises WHERE DayID =" + "'" + dayID +"'" + " or DayID = 15", null);
}


//opens the full history view
public void clickHistory(View v){
Intent intent = new Intent(Details.this, History.class);
intent.putExtra("EXERCISE_NAME", cursor.getString(2));
intent.putExtra("EXERCISE_TYPE", cursor.getInt(3));
startActivity(intent);
}


//different functions for changing item visibility dynamically
public void showReps(){
   ((TextView)findViewById(R.id.txtWeight)).setVisibility(View.INVISIBLE);
   ((TextView)findViewById(R.id.txtBand)).setVisibility(View.INVISIBLE);
   ((TextView)findViewById(R.id.txtAssist)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtReps)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtTime)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtWeightValue)).setVisibility(View.INVISIBLE);
   ((TextView)findViewById(R.id.txtBandValue)).setVisibility(View.INVISIBLE);
   ((TextView)findViewById(R.id.txtAssistValue)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtRepsValue)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtTimeValue)).setVisibility(View.GONE); 
	
   ((LinearLayout)findViewById(R.id.llReps)).setVisibility(View.VISIBLE);
   ((LinearLayout)findViewById(R.id.llWeight)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llAssist)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llBand)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llStopwatch)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llSpinners)).setVisibility(View.VISIBLE);
   
}

public void showWeights() {

   ((TextView)findViewById(R.id.txtWeight)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtBand)).setVisibility(View.INVISIBLE);
   ((TextView)findViewById(R.id.txtAssist)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtReps)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtTime)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtWeightValue)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtBandValue)).setVisibility(View.INVISIBLE);
   ((TextView)findViewById(R.id.txtAssistValue)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtRepsValue)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtTimeValue)).setVisibility(View.GONE);
   
   ((LinearLayout)findViewById(R.id.llReps)).setVisibility(View.VISIBLE);
   ((LinearLayout)findViewById(R.id.llWeight)).setVisibility(View.VISIBLE);
   ((LinearLayout)findViewById(R.id.llAssist)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llBand)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llStopwatch)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llSpinners)).setVisibility(View.VISIBLE);
}

public void showTime() {
	   ((TextView)findViewById(R.id.txtWeight)).setVisibility(View.GONE);
	   ((TextView)findViewById(R.id.txtBand)).setVisibility(View.INVISIBLE);
	   ((TextView)findViewById(R.id.txtAssist)).setVisibility(View.INVISIBLE);
	   ((TextView)findViewById(R.id.txtReps)).setVisibility(View.GONE);
	   ((TextView)findViewById(R.id.txtTime)).setVisibility(View.VISIBLE);
	   ((TextView)findViewById(R.id.txtWeightValue)).setVisibility(View.GONE);
	   ((TextView)findViewById(R.id.txtBandValue)).setVisibility(View.INVISIBLE);
	   ((TextView)findViewById(R.id.txtAssistValue)).setVisibility(View.INVISIBLE);
	   ((TextView)findViewById(R.id.txtRepsValue)).setVisibility(View.GONE);
	   ((TextView)findViewById(R.id.txtTimeValue)).setVisibility(View.VISIBLE);
	   ((LinearLayout)findViewById(R.id.llSpinners)).setVisibility(View.GONE);
	   ((LinearLayout)findViewById(R.id.llStopwatch)).setVisibility(View.VISIBLE);
}

public void showAssist() {
   ((TextView)findViewById(R.id.txtWeight)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtBand)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtAssist)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtReps)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtTime)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtWeightValue)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtBandValue)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtAssistValue)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtRepsValue)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtTimeValue)).setVisibility(View.GONE);
   
   ((LinearLayout)findViewById(R.id.llReps)).setVisibility(View.VISIBLE);
   ((LinearLayout)findViewById(R.id.llWeight)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llAssist)).setVisibility(View.VISIBLE);
   ((LinearLayout)findViewById(R.id.llBand)).setVisibility(View.VISIBLE);
   ((LinearLayout)findViewById(R.id.llStopwatch)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llSpinners)).setVisibility(View.VISIBLE);
}

public void showBand(){

   ((TextView)findViewById(R.id.txtWeight)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtBand)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtAssist)).setVisibility(View.INVISIBLE);
   ((TextView)findViewById(R.id.txtReps)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtTime)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtWeightValue)).setVisibility(View.GONE);
   ((TextView)findViewById(R.id.txtBandValue)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtAssistValue)).setVisibility(View.INVISIBLE);
   ((TextView)findViewById(R.id.txtRepsValue)).setVisibility(View.VISIBLE);
   ((TextView)findViewById(R.id.txtTimeValue)).setVisibility(View.GONE);
   
   ((LinearLayout)findViewById(R.id.llReps)).setVisibility(View.VISIBLE);
   ((LinearLayout)findViewById(R.id.llWeight)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llAssist)).setVisibility(View.GONE);
   ((LinearLayout)findViewById(R.id.llBand)).setVisibility(View.VISIBLE);
   ((LinearLayout)findViewById(R.id.llStopwatch)).setVisibility(View.GONE);
}

public void hideStopButton(){
	((Button)findViewById(R.id.stopButton)).setVisibility(View.GONE);
	((Button)findViewById(R.id.startButton)).setVisibility(View.VISIBLE);
	((Button)findViewById(R.id.resetButton)).setVisibility(View.VISIBLE);
}

public void showStopButton(){
	((Button)findViewById(R.id.stopButton)).setVisibility(View.VISIBLE);
	((Button)findViewById(R.id.startButton)).setVisibility(View.GONE);
	((Button)findViewById(R.id.resetButton)).setVisibility(View.GONE);
}


//checking the user's equip preferences to show weights or bands
public void weightsOrBands(){
	if(equipPref.equals("Generic Bands")  || equipPref.equals("X Bands")){
		showBand();
	}
	else {
		showWeights();
	}
}
/*The section of code is in support of the stopwatch timer*/
//actions when clicking the start button
public void startClick (View view){
	if(stopped){
		startTime = System.currentTimeMillis() - elapsedTime; 
	}
	else{
		startTime = System.currentTimeMillis();
	}
	showStopButton();
	mHandler.removeCallbacks(startTimer);
    mHandler.postDelayed(startTimer, 0);
}
//actions when clicking the stop button
public void stopClick (View view){
	mHandler.removeCallbacks(startTimer);
	stopped = true;
	hideStopButton();
}
//actions when clicking the reset button
public void resetClick (View view){
	stopped = false;
	((TextView)findViewById(R.id.timer)).setText("00:00:00"); 	
}


private Runnable startTimer = new Runnable() {
 	   public void run() {
 		   elapsedTime = System.currentTimeMillis() - startTime;
 		   updateTimer(elapsedTime);
 	       mHandler.postDelayed(this,REFRESH_RATE);
 	   }
 	};
 	//logic for building the timer
private void updateTimer (float time){
	secs = (long)(time/1000);
	mins = (long)((time/1000)/60);
	hrs = (long)(((time/1000)/60)/60);
	
	/* Convert the seconds to String 
	 * and format to ensure it has
	 * a leading zero when required
	 */
	secs = secs % 60;
	seconds=String.valueOf(secs);
	if(secs == 0){
		seconds = "00";
	}
	if(secs <10 && secs > 0){
		seconds = "0"+seconds;
	}
	
	/* Convert the minutes to String and format the String */
	
	mins = mins % 60;
	minutes=String.valueOf(mins);
	if(mins == 0){
		minutes = "00";
	}
	if(mins <10 && mins > 0){
		minutes = "0"+minutes;
	}
	
	/* Convert the hours to String and format the String */
	
	hours=String.valueOf(hrs);
	if(hrs == 0){
		hours = "00";
	}
	if(hrs <10 && hrs > 0){
		hours = "0"+hours;
	}
	    	
	/* Setting the timer text to the elapsed time */
	((TextView)findViewById(R.id.timer)).setText(hours + ":" + minutes + ":" + seconds);
}


  /* This guy makes a dialog asking if the user wants to share their stats with their friends*/
    public void dialogShare()
    {
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
    	builder
    	.setMessage("Congratulations! you just finished the " + dayName + " workout!! Do you want to tell your friends?")
    	.setTitle("Share with your Friends?")
        .setCancelable(false)
        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
        	//since they said yes, we are going to show them the share dialog
			public void onClick(DialogInterface dialog, int which) {
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String URL = "http://market.android.com/details?id=com.fidotechnologies.ultitrack92";
				String shareBody = "I just completed " + dayName + " of the P90X, and I tracked it using UltiTrack! get it here " + URL;
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "I am Awesome!!!");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Share via"));
				finish();
				
			}
            })

         .setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
			//since they said no, we take them to the launch activity
            	
			startActivity(intentHome);
			}
            });
    	
    	AlertDialog alert = builder.create();
    	
    	alert.show();
    }

    @Override
    public void onBackPressed() {
    	   db.close();
    	   finish();
    	}

	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    return true;
	  }

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