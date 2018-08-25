package com.example.android_project;

public class StudentRecord 
{
	String name = null;
	int rollno = 0;
	boolean selected = false;
	
	public StudentRecord(int rollno,String name, boolean selected) 
	{
		super();
		this.name = name;
		this.rollno = rollno;
		this.selected = selected;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRollno() {
		return rollno;
	}

	public void setRollno(int rollno) {
		this.rollno = rollno;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
