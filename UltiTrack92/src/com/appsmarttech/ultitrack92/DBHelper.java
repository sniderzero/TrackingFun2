package com.appsmarttech.ultitrack92;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
	// setting DB name
	public static final String DATABASE_NAME = "UT92";
	
	protected Context context;
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, 2);
		this.context = context;
	}
	//building DB based on sql.xml file in res/raw folder
	@Override
	public void onCreate(SQLiteDatabase db) {
		String s;
		try {
			
			InputStream in = context.getResources().openRawResource(R.raw.sql);
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = builder.parse(in, null);
			NodeList statements = doc.getElementsByTagName("statement");
			for (int i=0; i<statements.getLength(); i++) {
				s = statements.item(i).getChildNodes().item(0).getNodeValue();
				db.execSQL(s);
			}
		} catch (Throwable t) {
			Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
		}
	}
	// on upgrade - deleting existing tables and rebuilding - I need to look more into this are of SQLLITE
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("UPDATE Exercises SET ExerName= 'Tonys Triangle' WHERE _id = 245");
		db.execSQL("UPDATE Exercises SET ExerName= 'Tonys Triangle' WHERE _id = 233");
		db.execSQL("UPDATE Exercises SET ExerName= 'Tonys Triangle' WHERE _id = 237");
		db.execSQL("UPDATE Exercises SET ExerName= 'Tonys Triangle' WHERE _id = 241");
		}
	
	
}

