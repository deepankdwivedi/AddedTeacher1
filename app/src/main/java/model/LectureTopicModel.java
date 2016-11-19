package model;

import java.util.ArrayList;

public class LectureTopicModel {
	
	public static ArrayList<LectureTopicModel> lecture_topic_content;
	public int lect_no;
	public int topic_id;
	public String topic;
	public String objective;
	public String intro;
	public String quest;
	public String activity;
	public String assignment;
	
	
	public LectureTopicModel(int  i, int j, String topic, String objective, String intro, String quest, String activity, String assignment) {
		this.lect_no=i;
		this.topic_id=j;
		this.topic=topic;
		this.objective=objective;
		this.intro=intro;
		this.quest=quest;
		this.activity=activity;
		this.assignment=assignment;
	}

}
