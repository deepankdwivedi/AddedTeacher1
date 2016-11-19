package com.added.addedteacher.database;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.added.addedteacher.Teacher;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import MySharedPreferences.MySharedPreferences;
import model.CalendarCollection;
import model.ChapterLectureModel;
import model.Circular;
import model.ClassSectionModel;
import model.Contact;
import model.CurrentLectureModel;
import model.LectureSchedule;
import model.LectureTopicModel;
import model.ListModel;
import model.ResponseReceiveModel;
import model.StudentAbsentPresentModel;
import model.StudentModel;
import model.SyncModel;
import model.TeacherSubstitutionModel;
import model.UploadRowDataModel;
import model.ViewAssignmentModel;

public class MyDatabase extends SQLiteAssetHelper {

    String[] class_ids;
    String[] sub_ids;
    String[] sec_ids;
    String class_names[];
    String sec_names[];
    String sub_names[];
    Context context;
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");
    public static MyDatabase instance;

    public MyDatabase(Context homeFragment) {

        super(homeFragment, DBConstants.DATABASE_NAME, null, DBConstants.DATABASE_VERSION);
        // TODO Auto-generated constructor stub
        this.context = homeFragment;
    }
    public static synchronized MyDatabase getDatabaseInstance(Context context)
    {
        if(instance==null)
            instance=new MyDatabase(context);
        return  instance;
    }



    public String getTimeStamp(){

        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        return ts;

    }

    public Contact Get_ContactDetails() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM teacher", null);
        if (cursor != null && cursor.moveToFirst()) {
            Contact cont = new Contact(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getInt(6),
                    cursor.getString(7), cursor.getString(8),
                    cursor.getInt(9), cursor.getString(10));
            // return contact
            cursor.close();
            db.close();

            return cont;

        }
        return null;
    }


    public ArrayList<String[]> getSubjectSection() {


        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select " + DBConstants.CLASS_ID + ", "
                        + DBConstants.SEC_ID + ", "
                        + DBConstants.SUB_ID
                        + " FROM  "
                        + DBConstants.TEACHER_TABLE
                        + " INNER JOIN "
                        + DBConstants.ASSIGN_SUB_TEA_TABLE
                        + " ON "
                        + DBConstants.TEACHER_TABLE + ".username = "
                        + DBConstants.ASSIGN_SUB_TEA_TABLE + ".username"
                , null);
        class_ids = new String[cursor.getCount()];
        sec_ids = new String[cursor.getCount()];
        sub_ids = new String[cursor.getCount()];


        if (cursor != null && cursor.moveToFirst()) {
            do {
                String class_id = cursor.getString(cursor.getColumnIndex(DBConstants.CLASS_ID));
                String sec_id = cursor.getString(cursor.getColumnIndex(DBConstants.SEC_ID));
                String sub_id = cursor.getString(cursor.getColumnIndex(DBConstants.SUB_ID));
                Log.v("class_id", class_id);
                Log.v("sec_id", sec_id);
                Log.v("sub_id", sub_id);
                class_ids[cursor.getPosition()] = class_id;
                sec_ids[cursor.getPosition()] = sec_id;
                sub_ids[cursor.getPosition()] = sub_id;
            } while (cursor.moveToNext());
        }
        cursor.close();
        class_names = new String[cursor.getCount()];
        sub_names = new String[cursor.getCount()];
        sec_names = new String[cursor.getCount()];


        //getting class values

        for (int i = 0; i < class_ids.length; i++) {
            Cursor cursor1 = db.rawQuery("Select " + DBConstants.CLASS_VALUE
                            + " FROM  "
                            + DBConstants.CLASS_TABLE
                            + " WHERE "
                            + DBConstants.CLASS_ID
                            + " = "
                            + class_ids[i]
                    , null);

            if (cursor1 != null && cursor1.moveToFirst()) {
                class_names[i] = cursor1.getString(cursor1.getColumnIndex(DBConstants.CLASS_VALUE));
                Log.v("class_value", class_names[i]);
            }
            cursor1.close();
        }


        //getting sub name

        for (int i = 0; i < sub_ids.length; i++) {
            Cursor cursor1 = db.rawQuery("Select " + DBConstants.SUB_NAME
                            + " FROM  "
                            + DBConstants.SUB_TABLE
                            + " WHERE "
                            + DBConstants.SUB_ID
                            + " = "
                            + sub_ids[i]
                    , null);

            if (cursor1 != null && cursor1.moveToFirst()) {
                sub_names[i] = cursor1.getString(cursor1.getColumnIndex(DBConstants.SUB_NAME));
                Log.v("sub_names", sub_names[i]);
            }
            cursor1.close();

        }

        // getting sections

        for (int i = 0; i < sub_ids.length; i++) {
            Cursor cursor1 = db.rawQuery("Select " + DBConstants.SECTION_NAME
                            + " FROM  "
                            + DBConstants.SEC_TABLE
                            + " WHERE "
                            + DBConstants.SEC_ID
                            + " = "
                            + sec_ids[i]
                    , null);

            if (cursor1 != null && cursor1.moveToFirst()) {
                sec_names[i] = cursor1.getString(cursor1.getColumnIndex(DBConstants.SECTION_NAME));
                Log.v("sec_names", sec_names[i]);
            }
            cursor1.close();

        }
        ArrayList<String[]> subject_section = new ArrayList<String[]>();
        subject_section.add(sub_names);
        subject_section.add(class_names);
        subject_section.add(sec_names);
        db.close();
        return subject_section;

    }

    public ArrayList<ViewAssignmentModel> get_assignment() {

        ArrayList<ViewAssignmentModel>assignment_list=new ArrayList<>();
        // Assignment.assignments_arraylist = new ArrayList<Assignment>();

        String sub_line[];
//
        SQLiteDatabase db = this.getWritableDatabase();
        // ViewAssignmentModel.ar=null;
        Cursor cursor = db.rawQuery(
                "Select class_id,sub_id,ch_id,detail,sub_line from assignment", null);
        int l = cursor.getCount();
        sub_line =  new String[l];


        if (cursor != null && cursor.moveToFirst()) {
            do {


                //ViewAssignmentModel.ar = new ArrayList<>();
                int id = cursor.getInt(0);
                Log.e("id", id+" ");
                String className=getClassName(cursor.getInt(cursor.getColumnIndex("class_id")));
                String subject=get_subject(cursor.getInt(cursor.getColumnIndex("sub_id")));
                String sub = cursor.getString(cursor.getColumnIndex("sub_line"));
                System.out.println(" className"+className+"  subject"+subject+"subject line "+sub);
                Log.e("sub_line", sub);
                //ViewAssignmentModel.ar.add(new ViewAssignmentModel(id,sub));
                assignment_list.add(new ViewAssignmentModel(className,sub,subject));


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return teacher_arr;
        //return ViewAssignmentModel.ar;
        return assignment_list;
    }

    public String getClassName(int class_id) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select c_value from class where class_id="+class_id,null);
        String c_value = null;
        if(cursor!=null && cursor.moveToFirst())
        {
            c_value=cursor.getString(cursor.getColumnIndex("c_value"));
        }
        return  c_value;
    }

    public String[] get_particular_assign_details(String heading)
    {

        Log.e("heading ",heading);
        String[] details;
        SQLiteDatabase db = this.getWritableDatabase();
        // ViewAssignmentModel.ar=null;
        Cursor cursor = db.rawQuery(
                "Select class_id,sec_id,sub_id,ch_id,sub_line,detail from assignment where sub_line='" + heading + "'", null);
        int l = cursor.getCount();
        details= new String[6];
        int class_id,sec_id,sub_id,chap_id;

//          if (cursor!=null)
//              cursor.moveToFirst();
//
//
        if (cursor!=null && cursor.moveToFirst() )
        {
            do {

                class_id = cursor.getInt(cursor.getColumnIndex("class_id"));
                Log.e("class_id",class_id+" ");
                sec_id = cursor.getInt(cursor.getColumnIndex("sec_id"));
                Log.e("sec_id",sec_id+" ");
                sub_id = cursor.getInt(cursor.getColumnIndex("sub_id"));
                Log.e("sub_id",sub_id+" ");
                chap_id = cursor.getInt(cursor.getColumnIndex("ch_id"));
                Log.e("chap_id",chap_id+" ");
                String sub_line = cursor.getString(cursor.getColumnIndex("sub_line"));
                Log.e("sub_line",sub_line);
                String detail = cursor.getString(cursor.getColumnIndex("detail"));
                Log.e("detailt",detail);

                details[0]=class_id+" ";
                details[1]=sec_id+" ";
                details[2]=sub_id+" ";
                details[3]=chap_id+" ";
                details[4]=sub_line;
                details[5]=detail;

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return details;




    }


    public String getSubjectID(String subjectName, SQLiteDatabase db) {
        //SQLiteDatabase db=this.getReadableDatabase();
        String subId = null;
        Cursor cursor1 = db.rawQuery("Select " + DBConstants.SUB_ID
                        + " FROM  "
                        + DBConstants.SUB_TABLE
                        + " WHERE "
                        + DBConstants.SUB_NAME
                        + " = \""
                        + subjectName + "\""
                , null);
        if (cursor1 != null && cursor1.moveToFirst()) {
            subId = cursor1.getString(cursor1.getColumnIndex(DBConstants.SUB_ID));
            Log.v("sec_names", subId);
        }
        cursor1.close();
        //db.close();
        return subId;

    }

    public ArrayList<ChapterLectureModel> getChaptersAndLecturesFromSubjectName(String classid, String secid, String subid) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getReadableDatabase();
        //String subjectId=getSubjectID(subjectName,db);
        ChapterLectureModel.chapter_totalLectures = new ArrayList<ChapterLectureModel>();
        Cursor cursor = db.rawQuery("Select " + DBConstants.CH_NAME + ", "
                        + DBConstants.CHAPTER_TABLE + "."
                        + DBConstants.CH_ID + ", "
                        + DBConstants.LECTURE_TABLE + "."
                        + DBConstants.LECTURE_TOTAL
                        + " FROM  "
                        + DBConstants.CHAPTER_TABLE
                        + " INNER JOIN "
                        + DBConstants.LECTURE_TABLE
                        + " ON "
                        + DBConstants.CHAPTER_TABLE + "." + DBConstants.CH_ID
                        + " = "
                        + DBConstants.LECTURE_TABLE + "." + DBConstants.CH_ID
                        + " WHERE "
                        + DBConstants.CHAPTER_TABLE + ". "
                        + DBConstants.SUB_ID
                        + " = "
                        + subid
                        + " AND "
                        + DBConstants.CHAPTER_TABLE + "."
                        + DBConstants.CLASS_ID
                        + " = "
                        + classid
                        + " AND "
                        + DBConstants.LECTURE_TABLE + "."
                        + DBConstants.SEC_ID
                        + " = "
                        + secid
                , null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ChapterLectureModel.chapter_totalLectures.add(new ChapterLectureModel(cursor.getInt(cursor.getColumnIndex(DBConstants.CH_ID)),
                        cursor.getString(cursor.getColumnIndex(DBConstants.CH_NAME)), cursor.getInt(cursor.getColumnIndex(DBConstants.LECTURE_TOTAL)),0));
                Log.v("ch_name", cursor.getString(cursor.getColumnIndex(DBConstants.CH_NAME)));
                Log.v("ch_id", cursor.getString(cursor.getColumnIndex(DBConstants.CH_ID)));
                Log.v("lec_total", cursor.getString(cursor.getColumnIndex(DBConstants.LECTURE_TOTAL)));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return ChapterLectureModel.chapter_totalLectures;
    }

	/*public ArrayList<String> getChapterIdFromChapterName(String chapterName)
    {
		
		SQLiteDatabase db=this.getReadableDatabase();
		String subjectId=getSubjectID(subjectName,db);
		ArrayList<String> chapters=new ArrayList<String>();
		Cursor cursor=db.rawQuery("Select "+DBConstants.CH_NAME
				+" FROM  "
				+DBConstants.CHAPTER_TABLE
				+" WHERE "
				+DBConstants.SUB_ID
				+" = "
				+subjectId
				, null);
		if(cursor!=null && cursor.moveToFirst())
		{
			do{
				chapters.add(cursor.getString(cursor.getColumnIndex(DBConstants.CH_NAME)));
		}while(cursor.moveToNext());
		}
		db.close();
		cursor.close();
		return null;
		
	}*/

    public ArrayList<String[]> getClassIds() {
        ArrayList<String[]> ids = new ArrayList<String[]>();
        ids.add(class_ids);
        ids.add(sec_ids);
        ids.add(sub_ids);
        return ids;

    }

    public ArrayList<LectureTopicModel> getTopicInLecture(int ch_id) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getReadableDatabase();
        LectureTopicModel.lecture_topic_content = new ArrayList<LectureTopicModel>();
        Cursor cursor = db.rawQuery("SELECT "
                + DBConstants.LECT_TOPIC + "."
                + DBConstants.LECT_NO + " ,"
                + DBConstants.TOPIC + "."
                + DBConstants.TOPIC_ID + " ,"
                + DBConstants.TOPIC + "." +
                    DBConstants.TOPIC + " ,"
                + DBConstants.TOPIC_INSIGHT + "."
                + DBConstants.OBJECTIVE + ", "
                + DBConstants.TOPIC_INSIGHT + "."
                + DBConstants.INTRO + ", "
                + DBConstants.TOPIC_INSIGHT + "."
                + DBConstants.QUEST + ", "
                + DBConstants.TOPIC_INSIGHT + "."
                + DBConstants.ACTIVITY + ", "
                + DBConstants.TOPIC_INSIGHT + "."
                + DBConstants.TEST_ASSIGNMENT
                + " FROM "
                + DBConstants.TOPIC
                + " INNER JOIN "
                + DBConstants.LECT_TOPIC
                + " ON "
                + DBConstants.TOPIC + "."
                + DBConstants.TOPIC_ID
                + " = "
                + DBConstants.LECT_TOPIC + "."
                + DBConstants.TOPIC_ID
                + " INNER JOIN "
                + DBConstants.TOPIC_INSIGHT
                + " ON "
                + DBConstants.TOPIC_INSIGHT + "."
                + DBConstants.TOPIC_ID
                + " = "
                + DBConstants.LECT_TOPIC + "."
                + DBConstants.TOPIC_ID
                + " WHERE "
                + DBConstants.TOPIC + "."
                + DBConstants.CH_ID
                + " = "
                + ch_id, null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                Log.v("lect_no", cursor.getInt(cursor.getColumnIndex(DBConstants.LECT_NO)) + "");
                Log.v("lect_no", cursor.getInt(cursor.getColumnIndex(DBConstants.TOPIC_ID)) + "");
                Log.v("lect_no", cursor.getString(cursor.getColumnIndex(DBConstants.TOPIC)) + "");
                Log.v("lect_no", cursor.getString(cursor.getColumnIndex(DBConstants.OBJECTIVE)) + "");
                Log.v("lect_no", cursor.getString(cursor.getColumnIndex(DBConstants.INTRO)) + "");
                Log.v("lect_no", cursor.getString(cursor.getColumnIndex(DBConstants.QUEST)) + "");
                Log.v("lect_no", cursor.getString(cursor.getColumnIndex(DBConstants.ACTIVITY)) + "");
                Log.v("lect_no", cursor.getString(cursor.getColumnIndex(DBConstants.TEST_ASSIGNMENT)) + "");
                LectureTopicModel.lecture_topic_content.add(new LectureTopicModel(cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7)));
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();
        return LectureTopicModel.lecture_topic_content;
    }

    public void getContentSetValues() {
        // TODO Auto-generated method stub

    }

    public String[] getSubjectNames() {
        return sub_names;
    }

    public String[] getClassNames() {
        return class_names;
    }

    public String[] getSectionName() {
        return sec_names;
    }


    public Teacher getTeachersDetails(String tea) {
        SQLiteDatabase db = this.getReadableDatabase();
        Teacher teacher = new Teacher();
        Cursor cursor = db.rawQuery("Select tea_name,sch_id,dob,address,mobile,email,join_date from teacher where username='"+tea+"'", null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("tea_name"));
            String joindate = cursor.getString(cursor.getColumnIndex("join_date"));
            String dob = cursor.getString(cursor.getColumnIndex("dob"));
            String sch_id = getSchoolNameFromSchoolId(cursor.getString(cursor.getColumnIndex("sch_id")));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String mobile = cursor.getString(cursor.getColumnIndex("mobile"));
            teacher.setName(name);
            teacher.setMember(joindate);
            teacher.setDob(dob);
            teacher.setUniv(sch_id);
            teacher.setAddress(address);
            teacher.setMail1(email);
            teacher.setContact(mobile);


        }
        return teacher;
    }

    private String getSchoolNameFromSchoolId(String sch_id) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select sch_name from school where sch_code='"+sch_id+"'",null);
        if(cursor!=null && cursor.moveToFirst())
        {
            return cursor.getString(cursor.getColumnIndex("sch_name"));
        }
        return null;
    }

    public ArrayList<StudentModel> getStudentsName(int class_id,int sec_id,String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<StudentModel> studentsName = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select stu_name,stu_rollno,stu_username from students where stu_class="+class_id+" and stu_section="+sec_id, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                StudentModel stuModel = new StudentModel();
                stuModel.setName(cursor.getString(cursor.getColumnIndex("stu_name")));
                stuModel.setRollno(cursor.getString(cursor.getColumnIndex("stu_rollno")));
                stuModel.setUsername(cursor.getString(cursor.getColumnIndex("stu_username")));
                Cursor attendanceStatusCursor=db.rawQuery("select d_id from stu_attend1 where date='"+date+"' and class_id="+class_id+" and sec_id="+sec_id,null);
                if(attendanceStatusCursor!=null && attendanceStatusCursor.moveToFirst()) {
                    String d_id = attendanceStatusCursor.getString(attendanceStatusCursor.getColumnIndex("d_id"));
                    Cursor getAttendanceStatus = db.rawQuery("select student_username_absent from stu_attend2 where d_id='" + d_id + "' and student_username_absent='" + cursor.getString(cursor.getColumnIndex("stu_username"))+"' and class_id="+class_id+" and sec_id="+sec_id, null);
                    getAttendanceStatus.moveToFirst();
                    if (getAttendanceStatus.getCount() > 0) {
                        stuModel.setAttendanceStatus(false);
                    } else {

                        stuModel.setAttendanceStatus(true);
                    }
                }
                else
                {
                    stuModel.setAttendanceStatus(false);
                }
                studentsName.add(stuModel);
            } while (cursor.moveToNext());
        }

        return studentsName;
    }

    public Boolean getSchoolRunningstatus(String dateFormatString) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "Select date_from ,date_to from sch_basic_detail_1 where date_from <="
                        + "'" + dateFormatString + "'" + "AND" + "'"
                        + dateFormatString + "'" + "<=date_to", null);
        Cursor holiday = db.rawQuery(
                "Select date_from ,date_to from holiday where date_from <="
                        + "'" + dateFormatString + "'" + "AND" + "'"
                        + dateFormatString + "'" + "<=date_to", null);
        if (cursor.getCount() > 0) {
            cursor.close();

            Cursor cursor1 = db.rawQuery(
                    "Select date from academic_calendar where date=" + "'"
                            + dateFormatString + "' " + " and  holiday=" + 1, null);
            Log.v("cursor", "this is if of cursor");
            if (cursor1.getCount() > 0) {
                Log.v("cursor", "this is if of cursor1");
                cursor1.close();
                db.close();
                return false;
            } else {
                cursor1.close();

                Cursor cursor2 = db.rawQuery(
                        "Select date from event where date=" + "'"
                                + dateFormatString + "'" + " and holiday=" + 1,
                        null);
                Log.v("cursor", "this is if of cursor 2");
                if (cursor2.getCount() > 0) {
                    cursor2.close();
                    db.close();
                    return false;
                } else {
                    cursor2.close();
                    db.close();
                    return true;
                }

            }

        } else {
            cursor.close();
            db.close();
            return false;
        }

    }

    public boolean getlectureEndPopUp(String dateFormatString) {
        SQLiteDatabase db = this.getReadableDatabase();
        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_WEEK);
        Cursor cursor = db.rawQuery("Select class_id ,sec_id ,sub_id , lecture from time_table where day=" + day + " and lecture=" + 1, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                cursor.getString(cursor.getColumnIndex("class_id"));
                cursor.getString(cursor.getColumnIndex("sub_id"));
                cursor.getString(cursor.getColumnIndex("sec_id"));
                cursor.getString(cursor.getColumnIndex("lecture"));
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }

    }

    public void getTodaysLectureConfirmation() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select ", null);
    }


    public Boolean isSunday() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select sunday_off from sch_basic_detail_2 where sunday_off=" + 1, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public Boolean getLastDay() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select month_end from sch_basic_detail_2 where month_end=" + 1, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public Boolean isSecondSaturday() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select sec_saturday from sch_basic_detail_2 where sec_saturday=" + 1, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public Boolean getLastSaturday() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select last_saturday from sch_basic_detail_2 where last_saturday=" + 1, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }


    }

    public void getDateDayLectures(Date dateFormatString) {
        SQLiteDatabase db = this.getReadableDatabase();
        Calendar c = Calendar.getInstance();
        c.setTime(dateFormatString);
        int day = c.get(Calendar.DAY_OF_WEEK);
        String tea= new MySharedPreferences(context).getTeacherIdSharedPreferencesId();

        LectureSchedule.timeTableLectures = new ArrayList<>();
        LectureSchedule.nextDayLectures = new ArrayList<>();
        /*Cursor cursor = db.rawQuery("Select class_id,sec_id,sub_id from time_table where day=" + day, null);*/
        Cursor cursor = db.rawQuery("select assign_sub_tea.class_id , assign_sub_tea.sub_id , assign_sub_tea.sec_id ," +
                " time_table.lecture from assign_sub_tea ,time_table where assign_sub_tea.class_id=time_table.class_id and " +
                " assign_sub_tea.sub_id=time_table.sub_id " +
                "and assign_sub_tea.sec_id=time_table.sec_id and assign_sub_tea.username='"+tea+"' and time_table.day="+day, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int class_id = cursor.getInt(cursor.getColumnIndex("class_id"));
                int sec_id = cursor.getInt(cursor.getColumnIndex("sec_id"));
                int sub_id = cursor.getInt(cursor.getColumnIndex("sub_id"));
                int lecture =cursor.getInt(cursor.getColumnIndex("lecture"));
                System.out.print("class_id"+class_id + " ");
                System.out.print("sec_id"+sec_id + " ");
                System.out.println("sub_id"+sub_id );
                /*Cursor teacher = db.rawQuery("select username from assign_sub_tea where class_id='" + class_id + "'" + " and sec_id='" + sec_id + "'" + " and sub_id='" + sub_id + "'", null);
                if (teacher != null && teacher.moveToFirst()) {
                    String teacherName = teacher.getString(teacher.getColumnIndex("username"));
                */ LectureSchedule.timeTableLectures.add(new LectureSchedule(class_id, sec_id, sub_id, lecture));

                /*}*/
            } while (cursor.moveToNext());
        }
       /* for (int i = 0; i < LectureSchedule.timeTableLectures.size(); i++) {

            LectureSchedule.chapterid = new ArrayList<>();
            LectureSchedule.lectures = new ArrayList<>();
            Cursor chapter_id = db.rawQuery("select ch_id from chapter where sub_id=" + LectureSchedule.timeTableLectures.get(i).getSub_id() +
                    " and class_id=" + LectureSchedule.timeTableLectures.get(i).getClass_id(), null);

            if (chapter_id != null && chapter_id.moveToFirst()) {
                do {
                    LectureSchedule.chapterid.add(chapter_id.getInt(chapter_id.getColumnIndex("ch_id")));
                } while (chapter_id.moveToNext());
            }


            for (int j = 0; j < LectureSchedule.chapterid.size(); j++) {
                Cursor x = db.rawQuery("Select ch_id,lec_total , lec_taken , complete , lec_order from lecture where sec_id=" +
                        LectureSchedule.timeTableLectures.get(i).getSec_id() + " and ch_id=" + LectureSchedule.chapterid.get(j), null);
                if (x != null && x.moveToFirst()) {
                    int lec_total = x.getInt(x.getColumnIndex("lec_total"));
                    int lect_taken = x.getInt(x.getColumnIndex("lec_taken"));
                    int complete = x.getInt(x.getColumnIndex("complete"));
                    int lect_order = x.getInt(x.getColumnIndex("lec_order"));
                    System.out.print("complete"+ complete + " ");
                    System.out.print("lect_taken"+ lect_taken + " ");
                    System.out.print("lect_order "+ lect_order + " ");
                    System.out.print("ch_id"+ x.getInt(x.getColumnIndex("ch_id")) + " ");
                    System.out.println("lect_total"+ x.getInt(x.getColumnIndex("ch_id")) + " ");
                    LectureSchedule.lectures.add(new LectureSchedule(LectureSchedule.chapterid.get(j), lec_total, lect_taken, complete, lect_order));
                }
                lec_order:
                {
                    for (int k = 1; k <= LectureSchedule.lectures.size(); k++) {

                        for (int l = 0; l < LectureSchedule.lectures.size(); l++) {


                            if (LectureSchedule.lectures.get(l).lec_order == k) {
                                // Log.v("lec_order ", LectureSchedule.lectures.get(l).lec_order + "");
                                if (LectureSchedule.lectures.get(l).complete == 0) {
                                    int ch_id = LectureSchedule.lectures.get(l).ch_id;
                                    Cursor ch_name = db.rawQuery("select ch_name from chapter where ch_id=" + ch_id, null);
                                    ch_name.moveToFirst();
                                    LectureSchedule.nextDayLectures.add(new LectureSchedule(LectureSchedule.timeTableLectures.get(i).class_id,
                                            LectureSchedule.timeTableLectures.get(i).sec_id, LectureSchedule.timeTableLectures.get(i).sub_id,
                                            LectureSchedule.timeTableLectures.get(i).lecture,  ch_id,null,ch_name.getString(ch_name.getColumnIndex("ch_name"))));
                                    Log.v("chapter to be print ", ch_name.getString(ch_name.getColumnIndex("ch_name")));
                                    LectureSchedule.lectures.clear();
                                    break lec_order;
                                }

                            }

                        }
                    }


                }

            }


        }*/


    }

    public void getTopicForNextLecture() {
        SQLiteDatabase db = this.getReadableDatabase();
        for (int i = 0; i < LectureSchedule.nextDayLectures.size(); i++) {
            LectureSchedule.nextDayTopics = new ArrayList<>();
            Cursor lecture_takenCursor = db.rawQuery("Select lec_taken from lecture where ch_id=" + LectureSchedule.nextDayLectures.get(i).ch_id, null);
            if (lecture_takenCursor != null && lecture_takenCursor.moveToFirst()) {
                LectureSchedule.nextDayLectures.get(i).lec_taken = lecture_takenCursor.getInt(lecture_takenCursor.getColumnIndex("lec_taken")) + 1;
            }
            Cursor topic_idCursor = db.rawQuery("select topic_id from topic where ch_id=" + LectureSchedule.nextDayLectures.get(i).ch_id, null);
            ArrayList<Integer> topic_idArrayList = new ArrayList<>();
            if (topic_idCursor != null && topic_idCursor.moveToFirst()) {
                do {
                    topic_idArrayList.add(topic_idCursor.getInt(topic_idCursor.getColumnIndex("topic_id")));
                } while (topic_idCursor.moveToNext());
            }
            for (int j = 0; j < topic_idArrayList.size(); j++) {
                Cursor topicToBeTaughtCursor = db.rawQuery("select lect_no,topic_id from lect_topic where lect_no=" + LectureSchedule.nextDayLectures.get(i).lec_taken +
                        " and topic_id=" + topic_idArrayList.get(j), null);
                if (topicToBeTaughtCursor != null && topicToBeTaughtCursor.moveToFirst()) {
                    do {
                        int topicid = topicToBeTaughtCursor.getInt(topicToBeTaughtCursor.getColumnIndex("topic_id"));
                        Cursor topicNameCursor = db.rawQuery("select topic from topic where topic_id=" + topicid, null);
                        if (topicNameCursor != null && topicNameCursor.moveToFirst()) {
                            LectureSchedule.nextDayTopics.add(topicid);
                            Log.v("topic to be taught", topicNameCursor.getString(topicNameCursor.getColumnIndex("topic")));


                        }
                    } while (topicToBeTaughtCursor.moveToNext());
                }
            }
            LectureSchedule.nextDayLectures.get(i).topic=LectureSchedule.nextDayTopics;


        }
    }

    public String getSchoolStartTime() {

        SQLiteDatabase db = this.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Cursor cursor = db.rawQuery("Select start_time from sch_basic_detail_1 where date_from <="
                + "'" + sdf.format(calendar.getTime()) + "'" + "AND" + "'"
                + sdf.format(calendar.getTime()) + "'" + "<=date_to", null);

        if (cursor != null && cursor.moveToFirst()) {
            String start_time = cursor.getString(cursor.getColumnIndex("start_time"));
            // Log.v("start_time",start_time);
            return start_time;
        }

        return null;
    }
    public String getSchoolEndTime() {

        SQLiteDatabase db = this.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Cursor cursor = db.rawQuery("Select end_time from sch_basic_detail_1 where date_from <="
                + "'" + sdf.format(calendar.getTime()) + "'" + "AND" + "'"
                + sdf.format(calendar.getTime()) + "'" + "<=date_to", null);

        if (cursor != null && cursor.moveToFirst()) {
            String start_time = cursor.getString(cursor.getColumnIndex("end_time"));
            // Log.v("start_time",start_time);
            return start_time;
        }

        return null;
    }

    public Boolean getSchoolStartedStatus(String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor time1 = db.rawQuery("Select start_time,end_time from sch_basic_deatil_1 where start_time<='" + time + "'" + " and " + "'" + time + "'" + "<=end_time", null);
        if (time1 != null && time1.moveToFirst()) {
            do {
                System.out.println("start_time:" + time1.getString(time1.getColumnIndex("start_time")));

            } while (time1.moveToNext());
            return true;
        } else {
            System.out.println("No Data Found");
            return false;
        }

    }

    public Boolean getCheckTodaysLectureTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select name from sqlite_master where type='table' and name='todays_lecture_table'", null);
        if (cursor.getCount() > 0) {
            System.out.println("Cursor count :"+cursor.getCount());
            System.out.println("Table  exist ");
            db.close();
            return true;
        } else {
            db.execSQL("create table todays_lecture_table(id integer primary key autoincrement,topic_id integer , start_notification_generated integer , end_notification_generated integer " +
                    ",notification_confirmed integer, chapter text ,class_id integer,ch_id integer, sec_id integer ,lecture integer,sub_id integer,lec_no integer,subject text,start_time text ,end_time text,lastupdate text)");

            ContentValues values = new ContentValues();
            values.put("table_name","todays_lecture_table");
            db.insert("added_sync",null,values);
            System.out.println("Table does not exist,table created");
            db.close();
            return false;
        }
    }


    public void deleteTodaysLectureTable() {
        SQLiteDatabase db=this.getWritableDatabase();
        System.out.println("Table is going to be deleted");
        db.execSQL("Drop table if exists todays_lecture_table");
        db.delete("added_sync","table_name='todays_lecture_table'",null);
        db.close();
    }


    public NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public boolean checkForUpdate(String primaryKey,String tableName){

        SQLiteDatabase db=this.getWritableDatabase();
        String Query = "Select id from "+tableName+" where id="+Integer.parseInt(primaryKey) ;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount()>0){
            cursor.close();
            db.close();
            return true;
        }else {
            cursor.close();
            db.close();
            return false;
        }


    }

    public void updateNewData(String primarykey,String tableName){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        for(int i=0;i< ResponseReceiveModel.responseReceiveArrayList.size();i++) {
            contentValues.put(ResponseReceiveModel.responseReceiveArrayList.get(i).columnName, ResponseReceiveModel.responseReceiveArrayList.get(i).columnValue);

        }
        contentValues.put("isSync", 1);


        db.update(tableName,contentValues,"id"+"=?",new String[]{String.valueOf(Integer.parseInt(primarykey))});
        Log.e("adil","database updated");


//        long id=db.insert("school",null,contentValues);

    }

    public void showData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor=db.query("school",new String[]{"sch_name"},null,null,null,null,null);
        cursor.moveToFirst();
        Log.e("adil","show "+cursor.getColumnCount());
            Log.e("adil",cursor.getString(cursor.getColumnIndexOrThrow("sch_name")));


    }

    public void insertNewData(String tableName){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        for(int i=0;i< ResponseReceiveModel.responseReceiveArrayList.size();i++) {
            contentValues.put(ResponseReceiveModel.responseReceiveArrayList.get(i).columnName, ResponseReceiveModel.responseReceiveArrayList.get(i).columnValue);
        }
        System.out.println("Database Inserted");
        contentValues.put("isSync", 1);

        db.insert(tableName,null,contentValues);
        db.close();
        //showData();

    }


    public void updateNewUploadData(String primaryKey, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor = db.rawQuery("select table_name from added_sync where table_name='" + tableName + "'", null);
        if (cursor.getCount() > 0) {
            for (int i = 0; i < ResponseReceiveModel.responseReceiveArrayList.size(); i++) {
                contentValues.put(ResponseReceiveModel.responseReceiveArrayList.get(i).columnName, ResponseReceiveModel.responseReceiveArrayList.get(i).columnValue);
            }
            db.update(tableName, contentValues, "cid" + "= ?", new String[]{String.valueOf(Integer.parseInt(primaryKey))});
            Log.e("adil", "database updated");
        } else {
            Log.e("adil", tableName + " Table does not exist");
        }
        cursor.close();



    }

//    Cursor cursor = db.rawQuery(
//            "Select s_id from school where s_id='xyz'");


    public void insertTodaysLecturesDatabase(Context context) {
        this.context=context;
        Calendar calendar=Calendar.getInstance();
        getDateDayLectures(calendar.getTime());
        getTopicForNextLecture();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "Select start_time from sch_basic_detail_1 where date_from <="
                        + "'" + sdf.format(calendar.getTime()) + "'" + "AND" + "'"
                        + sdf.format(calendar.getTime()) + "'" + "<=date_to", null);
        String[] start_time = new String[2];
        if(cursor!=null && cursor.moveToFirst())
        {
            start_time=cursor.getString(cursor.getColumnIndex("start_time")).split(":");
        }
        for(int i=0;i<LectureSchedule.timeTableLectures.size();i++)
        {
            String topics="";
            ContentValues contentValues=new ContentValues();

                contentValues.put("topic_id", 0);
                contentValues.put("start_notification_generated",0);
                contentValues.put("end_notification_generated",0);
                contentValues.put("lec_no",0);
                contentValues.put("chapter", " ");
                contentValues.put("class_id", LectureSchedule.timeTableLectures.get(i).class_id);
                contentValues.put("sec_id", LectureSchedule.timeTableLectures.get(i).sec_id);
                contentValues.put("sub_id", LectureSchedule.timeTableLectures.get(i).sub_id + "");
                contentValues.put("ch_id", 0);
                contentValues.put("lecture", LectureSchedule.timeTableLectures.get(i).lecture+"");
                contentValues.put("lastupdate",getTimeStamp());
                //String time = timeFormat.format(calendar.getTime());
                Cursor cursor23=db.rawQuery("Select start_time , end_time from lec_timing where lecture="+LectureSchedule.timeTableLectures.get(i).lecture,null);
                //System.out.println("current_time"+time);
                if(cursor23!=null && cursor23.moveToFirst()) {
                    contentValues.put("end_time", cursor23.getString(cursor23.getColumnIndex("end_time")));
                    contentValues.put("start_time", cursor23.getString(cursor23.getColumnIndex("start_time")));
                }
                //contentValues.put("lastupdate",getTimeStamp());
           /* mNotificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent=new Intent(context, MainActivity.class);
                    intent.putExtra("notificationid", 1);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent contentIntent = PendingIntent.getActivity(context, LectureSchedule.nextDayLectures.get(i).lecture,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Lecture Ended!")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Lecture "+LectureSchedule.nextDayLectures.get(i).lecture+" ended!"))
                            .setContentText("Lecture "+LectureSchedule.nextDayLectures.get(i).lecture+" ended!").setAutoCancel(true);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(LectureSchedule.nextDayLectures.get(i).lecture, mBuilder.build());*/
                db.insert("todays_lecture_table", null, contentValues);


        }

    }


    public Boolean getNotificationFromTodaysLectureTable(String time) {

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor end_time=db.rawQuery("select end_time from todays_lecture_table where end_time='"+time+"'",null);
        if(end_time.getCount()>0)
        {
            end_time.close();
            db.close();
            return true;
        }
        else
        {
            end_time.close();
            db.close();
            return false;
        }

    }

    public String getCurrentLecture(String time) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select lecture from lec_timing where start_time<='"+time+"' and end_time>='"+time+"'",null);
       if(cursor!=null&& cursor.moveToFirst())
        {
            String lecture=cursor.getString(cursor.getColumnIndex("lecture"));
            return lecture;
        }
        return "";
    }


    public boolean insert_assignment_details(String testid,int classid,int secid, int subid, int chapid,String subline, String subtext)

    {
        System.out.println("Details : "+testid+classid+subid+chapid+subline+subtext);
        SQLiteDatabase db=getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("class_id", classid);
        contentValues.put("sec_id", secid);
        contentValues.put("sub_id", subid);
        contentValues.put("ch_id", chapid);
        contentValues.put("sub_line", subline);
        contentValues.put("detail", subtext);
        contentValues.put("assign_id", testid);
        contentValues.put("isSync", 0);
        long rowid=db.insert("assignment", null, contentValues);
        db.close();
        if(rowid==-1)
        {
            return false;
        }
        return true;


    }


    public ArrayList<ChapterLectureModel> getLectureEndChapterId(int notificationid, String tea, int day) {
        SQLiteDatabase db=this.getReadableDatabase();
        ChapterLectureModel.chapter_totalLectures=new ArrayList<>();
        Cursor cursor1=db.rawQuery("select  assign_sub_tea.sub_id,assign_sub_tea.class_id from assign_sub_tea ,time_table where assign_sub_tea.class_id=time_table.class_id and  assign_sub_tea.sub_id=time_table.sub_id  and assign_sub_tea.sec_id=time_table.sec_id and assign_sub_tea.username='"+tea+"' and time_table.day="+day+" and time_table.lecture="+notificationid,null);
        if(cursor1!=null && cursor1.moveToFirst()) {
            System.out.println("sub_id"+cursor1.getInt(cursor1.getColumnIndex("sub_id"))+" class_id "+cursor1.getInt(cursor1.getColumnIndex("class_id")));
            Cursor cursor = db.rawQuery("select ch_id ,ch_name from chapter where sub_id=" + cursor1.getInt(cursor1.getColumnIndex("sub_id")) + " and class_id=" + cursor1.getInt(cursor1.getColumnIndex("class_id")), null);
            System.out.println("Cursor.getCount()="+cursor.getCount());
            ArrayList<ChapterLectureModel> chapterIdArrayList = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ChapterLectureModel.chapter_totalLectures.add(new ChapterLectureModel(cursor.getInt(cursor.getColumnIndex("ch_id")), cursor.getString(cursor.getColumnIndex("ch_name")), 0, cursor1.getInt(cursor1.getColumnIndex("sub_id"))));
                    System.out.println(cursor.getString(cursor.getColumnIndex("ch_name")));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return chapterIdArrayList;
        }
        return null;
    }

    public void getRemainingTopicsfromChapterId(int ch_id,int class_id,int sec_id) {
        SQLiteDatabase db=this.getReadableDatabase();
        LectureSchedule.lectureEndArrayList =new ArrayList<>();
                Cursor topicCursor = db.rawQuery("select topic,topic_id from topic where ch_id="+ch_id+" and topic_id not in(select topic_id from topic_count where ch_id="+ch_id+" and class_id="+class_id+" and sec_id="+sec_id+")", null);
                if (topicCursor != null && topicCursor.moveToFirst()) {
                    do {
                        String topic = topicCursor.getString(topicCursor.getColumnIndex("topic"));
                        int topic_id = topicCursor.getInt(topicCursor.getColumnIndex("topic_id"));
                        LectureSchedule.lectureEndArrayList.add(new LectureSchedule(topic_id, topic));
                    }
                    while (topicCursor.moveToNext());
                }
        }




    public void confirmLectureEndNotification(int notificationid) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("notification_confirmed",1);
        contentValues.put("lastupdate",getTimeStamp());
        db.update("todays_lecture_table",contentValues,"lecture"+"= ?",new String[]{Integer.toString(notificationid)});
        db.close();
    }

        public synchronized void getTimeStampFromSyncTable() {
            SQLiteDatabase db=this.getWritableDatabase();
    //        if (db.isOpen())
    //            db.close();
            SyncModel.added_sync_array_list=new ArrayList<>();
            Cursor cursor=db.rawQuery("select table_name,lastupdate from added_sync",null);
            Log.e("xyz","cursor count = "+cursor.getCount());
            if(cursor!=null && cursor.moveToFirst())
            {
                do {
                    SyncModel.added_sync_array_list.add(new SyncModel(cursor.getInt(cursor.getColumnIndex("lastupdate")),cursor.getString(cursor.getColumnIndex("table_name"))));
                }while(cursor.moveToNext());
            }
        }

    public void updateTopicCompletion(int[] topic_id,int class_id,int sec_id,int ch_id,int lecture,int sub_id) {
        Calendar calendar = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < topic_id.length; i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("complete",1);
            contentValues.put("class_id",class_id);
            contentValues.put("ch_id",ch_id);
            contentValues.put("sec_id",sec_id);
            contentValues.put("lecture",lecture);
            contentValues.put("date",sdf.format(calendar.getTime()));
            contentValues.put("sub_id",sub_id);
            contentValues.put("topic_id",topic_id[i]);
            contentValues.put("isSync",0);
            db.insert("topic_count",null,contentValues);
        }


    }


    public String[] get_news_title()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String title=null;
        Cursor c1=db.rawQuery("select subject from newsandnotice order by id desc ",null);
        String arr[]=new String[c1.getCount()];
        if(c1!=null && c1.moveToFirst())
        {
            do{
                title=c1.getString(c1.getColumnIndex("subject"));
                System.out.println(title);
                arr[c1.getPosition()]=title;
            }while(c1.moveToNext());
            ContentValues contentValues=new ContentValues();
            contentValues.put("notification_generated",1);
            db.update("newsandnotice",contentValues,null,null);
        }
        c1.close();
        return arr;

    }
    public String[] get_news_detail()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String title=null;
        Cursor c1=db.rawQuery("select detail from newsandnotice order by id desc",null);
        String arr_detail[]=new String[c1.getCount()];
        if(c1!=null && c1.moveToFirst())
        {
            do{
                title=c1.getString(c1.getColumnIndex("detail"));
                System.out.println(title);
                arr_detail[c1.getPosition()]=title;
            }while(c1.moveToNext());


        }
        c1.close();
        return arr_detail;


    }
    public String[] get_news_place()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String title=null;
        Cursor c1=db.rawQuery("select place from newsandnotice order by id desc",null);
        String arr_place[]=new String[c1.getCount()];
        if(c1!=null && c1.moveToFirst())
        {
            do{
                title=c1.getString(c1.getColumnIndex("place"));
                System.out.println(title);
                arr_place[c1.getPosition()]=title;
            }while(c1.moveToNext());


        }
        c1.close();
        return arr_place;


    }
    public String[] get_news_timings()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String title=null;
        Cursor c1=db.rawQuery("select timings from newsandnotice order by id desc ",null);
        String arr_timings[]=new String[c1.getCount()];
        if(c1!=null && c1.moveToFirst())
        {
            do{
                title=c1.getString(c1.getColumnIndex("timings"));
                System.out.println(title);
                arr_timings[c1.getPosition()]=title;
            }while(c1.moveToNext());


        }
        c1.close();
        return arr_timings;


    }

    public Boolean check_for_date() {
        SQLiteDatabase db = this.getReadableDatabase();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());
//		Cursor cursor = db.rawQuery("delete FROM stu_attend1  WHERE date ='"
//				+ date + "'", null);
        Cursor cursor = db.rawQuery("SELECT * FROM stu_attend1  WHERE date ='"
                + date + "'", null);

        if (cursor.getCount()>0) {
            cursor.close();
            db.close();
            return true;

        } else {
            cursor.close();
            db.close();
            return false;
        }
    }



    String Name_Roll_str[][];


    public String[][] Get_ContactDetails_students(int stuclass,int stusec) {
        System.out.println("stu_class"+stuclass+"   stu_sec"+stusec);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT stu_name,stu_username,stu_rollno FROM students WHERE stu_class ='"
                + stuclass + "' AND stu_section='" + stusec
                + "' order by stu_rollno asc", null);
        int z = cursor.getCount();
        //Contact cont;
        Name_Roll_str = new String[2][z];

        // Roll_str= new String[z];
        int i = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // String id = cursor.getString(cursor
                // .getColumnIndex(DBConstants.ID));
                // String name = cursor.getString(cursor
                // .getColumnIndex(DBConstants.NAME));
                // String password = cursor.getString(cursor
                // .getColumnIndex(DBConstants.PASSWORD));
                String tempp=null;
                String temp = cursor.getString(cursor.getColumnIndex("stu_name"));
                String temp_username = cursor.getString(cursor.getColumnIndex("stu_username"));
                tempp = cursor.getInt(cursor.getColumnIndex("stu_rollno"))+"";

                Name_Roll_str[0][i] = tempp + "." + temp;
                Name_Roll_str[1][i] = temp_username;
                System.out.print("username:"+temp_username);
                i++;

                // cont = new Contact(cursor.getString(1),cursor.getInt(8));
            } while (cursor.moveToNext());

            // return contact
            cursor.close();
            db.close();

            return Name_Roll_str;

        }
        return null;
    }


    public ArrayList<String> get_updated_names() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> list = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());
        Cursor cursor = db
                .rawQuery(
                        "SELECT stu_attend2.student_username_absent FROM stu_attend1 join stu_attend2 on stu_attend2.d_id=stu_attend1.d_id WHERE stu_attend1.date ='"
                                + date + "'", null);
        // int z=cursor.getCount();
        // String temp[]=new String [z] ;
        // int i=0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // String id = cursor.getString(cursor
                // .getColumnIndex(DBConstants.ID));
                String name = cursor.getString(cursor
                        .getColumnIndex("student_username_absent"));
                // String password = cursor.getString(cursor
                // .getColumnIndex(DBConstants.PASSWORD));
                System.out.println("name :" + name);
                // list.add(name);
                list.add(name);
                // i++;
                // cont = new Contact(cursor.getString(1),cursor.getInt(8));
            } while (cursor.moveToNext());
            // System.out.println(list);
            // return contact
            cursor.close();
            db.close();
            // return list;
        }
        return list;

    }


    public boolean Insert_ContactDetails(ArrayList<String> list,int class_id,int sec_id) {

        Calendar calendar = Calendar.getInstance();
        // Date current_time=calendar.getTime();
        // SimpleDateFormat timeformat= new SimpleDateFormat("HH:MM:SS");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String temp1=null;
        // String formattetime =timeformat.format(calendar.getTime());
        // String formatteddate =dateFormat.format(new Date());
        String date = dateFormat.format(calendar.getTime());
        String time = new SimpleDateFormat("HH:MM:SS").format(new Date());
        Boolean b=check_for_date();
        System.out.println(b);
//		SQLiteDatabase db3 = this.getReadableDatabase();
//		 db3.delete("stu_attend2", null, null);
        //("delete FROM stu_attend2  WHERE id ='4'", null);

        if (b) {
            SQLiteDatabase db3 = this.getReadableDatabase();

            //System.out.print("Hello");
            Cursor cursor1 = db3.rawQuery(
                    "SELECT d_id FROM stu_attend1  WHERE date ='" + date + "'",
                    null);
            if (cursor1 != null && cursor1.moveToFirst()) {
                temp1 = cursor1.getString(cursor1
                        .getColumnIndex("d_id"));
                System.out.println("temp1 "+temp1);
                cursor1.close();
            }
//				Cursor cursor2 = db3.rawQuery(
//						"DELETE FROM stu_attend2  WHERE id ='" + temp1 + "'",
//						//"DELETE FROM stu_attend2",
//						null);
            //String str[]={Integer.toString(temp1)};
            db3.delete("stu_attend2","d_id='"+temp1+"'", null);
            db3.delete("stu_attend1","d_id='"+temp1+"'",null);
            //System.out.print("Hi");
            //cursor2.close();11111
//				Cursor cursor3 = db3.rawQuery(
//						"DELETE FROM stu_attend1  WHERE id ='" + temp1 + "'",
//						null);
//				cursor3.close();

            //System.out.print("Bye");
            db3.close();

            //return true;
        } //else {
        SQLiteDatabase db1 = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("d_id", date);
        contentValues.put("date", date);
        contentValues.put("sec_id", sec_id);
        contentValues.put("class_id", class_id);
        contentValues.put("time", time);
        //contentValues.put("lastupdate",getTimeStamp());
        contentValues.put("isSync",0);

        db1.insert("stu_attend1", null, contentValues);
           for (int i = 0; i < list.size(); i++) {
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("d_id", date);
                contentValues1.put("student_username_absent", list.get(i));
                contentValues1.put("reason","na");
                contentValues1.put("class_id",class_id);
                contentValues1.put("sec_id",sec_id);
                contentValues1.put("isSync",0);
               // contentValues.put("lastupdate",getTimeStamp());

                // contentValues.put("email", email);
                // contentValues.put("street", street);
                // contentValues.put("place", place);
                db1.insert("stu_attend2", null, contentValues1);
            }
            //list.clear();

        db1.close();
        return true;
    }

    public int[] get_class_details(String username)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select class_id from assign_sub_tea where username ="+"'"+username+"'", null);
        int length=c.getCount();
        System.out.println(length);
        int class_arr[]=new int[length];
        int i=0;
        if(c!=null && c.moveToFirst())
        {

            do{



                class_arr[i]=c.getInt(c.getColumnIndex("class_id"));
                Log.v("class",class_arr[i]+"");
                i++;


            }while(c.moveToNext());
        }
        c.close();
        db.close();
        return class_arr;
    }

    public int[] get_section_details(String username)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select sec_id from assign_sub_tea where username ="+"'"+username+"'", null);
        int length=c.getCount();
        int sec_arr[]=new int[length];
        if(c!=null && c.moveToFirst())
        {
            int i=0;
            do{


                sec_arr[i]=c.getInt(c.getColumnIndex("sec_id"));
                i++;

            }while(c.moveToNext());
        }
        c.close();
        db.close();
        return sec_arr;
    }
    public String get_class(String c)
    {
        String c_value=" ";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor= db.rawQuery("Select c_value from class where class_id='"+c+"'", null);
        int l = cursor.getCount();

        if(cursor!=null && cursor.moveToFirst()) {
            do {
                {
                    c_value = cursor.getString(cursor.getColumnIndex("c_value"));
                    Log.e("c_value", c_value);

                }
            } while (cursor.moveToNext());
        }

        cursor.close();;
        db.close();

        return  c_value;
    }


    public String[] get_class(int class_arr[])
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int l=class_arr.length;
        String classValue_arr[]=new String[l];
        for(int j=0;j<l;j++)
        {
            Cursor c=db.rawQuery("Select c_value from class where class_id="+class_arr[j], null);
            if(c!=null && c.moveToFirst())
            {

                do{

                    classValue_arr[j]=c.getString(c.getColumnIndex("c_value"))+" ";

                }while(c.moveToNext());
            }
            c.close();
        }

        db.close();
        return classValue_arr;

    }
    public String get_sec(String c)
    {


        String section=" ";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select section from section where sec_id='"+c+"'",null);

        int l = cursor.getCount();


        if(cursor!=null && cursor.moveToFirst())
        {
            do{

                section=cursor.getString(cursor.getColumnIndex("section"));
                Log.e("Section",section);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return section;
    }
    public String get_chap_name(String c)
    {
        String ch_name=" ";

        Log.e("ChapterID  ",c);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor=db.rawQuery("Select ch_name from chapter where ch_id="+c ,null);

        int l = cursor.getCount();
        Log.e("Size", l+"");



        if(cursor!=null && cursor.moveToFirst())
        {
            do{
                ch_name = cursor.getString(cursor.getColumnIndex("ch_name"));
                Log.e("ch_name",ch_name);

            }   while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.e("Chapter again", ch_name);
        return ch_name;
    }

    public String[] getSs(String s)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select sub_line,detail from assignment where sub_line='"+s+"'",null);
        int l=cursor.getCount();

        String [] ss=new  String[2];

        if(cursor!=null && cursor.moveToFirst())
        {
            do{

                String sub_line = cursor.getString(cursor.getColumnIndex("sub_line"));
                Log.e("sub_line",sub_line);
                String detail = cursor.getString(cursor.getColumnIndex("detail"));
                Log.e("detail",detail);

                ss[0]=sub_line;
                ss[1]=detail;


            }   while (cursor.moveToNext());
        }


        cursor.close();
        db.close();

        return  ss;
    }

    public String get_sub(String c)
    {

        String sub_name=" ";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select sub_name from subject where sub_id='"+c+"'", null);
        int l= cursor.getCount();

        if(cursor!=null && cursor.moveToFirst())
        {
            do {

                sub_name = cursor.getString(cursor.getColumnIndex("sub_name"));
                Log.e("sub_name",sub_name);

            }   while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return  sub_name;

    }



    public String[] get_sec(int sec_arr[])
    {
        SQLiteDatabase db=this.getWritableDatabase();
        int l=sec_arr.length;
        String secValue_arr[]=new String[l];
        for(int j=0;j<l;j++)
        {
            Cursor c=db.rawQuery("Select section from section where sec_id="+sec_arr[j], null);
            if(c!=null && c.moveToFirst())
            {

                do{
                    secValue_arr[j]=c.getString(c.getColumnIndex("section"));
                }while(c.moveToNext());
            }
            c.close();
        }
        db.close();
        return secValue_arr;

    }

    public int[] get_subject_details(int class_id,int sec_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        System.out.println("class_id:"+class_id+" sec_id:"+sec_id);
        Cursor c=db.rawQuery("select sub_id from assign_sub_tea where class_id ="+class_id+" and sec_id="+sec_id, null);
        int length=c.getCount();
        int subject_arr[]=new int[length];
        int i=0;
        if(c!=null && c.moveToFirst())
        {

            do{
                subject_arr[i]=c.getInt(c.getColumnIndex("sub_id"));
                System.out.println("sub_id :"+subject_arr[i]);
                i++;

            }while(c.moveToNext());
        }
        c.close();
        db.close();
        return subject_arr;
    }
    public String get_subject(int subject_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String subjectValue=null;

        Cursor c=db.rawQuery("Select sub_name from subject where sub_id ="+subject_id,null);
        if(c!=null && c.moveToFirst())
        {
                subjectValue=c.getString(c.getColumnIndex("sub_name"));
                if(subjectValue == null)
                {
                    return "No Subject Found";
                }
            c.close();
            return  subjectValue;
        }
        db.close();
        return "No Subject Found";
    }

    public int[]get_chapter_detail(int subject_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();


        Cursor c=db.rawQuery("select ch_id from chapter where sub_id = "+"'"+subject_id+"'",null);
        int chap=c.getCount();

        int chap_id[]=new int[chap];
        if(c!=null && c.moveToFirst())
        {
            for(int i=0;i<chap;i++)
            {
                chap_id[i]=c.getInt(c.getColumnIndex("ch_id"));
                c.moveToNext();
            }
        }

        c.close();
        db.close();
        return chap_id;


    }

    public String get_chapter(int chap_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        String chapValue=null;
        Cursor c=db.rawQuery("Select ch_name from chapter where ch_id ="+chap_id,null);
        if(c!=null && c.moveToFirst())
        {
            do{

                chapValue=c.getString(c.getColumnIndex("ch_name"));

            }while(c.moveToNext());
        }
        c.close();

        db.close();
        return chapValue;
    }


    String str[][];
    public String[][] last_day_Absent(String username)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();
        //ArrayList<String> list = new ArrayList<String>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());
        String temp=null;
        Cursor cursor = dbb.rawQuery(
                "SELECT d_id FROM stu_attend1  WHERE class_id="+getClassTeacherClassId(username)+" and sec_id="+getClassTeacherSectionId(username), null);
        if (cursor != null && cursor.moveToFirst()) {
            if (cursor.getCount() > 1) {
                cursor.moveToLast();
                cursor.move(-1);
                System.out.println("cursor position" + cursor.getPosition());
                temp = cursor.getString(cursor.getColumnIndex("d_id"));
            }
            else
            {
                temp="";
            }


            cursor.close();
            Cursor cursor1 = dbb.rawQuery(
                    "SELECT student_username_absent,reason FROM stu_attend2  WHERE d_id ='" + temp + "' and class_id=" + getClassTeacherClassId(username) + " and sec_id=" + getClassTeacherSectionId(username), null);
            int z = cursor1.getCount();
            System.out.println(z);
            str = new String[3][z];
            int i = 0;
            if (cursor1 != null && cursor1.moveToFirst()) {
                do {
                    // String id = cursor.getString(cursor
                    // .getColumnIndex(DBConstants.ID));
                    String name = cursor1.getString(cursor1
                            .getColumnIndex("student_username_absent"));
                    String reason = cursor1.getString(cursor1
                            .getColumnIndex("reason"));
                    Cursor cursor3 = dbb.rawQuery(
                            "SELECT stu_name,stu_rollno FROM students  WHERE stu_username ='" + name + "'", null);
                    if (cursor3 != null && cursor3.moveToFirst()) {
                        String namee = cursor3.getString(cursor3
                                .getColumnIndex("stu_name"));

                        int roll = cursor3.getInt(cursor3.getColumnIndex("stu_rollno"));
                        cursor3.close();
                        str[0][i] = Integer.toString(roll) + "." + namee;
                    }
                    str[1][i] = reason;
                    str[2][i] = name;
                    i++;
                    //list.add(name);
                    System.out.println(name);
                    //System.out.println(reason);
                } while (cursor1.moveToNext());
                // System.out.println(list);
                // return contact
                cursor1.close();
                dbb.close();
                return str;
            }
        }
        return null;
    }
    public void last_day_absent_update(ArrayList<String> list1,ArrayList<String> list2)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());
        int temp=0;
        Cursor cursor = db.rawQuery(
                "SELECT d_id FROM stu_attend1  WHERE date ='" + date + "'", null);
        if (cursor != null && cursor.moveToFirst())
            temp = cursor.getInt(cursor.getColumnIndex("d_id"));
        temp=temp-1;
        cursor.close();
        //int i=0;
        for(int i=0;i<list1.size();i++)
        {
            ContentValues values = new ContentValues();
//			if(list1.get(i)==null)
//				 values.put("reason","na");
            //else
            values.put("reason",list1.get(i));
            values.put("lastupdate",getTimeStamp());
            values.put("isSync",0);

//		Cursor cursor1 = db.rawQuery(
//				"UPDATE stu_attend2 SET reason ='" + list1.get(i) + "' WHERE id ='" + temp + "' AND student_username_absent='" + list2.get(i) + "'", null);
            //cursor1.close();
            db.update("stu_attend2",values,"d_id" + " = ? AND " + "student_username_absent" + " = ?",new String[]{Integer.toString(temp), list2.get(i)});

        }
        System.out.println(list1);
        System.out.println(list2);
    }


    public boolean checkForDelete(String primaryKey,String tableName) {
        SQLiteDatabase db=this.getWritableDatabase();
        String Query = "Select id from "+tableName+" where id="+Integer.parseInt(primaryKey) ;
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount()>0){
            cursor.close();
            db.close();
            return true;
        }else {
            cursor.close();
            db.close();
            return false;
        }

    }
    public void deleteData(String primarykey,String tableName){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName,"id"+"= ?",new String[]{String.valueOf(Integer.parseInt(primarykey))});
        Log.e("adil","database deleted");


//        long id=db.insert("school",null,contentValues);

    }

    public String getTeacherNameFromId(String username) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select tea_name from teacher where username='"+username+"'",null);
        if(cursor!=null && cursor.moveToFirst())
        {
            String tea_name=cursor.getString(cursor.getColumnIndex("tea_name"));
            return tea_name;
        }


        return "NA";
    }
    public void getClassSecSubId(int day, int lectureno)
    {
        CurrentLectureModel.currentLectureModelArrayList= new ArrayList<>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select assign_sub_tea.class_id,assign_sub_tea.sec_id,assign_sub_tea.sub_id from time_table,assign_sub_tea where assign_sub_tea.class_id=time_table.class_id and assign_sub_tea.sec_id=time_table.sec_id and assign_sub_tea.sub_id=time_table.sub_id and lecture="+lectureno+" and day='"+day+"'",null);
        if(cursor !=null && cursor.moveToFirst())
        {

                int class_id=cursor.getInt(cursor.getColumnIndex("class_id"));
                int sec_id=cursor.getInt(cursor.getColumnIndex("sec_id"));
                int sub_id=cursor.getInt(cursor.getColumnIndex("sub_id"));
            System.out.println(" class"+class_id+"  sec_id"+sec_id+"  sub_id"+ sub_names);
                CurrentLectureModel.currentLectureModelArrayList.add(new CurrentLectureModel(class_id,sec_id,sub_id));

        }

    }

    public List<ViewAssignmentModel> get_test() {
        ArrayList<ViewAssignmentModel>assignment_list=new ArrayList<>();
        // Assignment.assignments_arraylist = new ArrayList<Assignment>();

        String sub_line[];
//
        SQLiteDatabase db = this.getWritableDatabase();
        // ViewAssignmentModel.ar=null;
        Cursor cursor = db.rawQuery(
                "Select class_id,sub_id,ch_id,detail,sub_line,date from test", null);
        int l = cursor.getCount();
        sub_line =  new String[l];

        if (cursor!=null)
            cursor.moveToFirst();

        if (cursor != null) {
            do {


                //ViewAssignmentModel.ar = new ArrayList<>();
                int id = cursor.getInt(0);
                Log.e("id", id+" ");
                String className=getClassName(cursor.getInt(cursor.getColumnIndex("class_id")));
                String subject=get_subject(cursor.getInt(cursor.getColumnIndex("sub_id")));
                String sub = cursor.getString(cursor.getColumnIndex("sub_line"));
                System.out.println(" className"+className+"  subject"+subject+"subject line "+sub);

                //ViewAssignmentModel.ar.add(new ViewAssignmentModel(id,sub));
                assignment_list.add(new ViewAssignmentModel(className,sub,subject));


            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return teacher_arr;
        //return ViewAssignmentModel.ar;
        return assignment_list;
    }


    public boolean insert_test_details(String testId, int selclas, int subjectId, int chapterId, String subjectLine, String subjectDesc, int selsec, String img) {


        SQLiteDatabase db=getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("date", selclas);
        contentValues.put("class_id", selclas);
        contentValues.put("sec_id", selsec);
        contentValues.put("sub_id", subjectId);
        contentValues.put("ch_id", chapterId);
        contentValues.put("sub_line", subjectLine);
        contentValues.put("lect_no", subjectLine);
        contentValues.put("isSync",0);
        contentValues.put("detail", subjectDesc);
        contentValues.put("test_id", testId);
        long rowid=db.insert("assignment", null, contentValues);
        db.close();
        if(rowid==-1)
        {
            return false;
        }
            return true;
    }


    public String[] get_particular_test_details(String heading)
    {

        Log.e("heading ",heading);
        String[] details;
        SQLiteDatabase db = this.getWritableDatabase();
        // ViewAssignmentModel.ar=null;
        Cursor cursor = db.rawQuery(
                "Select class_id,sec_id,sub_id,ch_id,sub_line,detail from test where sub_line='" + heading + "'", null);
        int l = cursor.getCount();
        details= new String[6];
        int class_id,sec_id,sub_id,chap_id;

//          if (cursor!=null)
//              cursor.moveToFirst();
//
//
        if (cursor!=null && cursor.moveToFirst() )
        {
            do {

                class_id = cursor.getInt(cursor.getColumnIndex("class_id"));
                Log.e("class_id",class_id+" ");
                sec_id = cursor.getInt(cursor.getColumnIndex("sec_id"));
                Log.e("sec_id",sec_id+" ");
                sub_id = cursor.getInt(cursor.getColumnIndex("sub_id"));
                Log.e("sub_id",sub_id+" ");
                chap_id = cursor.getInt(cursor.getColumnIndex("ch_id"));
                Log.e("chap_id",chap_id+" ");
                String sub_line = cursor.getString(cursor.getColumnIndex("sub_line"));
                Log.e("sub_line",sub_line);
                String detail = cursor.getString(cursor.getColumnIndex("detail"));
                Log.e("detailt",detail);

                details[0]=class_id+" ";
                details[1]=sec_id+" ";
                details[2]=sub_id+" ";
                details[3]=chap_id+" ";
                details[4]=sub_line;
                details[5]=detail;

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return details;


    }


    public boolean insert_attendance_details(String attendance_date,String timestamp){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("date",attendance_date);
        contentValues.put("time_in",timestamp);
        contentValues.put("isSync",0);
        db.insert("self_attendance",null,contentValues);
        db.close();

        return true;
    }
    public boolean check_date_for_attendance_for_setting_image(String date){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor= db.rawQuery("Select time_in from self_attendance where date="+"'"+date+"'",null);
        if(cursor.getCount()>0)
        {
            System.out.println("attendacne "+"true");
            return true;
        }
        else
        {
            System.out.println("attendacne "+"false");
            return false;
        }
    }




    public void getPresentDates() {
        // TODO Auto-generated method stub
        SQLiteDatabase db=this.getReadableDatabase();
        CalendarCollection.present_dates=new ArrayList<String>();
        Cursor  cursor=db.rawQuery("Select date from self_attendance", null);
        if(cursor!=null && cursor.moveToFirst())
        {
            do{
                CalendarCollection.present_dates.add(cursor.getString(cursor.getColumnIndex("date")));
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    public void getHolidayDates()
    {

        SQLiteDatabase db=this.getReadableDatabase();

        CalendarCollection.holiday_dates=new ArrayList<CalendarCollection>();
        Cursor  cursor=db.rawQuery("select date , event from academic_calendar where holiday='1'", null);
        if(cursor!=null && cursor.moveToFirst())
        {
            do{
                System.out.println("holiday"+cursor.getPosition());
                CalendarCollection.holiday_dates.add(new CalendarCollection(cursor.getString(cursor.getColumnIndex("date")),cursor.getString(cursor.getColumnIndex("event"))));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }


    public String getClassTeacherClassSection(String username)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String className=null;
        Cursor cursor=db.rawQuery("select cls_teacher_cls,cls_teacher_section from teacher where username='"+username+"'",null);
        if(cursor!=null && cursor.moveToFirst())
        {
            className=getClassName(cursor.getInt(cursor.getColumnIndex("cls_teacher_cls")))+getSectionName(cursor.getInt(cursor.getColumnIndex("cls_teacher_section")));

        }
        return className;
    }

    public int getClassTeacherClassId(String username)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<Integer> classSection= new ArrayList<>();
        Cursor cursor=db.rawQuery("select cls_teacher_cls from teacher where username='"+username+"'",null);
        if(cursor !=null && cursor.moveToFirst())
        {
            return cursor.getInt(cursor.getColumnIndex("cls_teacher_cls"));
        }
        return  0 ;
    }

    public int getClassTeacherSectionId(String username)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<Integer> classSection= new ArrayList<>();
        Cursor cursor=db.rawQuery("select cls_teacher_section from teacher where username='"+username+"'",null);
        if(cursor !=null && cursor.moveToFirst())
        {
            return cursor.getInt(cursor.getColumnIndex("cls_teacher_section"));
        }
        return  0 ;
    }

    public String getSectionName(int cls_teacher_section) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select section from section where sec_id="+cls_teacher_section,null);
        String c_value = null;
        if(cursor!=null && cursor.moveToFirst())
        {
            c_value=cursor.getString(cursor.getColumnIndex("section"));
        }
        return  c_value;
    }


    public void getStudentPresentDates(String username) {
        StudentAbsentPresentModel.absentDaysArrayList= new ArrayList<>();
        StudentAbsentPresentModel.presentDaysArrayList= new ArrayList<>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select date,d_id from stu_attend1",null);
        if(cursor!=null && cursor.moveToFirst())
        {
            do {
                String id = cursor.getString(cursor.getColumnIndex("d_id"));

                Cursor cursor1 = db.rawQuery("select student_username_absent,reason from stu_attend2 where d_id='" + id + "' and student_username_absent='" + username + "'", null);
                if (cursor1.getCount() > 0) {
                    cursor1.moveToFirst();
                    StudentAbsentPresentModel.absentDaysArrayList.add(new StudentAbsentPresentModel(cursor.getString(cursor.getColumnIndex("date")), cursor1.getString(cursor1.getColumnIndex("reason"))));
                } else {
                    StudentAbsentPresentModel.presentDaysArrayList.add(cursor.getString(cursor.getColumnIndex("date")));
                }
            }while (cursor.moveToNext());
        }
    }

    public void get_list()
    {


        ListModel.list_arraylist = new ArrayList<>();
        SQLiteDatabase db1 = this.getWritableDatabase();

        Cursor cursor = db1.rawQuery(
                "Select name,link from student_details", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                ArrayList<String> ar = new ArrayList<String>();
                String name = cursor.getString(cursor.getColumnIndex("name"));
                System.out.println("name"+ name);
                String link = cursor.getString(cursor.getColumnIndex("link"));
                System.out.println("link"+ link);
                ar.add(name);
                ar.add(link);

                ListModel.list_arraylist.add(new ListModel(ar));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db1.close();
        // return teacher_arr;


    }


    public int check_if_download(String url)
    {
        int downloaded=0;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor c=db.rawQuery("select downloaded from downloaded_files where url ="+"'"+url+"'" , null);
        if(c!=null && c.moveToFirst())
        {
            do{
                downloaded=c.getInt(c.getColumnIndex("downloaded"));
            }while(c.moveToNext());
        }
        return downloaded;
    }


    public boolean insert_circular_detail(String url,int downloaded)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("url",url);
        contentValues.put("isSync",0);
        contentValues.put("downloaded",downloaded);
        db.insert("downloaded_files", null,contentValues);
        db.close();

        return true;
    }


    public int getTotalLecturesForChapeter(int ch_id) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select lec_total from lecture where ch_id="+ch_id,null);
        if(cursor !=null && cursor.moveToFirst())
        {
         return cursor.getInt(cursor.getColumnIndex("lec_total"));
        }
        return 0;

    }

    public ArrayList<String> getTableNameFromDatabase() {
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<String> tableNameArrayList= new ArrayList<>();
        Cursor cursor=db.rawQuery("select table_name from added_sync_upload",null);

        if(cursor!=null && cursor.moveToFirst())
        {
            do {
                tableNameArrayList.add(cursor.getString(cursor.getColumnIndex("table_name")));

            }while(cursor.moveToNext());
        }
        return tableNameArrayList;
    }

    public ArrayList<String> getColumnDataFromTable(String tableName) {
        ArrayList<String> columnNameArrayList=new ArrayList<>();
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from "+tableName,null);

        int no_of_column=cursor.getColumnCount();
        if(cursor!=null)
        {
            for(int i=0;i<no_of_column;i++)
            {
                columnNameArrayList.add(cursor.getColumnName(i));
                //System.out.print("-"+cursor.getColumnName(i));
            }

        }
        return columnNameArrayList;

    }


    public void getRowDataFromTable(String tableName)
    {
        UploadRowDataModel.rowDataModelArray=new ArrayList<>();
        SQLiteDatabase db=this.getWritableDatabase();
        String rowData[]= new String[20];
        Cursor cursor=db.rawQuery("select * from "+tableName+" where isSync=0",null);
        if(cursor!=null && cursor.moveToFirst())
        {
            do{

                for(int i=0;i<cursor.getColumnCount();i++)
                {
                    int type = cursor.getType(i);
                    //System.out.print(type+"  type");
                    switch (type) {
                        case Cursor.FIELD_TYPE_INTEGER:
                            rowData[i]=cursor.getInt(i)+"";
                            //System.out.println("Integer"+rowData[i]);
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            rowData[i]=cursor.getString(i);
                            //System.out.println("String"+rowData[i]);
                            break;
                        case Cursor.FIELD_TYPE_NULL:
                            rowData[i]="";
                            //System.out.println("Null"+rowData[i]);
                            break;

                    }
                    for(int j=cursor.getColumnCount();j<rowData.length;j++)
                    {
                        rowData[j]=null;
                    }

                }
                UploadRowDataModel.rowDataModelArray.add(new UploadRowDataModel(rowData[0],rowData[1]
                        ,rowData[2],rowData[3],rowData[4],rowData[5],rowData[6]
                        ,rowData[7],rowData[8],rowData[9],rowData[10],rowData[11]
                        ,rowData[12],rowData[13],rowData[14],rowData[15],rowData[16]
                        ,rowData[17],rowData[18],rowData[19]));

            }while(cursor.moveToNext());

        }
    }


    public int getTotalLectures() {
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select total_lecture from sch_basic_detail_2",null);
        if(cursor !=null && cursor.moveToFirst())
        {
            int total_lectures=cursor.getInt(cursor.getColumnIndex("total_lecture"));
            return total_lectures;
        }
        return  0;
    }



    public String getClassNameFromTimeTable(int day, int lecture, String tea) {
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select assign_sub_tea.class_id from assign_sub_tea inner join  time_table on assign_sub_tea.class_id=time_table.class_id and assign_sub_tea.sec_id=time_table.sec_id and assign_sub_tea.sub_id=time_table.sub_id where time_table.day="+day+" and time_table.lecture="+lecture+" and assign_sub_tea.username='"+tea+"'",null);
        if(cursor !=null && cursor.moveToFirst())
        {
            String className=getClassName(cursor.getInt(cursor.getColumnIndex("class_id")));
            if(className== null)
            {
                return "-";
            }
            System.out.print("class:"+className+" ");
            return className;
        }
        return  "-";
    }

    public String getSectionNameFromTimeTable(int day, int lecture, String tea) {
        SQLiteDatabase db= this.getWritableDatabase();
        Cursor cursor= db.rawQuery("select assign_sub_tea.sec_id from assign_sub_tea inner join  time_table on assign_sub_tea.class_id=time_table.class_id and assign_sub_tea.sec_id=time_table.sec_id and assign_sub_tea.sub_id=time_table.sub_id where time_table.day="+day+" and time_table.lecture="+lecture+" and assign_sub_tea.username='"+tea+"'",null);
        if(cursor !=null && cursor.moveToFirst())
        {
            String secName=getSectionName(cursor.getInt(cursor.getColumnIndex("sec_id")));
            if(secName == null)
            {
                return "-";
            }

            return secName;
        }
        System.out.print("sec:" );
        return  "-";
    }

    public int getSubjectIdFromTimeTable(int day, int lecture,String tea) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select assign_sub_tea.sub_id from assign_sub_tea inner join  time_table on assign_sub_tea.class_id=time_table.class_id and assign_sub_tea.sec_id=time_table.sec_id and assign_sub_tea.sub_id=time_table.sub_id where time_table.day="+day+" and time_table.lecture="+lecture+" and assign_sub_tea.username='"+tea+"'", null);
        if (cursor != null && cursor.moveToFirst()) {

            int sub_id=cursor.getInt(cursor.getColumnIndex("sub_id"));
            if(sub_id==0)
            {
                return 0;
            }
            System.out.print("sub:"+sub_id+" ");
            return sub_id;
        }
        return 0;
    }

    public boolean      getCheckTodaysLectureTableForCurrentLecture() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select name from sqlite_master where type='table' and name='todays_lecture_table'", null);
        if (cursor.getCount() > 0) {
            System.out.println("Table  exist ");
            cursor.close();
            db.close();
            return true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean getLectureStartNotificationFromTodaysLectureTable(String time) {

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor end_time=db.rawQuery("select start_time from todays_lecture_table where start_time='"+time+"'",null);
        if(end_time.getCount()>0)
        {
            end_time.close();
            db.close();
            return true;
        }
        else
        {
            end_time.close();
            db.close();
            return false;
        }

    }

    public int getLectureEndClassId(int notificationid) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select class_id from todays_lecture_table where lecture="+notificationid,null);
        int i=0;
        if(cursor!=null && cursor.moveToFirst())
        {
            i=cursor.getInt(cursor.getColumnIndex("class_id"));
        }
        cursor.close();
        db.close();
        return i;
    }

    public int getLectureEndSecId(int notificationid) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select sec_id from todays_lecture_table where lecture="+notificationid,null);
        int i=0;
        if(cursor!=null && cursor.moveToFirst())
        {
            i=cursor.getInt(cursor.getColumnIndex("sec_id"));
        }
        cursor.close();
        db.close();
        return i;
    }
    public int getLectureEndSubId(int notificationid) {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select sub_id from todays_lecture_table where lecture="+notificationid,null);
        int i=0;
        if(cursor!=null && cursor.moveToFirst())
        {
            i=cursor.getInt(cursor.getColumnIndex("sub_id"));
        }
        cursor.close();
        db.close();
        return i;
    }


    public boolean checkForDeleteTodaysLectureTable(String time) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select *  from sch_basic_detail_1 where start_time <='"+time+"' and  recycle_time>='"+time+"'",null);
        if(cursor.getCount()>0)
        {
            return false;

        }
        else

        {
            return true;
        }
    }

    public void  getTeacherSubjectClasses(String teacherNameFromId) {
        SQLiteDatabase db=this.getReadableDatabase();
        ClassSectionModel.classSectionModelArrayList= new ArrayList<>();
        Cursor cursor=db.rawQuery("select distinct class_id,sec_id from assign_sub_tea where username='"+teacherNameFromId+"'",null);
        if(cursor!=null && cursor.moveToFirst())
        {
            do {
                int class_id=cursor.getInt(cursor.getColumnIndex("class_id"));
                int sec_id=cursor.getInt(cursor.getColumnIndex("sec_id"));
                ClassSectionModel.classSectionModelArrayList.add(new ClassSectionModel(class_id,getClassName(class_id),sec_id,getSectionName(sec_id)));

                }while(cursor.moveToNext());
            }


        }

    public String getSchoolRecycleTime() {
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Cursor cursor = db.rawQuery("Select recycle_time from sch_basic_detail_1 where date_from <="
                + "'" + sdf.format(calendar.getTime()) + "'" + "AND" + "'"
                + sdf.format(calendar.getTime()) + "'" + "<=date_to", null);

        if (cursor != null && cursor.moveToFirst()) {
            String start_time = cursor.getString(cursor.getColumnIndex("recycle_time"));
            // Log.v("start_time",start_time);
            return start_time;
        }

        return null;

    }

    public void getSubstitutionList()
    {
        Calendar calendar = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select id,class_id,sub_id,sec_id,lecture,absent_teacher_username,substitution_status from classes_substitution where date='"+sdf.format(calendar.getTime())+"'",null);
        TeacherSubstitutionModel.teacherSubstitutionModelArrayList= new ArrayList<>();
        if(cursor!=null && cursor.moveToFirst())
        {
            do {
                    TeacherSubstitutionModel.teacherSubstitutionModelArrayList.add(new TeacherSubstitutionModel(cursor.getInt(cursor.getColumnIndex("class_id")),cursor.getInt(cursor.getColumnIndex("id")),cursor.getInt(cursor.getColumnIndex("substitution_status")),cursor.getInt(cursor.getColumnIndex("sec_id")),cursor.getInt(cursor.getColumnIndex("sub_id")),cursor.getString(cursor.getColumnIndex("lecture")),cursor.getString(cursor.getColumnIndex("absent_teacher_username"))));
            }while (cursor.moveToNext());

        }

    }


    public long confirmSubstitution(int id) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("substitution_status",1);
        long rowid=db.update("classes_substitution",contentValues,"id=?",new String[]{String.valueOf(id)});
        return rowid;
    }
    public long cancelSubstitution(int id) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("substitution_status",0);
        long rowid=db.update("classes_substitution",contentValues,"id=?",new String[]{String.valueOf(id)});
        return rowid;
    }

    public boolean getSubstitutionNotification() {
        Calendar calendar = Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select lecture from classes_substitution where notification_generated=0 and substitution_status =0 and date='"+sdf.format(calendar.getTime())+"'",null);
        if(cursor!=null && cursor.getCount()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public int getCircularNotification() {
        int i=0;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select * from circular where notification_generated=0",null);
        if(cursor!=null && cursor.getCount()>0)
        {
            return cursor.getCount();
        }
        else
        {
            return cursor.getCount();
        }
    }
    public int getNoticeNotification() {
        int i=0;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select * from newsandnotice where notification_generated=0",null);
        if(cursor!=null && cursor.getCount()>0)
        {
            return cursor.getCount();
        }
        else
        {
            return cursor.getCount();
        }
    }

    public int getChapterFromTodaysLectureTable(int lecture) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("Select ch_id from todays_lecture_table where lecture="+lecture,null);
        if(cursor!=null && cursor.moveToFirst())
        {
            int ch_id=cursor.getInt(cursor.getColumnIndex("ch_id"));
            if(ch_id==0)
            {
                return 0;
            }
            else
            {
                return ch_id;
            }
        }

        return 0;
    }

    public int getCurrentChapterToBeTaught() {
        SQLiteDatabase db=this.getWritableDatabase();
        for(int i=0;i<ChapterLectureModel.chapter_totalLectures.size();i++) {
            Cursor cursor = db.rawQuery("select topic_id from topic_count where ch_id=" + ChapterLectureModel.chapter_totalLectures.get(i).chap__id,null);
           Cursor topicCountCursor=db.rawQuery("select topic_id from topic where ch_id=" + ChapterLectureModel.chapter_totalLectures.get(i).chap__id,null);
           if(cursor.getCount()==topicCountCursor.getCount())
           {

           }
            else
           {
               if(cursor.getCount()>0 && cursor.getCount()<topicCountCursor.getCount())
               {
                   return ChapterLectureModel.chapter_totalLectures.get(i).chap__id;
               }

           }
           }
        return 0;
    }

    private boolean getAllTopicCompleted(int chap__id,SQLiteDatabase db)
    {
        Cursor totalCompletedTopicCountCursor=db.rawQuery("select topic_id from topic_count where ch_id=" + chap__id,null);
        Cursor topicCountCursor=db.rawQuery("select topic_id from topic where ch_id=" + chap__id,null);
        if (totalCompletedTopicCountCursor.getCount()==topicCountCursor.getCount())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void insertIntoTodaysLectureTable(int ch_id1,int lecture) {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("ch_id",ch_id1);
        db.update("todays_lecture_table",contentValues,"lecture=?",new String[]{String.valueOf(lecture)});
    }

    public void get_circular(String d) {

        Circular.circular_arraylist = new ArrayList<Circular>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "Select date,created_by,subline,c_id from circular order by date desc", null);
        int l = cursor.getCount();
        // String teacher_arr[][]=new String[3][l];
        // String teacher_user[]=new String[l];
        // int i=0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                ArrayList<String> ar = new ArrayList<String>();
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String c_id = cursor.getString(cursor.getColumnIndex("c_id"));
                String created_by = cursor.getString(cursor
                        .getColumnIndex("created_by"));
                String sub_line = cursor.getString(cursor
                        .getColumnIndex("subline"));

                // teacher_arr[0][i]=date;
                // teacher_arr[1][i]=detail;
                // teacher_arr[2][i]=created_by;
                //
                // i++;
                ar.add(date);
                ar.add(sub_line);
                ar.add(created_by);

                ar.add(c_id);

                Circular.circular_arraylist.add(new Circular(ar));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return teacher_arr;

    }



    public String[] circularp(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "Select date,detail,created_by,subline from circular where c_id='"
                        + id + "'", null);
        int l = cursor.getCount();
        String teacher_arr[] = new String[5];
        // String teacher_user[]=new String[l];
        // int i=0;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // ArrayList<String> ar=new ArrayList<String>();
                String date = cursor.getString(cursor.getColumnIndex("date"));
                Log.e("date", date);
                String detail = cursor.getString(cursor
                        .getColumnIndex("detail"));
                Log.e("detail", detail);
                String created_by = cursor.getString(cursor
                        .getColumnIndex("created_by"));
                Log.e("created_by", created_by);
                String sub_line = cursor.getString(cursor
                        .getColumnIndex("subline"));
                Log.e("sub_line", sub_line);
                /*String username = cursor.getString(cursor
                        .getColumnIndex("username"));
                Log.e("username", username);
*/
                teacher_arr[0] = date;
                teacher_arr[1] = detail;
                teacher_arr[2] = created_by;
                teacher_arr[3] = sub_line;
                //teacher_arr[4] = username;

                // i++;

            } while (cursor.moveToNext());
            ContentValues contentValues=new ContentValues();
            contentValues.put("notification_generated",1);
            db.update("circular",contentValues,"c_id=?",new String[]{id});
        }
        cursor.close();
        db.close();
        return teacher_arr;
    }


    public void setStartNotificationGenerated(String lecture) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("start_notification_generated",1);
        db.update("todays_lecture_table",contentValues,"lecture=?",new String[]{String.valueOf(Integer.parseInt(lecture))});
    }

    public void setEndNotificationGenerated(String lecture) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("end_notification_generated",1);
        db.update("todays_lecture_table",contentValues,"lecture=?",new String[]{String.valueOf(Integer.parseInt(lecture))});
    }

    public boolean getStartNotificationGenerated(String lecture) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select start_notification_generated from todays_lecture_table where lecture="+Integer.parseInt(lecture),null);
        if(cursor!=null && cursor.moveToFirst())
        {
            int x=cursor.getInt(cursor.getColumnIndex("start_notification_generated"));
            if(x==1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return  false;
        }
    }

    public boolean getEndNotificationGenerated(String lecture) {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("select end_notification_generated from todays_lecture_table where lecture="+Integer.parseInt(lecture),null);
        if(cursor!=null && cursor.moveToFirst())
        {
            int x=cursor.getInt(cursor.getColumnIndex("end_notification_generated"));
            if(x==1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return  false;
        }
    }
    public String getDeleteLastupdate() {

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select lastupdate from delete_lastupdate",null);
        if(cursor!=null && cursor.moveToFirst())
        {
            String deleteLastupdate=cursor.getString(cursor.getColumnIndex("lastupdate"))+"";
            return deleteLastupdate;
        }
        else
        {
            return "0";
        }
    }
}
