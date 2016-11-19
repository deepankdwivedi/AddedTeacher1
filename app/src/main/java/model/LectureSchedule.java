package model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ASUS on 29-03-2016.
 */
public class LectureSchedule {




    public static ArrayList<LectureSchedule> timeTableLectures;
    public static ArrayList<Integer> chapterid;
    public static ArrayList<LectureSchedule> lectures;
    public static ArrayList<LectureSchedule> nextDayLectures;
    public static ArrayList<Integer> nextDayTopics;
    public static ArrayList<Integer> topic;
    public static ArrayList<LectureSchedule> otherLectureEndArrayList;
    public static ArrayList<LectureSchedule> lectureEndArrayList;
    public int lec_no;
    //public  int lec_no;
    public int topic_id;
    public String topicname;
    public  String ch_name;
    public  int lec_order;
    public  int complete;
    public int lec_taken;
    public  int class_id;
    public  int sec_id;
    public  int sub_id;

    public LectureSchedule(int class_id, int sec_id, int sub_id, int lecture, int ch_id, ArrayList<Integer> topics, String ch_name) {
        this.class_id=class_id;
        this.sec_id=sec_id;
        this.sub_id=sub_id;
        this.lecture=lecture;
        this.ch_id=ch_id;
        this.topic=topics;
        this.ch_name=ch_name;
    }

    public LectureSchedule(int topic_id, String topic) {
        this.topicname=topic;
        this.topic_id=topic_id;
    }
    public LectureSchedule(String topic,int topic_id, int ch_id,int lec_no) {
        this.topicname=topic;
        this.lec_no=lec_no;
        this.topic_id=topic_id;
        this.ch_id=ch_id;
    }

    public int getLec_order() {
        return lec_order;
    }

    public void setLec_order(int lec_order) {
        this.lec_order = lec_order;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int getLec_taken() {
        return lec_taken;
    }

    public void setLec_taken(int lec_taken) {
        this.lec_taken = lec_taken;
    }

    public int getLec_total() {
        return lec_total;
    }

    public void setLec_total(int lec_total) {
        this.lec_total = lec_total;
    }

    public  int lecture;
    public  int ch_id;
    public int lec_total;




    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getSec_id() {
        return sec_id;
    }

    public void setSec_id(int sec_id) {
        this.sec_id = sec_id;
    }

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }


    public int getCh_id() {
        return ch_id;
    }

    public void setCh_id(int ch_id) {
        this.ch_id = ch_id;
    }

    public LectureSchedule(int class_id, int sec_id, int sub_id, int lecture) {
        this.class_id = class_id;
        this.sec_id=sec_id;
        this.sub_id=sub_id;
        this.lecture=lecture;


    }

    public LectureSchedule(int ch_id,int lec_total,int lec_taken ,int complete, int lec_order)
                {
                    this.ch_id=ch_id;
                    this.lec_total=lec_total;
                    this.lec_taken=lec_taken;
                    this.complete=complete;
                    this.lec_order=lec_order;


    }
}
