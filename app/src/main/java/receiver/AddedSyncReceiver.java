package receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.added.addedteacher.database.MyDatabase;

import java.util.Calendar;

import service.AddedSyncService;

/**
 * Created by ASUS on 09-04-2016.
 */
public class AddedSyncReceiver extends WakefulBroadcastReceiver {
    Intent service;
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;
    MyDatabase db;



    @Override
    public void onReceive(Context context, Intent intent) {
        service = new Intent(context, AddedSyncService.class);
        context.startService(service);
    }

    public void setAlarm(Context context) {

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AddedSyncReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        db = MyDatabase.getDatabaseInstance(context);
        Calendar calendar = Calendar.getInstance();
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 300*1000, alarmIntent);

    }

    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }


    }

}