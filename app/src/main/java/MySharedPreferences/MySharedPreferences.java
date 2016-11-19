package MySharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Asus 1 on 6/24/2016.
 */
public class MySharedPreferences {

     Context context;

    public MySharedPreferences(Context context)
    {
     this.context=context;
    }
    private static final String PREF_NAME = "TeacherLogin";
    public static final String TEACHER_ID = "tea_id";
    public static final String SCHOOL_ID = "sch_id";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String IS_DATE_CORRECT = "isDateCorrect";



     public  String getTeacherIdSharedPreferencesId()
     {
         SharedPreferences preferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
         String string=preferences.getString(TEACHER_ID,"hemant");
         return string;
     }

    public  String getSchoolIdSharedPreferencesId()
    {
        SharedPreferences preferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        String string=preferences.getString(SCHOOL_ID,"noschoolid");
        return string;
    }
    public  String getisDateCorrect()
    {
        SharedPreferences preferences=context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        String string=preferences.getString(IS_DATE_CORRECT,"");
        return string;
    }



}
