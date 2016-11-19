package model;

import java.util.ArrayList;

/**
 * Created by Asus 1 on 6/24/2016.
 */
public class CurrentLectureModel {

    public static ArrayList<CurrentLectureModel> currentLectureModelArrayList;
    public  int section;
    public  int subject;
    public  int classes;

    public CurrentLectureModel(int Class, int section, int Subject )
    {
        this.subject=Subject;
        this.section=section;
        this.classes=Class;
    }


}
