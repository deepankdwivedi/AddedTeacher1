package model;

import java.util.ArrayList;

public class ChapterLectureModel {
	public String ch_name;
	public int chap__id;
	public int totalLectures,sub_id;
	public static ArrayList<ChapterLectureModel> chapter_totalLectures;
	
	
	public ChapterLectureModel(int i, String ch_name, int lec_total,int sub_id)
	{
		this.ch_name=ch_name;
		this.chap__id=i;
		this.totalLectures=lec_total;
		this.sub_id=sub_id;
	}


}
