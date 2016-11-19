package com.added.addedteacher;

import android.app.Fragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.added.addedteacher.database.MyDatabase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import MySharedPreferences.MySharedPreferences;

/**
 * Created by Asus 1 on 7/1/2016.
 */
public class SelfAttendance extends Fragment {

    Button attendance;
    public TextView markAtten;
    public ImageButton action_done;
    String formattedtime;
    String formatteddate;
    String status;
    MyDatabase database;
    View.OnClickListener attendanceListener, alreadyDoneAttendanceListener;
    Context context;
    private String url = "http://192.168.1.6/h_service/pria_service/attendance_teacher.php";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.self_attendance, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity().getBaseContext();
        database = MyDatabase.getDatabaseInstance(context);
        markAtten= (TextView) getView().findViewById(R.id.textView10);
        Calendar calendar1 = Calendar.getInstance(TimeZone.getDefault());
        Date current_time = calendar1.getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:SS", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        alreadyDoneAttendanceListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, " Already Marked ", Toast.LENGTH_LONG).show();
            }
        };

        attendanceListener = new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                new AlertDialog.Builder(getActivity())
                        .setTitle("Attendance")
                        .setMessage("Are you sure you want to mark your attendance?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                new AlertDialogAsyncTask().execute(url);
                            }
                        })

                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        };
        formattedtime = timeFormat.format(calendar1.getTime());
        formatteddate = dateFormat.format(new Date());
        action_done = (ImageButton) getView().findViewById(R.id.AttendanceButton1);

        if (database.check_date_for_attendance_for_setting_image(formatteddate)) {
            action_done.setImageResource(R.drawable.confirm_attendance);
            markAtten.setText("Attendance Marked");
            action_done.setOnClickListener(alreadyDoneAttendanceListener);

        } else {
            action_done.setOnClickListener(attendanceListener);
        }

        attendance = (Button) getView().findViewById(R.id.attendance);
        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TeacherAttendanceFragment fragment= new TeacherAttendanceFragment();
                getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();
            }

        });
    }


    public class AlertDialogAsyncTask extends AsyncTask<String, Void, String> {


        private String data;

        protected String doInBackground(String... arg0) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String tea= new MySharedPreferences(context).getTeacherIdSharedPreferencesId();
            String sch= new MySharedPreferences(context).getSchoolIdSharedPreferencesId();
            params.add(new BasicNameValuePair("tea", tea));
            params.add(new BasicNameValuePair("sch",sch));

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(arg0[0]);
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "networkerror";
            }
            try {

                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                data = EntityUtils.toString(entity);
                return data;

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "networkerror";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "networkerror";
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "networkerror";
            }


        }

        @Override
        protected void onPostExecute(String result) {
            String con = null;
            String timestamp = null;
            if(!result.equals("networkerror")) {
                System.out.println("Attendance Response :" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    con = jsonObject.getString("con");
                    timestamp = jsonObject.getString("timestamp");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (con.equals("yes") && con != null) {
                    Toast.makeText(context, "" +
                            "Present", Toast.LENGTH_LONG).show();
                    status = "present";


                    NotificationCompat.Builder notbuilder = new NotificationCompat.Builder(context);
                    NotificationManager notmanager = (NotificationManager) getActivity().getSystemService(context.NOTIFICATION_SERVICE);

                    notbuilder.setSmallIcon(R.drawable.add);
                    notbuilder.setContentTitle("Attendance Marked");
                    notbuilder.setContentText("You've been marked present!");
                    Intent intent = new Intent(context, MainActivity.class);
                    TaskStackBuilder taskBuilder = TaskStackBuilder.create(context);

                    taskBuilder.addParentStack(MainActivity1.class);
                    taskBuilder.addNextIntent(intent);
                    PendingIntent penIntent = taskBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                    notbuilder.setContentIntent(penIntent);
                    notmanager.notify(0, notbuilder.build());
                    notbuilder.setAutoCancel(true);

                    action_done.setImageResource(R.drawable.confirm_attendance);
                    action_done.setEnabled(false);
                    markAtten.setText("Attendance Marked");
                    //DBOpenHelper openhelper= new DBOpenHelper(context, DBConstants.Attendance_Database, null, DBConstants.DATABASE_VERSION);
                    // dbopenhelper.insert_attendanceDetailDatabase(formatteddate,formattedtime,status);

                    database.insert_attendance_details(formatteddate, timestamp);
                    //SharedPreferences sharedpreferences=getSharedPreferences(myPreferences, MODE_PRIVATE);
                    //Editor editor=sharedpreferences.edit();
                    //editor.putString("time",formattedtime);
                    //editor.putString("date",formatteddate);

				/*String vstatus= dbopenhelper.get_attendance_detail(formatteddate);
                if(vstatus.equals("present"))
				{
					action_done.setEnabled(false);
				}
				else
				{
					action_done.setEnabled(true);
				}
			    */


                }
            }else {
                status = "absent";
                Toast.makeText(context, "Unable To Connect", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(result);
        }
    }


}
