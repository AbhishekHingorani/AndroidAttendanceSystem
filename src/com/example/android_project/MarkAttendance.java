package com.example.android_project;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MarkAttendance extends Activity {

	MyCustomAdapter dataAdapter = null;
	Cursor cursor;
	Button btnRegister;
	String date, subject;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark_attendance);

		Intent i = getIntent();
		date = i.getStringExtra("Date");
		subject = i.getStringExtra("Subject");
		
		btnRegister = (Button) findViewById(R.id.findSelected);
		
		displayListView();
		checkButtonClick();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mark_attendance, menu);
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

		if(isTableExists(mydatabase, "Student"))
		{
			cursor = mydatabase.rawQuery("Select * from Student",null);

			// Array list of Students
			ArrayList<StudentRecord> studentList = new ArrayList<StudentRecord>();
			StudentRecord studRec;

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				int rollNo = cursor.getInt(cursor.getColumnIndex("RollNo"));
				String name = cursor.getString(cursor.getColumnIndex("Name"));

				studRec = new StudentRecord(rollNo, name, false);
				studentList.add(studRec);

				cursor.moveToNext();
			}

			// create an ArrayAdaptar from the String Array
			dataAdapter = new MyCustomAdapter(this, R.layout.student_record,studentList);
			ListView listView = (ListView) findViewById(R.id.listView1);

			// Assign adapter to ListView
			listView.setAdapter(dataAdapter);

			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// When clicked, show a toast with the TextView text
					StudentRecord StudRec = (StudentRecord) parent
							.getItemAtPosition(position);
				}
			});
		}
		else
		{
			Toast.makeText(MarkAttendance.this, "No records found...", Toast.LENGTH_SHORT).show();
			btnRegister.setText("Add new student");
		}
	}

	private class MyCustomAdapter extends ArrayAdapter<StudentRecord> {

		private ArrayList<StudentRecord> studList;

		public MyCustomAdapter(Context context, int textViewResourceId,
				ArrayList<StudentRecord> studList) {
			super(context, textViewResourceId, studList);
			this.studList = new ArrayList<StudentRecord>();
			this.studList.addAll(studList);
		}

		private class ViewHolder {
			TextView rollNo;
			CheckBox name;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.student_record, null);

				holder = new ViewHolder();
				holder.rollNo = (TextView) convertView.findViewById(R.id.textViewRollNo);
				holder.name = (CheckBox) convertView.findViewById(R.id.checkBoxName);
				convertView.setTag(holder);

				holder.name.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						StudentRecord student = (StudentRecord) cb.getTag();
						
						student.setSelected(cb.isChecked());
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			StudentRecord student = studList.get(position);
			holder.rollNo.setText(" (" + student.getRollno() + ")");
			holder.name.setText(student.getName());
			holder.name.setChecked(student.isSelected());
			holder.name.setTag(student);

			return convertView;
		}

	}

	private void checkButtonClick() {


		btnRegister = (Button) findViewById(R.id.findSelected);
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(btnRegister.getText().equals("Register"))
				{
					SQLiteDatabase mydatabase = openOrCreateDatabase("StudentAttendance",MODE_PRIVATE,null);
					mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Attendance(RollNo int ,subject VARCHAR, date VARCHAR, isPresent CHAR);");

					ArrayList<StudentRecord> studList = dataAdapter.studList;
					for(int i=0;i<studList.size();i++){
						StudentRecord student = studList.get(i);
						int rno = student.getRollno();
						char selected = 'A';
						if(student.isSelected()){
							selected = 'P';
						}
						mydatabase.execSQL("INSERT INTO Attendance VALUES("+rno+",'"+subject+"','"+date+"','"+selected+"');");
					}

					Toast.makeText(getApplicationContext(),"Attendance inserted successfully", Toast.LENGTH_LONG).show();

					mydatabase.close();
					btnRegister.setText("Return to Home");
				}
				else if(btnRegister.getText().equals("Add new student"))
				{
					startActivity(new Intent(MarkAttendance.this,AddNewStudent.class));
				}
				else
				{
					startActivity(new Intent(MarkAttendance.this,Home.class));
				}
			}
		});

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
