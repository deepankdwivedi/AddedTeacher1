package model;
import java.util.ArrayList;


public class Assignment {
	public String class_value,sec_value,subject;
	public static ArrayList<Assignment> classSection;
	public static ArrayList<Assignment> classes;
	public static ArrayList<Assignment> subjects;
	public static ArrayList<Assignment> topics;
	String clas;
	 public Assignment(String class_value,String sec_value) {
		this.class_value=class_value;
		this.sec_value=sec_value;
		// TODO Auto-generated constructor stub
	} 
	 public Assignment(String subject)
	 {
		 this.subject=subject;
	 }

	public static ArrayList<Assignment> getClasses() {
		return classes;
	}
	public static void setClasses(ArrayList<Assignment> classes) {
		Assignment.classes = classes;
	}
	public static ArrayList<Assignment> getSubjects() {
		return subjects;
	}
	public static void setSubjects(ArrayList<Assignment> subjects) {
		Assignment.subjects = subjects;
	}
	public static ArrayList<Assignment> getTopics() {
		return topics;
	}
	public static void setTopics(ArrayList<Assignment> topics) {
		Assignment.topics = topics;
	}
	public String getClas() {
		return clas;
	}
	public void setClas(String clas) {
		this.clas = clas;
	}
	
	

}
