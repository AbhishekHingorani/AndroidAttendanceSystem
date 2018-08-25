package com.example.android_project;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Home extends Activity {

	Button btnMarkAtd, btnViewAtd, btnAddNewStd, btnDeleteStud;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		btnMarkAtd = (Button) findViewById(R.id.buttonMarkAttendence);
		btnViewAtd = (Button) findViewById(R.id.buttonViewAttendence);
		btnAddNewStd = (Button) findViewById(R.id.buttonAddNewStud);
		btnDeleteStud = (Button) findViewById(R.id.buttonDeleteStudent);
		
		btnMarkAtd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SQLiteDatabase mydatabase = openOrCreateDatabase("StudentAttendance",MODE_PRIVATE,null);
				if(isTableExists(mydatabase, "Student"))
				{
					startActivity(new Intent(Home.this,SelectDetails.class));
				}
				else
				{
					Toast.makeText(Home.this, "No records found...", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnViewAtd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Home.this,ViewAttendance.class));
			}
		});
		
		btnAddNewStd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Home.this,AddNewStudent.class));
			}
		});
		
		btnDeleteStud.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(Home.this,DeleteStudent.class));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
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
	
	boolean isTableExists(SQLiteDatabase db, String tableName)
	{
		if (tableName == null || db == null || !db.isOpen())
		{
			return false;
		}
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
		if (!cursor.moveToFirst())
		{
			cursor.close();
			return false;
		}
		int count = cursor.getInt(0);
		cursor.close();
		return count > 0;
	}
}
