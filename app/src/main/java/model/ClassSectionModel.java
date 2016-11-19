package model;

import java.util.ArrayList;

/**
 * Created by Asus 1 on 8/5/2016.
 */
public class ClassSectionModel {


    public int class_id;
    public String c_name;
    public int sec_id;
    public String section;
    public static ArrayList<ClassSectionModel> classSectionModelArrayList;

    public ClassSectionModel(int class_id,String c_name,int sec_id,String section)
    {
        this.class_id=class_id;
        this.c_name=c_name;
        this.sec_id=sec_id;
        this.section=section;
    }
}
