package com.appsmarttech.ultitrack92;

import java.util.Calendar;

public class functions {

	   public static String getDate(){
		   // get the current date and turn it into a String
		   int intYear;
		   int intMonth;
		   int intDay;
		   String fullDate;
		        Calendar c = Calendar.getInstance();
		        intYear = c.get(Calendar.YEAR);
		        intMonth = c.get(Calendar.MONTH) + 1;
		        intDay = c.get(Calendar.DAY_OF_MONTH);
		        
		        Integer.toString(intYear);
		        Integer.toString(intMonth);
		        Integer.toString(intDay);
		        fullDate = intMonth + "/" + intDay + "/" + intYear;
		        
		        return fullDate;
		        
		   }
	
	   
	   
}
