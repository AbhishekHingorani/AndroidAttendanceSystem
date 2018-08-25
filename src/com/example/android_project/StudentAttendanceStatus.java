package com.example.android_project;

public class StudentAttendanceStatus 
{
	String name = null;
	int rollno = 0;
	float progress = 0;

	public StudentAttendanceStatus(String name, int rollno, float percent) {
		super();
		this.name = name;
		this.rollno = rollno;
		this.progress = percent;
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

	public float getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

}
