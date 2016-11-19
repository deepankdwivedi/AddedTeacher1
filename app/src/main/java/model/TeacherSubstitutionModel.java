package model;

import java.util.ArrayList;

/**
 * Created by Asus 1 on 9/2/2016.
 */
public class TeacherSubstitutionModel {

    public static ArrayList<TeacherSubstitutionModel> teacherSubstitutionModelArrayList;
    public int class_id,sec_id,sub_id,id,substitution_status;
    public String lecture,absentTeacherUsername;

    public TeacherSubstitutionModel(int class_id,int id,int substitution_status,int sec_id,int sub_id,String lecture,String absentTeacherUsername)
    {
        this.sec_id=sec_id;
        this.sub_id=sub_id;
        this.substitution_status=substitution_status;
        this.class_id=class_id;
        this.lecture=lecture;
        this.id=id;
        this.absentTeacherUsername=absentTeacherUsername;
    }
}
