package model;

import java.util.ArrayList;

/**
 * Created by Asus 1 on 7/4/2016.
 */
public class TimeTableScheduleModel {

    int day,lecture,class_id,sec_id,sub_id;
    public static  ArrayList<TimeTableScheduleModel> timeTableScheduleModelArrayList;


    public TimeTableScheduleModel(int day,int lecture,int class_id,int sec_id,int sub_id)
    {
        this.day=day;
        this.lecture=lecture;
        this.class_id=class_id;
        this.sec_id=sec_id;
        this.sub_id=sub_id;
    }

}
