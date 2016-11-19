package model;

import java.util.ArrayList;

public class CalendarCollection {
	public String presentDate="";
	public String message="";
	public String holidayDate="";
	
	
	public static ArrayList<CalendarCollection> date_collection_arr;
	public static ArrayList<String> present_dates;
	public static ArrayList<CalendarCollection> holiday_dates;
		
	
	
	public CalendarCollection(String presentDate) {
		
		this.presentDate = presentDate;
	}
	public CalendarCollection(String holidayDate, String event) {
		
		this.holidayDate = holidayDate;
		this.message=event;
	}

	public String getPresentDate() {
		return presentDate;
	}
	public void setPresentDate(String presentDate) {
		this.presentDate = presentDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public CalendarCollection() {
		// TODO Auto-generated constructor stub
	}

}
