package com.example.android_project;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class DeleteStudent extends Activity {

	ArrayList<String> list = new ArrayList<String>();
	ListView lv;
	int SelRno;
	SQLiteDatabase mydatabase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete_student);

		lv = (ListView) findViewById(R.id.listViewDeleteStudents);

		mydatabase = openOrCreateDatabase("StudentAttendance",MODE_PRIVATE,null);
		
		if(isTableExists(mydatabase,"Student"))
			refreshList();
		else
	    	Toast.makeText(DeleteStudent.this, "No records found...", Toast.LENGTH_SHORT).show();

		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				SelRno = Integer.parseInt(((String) parent.getItemAtPosition(position)).substring(0, 1));
				
				AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

				builder.setTitle("Confirm");
				builder.setMessage("Are you sure?");

				builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

				    public void onClick(DialogInterface dialog, int which) {
				        
				    	mydatabase.execSQL("DELETE FROM Student WHERE RollNo="+SelRno+"");
				    	mydatabase.execSQL("DELETE FROM Attendance WHERE RollNo="+SelRno+"");
				    	Toast.makeText(DeleteStudent.this, "RollNo. " + SelRno+" deleted...", Toast.LENGTH_SHORT).show();
				    	refreshList();
				        dialog.dismiss();
				    }
				});

				builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

				    @Override
				    public void onClick(DialogInterface dialog, int which) {

				        // Do nothing
				        dialog.dismiss();
				    }
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_student, menu);
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
	
	void refreshList()
	{
		Cursor cursor = mydatabase.rawQuery("Select * from Student",null);
		list = new ArrayList<String>();
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) 
		{
			int rno = cursor.getInt(cursor.getColumnIndex("RollNo"));
			String name = cursor.getString(cursor.getColumnIndex("Name"));

			list.add(rno + "  -   " + name);
			cursor.moveToNext();
		}

		ArrayAdapter<String> adp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , list);

		lv.setAdapter(adp);
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
