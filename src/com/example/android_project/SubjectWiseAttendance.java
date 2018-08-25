package com.example.android_project;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SubjectWiseAttendance extends Activity {

	int rno;
	ArrayList<String> list = new ArrayList<String>();
	ListView lv;
	TextView tv, tvPhone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subject_wise_attendance);
		
		Intent i = getIntent();
		rno = i.getExtras().getInt("rno");
		
		lv = (ListView) findViewById(R.id.listViewSubAttd);
		tv = (TextView) findViewById(R.id.textViewStudName);
		tvPhone = (TextView) findViewById(R.id.textViewPhoneNo);
		
		getSubjectAttendance();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.subject_wise_attendance, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	void getSubjectAttendance()
	{
		SQLiteDatabase mydatabase = openOrCreateDatabase("StudentAttendance",MODE_PRIVATE,null);

		Cursor cursor = mydatabase.rawQuery("Select * from Subjects",null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			String sub = cursor.getString(0); //getting subject
			
			
			Cursor myCursor = mydatabase.rawQuery("Select * from Attendance where RollNo="+rno+" and subject='"+sub+"'",null);
			myCursor.moveToFirst();
			float total = myCursor.getCount();
			myCursor = mydatabase.rawQuery("Select * from Attendance where RollNo="+rno+" and isPresent='P' and subject='"+sub+"'",null);
			myCursor.moveToFirst();
			float present = myCursor.getCount();
			float percent = (present/total)*100;
			
			if(Float.isNaN(percent))
				percent = 0f;
			
			String str = sub + " :   " + new DecimalFormat("##.##").format(percent) + "%";
			list.add(str);

			cursor.moveToNext();
		}
		
		ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , list);
		lv.setAdapter(adp);
		
		Cursor c = mydatabase.rawQuery("Select Name from Student where RollNo="+rno,null);
		c.moveToFirst();
		String name = c.getString(0);//cursor.getColumnIndex("Name"));
		tv.setText(name + "'s attendance : ");
		
		c = mydatabase.rawQuery("Select PhoneNo from Student where RollNo="+rno,null);
		c.moveToFirst();
		String phone = c.getString(0);//cursor.getColumnIndex("Name"));
		tvPhone.setText(phone);
	}
}
