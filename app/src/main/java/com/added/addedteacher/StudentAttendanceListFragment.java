package com.added.addedteacher;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.added.addedteacher.database.MyDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import MySharedPreferences.MySharedPreferences;
import adapter.StudentAdapter;
import model.StudentModel;

/**
 * Created by Asus 1 on 7/2/2016.
 */
public class StudentAttendanceListFragment extends Fragment {
    MyDatabase db;

    ArrayList<StudentModel> listarr;
    TextView classNameTextView;
    ListView studentAttendanceListView;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v=inflater.inflate(R.layout.student_list_attendance, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = MyDatabase.getDatabaseInstance(getActivity().getApplicationContext());
        Bundle bundle=this.getArguments();
        int class_id=bundle.getInt("class_id");
        int sec_id=bundle.getInt("sec_id");
        Calendar calendar=Calendar.getInstance();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date=sdf.format(calendar.getTime());

        if (/*db.getClassTeacherSectionId(new MySharedPreferences(getActivity().getBaseContext()).getTeacherIdSharedPreferencesId()) != 0*/true) {
            classNameTextView = (TextView) getView().findViewById(R.id.textView11);
            /*SharedPreferences preferences = getActivity().getSharedPreferences(SessionManager.PREF_NAME, getActivity().MODE_PRIVATE);
            classNameTextView.append(" " + db.getClassTeacherClassSection(preferences.getString(SessionManager.TEACHER_ID, "hemant")));
            */
            classNameTextView.setText(db.getClassName(class_id)+"  "+db.getSectionName(sec_id));
            studentAttendanceListView = (ListView) getView().findViewById(R.id.listView2);
            String tea = new MySharedPreferences(getActivity().getBaseContext()).getTeacherIdSharedPreferencesId();
            //listarr = db.getStudentsName(db.getClassTeacherClassId(tea), db.getClassTeacherSectionId(tea));
            listarr = db.getStudentsName(class_id, sec_id,date);
            StudentAdapter adap = new StudentAdapter(getActivity().getApplicationContext(),
                    R.layout.list_view_layout, listarr);
            studentAttendanceListView.setAdapter(adap);
            studentAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String username = listarr.get(position).getUsername();
                    StudentAttendanceCalendarFragment fragment = new StudentAttendanceCalendarFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", listarr.get(position).getName());
                    bundle.putString("username", username);
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.framecontainer, fragment).addToBackStack(null).commit();
                }

            });


        } else {
            setViewLayout(R.layout.notclassteacher);
        }
    }
    private void setViewLayout(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(v);
    }
}

