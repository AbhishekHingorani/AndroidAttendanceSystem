package com.example.android_project;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ViewAttendance extends Activity {

	CustomAttendanceAdapter dataAdapter = null;
	Cursor cursor;
	ArrayList<Integer> rollNoList;
	int rollNo;
	float percent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_attendance);
		
		displayListView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_attendance, menu);
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

	private void displayListView() {

		SQLiteDatabase mydatabase = openOrCreateDatabase("StudentAttendance",MODE_PRIVATE,null);

		if(isTableExists(mydatabase, "Attendance"))
		{
			cursor = mydatabase.rawQuery("Select * from Attendance",null);

			// Array list of Students
			ArrayList<StudentAttendanceStatus> studentList = new ArrayList<StudentAttendanceStatus>();
			StudentAttendanceStatus studRec;
			
			rollNoList = new ArrayList<Integer>();
			
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) 
			{
				rollNo = cursor.getInt(cursor.getColumnIndex("RollNo"));
				
				while(isUniqueRollNo() == false && !cursor.isAfterLast())
				{
					cursor.moveToNext();
					
					if(!cursor.isAfterLast())
						rollNo = cursor.getInt(cursor.getColumnIndex("RollNo"));
				}
				
				if(cursor.isAfterLast())
					break;
				
				Cursor myCursor = mydatabase.rawQuery("Select * from Attendance where RollNo="+rollNo,null);
				myCursor.moveToFirst();
				float total = myCursor.getCount();
				myCursor = mydatabase.rawQuery("Select * from Attendance where RollNo="+rollNo+" and isPresent='P'",null);
				myCursor.moveToFirst();
				float present = myCursor.getCount();
				percent = (present/total)*100;
				
				myCursor = mydatabase.rawQuery("Select Name from Student where RollNo="+rollNo,null);
				myCursor.moveToFirst();
				String name = myCursor.getString(0);//cursor.getColumnIndex("Name"));

				studRec = new StudentAttendanceStatus(name, rollNo, percent);
				studentList.add(studRec);

				cursor.moveToNext();
			}

			// create an ArrayAdaptar from the String Array
			dataAdapter = new CustomAttendanceAdapter(this, R.layout.student_attendance_status,studentList);
			ListView listView = (ListView) findViewById(R.id.listView1);

			// Assign adapter to ListView
			listView.setAdapter(dataAdapter);
			
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// When clicked, show a toast with the TextView text
					StudentAttendanceStatus StudRec = (StudentAttendanceStatus) parent
							.getItemAtPosition(position);
					Intent i = new Intent(ViewAttendance.this,SubjectWiseAttendance.class);
					i.putExtra("rno", StudRec.getRollno());
					startActivity(i);
				}
			});
		}
		else
		{
			Toast.makeText(ViewAttendance.this, "No records found...", Toast.LENGTH_SHORT).show();
		}
	}


	private class CustomAttendanceAdapter extends ArrayAdapter<StudentAttendanceStatus> {

		private ArrayList<StudentAttendanceStatus> studList;

		public CustomAttendanceAdapter(Context context, int textViewResourceId,
				ArrayList<StudentAttendanceStatus> studList) {
			super(context, textViewResourceId, studList);
			this.studList = new ArrayList<StudentAttendanceStatus>();
			this.studList.addAll(studList);
		}

		private class ViewHolder {
			TextView rollNo;
			TextView name;
			TextView progress;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.student_attendance_status, null);

				holder = new ViewHolder();
				holder.rollNo = (TextView) convertView.findViewById(R.id.textView_S_Rno);
				holder.name = (TextView) convertView.findViewById(R.id.textView_S_Name);				
				holder.progress = (TextView) convertView.findViewById(R.id.textView_percent);
				
				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			StudentAttendanceStatus student = studList.get(position);
			holder.rollNo.setText(" (" + student.getRollno() + ")");
			holder.name.setText(student.getName());
			
			if(student.getProgress() >= 75)
				holder.progress.setTextColor(Color.rgb(0, 90, 0));
			else if(student.getProgress() < 75 && student.getProgress() > 50)
				holder.progress.setTextColor(Color.rgb(245, 127, 23));
			else
				holder.progress.setTextColor(Color.RED);
			
			holder.progress.setText(new DecimalFormat("##.##").format(student.getProgress()) + "%");
			//holder.name.setTag(student);

			return convertView;
		}
		
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
	
	boolean isUniqueRollNo()
	{
		boolean check;
		if(rollNoList.contains(rollNo))
			check = false;
		else
		{
			check = true;
			rollNoList.add(rollNo);
		}
		
		return check;
	}
}
