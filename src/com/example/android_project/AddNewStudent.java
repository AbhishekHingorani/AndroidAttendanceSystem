package com.example.android_project;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNewStudent extends Activity implements OnClickListener{

	EditText ETrollNo, ETName, ETPhoneNo;
	Button btnSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_student);
		
		ETrollNo = (EditText) findViewById(R.id.editTextRollNo); 
		ETName = (EditText) findViewById(R.id.editTextName);  
		ETPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
		
		btnSubmit = (Button) findViewById(R.id.buttonSubmit);
		btnSubmit.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_student, menu);
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

	@Override
	public void onClick(View v) 
	{
		if(ETrollNo.getText().toString().equals("")  || ETName.getText().toString().equals(""))
		{
			Toast.makeText(this, "All Fields must be filled...", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(ETName.length() > 20)
		{
			Toast.makeText(this, "Name can't be more than 20 characters...", Toast.LENGTH_SHORT).show();
			return;
		}
		
		SQLiteDatabase mydatabase = openOrCreateDatabase("StudentAttendance",MODE_PRIVATE,null);
		
		mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Student(RollNo int PRIMARY KEY,Name VARCHAR,PhoneNo VARCHAR);");
		Cursor c = mydatabase.rawQuery("select * from Student where rollno="+Integer.parseInt(ETrollNo.getText().toString()), null);
		
		if(c.getCount() > 0)
		{
			Toast.makeText(this, "This roll no. is already assigned... ", Toast.LENGTH_SHORT).show();
			ETrollNo.setText("");
			ETrollNo.requestFocus();
		}
		else
		{
			mydatabase.execSQL("INSERT INTO Student VALUES("+Integer.parseInt(ETrollNo.getText().toString())+",'"+ETName.getText().toString()+"','"+ETPhoneNo.getText().toString()+"');");
			mydatabase.close();
			Toast.makeText(this, "Inserted...", Toast.LENGTH_SHORT).show();
			ETrollNo.setText("");
			ETPhoneNo.setText("");
			ETName.setText("");
			ETrollNo.requestFocus();
		}
	}
}
