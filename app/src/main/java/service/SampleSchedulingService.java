package service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.added.addedteacher.MainActivity1;
import com.added.addedteacher.R;
import com.added.addedteacher.database.MyDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SampleSchedulingService extends Service {
    private Boolean generateNotification;


    MyDatabase db;
    private NotificationManager mNotificationManager;
    private Boolean todays_lecture_table_exist;
    public Boolean lectureEndHit;
    private boolean lectureStartHit;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        db=MyDatabase.getDatabaseInstance(getApplicationContext());
        Calendar calendar =Calendar.getInstance();
        SimpleDateFormat df=new SimpleDateFormat("HH:mm");
        String time=df.format(calendar.getTime());
        System.out.println("current_time"+time);
        // Release the wake lock provided by the BroadcastReceiver.
        //SampleAlarmReceiver.completeWakefulIntent(intent);
        //new GetTime().execute("http://192.168.1.6/services/timestamp.php");
        todays_lecture_table_exist=db.getCheckTodaysLectureTable();
        if(todays_lecture_table_exist) {

            if (db.checkForDeleteTodaysLectureTable(time)) {
                System.out.println("Delete is true ");
                db.deleteTodaysLectureTable();
            } else {

                lectureStartHit = db.getLectureStartNotificationFromTodaysLectureTable(time);
                lectureEndHit = db.getNotificationFromTodaysLectureTable(time);
                if (lectureEndHit) {

                    String lecture = db.getCurrentLecture(time);
                    if(db.getEndNotificationGenerated(lecture))
                    {
                        System.out.println("Lecture End Notification generated true for"+lecture);
                    }
                    else {
                        Log.e("Lecture ", lecture + " ended");
                        mNotificationManager = (NotificationManager)
                                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity1.class);
                        intent1.putExtra("notificationid", lecture);
                        intent1.putExtra("lecture", lecture);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), Integer.parseInt(lecture),
                                intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("Lecture Ended!")
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText("Lecture " + lecture + " ended!")).setContentText("Lecture " + lecture + " ended!").setAutoCancel(false)/*.setSound(soundUri)*/.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                        db.setEndNotificationGenerated(lecture);
                        mBuilder.setContentIntent(contentIntent);

                        mNotificationManager.notify(Integer.parseInt(lecture), mBuilder.build());
                    }
                }
                if (lectureStartHit) {
                    String lecture = db.getCurrentLecture(time);
                    if(db.getStartNotificationGenerated(lecture))
                    {
                        System.out.println("Lecture Start Notification generated true for"+lecture);
                    }
                    else {
                        Log.e("Lecture ", lecture + " started");
                        mNotificationManager = (NotificationManager)
                                getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent intent1 = new Intent(getApplicationContext(), MainActivity1.class);

                        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent1.putExtra("notificationid", lecture + 10);
                        intent1.putExtra("lecture", lecture);
                        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), Integer.parseInt(lecture) + 10,
                                intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(getApplicationContext())
                                        .setSmallIcon(R.mipmap.ic_launcher)
                                        .setContentTitle("Lecture Started!")
                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                .bigText("Lecture " + lecture + " started!"))
                                        .setContentText("Lecture " + lecture + " started!").setAutoCancel(false).setSound(soundUri);

                        mBuilder.setContentIntent(contentIntent);
                        db.setStartNotificationGenerated(lecture);
                        mNotificationManager.notify(Integer.parseInt(lecture) + 10, mBuilder.build());
                    }
                }
            }
        }
        else if(!db.checkForDeleteTodaysLectureTable(time))
        {
            db.insertTodaysLecturesDatabase(getApplicationContext());
        }
        //generateNotification=db.getNotificationStatus(time);
        //sendNotification(time);

        if(db.getSubstitutionNotification())

        {

            mNotificationManager = (NotificationManager)
                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent1=new Intent(getApplicationContext(), MainActivity1.class);

            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.putExtra("substitution",1);

            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1000,
                    intent1, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("You have been alloted new Substitution")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("Substitution"))
                            .setContentText("You have been alloted Substitution").setAutoCancel(false);

            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(1000, mBuilder.build());

        }
        if(db.getCircularNotification()!=0)

        {

            mNotificationManager = (NotificationManager)
                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent1=new Intent(getApplicationContext(), MainActivity1.class);

            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.putExtra("circular",1);

            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1000,
                    intent1, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("You have "+db.getCircularNotification()+" new Circulars")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("You have "+db.getCircularNotification()+" new Circulars"))
                            .setContentText("You have "+db.getCircularNotification()+" new Circulars").setAutoCancel(false);

            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(2000, mBuilder.build());

        }
        if(db.getNoticeNotification()!=0)

        {

            mNotificationManager = (NotificationManager)
                    getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent1=new Intent(getApplicationContext(), MainActivity1.class);

            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent1.putExtra("notice",1);

            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 1000,
                    intent1, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("You have "+db.getCircularNotification()+" new Notice")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText("You have "+db.getCircularNotification()+" new Notice"))
                            .setContentText("You have "+db.getCircularNotification()+" new Notice").setAutoCancel(false);

            mBuilder.setContentIntent(contentIntent);

            mNotificationManager.notify(3000, mBuilder.build());

        }


        return super.onStartCommand(intent, flags, startId);
    }


    public class GetTime extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();

            try {
                java.net.URL url = new URL(args[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {


        }


    }
    }
