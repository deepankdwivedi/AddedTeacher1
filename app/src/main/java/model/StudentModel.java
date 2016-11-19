package model;

public class StudentModel {
	public String name;
	public String rollno;

	public String getUsername() {
		return username;
	}

	public String username;

	public Boolean getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(Boolean attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}

	public Boolean attendanceStatus;

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRollno() {
		return rollno;
	}

	public void setRollno(String rollno) {
		this.rollno = rollno;
	}

	
}
