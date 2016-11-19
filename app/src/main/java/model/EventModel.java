package model;

import java.util.ArrayList;

public class EventModel {
	public static ArrayList<EventModel> eventarray_list;
	public String evnt_name;
	public String evnt_place;
	public String evnt_timmings;
	public String desc;
	
	
		private String image;

	public EventModel(String s, String s1, String s2, String s3) {
		this.evnt_name=s;
		this.evnt_place=s1;
		this.evnt_timmings=s3;
		this.desc=s2;


	}

	public String getEvnt_name() {
		return evnt_name;
	}
	public void setEvnt_name(String evnt_name) {
		this.evnt_name = evnt_name;
	}
	public String getEvnt_place() {
		return evnt_place;
	}
	public void setEvnt_place(String evnt_place) {
		this.evnt_place = evnt_place;
	}
	public String getEvnt_timmings() {
		return evnt_timmings;
	}
	public void setEvnt_timmings(String evnt_timmings) {
		this.evnt_timmings = evnt_timmings;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc1) {
		this.desc = desc1;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	

}
