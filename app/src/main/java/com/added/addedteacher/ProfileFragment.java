package com.added.addedteacher;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.added.addedteacher.database.MyDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import MySharedPreferences.MySharedPreferences;

/**
 * Created by ASUS on 25-03-2016.
 */
public class ProfileFragment extends Fragment {

    Teacher teacher= new Teacher();
    ArrayList<Teacher> teacherList;
    public ImageView profile;
    MyDatabase db;
    private int c=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.teacher_profile,container,false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle("Home");
        db=MyDatabase.getDatabaseInstance(getActivity().getApplicationContext());
        String tea= new MySharedPreferences(getActivity().getApplicationContext()).getTeacherIdSharedPreferencesId();
        Teacher teacher=db.getTeachersDetails(tea);

        teacherList=new ArrayList<Teacher>();
        profile=(ImageView)getView().findViewById(R.id.profileView1);
        TextView name=(TextView)getView().findViewById(R.id.nameView2);
        TextView member=(TextView)getView().findViewById(R.id.memberView4);
        TextView dob=(TextView)getView().findViewById(R.id.dobView5);
        TextView univ=(TextView)getView().findViewById(R.id.univView6);
        TextView address=(TextView)getView().findViewById(R.id.addressView9);
        TextView mail1=(TextView)getView().findViewById(R.id.mail1View13);
        TextView phn=(TextView)getView().findViewById(R.id.phnView16);


        name.setText("Name:"+teacher.getName());
        member.setText("Member Since:"+teacher.getMember());
        dob.setText("DoB:"+teacher.getDob());
        univ.setText("School:"+teacher.getUniv());
        address.setText("Address:"+teacher.getAddress());
        mail1.setText("Email:"+teacher.getMail1());
        phn.setText("Phone:"+teacher.getContact());
        Calendar calendar =Calendar.getInstance();
        SimpleDateFormat df=new SimpleDateFormat("HH:mm");
        String time=df.format(calendar.getTime());
        SharedPreferences sharedpreferences=getActivity().getSharedPreferences("currentlectire", getActivity().MODE_PRIVATE);
/*
        if(db.getCurrentLecture(time) && sharedpreferences.getInt("isLecture",0)==0)
        {
            SharedPreferences.Editor editor=sharedpreferences.edit();
            editor.putInt("isLecture",1);
            editor.commit();
            CurrentLectureStatusFragment fragment= new CurrentLectureStatusFragment();
            getFragmentManager().beginTransaction().replace(R.id.framecontainer,fragment).addToBackStack(null).commit();
        }
*/




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedpreferences=getActivity().getSharedPreferences("currentlectire", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putInt("isLecture",0);
        editor.commit();


    }
}



