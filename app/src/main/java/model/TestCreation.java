package model;

import java.util.ArrayList;





public class TestCreation {
	public String class_value,sec_value,subject;
	public static ArrayList<TestCreation> classSection;

	public static ArrayList<TestCreation> classes;
	public static ArrayList<TestCreation> subjects;
	public static ArrayList<TestCreation> topics;
	String clas;
	 public TestCreation(String class_value,String sec_value) {
			this.class_value=class_value;
			this.sec_value=sec_value;
	 }
	 public  TestCreation(String subject) {
		// TODO Auto-generated constructor stub
		 this.subject=subject;
	}
	 {
		 this.subject=subject;
	 }
	public static ArrayList<TestCreation> getClasses() {
		return classes;
	}
	public static void setClasses(ArrayList<TestCreation> classes) {
		TestCreation.classes = classes;
	}
	public static ArrayList<TestCreation> getSubjects() {
		return subjects;
	}
	public static void setSubjects(ArrayList<TestCreation> subjects) {
		TestCreation.subjects = subjects;
	}
	public static ArrayList<TestCreation> getTopics() {
		return topics;
	}
	public static void setTopics(ArrayList<TestCreation> topics) {
		TestCreation.topics = topics;
	}
	public String getClas() {
		return clas;
	}
	public void setClas(String clas) {
		this.clas = clas;
	}
	
	

}
