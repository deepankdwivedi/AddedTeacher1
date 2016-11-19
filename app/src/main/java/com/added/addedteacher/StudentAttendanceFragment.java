package com.added.addedteacher;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.added.addedteacher.database.MyDatabase;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import MySharedPreferences.MySharedPreferences;

/**
 * Created by ASUS on 22-03-2016.
 */
public class StudentAttendanceFragment extends Fragment{

    MyDatabase db;
    TextView tv;
    String s = "";
    public static final String IS_DATE_CORRECT = "isDateCorrect";
    int Num_Rows;
    int Num_Extra;
    int Extra_Row = 0;
    int attend[][];
    String attend_final[];
    //String sample[]={"najmul","nabeel","naziya"};
    String[][] dataa;
    String[][] dataa_username;
    private final static int Num_Col = 3;
    // TextView tv;
    String value;
    Button buttons[][] ;
    ArrayList<String> list;
    ArrayList<String> list_update;
    //String singledata[]= new String[100];
    final String DATE_FORMAT_NOW = "yyyy-MM-dd";
    String singledata[][];
    int total;
    View v;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.student_attendance,container,false);
        return v;

    }
    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(v);

    }
    String date,time;
    SimpleDateFormat sdf1 = new SimpleDateFormat(DATE_FORMAT_NOW);
    SimpleDateFormat df=new SimpleDateFormat("HH:mm");


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Calendar calendar=Calendar.getInstance();

        date=sdf1.format(calendar.getTime());
        time=df.format(calendar.getTime());
        db = MyDatabase.getDatabaseInstance(getActivity().getApplicationContext());
        getActivity().setTitle("Student Attendance");
        if(!sdf1.format(calendar.getTime()).equals(new MySharedPreferences(getActivity().getApplicationContext()).getisDateCorrect()))
        {
            new GetTime().execute("http://192.168.1.6/services/timestamp.php");
        }
        else
        {
            SharedPreferences preferences = getActivity().getSharedPreferences(SessionManager.PREF_NAME, getActivity().MODE_PRIVATE);
            if (db.getClassTeacherSectionId(preferences.getString(SessionManager.TEACHER_ID, "nousername")) == 0) {
                setViewLayout(R.layout.notclassteacher);
            } else {
                singledata = db.Get_ContactDetails_students(db.getClassTeacherClassId(new MySharedPreferences(getActivity().getApplicationContext()).getTeacherIdSharedPreferencesId()), db.getClassTeacherSectionId(new MySharedPreferences(getActivity().getBaseContext()).getTeacherIdSharedPreferencesId()));
                tv = (TextView) getView().findViewById(R.id.textView1);

                tv.setText(" " + db.getClassTeacherClassSection(preferences.getString(SessionManager.TEACHER_ID, "nousername")));

                // +"\n Name: "+contacts.getStu_name());
                System.out.println(singledata[0].length);
                total = singledata[0].length;
                rowFix();

                // for(int i=0;i<total;i++)
                // {
                // s=s+singledata[i];
                // }
                // tv.setText(s+" ");


                int z = 0, i;
                for (i = 0; i < Num_Rows; i++) {
                    for (int j = 0; j < Num_Col; j++) {
                        dataa[i][j] = singledata[0][z];
                        dataa_username[i][j] = singledata[1][z];
                        z++;
                    }
                }
                for (int k = 0; k < Num_Extra; k++) {
                    dataa[Num_Rows][k] = singledata[0][z];
                    dataa_username[Num_Rows][k] = singledata[1][z];
                    z++;
                }

                populateButton();
            }
        }


    }

    private void rowFix() {
        Num_Rows = (total / Num_Col);
        Num_Extra = (total % Num_Col);
        if (Num_Extra != 0) {
            Extra_Row = 1;
        }
        //attend_final = new int[100];
        attend = new int[Num_Rows + Extra_Row][Num_Col];
        dataa = new String[Num_Rows + Extra_Row][Num_Col];
        dataa_username = new String[Num_Rows + Extra_Row][Num_Col];
        buttons= new Button[Num_Rows+Extra_Row][Num_Col];
        //singledata = new String[to];
//		for (int i = 0; i < Num_Rows + Extra_Row; i++)
//			for (int j = 0; j < Num_Col; j++)
//				attend[i][j] = 0;

    }

    private void populateButton() {


        TableLayout table = (TableLayout)getView(). findViewById(R.id.tablee);
        for (int row = 0; row < Num_Rows; row++) {
            TableRow tableRow = new TableRow(getActivity().getApplicationContext());
            // table.addView(table);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,1.0f));
            table.addView(tableRow);

            for (int col = 0; col < Num_Col; col++) {
                final int Final_Col = col;
                final int Final_Row = row;
                final ToggleButton button = new ToggleButton(getActivity().getApplicationContext());
                button.setBackgroundColor(Color.TRANSPARENT);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT, 1.0f));
                button.setText(dataa[row][col]);
                // button.setText(row+" ");
                button.setTextColor(Color.parseColor("#000000"));
                button.setPadding(0, 0, 0, 0);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (button.isChecked()) {
                            button.setText(dataa[Final_Row][Final_Col]);
                            button.setBackgroundColor(Color
                                    .parseColor("#9CD159"));
                            int k=list.indexOf(dataa_username[Final_Row][Final_Col]);
                            if(k>=0)
                                list.remove(k);
//							TextView tv=(TextView)findViewById(R.id.textView1);
//							tv.setText(k+" ");
//							int k=list_update.indexOf(dataa[Final_Row][Final_Col]);
//							if(k>=0)
//							list_update.remove(k);
//							if(myDatabase.check_for_date()&&list_update.size()>0)
//							{
//							for(int i=0;i<list_update.size();i++)
//							{
//								if(list_update.get(i).equalsIgnoreCase(dataa_username[Final_Row][Final_Col]));
//							list_update.remove(i);
//							}
//							}
                            //attend[Final_Row][Final_Col] = 1;
                        } else {
                            button.setText(dataa[Final_Row][Final_Col]);
                            button.setBackgroundColor(Color
                                    .parseColor("#FF5757"));
                            //attend[Final_Row][Final_Col] = 0;
                            list.add(dataa_username[Final_Row][Final_Col]);
//			TextView tv=(TextView)findViewById(R.id.textView1);
//							tv.setText(list+" ");

                        }
                        //gridButton(Final_Row, Final_Col);

                    }

                });
                tableRow.addView(button);
                buttons[row][col] = button;

            }
        }
        // System.out.println(Num_Extra);
        if (Num_Extra != 0) {

            TableRow tableRow = new TableRow(getActivity().getApplicationContext()
            );
            // table.addView(table);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            table.addView(tableRow);

            for (int col = 0; col < Num_Extra; col++) {
                final int Final_Coll = col;
                final int Final_Roww = Num_Rows;
                final ToggleButton button = new ToggleButton(getActivity().getApplicationContext());
                button.setBackgroundColor(Color.TRANSPARENT);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.MATCH_PARENT));
                button.setText(dataa[Num_Rows][col]);
                // button.setText(Final_Roww+" ");
                button.setTextColor(Color.parseColor("#000000"));
                button.setPadding(0, 0, 0, 0);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (button.isChecked()) {
                            button.setText(dataa[Final_Roww][Final_Coll]);
                            button.setBackgroundColor(Color
                                    .parseColor("#9CD159"));
                            int k=list.indexOf(dataa_username[Final_Roww][Final_Coll]);
                            if(k>=0)
                                list.remove(k);
//							if(myDatabase.check_for_date()&& list_update.size()>0)
//							{
//							for(int i=0;i<list_update.size();i++)
//							{
//								if(list_update.get(i).equalsIgnoreCase(dataa_username[Final_Roww][Final_Coll]));
//							list_update.remove(i);
//							}
//							}
                            //attend[Final_Roww][Final_Coll] = 1;
                        } else {
                            button.setText(dataa[Final_Roww][Final_Coll]);
                            button.setBackgroundColor(Color
                                    .parseColor("#FF5757"));
                            //attend[Final_Roww][Final_Coll] = 0;
                            list.add(dataa_username[Final_Roww][Final_Coll]);
                        }
                        //gridButton1(Final_Roww, Final_Coll);

                    }

                });
                tableRow.addView(button);
                buttons[Num_Rows][col] = button;
            }
        }
        if(db.check_for_date())
        {	//ArrayList<String> list_update=new ArrayList<String>();
            //list_update=0;
            list=db.get_updated_names();
            //TextView tv=(TextView)findViewById(R.id.textView1);
            if(list.size()>0)
            {
                //tv.setText(list_update+" ");
                for(int i=0;i<Num_Rows+Extra_Row;i++)
                {
                    for(int j=0;j<Num_Col;j++)
                    {
                        for(int k=0;k<list.size();k++)
                        {
                            if(list.get(k).equalsIgnoreCase(dataa_username[i][j]))
                            {
                                buttons[i][j].setBackgroundColor(Color
                                        .parseColor("#FF5757"));
                                //list.clear();
                                //list_update.add(dataa_username[i][j]);

                            }
                        }
                    }
                }
            }

        }
        else
        {
            list = new ArrayList<String>();
        }
        TableRow tableRow = new TableRow(getActivity().getApplicationContext());
        // table.addView(table);

        tableRow.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
        tableRow.setGravity(Gravity.END);
        table.addView(tableRow);
        final Button last = new Button(getActivity().getApplicationContext());
        last.setLayoutParams(new TableRow.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT));
        last.setBackgroundResource(R.drawable.mybutton);
        last.setPadding(0, 0, 0, 0);
        // last.setGravity(Gravity.CENTER_HORIZONTAL);
        last.setText("Submit");

        last.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // SendDataToServer();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        InsertingData();

                    }

                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                // Toast.makeText(MainActivity.this, "Attendance Submitted",
                // Toast.LENGTH_LONG).show();
                // Intent i = new Intent(MainActivity.this, Absent.class);
//tv.setText(list+" ");
                // startActivity(i);
            }
        });

        tableRow.addView(last);

    }

    private void gridButton(int x, int y) {
        // Toast
        Button button = buttons[x][y];

        lockButtonSizes();

    }

    private void gridButton1(int x, int y) {
        // Toast
        Button button = buttons[x][y];

        lockButtonSizes1();

    }

    private void lockButtonSizes1() {

        for (int col = 0; col < Num_Extra; col++) {
            Button button = buttons[Num_Rows][col];
            int width = button.getWidth();
            button.setMaxWidth(width);
            button.setMinWidth(width);

            int height = button.getHeight();
            button.setMaxHeight(height);
            button.setMinHeight(height);

        }

    }

    private void lockButtonSizes() {
        for (int row = 0; row < Num_Rows; row++) {
            for (int col = 0; col < Num_Col; col++) {
                Button button = buttons[row][col];
                int width = button.getWidth();
                button.setMaxWidth(width);
                button.setMinWidth(width);

                int height = button.getHeight();
                button.setMaxHeight(height);
                button.setMinHeight(height);

            }

        }

    }
    public void InsertingData(){
        db = MyDatabase.getDatabaseInstance(getActivity().getApplicationContext());
        boolean resp;
//	for(int i=0;i<list_update.size();i++)
//	list.add(list_update.get(i));

        resp = db.Insert_ContactDetails(list,db.getClassTeacherClassId(new MySharedPreferences(getActivity().getApplicationContext()).getTeacherIdSharedPreferencesId()),db.getClassTeacherSectionId(new MySharedPreferences(getActivity().getBaseContext()).getTeacherIdSharedPreferencesId()));
//tv.setText(resp+" ");
        //if(resp)

        Toast.makeText(getActivity().getApplicationContext(), " Attendance Submitted ", Toast.LENGTH_SHORT)
                .show();
        AbsentFragment fragment= new AbsentFragment();
//        Intent i = new Intent(MainActivity.this, Absent.class);
//
//        startActivity(i);
        getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();

    }


    public class GetTime extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(args[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
                return null;
            }
            finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                System.out.print("Result:" + result);
                String[] splittedDateTime = result.split(" ");
                System.out.print("Net date:" + splittedDateTime[0]);
                System.out.println("Net time" + splittedDateTime[1]);
                String time1[]=splittedDateTime[1].split(":");
                if (date.equals(splittedDateTime[0])) {
                    Calendar minusMinutes = Calendar.getInstance();
                    minusMinutes.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time1[0]));
                    minusMinutes.set(Calendar.MINUTE, Integer.parseInt(time1[1]));
                    minusMinutes.add(Calendar.MINUTE, -3);
                    String minusMinutesTime = df.format(minusMinutes.getTime());
                    Calendar plusMinutes = Calendar.getInstance();
                    plusMinutes.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time1[0]));
                    plusMinutes.set(Calendar.MINUTE, Integer.parseInt(time1[1]));
                    plusMinutes.add(Calendar.MINUTE, 3);
                    String plusMinutesTime = df.format(plusMinutes.getTime());
                    if (time.compareTo(plusMinutesTime) < 0 && time.compareTo(minusMinutesTime) > 0) {
                        System.out.println("Time is correct");
                        SharedPreferences pref =getActivity().getApplicationContext().getSharedPreferences("TeacherLogin", 0);
                        pref.edit().putString(IS_DATE_CORRECT,date).commit();
                       MyDatabase db1 = MyDatabase.getDatabaseInstance(getActivity().getApplicationContext());
                        if (db.getClassTeacherSectionId(new MySharedPreferences(getActivity()).getTeacherIdSharedPreferencesId()) != 0) {
                            singledata = db1.Get_ContactDetails_students(db1.getClassTeacherClassId(new MySharedPreferences(getActivity().getApplicationContext()).getTeacherIdSharedPreferencesId()), db1.getClassTeacherSectionId(new MySharedPreferences(getActivity().getBaseContext()).getTeacherIdSharedPreferencesId()));
                            tv = (TextView) getView().findViewById(R.id.textView1);
                            SharedPreferences preferences = getActivity().getSharedPreferences(SessionManager.PREF_NAME, getActivity().MODE_PRIVATE);
                            if (db1.getClassTeacherClassSection(preferences.getString(SessionManager.TEACHER_ID, "nousername")) == null) {
                                setViewLayout(R.layout.notclassteacher);
                            } else {
                                tv.setText(" " + db1.getClassTeacherClassSection(preferences.getString(SessionManager.TEACHER_ID, "nousername")));

                                // +"\n Name: "+contacts.getStu_name());
                                System.out.println(singledata[0].length);
                                total = singledata[0].length;
                                rowFix();

                                // for(int i=0;i<total;i++)
                                // {
                                // s=s+singledata[i];
                                // }
                                // tv.setText(s+" ");


                                int z = 0, i;
                                for (i = 0; i < Num_Rows; i++) {
                                    for (int j = 0; j < Num_Col; j++) {
                                        dataa[i][j] = singledata[0][z];
                                        dataa_username[i][j] = singledata[1][z];
                                        z++;
                                    }
                                }
                                for (int k = 0; k < Num_Extra; k++) {
                                    dataa[Num_Rows][k] = singledata[0][z];
                                    dataa_username[Num_Rows][k] = singledata[1][z];
                                    z++;
                                }

                                populateButton();
                            }

                        } else {
                            setViewLayout(R.layout.notclassteacher);
                        }
                    } else {

                        Toast.makeText(getActivity(), "Please update your Date and Time as server", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getActivity(), "Please update your Date and Time as server", Toast.LENGTH_SHORT).show();

                }


            }
            else
            {
                Toast.makeText(getActivity(),"Network Problem!",Toast.LENGTH_SHORT).show();
            }
        }

    }


}
