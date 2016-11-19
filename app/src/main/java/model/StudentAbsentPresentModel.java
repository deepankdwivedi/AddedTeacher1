package model;

import java.util.ArrayList;

/**
 * Created by Asus 1 on 7/2/2016.
 */
public class StudentAbsentPresentModel {

    public static  ArrayList<String> presentDaysArrayList;
    public static ArrayList<StudentAbsentPresentModel> absentDaysArrayList;
    public String reason;
    public  String date;

    public StudentAbsentPresentModel(String date, String reason)
    {
        this.date=date;
        this.reason=reason;
    }

    public StudentAbsentPresentModel(String date)
    {
        this.date=date;
    }
}
