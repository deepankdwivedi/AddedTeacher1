package com.added.addedteacher;

import android.app.FragmentManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.added.addedteacher.database.MyDatabase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import MySharedPreferences.MySharedPreferences;
import httpservice.ServiceHandler;
import model.ChapterLectureModel;
import model.LectureSchedule;
import receiver.AddedSyncReceiver;
import receiver.SampleAlarmReceiver;

import static com.added.addedteacher.R.id.lecture;

public class MainActivity1 extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    String dateFormatString;
    TextView date;
    MyDatabase db;

    Date today = new Date();
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SampleAlarmReceiver alarm ;
    private String url="http://192.168.1.6/h_service/pria_service/attendance_teacher.php";
    Context context;
    public TextView markAtten;
    private String myPreferences;
    String status;
    View.OnClickListener attendanceListener,alreadyDoneAttendanceListener;
    MyDatabase database;
    AddedSyncReceiver added_sync ;
    NotificationManager notificationManager;
    ArrayList<String> tableNameArrayList,tableColumnArrayList;
    Boolean schoolRunning, startPopUp, lectureEndPopUp, sunday, second_saturday, last_saturday, month_end, nextLectureSchoolRunning;
    private TextView teacherName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        added_sync = new AddedSyncReceiver();
        alarm = new SampleAlarmReceiver();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        database=new MyDatabase(MainActivity1.this);
        context=MainActivity1.this;
        String tea= new MySharedPreferences(context).getTeacherIdSharedPreferencesId();
        System.out.println(tea+" This is teacher id");
        setSupportActionBar(toolbar);
        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        TextView text = (TextView) header.findViewById(R.id.navigationHeaderTeacherName);


        db = MyDatabase.getDatabaseInstance(this);
        text.setText(db.getTeacherNameFromId(tea));
        dateFormatString = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        Calendar calendar = Calendar.getInstance();
        //new  SendDevicInformation().execute();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
                    if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
                        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (mWifi.isConnected()) {
                            Calendar calendar = Calendar.getInstance();
                            //Toast.makeText(MainActivity.this, "Wifi Connected", Toast.LENGTH_SHORT).show();
                            schoolRunning = getSchoolRunningStatus(calendar.getTime(),context);
                            if (schoolRunning) {
                                //Toast.makeText(MainActivity1.this, "School Running Today ", Toast.LENGTH_LONG).show();
                                alarm.setAlarm(context);
                                added_sync.setAlarm(getApplicationContext());


                            } else {
                                //Toast.makeText(MainActivity1.this, "School not Running ", Toast.LENGTH_LONG).show();
                            }

                        }
                        //do stuff
                    } else {
                        // wifi connection was lost
                        //Toast.makeText(MainActivity1.this,"Wifi Disconnected",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        added_sync.setAlarm(context);
        alarm.setAlarm(context);
        schoolRunning = getSchoolRunningStatus(calendar.getTime(),context);
        if (schoolRunning) {
            alarm.setAlarm(context);
           // Toast.makeText(MainActivity1.this, "School Running Today ", Toast.LENGTH_LONG).show();
            added_sync.setAlarm(context);

        } else {
           // Toast.makeText(MainActivity1.this, "School not Running ", Toast.LENGTH_LONG).show();
        }
        ProfileFragment fragment = new ProfileFragment();
        getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();




    }

    Calendar calendar1 = Calendar.getInstance();







    @Override
    protected void onNewIntent(Intent intent) {
        final Bundle extras = intent.getExtras();
        String tea=new MySharedPreferences(MainActivity1.this).getTeacherIdSharedPreferencesId();
        Calendar calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_WEEK);
            final int lecture=Integer.parseInt(extras.getString("lecture"));
        System.out.println("lecture "+lecture);
        if (extras.containsKey("notificationid") && Integer.parseInt(extras.getString("notificationid"))>10) {
            System.out.println("In the onNewIntent");
             int ch_id= db.getChapterFromTodaysLectureTable(lecture);
            if(ch_id!=0)
            {

            }
            else
            {
                decideChapterForTodaysLectureTable(lecture,tea,day);
            }

        }
        if (extras.containsKey("notificationid") && Integer.parseInt(extras.getString("notificationid"))<10) {
            System.out.println("notification_id:"+Integer.parseInt(extras.getString("notificationid")));
            final int ch_id= db.getChapterFromTodaysLectureTable(lecture);
            if(ch_id==0)
            {

                decideChapterForTodaysLectureTable1(lecture,tea,day);



            }
            else
            {
                final int ch_id1= db.getChapterFromTodaysLectureTable(lecture);
                final int class_id=db.getLectureEndClassId(lecture);
                final int sec_id=db.getLectureEndSecId(lecture);
                final int sub_id=db.getLectureEndSubId(lecture);

                db.getRemainingTopicsfromChapterId(ch_id1,class_id,sec_id);
                String topic[] = new String[LectureSchedule.lectureEndArrayList.size()];
                for (int i = 0; i < LectureSchedule.lectureEndArrayList.size(); i++) {
                    topic[i] = LectureSchedule.lectureEndArrayList.get(i).topicname;
                }


                //setContentView(R.layout.activity_main);
                AlertDialog dialog2;
                final ArrayList<Integer> seletedItems = new ArrayList();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity1.this);
                builder.setTitle("You taught ?");
                builder.setMultiChoiceItems(topic, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            // indexSelected contains the index of item (of which checkbox checked)
                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    // write your code when user checked the checkbox
                                    seletedItems.add(indexSelected);
                                } else if (seletedItems.contains(indexSelected)) {
                                    // Else, if the item is already in the array, remove it
                                    // write your code when user Uchecked the checkbox
                                    seletedItems.remove(Integer.valueOf(indexSelected));
                                }
                            }
                        })
                        // Set the action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (seletedItems.size() > 0) {
                                    //  Your code when user clicked on OK
                                    db.confirmLectureEndNotification(lecture);
                                    int topic_id[] = new int[seletedItems.size()];
                                    for (int i = 0; i < seletedItems.size(); i++) {
                                        topic_id[i] = LectureSchedule.lectureEndArrayList.get(seletedItems.get(i)).topic_id;
                                    }
                                    db.updateTopicCompletion(topic_id, class_id, sec_id, ch_id1, lecture, sub_id);


                                    //  You can write the code  to save the selected item here

                                }
                                notificationManager.cancel(Integer.parseInt(extras.getString("notificationid")));
                                notificationManager.cancel(Integer.parseInt(extras.getString("notificationid")) + 10);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //  Your code when user clicked on Cancel

                            }
                        });
                dialog2 = builder.create();//AlertDialog dialog; create like this outside onClick
                dialog2.show();
            }
        }
        if (extras.containsKey("substitution")) {
            getFragmentManager().beginTransaction().replace(R.id.framecontainer,new TeacherSubstitution()).addToBackStack(null).commit();
            notificationManager.cancel(1000);
        }
        if (extras.containsKey("circular")) {
            getFragmentManager().beginTransaction().replace(R.id.framecontainer,new ToViewCircular1()).addToBackStack(null).commit();
            notificationManager.cancel(2000);
        }
        if (extras.containsKey("notice")) {
            getFragmentManager().beginTransaction().replace(R.id.framecontainer,new NewsAndNoticeFragment()).addToBackStack(null).commit();
            notificationManager.cancel(3000);
        }
    }

    private void decideChapterForTodaysLectureTable(final int notificationid, String tea, int day) {
        System.out.println("decide lecture"+notificationid+" tea "+tea+" day "+day);
        db.getLectureEndChapterId(notificationid,tea,day);
        int ch_id1=db.getCurrentChapterToBeTaught();
        if(ch_id1==0)
        {
            AlertDialog dialog1;
            final int[] seletedChapters = new int[1];
            seletedChapters[0]=-1;
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Select Chapter");
            String chapter[]=new String[ChapterLectureModel.chapter_totalLectures.size()];
            System.out.println("Total Chapters Available :"+chapter.length);
            for(int i=0;i<ChapterLectureModel.chapter_totalLectures.size();i++)
            {
                chapter[i]=ChapterLectureModel.chapter_totalLectures.get(i).ch_name;
                System.out.println("Chapter Name"+ChapterLectureModel.chapter_totalLectures.get(i).ch_name);
            }

            builder1.setSingleChoiceItems(chapter,-1,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            seletedChapters[0] =which;
                        }


                    }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(seletedChapters[0]!=-1) {
                        db.insertIntoTodaysLectureTable(ChapterLectureModel.chapter_totalLectures.get(seletedChapters[0]).chap__id, notificationid);
                    }

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog1=builder1.create();
            dialog1.show();

        }
        else
        {
            db.insertIntoTodaysLectureTable(ch_id1,notificationid);
        }

    }

    private void decideChapterForTodaysLectureTable1(final int notificationid, String tea, int day) {
        System.out.println("decide lecture"+notificationid+" tea "+tea+" day "+day);
        db.getLectureEndChapterId(notificationid,tea,day);
        int ch_id1=db.getCurrentChapterToBeTaught();
        if(ch_id1==0)
        {
            AlertDialog dialog1;
            final int[] seletedChapters = new int[1];
            seletedChapters[0]=-1;
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Select Chapter");
            String chapter[]=new String[ChapterLectureModel.chapter_totalLectures.size()];
            System.out.println("Total Chapters Available :"+chapter.length);
            for(int i=0;i<ChapterLectureModel.chapter_totalLectures.size();i++)
            {
                chapter[i]=ChapterLectureModel.chapter_totalLectures.get(i).ch_name;
                System.out.println("Chapter Name"+ChapterLectureModel.chapter_totalLectures.get(i).ch_name);
            }

            builder1.setSingleChoiceItems(chapter,-1,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            seletedChapters[0] =which;
                        }


                    }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (seletedChapters[0] != -1) {
                        db.insertIntoTodaysLectureTable(ChapterLectureModel.chapter_totalLectures.get(seletedChapters[0]).chap__id, notificationid);
                        final int ch_id1 = db.getChapterFromTodaysLectureTable(lecture);
                        final int class_id = db.getLectureEndClassId(lecture);
                        final int sec_id = db.getLectureEndSecId(lecture);
                        final int sub_id = db.getLectureEndSubId(lecture);

                        db.getRemainingTopicsfromChapterId(ch_id1, class_id, sec_id);
                        String topic[] = new String[LectureSchedule.lectureEndArrayList.size()];
                        for (int i = 0; i < LectureSchedule.lectureEndArrayList.size(); i++) {
                            topic[i] = LectureSchedule.lectureEndArrayList.get(i).topicname;
                        }


                        //setContentView(R.layout.activity_main);
                        AlertDialog dialog2;
                        final ArrayList<Integer> seletedItems = new ArrayList();
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity1.this);
                        builder.setTitle("You taught ?");
                        builder.setMultiChoiceItems(topic, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    // indexSelected contains the index of item (of which checkbox checked)
                                    @Override
                                    public void onClick(DialogInterface dialog, int indexSelected,
                                                        boolean isChecked) {
                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            // write your code when user checked the checkbox
                                            seletedItems.add(indexSelected);
                                        } else if (seletedItems.contains(indexSelected)) {
                                            // Else, if the item is already in the array, remove it
                                            // write your code when user Uchecked the checkbox
                                            seletedItems.remove(Integer.valueOf(indexSelected));
                                        }
                                    }
                                })
                                // Set the action buttons
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Your code when user clicked on OK
                                        db.confirmLectureEndNotification(lecture);
                                        if (seletedItems.size() > 0) {
                                            int topic_id[] = new int[seletedItems.size()];
                                            for (int i = 0; i < seletedItems.size(); i++) {
                                                topic_id[i] = LectureSchedule.lectureEndArrayList.get(seletedItems.get(i)).topic_id;
                                            }
                                            db.updateTopicCompletion(topic_id, class_id, sec_id, ch_id1, lecture, sub_id);
                                        }
                                        notificationManager.cancel(lecture);
                                        notificationManager.cancel(lecture);
                                        //  You can write the code  to save the selected item here

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Your code when user clicked on Cancel

                                    }
                                });
                        dialog2 = builder.create();//AlertDialog dialog; create like this outside onClick
                        dialog2.show();
                    }
                } }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog1=builder1.create();
            dialog1.show();

        }
        else
        {
            db.insertIntoTodaysLectureTable(ch_id1,notificationid);
        }

    }


    private void getNextDayStatusAndLectures() {

        calendar1.add(Calendar.DAY_OF_YEAR, 1);
        System.out.println("Tommorow date:" + sdf.format(calendar1.getTime()));
        nextLectureSchoolRunning = getSchoolRunningStatus(calendar1.getTime(),context);
        if (nextLectureSchoolRunning) {
            db.getDateDayLectures(calendar1.getTime());
            db.getTopicForNextLecture();

            System.out.println("Next Lecture date" + calendar1.getTime());
        } else {
            getNextDayStatusAndLectures();

        }
    }

    public static Calendar getLastSaturday(Calendar cal, int offset) {
        int dayofweek;//1-Sunday,2-Monday so on....
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + offset);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); //set calendar to last day of month
        dayofweek = cal.get(Calendar.DAY_OF_WEEK); //get the day of the week for last day of month set above,1-sunday,2-monday etc
        if (dayofweek < Calendar.SATURDAY)  //Calendar.FRIDAY will return integer value =5
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 7 + Calendar.SATURDAY - dayofweek);
        else
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + Calendar.SATURDAY - dayofweek);
        return cal;
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

            // Handle the camera action

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backstack");
                for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            ProfileFragment fragment = new ProfileFragment();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

        } else if (id == R.id.studentAttendance) {

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            StudentAttendanceFragment fragment = new StudentAttendanceFragment();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();
        }
        else if (id == R.id.virtualPresence) {

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            VirtualPresenceFragment fragment = new VirtualPresenceFragment();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();
        }else if (id == R.id.notesSharing) {

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            NotesSharingFragment fragment = new NotesSharingFragment();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();
        }
        else if (id == R.id.studentAttendanceCalendar) {

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            ClassAttendanceListFragment fragment = new ClassAttendanceListFragment();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();
        }
        else if (id == R.id.viewCircular) {

            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            ToViewCircular1 fragment = new ToViewCircular1();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();
        }
        else if (id == R.id.teacherTimeTable) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }
            TimeTableFragment fragment = new TimeTableFragment();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();


        } else if (id == R.id.lecturePlan) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            LecturePlanActivity fragment = new LecturePlanActivity();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

        }
        else if (id == R.id.substitution) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            TeacherSubstitution fragment = new TeacherSubstitution();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

        }
        else if (id == R.id.selfAttendance) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            SelfAttendance fragment = new SelfAttendance();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

        }
        else if (id == R.id.currentLectureStatus) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            CurrentLectureStatusFragment fragment = new CurrentLectureStatusFragment();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();


        } else if (id == R.id.assignment) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            AssignmentMode fragment = new AssignmentMode();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

        } else if (id == R.id.test) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            TestMode fragment = new TestMode();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

        } /*else if (id == R.id.studentReport) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            SubjectReportFragment fragment = new SubjectReportFragment();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();
        }*/ else if (id == R.id.newsAndNotice) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }

            NewsAndNoticeFragment fragment = new NewsAndNoticeFragment();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

        } /*else if (id == R.id.addedStore) {


        } */else if (id == R.id.about) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }
            About fragment = new About();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

        }
        else if (id == R.id.update) {
            FragmentManager fm = getFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                Log.i("MainActivity", "popping backst1ack");
                for (int i = 0; i < fm.getBackStackEntryCount() - 1; i++) {
                    fm.popBackStack();
                }
            } else {
                Log.i("MainActivity", "nothing on backstack, calling super");

            }
            Update fragment = new Update();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean getSchoolRunningStatus(Date date,Context context) {
        MyDatabase db= MyDatabase.getDatabaseInstance(context);
        final String DATE_FORMAT_NOW = "yyyy-MM-dd";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            sunday = db.isSunday();
            if (sunday) {
                System.out.println("sundays date:" + sdf.format(calendar.getTime()));
                // Toast.makeText(getApplicationContext(), "Today is sunday", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        Date lastDayOfMonth = calendar.getTime();

            /*System.out.println("Today            : " + sdf.format(today));
            System.out.println("Last Day of Month: " + sdf.format(lastDayOfMonth));
            */
        if (sdf.format(date).equals(sdf.format(lastDayOfMonth))) {
            month_end = db.getLastDay();
            //Toast.makeText(getApplicationContext(), "Today is  END DAY", Toast.LENGTH_SHORT).show();
            if (month_end) {

                return false;
            }
        }
        SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_NOW);
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        cal1.set(Calendar.WEEK_OF_MONTH, 2);

        if (sdf1.format(cal1.getTime()).equals(sdf.format(date))) {
            second_saturday = db.isSecondSaturday();
            System.out.println("Second Saturday: " + sdf1.format(cal1.getTime()));
            if (second_saturday) {
                //   Toast.makeText(getApplicationContext(), "Today is second saturday", Toast.LENGTH_SHORT).show();
                return false;

            }
        }
        Calendar cal = Calendar.getInstance();
        cal = getLastSaturday(cal, 0);
        if (sdf1.format(cal.getTime()).equals(sdf.format(date))) {
            System.out.println("last_saturday : " + sdf1.format(cal.getTime()) + "");
            last_saturday = db.getLastSaturday();
            if (last_saturday) {
                // Toast.makeText(getApplicationContext(), "Today is last saturday", Toast.LENGTH_SHORT).show();
                return false;

            }
        } else {

            return db.getSchoolRunningstatus(sdf.format(date));

        }
        return false;

    }

    private class SendDevicInformation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = "http://192.168.1.6/h_service/deep_service/checkpoint2.php";
            ServiceHandler sh = new ServiceHandler();

            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            System.out.println(tsLong);
            WifiManager wifiMgr = (WifiManager) getApplicationContext()
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            String ipaddress = Formatter.formatIpAddress(wifiInfo.getIpAddress());
            String macaddres = wifiInfo.getMacAddress();
            System.out.println(ipaddress);
            System.out.println(wifiInfo.getSSID());
            System.out.println();


            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("username", "deepank"));
            params.add(new BasicNameValuePair("timestamp", ts));
            params.add(new BasicNameValuePair("ip_address", ipaddress));
            params.add(new BasicNameValuePair("device_id", macaddres));
            params.add(new BasicNameValuePair("port", "2425"));
            String response = sh.makeServiceCall(url, 2, params);

            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity1.this, s, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        }
        if (fm.getBackStackEntryCount() == 1) {
            finish();
        }


    }








}
