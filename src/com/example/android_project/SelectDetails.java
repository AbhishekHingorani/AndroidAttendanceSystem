package com.example.android_project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;






import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class SelectDetails extends Activity {

	Button btnAddNewSub, btnNext;
	Calendar myCalendar = Calendar.getInstance();
	EditText ETDate;
	Spinner spSubject;
	Cursor cursor;
	SQLiteDatabase mydatabase;
	private static final int TEXT_ID = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_details);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		spSubject = (Spinner) findViewById(R.id.spinnerSubject);
		
		btnAddNewSub = (Button) findViewById(R.id.buttonAddNewSubject);
		btnAddNewSub.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) 
			{
				showDialog(0);
			}
		});
		
		
		mydatabase = openOrCreateDatabase("StudentAttendance",MODE_PRIVATE,null);
		
		addValuesToSpinner();
		
		ETDate = (EditText) findViewById(R.id.editTextDate);
		
		String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);//, Locale.US);
        ETDate.setText(sdf.format(myCalendar.getTime()));
		
		ETDate.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				new DatePickerDialog(SelectDetails.this, date, myCalendar
	                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
	                 myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			}
	    });
		
		btnNext = (Button) findViewById(R.id.buttonNext);
		btnNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				if(ETDate.getText().toString().equals("") || spSubject.getSelectedItem().toString().equals(""))
				{
					Toast.makeText(SelectDetails.this, "Select Subject and Date...", Toast.LENGTH_LONG).show();
				}
				else
				{
					Intent i = new Intent(SelectDetails.this,MarkAttendance.class);
					i.putExtra("Date", ETDate.getText().toString());
					i.putExtra("Subject", spSubject.getSelectedItem().toString());
					startActivity(i);
					finish();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_details, menu);
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
	
	DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() 
	{
	    @Override
	    public void onDateSet(DatePicker view, int year, int monthOfYear,
	            int dayOfMonth) {
	        // TODO Auto-generated method stub
	        myCalendar.set(Calendar.YEAR, year);
	        myCalendar.set(Calendar.MONTH, monthOfYear);
	        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
	        
	        String myFormat = "dd/MM/yyyy"; //In which you need put here
	        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);//, Locale.US);
	        ETDate.setText(sdf.format(myCalendar.getTime()));
	    }

	};
	
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
	
	 /**
     * Called to create a dialog to be shown.
     */
    @Override
    protected Dialog onCreateDialog(int id) {

    	switch (id) {
    	case 0:
    		return createExampleDialog();
    	default:
    		return null;
    	}
    }
 
    /**
     * If a dialog has already been created,
     * this is called to reset the dialog
     * before showing it a 2nd time. Optional.
     */
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        if(id==0)
        {
        	EditText text = (EditText) dialog.findViewById(TEXT_ID); // Clear the input box.
            text.setText("");
        }
    }
 
    /**
     * Create and return an example alert dialog with an edit text box.
     */
    private Dialog createExampleDialog() {
 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Subjects");
        builder.setMessage("Enter New Subject :");
 
         // Use an EditText view to get user input.
         final EditText input = new EditText(this);
         input.setId(TEXT_ID);
         builder.setView(input);
 
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                
                if(value.equals("") || value.equals(" ") || value.equals("  ") || value.equals("   "))
                {
                	Toast.makeText(SelectDetails.this, "Invalid Text...", Toast.LENGTH_SHORT).show();
                	return;
                }
                
                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Subjects(subject VARCHAR PRIMARY KEY);");
        		mydatabase.execSQL("INSERT INTO Subjects VALUES('"+value+"');");
        		//mydatabase.close();
        		addValuesToSpinner();
        		Toast.makeText(SelectDetails.this, "Inserted", Toast.LENGTH_SHORT).show();
                return;
            }
        });
 
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
 
        return builder.create();
    }
    
    void addValuesToSpinner()
    {
    	if(isTableExists(mydatabase, "Subjects"))
		{
			cursor = mydatabase.rawQuery("Select * from Subjects",null);

			ArrayList<String> subjectList = new ArrayList<String>();

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				String name = cursor.getString(cursor.getColumnIndex("subject"));
				subjectList.add(name);

				cursor.moveToNext();
			}

			// create an ArrayAdaptar from the String Array
			ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjectList); //selected item will look like a spinner set from XML
			adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			adp.notifyDataSetChanged();
			spSubject.setAdapter(adp);
		}
		else
		{
			showDialog(0);
		}
    }
}
