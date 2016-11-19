package com.added.addedteacher;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.added.addedteacher.database.MyDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import adapter.StudentAdapter;
import adapter.StudentAdapter1;
import model.CurrentLectureModel;
import model.StudentModel;

/**
 * Created by ASUS on 24-03-2016.
 */
public class CurrentLectureStatusFragment extends Fragment {
    private String name;
    private int roll;
    TextView chapterName, subjectName, teacherName, className;
    ListView ls;
    StudentAdapter ea;
    MyDatabase db;
    int lec_total;
    ArrayList<StudentModel> listarr;
    ArrayList<String> stuName;
    Button createAssign, createTest, viewAssign, viewTest, viewLessonPlan;
    private static final String PREF_NAME = "TeacherLogin";
    private int ch_id;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.current_lecture_status, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = MyDatabase.getDatabaseInstance(getActivity().getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String start_time=db.getSchoolStartTime();
        String time = df.format(calendar.getTime());
           String lecture = db.getCurrentLecture(time);
        System.out.println("Current lecture:"+lecture);
            if (!lecture.equals("") && lecture.length()==1) {
                viewTest = (Button) getView().findViewById(R.id.button4);
                createAssign = (Button) getView().findViewById(R.id.button2);
                createTest = (Button) getView().findViewById(R.id.button3);
                viewAssign = (Button) getView().findViewById(R.id.button1);
                viewLessonPlan = (Button) getView().findViewById(R.id.button5);


                viewTest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToViewTest fragment = new ToViewTest();
                        getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();
                    }
                });
                viewAssign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ToViewAssign fragment = new ToViewAssign();
                        getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment, "test").
                                addToBackStack(null).
                                commit();

                    }
                });

                createAssign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AssignmentFragment fragment = new AssignmentFragment();
                        getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment, "assignment").
                                addToBackStack(null).
                                commit();


                    }
                });


                createTest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TestFragment fragment = new TestFragment();

                        getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment, "test").
                                addToBackStack(null).
                                commit();


                    }
                });

                viewLessonPlan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LecturePlanActivity fragment = new LecturePlanActivity();
                        /*Bundle bundle = new Bundle();
                        bundle.putInt("ch_id", ch_id);
                        bundle.putInt("total_lectures", lec_total);
                        fragment.setArguments(bundle);
                        fragment.setArguments(bundle);
                        */getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

                    }
                });


                getActivity().setTitle("My Current Lecture");
                ls = (ListView) getView().findViewById(R.id.StudentslistView1);

                //chapterName = (TextView) getView().findViewById(R.id.chaptersTextView);
                subjectName = (TextView) getView().findViewById(R.id.subjectTextView);
                className = (TextView) getView().findViewById(R.id.textView2);
                teacherName = (TextView) getView().findViewById(R.id.teacherNameTextView);
                SharedPreferences preferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                String tea_Name = db.getTeacherNameFromId(preferences.getString("tea_id", "noname")) + "";
                teacherName.setText(tea_Name);
                //ch_id = db.getLectureEndChapterId(lectureno);
                /*System.out.println("ch_id." + ch_id);
                lec_total = db.getTotalLecturesForChapeter(ch_id);
                System.out.println("total_lectures." + lec_total);*/
                db.getClassSecSubId(calendar.get(Calendar.DAY_OF_WEEK), Integer.parseInt(lecture));

                Calendar calendar1=Calendar.getInstance();
                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date=sdf.format(calendar1.getTime());

                //chapterName.setText(db.get_chapter(ch_id));
                if(CurrentLectureModel.currentLectureModelArrayList.size()==0)
                {
                    setViewLayout(R.layout.notlecture);

                }
                else {
                    subjectName.setText(db.get_subject(CurrentLectureModel.currentLectureModelArrayList.get(0).subject));
                    className.setText("Class : " + db.getClassName(CurrentLectureModel.currentLectureModelArrayList.get(0).classes) + " " + db.getSectionName(CurrentLectureModel.currentLectureModelArrayList.get(0).section));
                    listarr = db.getStudentsName(CurrentLectureModel.currentLectureModelArrayList.get(0).classes, CurrentLectureModel.currentLectureModelArrayList.get(0).section, date);
                    StudentAdapter1 adap = new StudentAdapter1(getActivity().getApplicationContext(),
                            R.layout.list_view_layout1, listarr);
                    ls.setAdapter(adap);
                    ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                            alertDialog.setTitle("Send Message");
                            alertDialog.setMessage("Enter Message");

                            final EditText input = new EditText(getActivity());
                            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT);
                            input.setLayoutParams(lp);
                            alertDialog.setView(input);


                            alertDialog.setPositiveButton("YES",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(getActivity().getBaseContext(), "Message Sent!", Toast.LENGTH_SHORT).show();
                                        }

                                    });

                            alertDialog.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                            alertDialog.show();
                        }

                    });

                }
            }
            if(lecture.equals("Break"))
            {
                setViewLayout(R.layout.lunchview);
                TextView schoolOver=(TextView)getView().findViewById(R.id.lunchView1);
                schoolOver.setText("Its Lunch Time ");
            }
            if(lecture.equals(""))
            {
                setViewLayout(R.layout.schoolover);
                TextView schoolOver=(TextView)getView().findViewById(R.id.schoolOverView1);
                schoolOver.setText("School Over..!! Will begin at "+start_time+" a.m.");
            }

    }

    private void setViewLayout(int id) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(v);
    }

    public class StudentsAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... arg0) {

            BufferedReader rd = null;
            StringBuilder sb = null;
            String line = null;

            try {
                URL url = new URL(arg0[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.connect();
                rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();

                while ((line = rd.readLine()) != null) {
                    sb.append(line + '\n');
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getActivity().getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
            String data = sb.toString();
            try {

                JSONObject jsob = new JSONObject(data);
                JSONArray jarr = jsob.getJSONArray("posts");
                for (int i = 0; i < jarr.length(); i++) {
                    StudentModel sm = new StudentModel();
                    JSONObject jreob = jarr.getJSONObject(i);
                    sm.setRollno(jreob.getString("roll_no"));
                    sm.setName(jreob.getString("name"));
                    listarr.add(sm);


                }
                return true;
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;

            }

        }

        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
                /*if (result == false) {
					// msg that data was not parsed
					//Toast.makeText(MainActivity.this, "Error Occured !", Toast.LENGTH_SHORT).show();
				} else {*/
           /* StudentAdapter adap = new StudentAdapter(getActivity().getApplicationContext(),
                    R.layout.list_view_layout, listarr);
            ls.setAdapter(adap);
            ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                          @Override
                                          public void onItemClick(AdapterView<?> arg0, View arg1,
                                                                  final int arg2, long arg3) {
                                              // TODO Auto-generated method stub
                                              AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(getActivity().getApplicationContext());
                                              //setting Dialog Title
                                              dialogBuilder.setTitle("Give your feedback");
                                              final EditText input = new EditText(getActivity().getApplicationContext());
                                              LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                                      LinearLayout.LayoutParams.MATCH_PARENT,
                                                      LinearLayout.LayoutParams.MATCH_PARENT);
                                              input.setLayoutParams(lp);
                                              dialogBuilder.setView(input);
                                              dialogBuilder.setCancelable(true)
                                                      //Setting Positive "Send" button
                                                      .setPositiveButton("send",new DialogInterface.OnClickListener() {

                                                          @Override

                                                          // TODO Auto-generated method stub
                                                          public void onClick(DialogInterface dialog, int id) {
                                                              //Calling Async Task for Dialog
                                                              String name = listarr.get(arg2).name;
                                                              String rollno = listarr.get(arg2).rollno;
                                                              Toast.makeText(getActivity().getApplicationContext(), "Feedback Submitted",Toast.LENGTH_LONG ).show();
                                                              //new AlertDialogAsyncTask(name,rollno).execute();
                                                          }
                                                      })
                                                      .setNegativeButton("Back", new DialogInterface.OnClickListener() {

                                                          @Override
                                                          public void onClick(DialogInterface dialog, int id) {
                                                              // TODO Auto-generated method stub
                                                              dialog.cancel();
                                                          }
                                                      });
                                              AlertDialog alert= dialogBuilder.create();
                                              alert.show();
                                          }
                                      }
            );

*/


				/*}*/
        }


        public class AlertDialogAsyncTask extends AsyncTask<String, Void, Boolean> {

            String nme, rolnum;

            public AlertDialogAsyncTask(String name, String rollno) {
                // TODO Auto-generated constructor stub
                nme = name;
                rolnum = rollno;

            }

            @Override
            protected Boolean doInBackground(String... arg0) {/*
                ListModel<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("user", "TEA35273"));
                params.add(new BasicNameValuePair("name", nme));
                params.add(new BasicNameValuePair("roll_no", rolnum));*/
                BufferedReader rd = null;

                StringBuilder sb = null;
                String line = null;
                try {
                    URL url = new URL(arg0[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("user", "TEA35273")
                            .appendQueryParameter("name", nme)
                            .appendQueryParameter("roll_no", rolnum);
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();
                    connection.connect();
                    rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    sb = new StringBuilder();

                    while ((line = rd.readLine()) != null) {
                        sb.append(line + '\n');
                    }


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getActivity().getApplicationContext(), "Error Occured", Toast.LENGTH_LONG).show();
                String data = sb.toString();
                JSONObject jobj = new JSONObject();


                return null;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                Toast.makeText(getActivity().getApplicationContext(), "Feedback sent", Toast.LENGTH_SHORT).show();
            }

        }


    }

}
